package com.vision_rent.automovil_unite.application.service;


import com.vision_rent.automovil_unite.application.dto.CreateRentalRequest;
import com.vision_rent.automovil_unite.application.dto.ExtendRentalRequest;
import com.vision_rent.automovil_unite.application.dto.RentalDto;
import com.vision_rent.automovil_unite.application.dto.ReturnVehicleRequest;
import com.vision_rent.automovil_unite.application.exception.InvalidOperationException;
import com.vision_rent.automovil_unite.application.exception.ResourceNotFoundException;
import com.vision_rent.automovil_unite.application.exception.UnauthorizedOperationException;
import com.vision_rent.automovil_unite.application.mapper.RentalDtoMapper;
import com.vision_rent.automovil_unite.domain.entity.Rental;
import com.vision_rent.automovil_unite.domain.entity.User;
import com.vision_rent.automovil_unite.domain.entity.Vehicle;
import com.vision_rent.automovil_unite.domain.exception.InvalidRentalOperationException;
import com.vision_rent.automovil_unite.domain.exception.VehicleNotAvailableException;
import com.vision_rent.automovil_unite.domain.repository.RentalRepository;
import com.vision_rent.automovil_unite.domain.repository.UserRepository;
import com.vision_rent.automovil_unite.domain.repository.VehicleRepository;
import com.vision_rent.automovil_unite.domain.service.RentalDomainService;
import com.vision_rent.automovil_unite.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de alquileres.
 */
@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final RentalDomainService rentalDomainService;
    private final UserDomainService userDomainService;
    private final RentalDtoMapper rentalDtoMapper;
    
    /**
     * Crea un nuevo alquiler.
     *
     * @param renterId ID del arrendatario
     * @param request Datos del alquiler
     * @return DTO del alquiler creado
     */
    @Transactional
    public RentalDto createRental(Long renterId, CreateRentalRequest request) {
        User renter = userRepository.findById(renterId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", renterId));
        
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo", "id", request.getVehicleId()));
        
        // Verificar si el usuario puede alquilar
        if (!userDomainService.canRentVehicle(renter)) {
            throw new UnauthorizedOperationException("El usuario no puede alquilar vehículos");
        }
        
        // Verificar si el vehículo está disponible en el rango de fechas
        if (!rentalDomainService.isVehicleAvailable(vehicle, request.getStartDateTime(), request.getEndDateTime())) {
            throw new VehicleNotAvailableException("El vehículo no está disponible en las fechas seleccionadas");
        }
        
        // Verificar si hay otros alquileres activos para el vehículo en el rango de fechas
        if (rentalRepository.existsActiveRentalForVehicle(vehicle.getId(), request.getStartDateTime(), request.getEndDateTime())) {
            throw new VehicleNotAvailableException("El vehículo ya está reservado para las fechas seleccionadas");
        }
        
        // Crear el alquiler
        Rental rental = new Rental();
        rental.setVehicle(vehicle);
        rental.setRenter(renter);
        rental.setStartDateTime(request.getStartDateTime());
        rental.setEndDateTime(request.getEndDateTime());
        rental.setStatus("PENDING");
        rental.setPaid(false);
        
        // Calcular precio
        BigDecimal totalPrice = rentalDomainService.calculateRentalPrice(vehicle, renter, request.getStartDateTime(), request.getEndDateTime());
        rental.setTotalPrice(totalPrice);
        
        // Aplicar descuento si corresponde
        if (renter.isEligibleForDiscount()) {
            rental = rentalDomainService.applyDiscount(rental, renter);
        }
        
        // Si se proporciona un paymentId, marcar como pagado
        if (request.getPaymentId() != null && !request.getPaymentId().isEmpty()) {
            rental.setPaymentId(request.getPaymentId());
            rental.setPaid(true);
            rental.setStatus("CONFIRMED");
        }
        
        Rental savedRental = rentalRepository.save(rental);
        
        // Incrementar contador de alquileres del vehículo
        vehicleRepository.incrementRentCount(vehicle.getId());
        
        return rentalDtoMapper.toDto(savedRental);
    }
    
    /**
     * Confirma un alquiler (después del pago).
     *
     * @param rentalId ID del alquiler
     * @param paymentId ID del pago
     * @param userId ID del usuario que realiza la operación
     * @return DTO del alquiler confirmado
     */
    @Transactional
    public RentalDto confirmRental(Long rentalId, String paymentId, Long userId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Alquiler", "id", rentalId));
        
        // Verificar si el usuario es el arrendatario
        if (!rental.getRenter().getId().equals(userId)) {
            throw new UnauthorizedOperationException("Solo el arrendatario puede confirmar el alquiler");
        }
        
        // Verificar si el alquiler está en estado pendiente
        if (!"PENDING".equals(rental.getStatus())) {
            throw new InvalidOperationException("Solo se pueden confirmar alquileres pendientes");
        }
        
        rental.setPaymentId(paymentId);
        rental.setPaid(true);
        rental.setStatus("CONFIRMED");
        
        Rental updatedRental = rentalRepository.save(rental);
        return rentalDtoMapper.toDto(updatedRental);
    }
    
    /**
     * Extiende un alquiler.
     *
     * @param request Datos de la extensión
     * @param userId ID del usuario que realiza la operación
     * @return DTO del alquiler extendido
     */
    @Transactional
    public RentalDto extendRental(ExtendRentalRequest request, Long userId) {
        Rental rental = rentalRepository.findById(request.getRentalId())
                .orElseThrow(() -> new ResourceNotFoundException("Alquiler", "id", request.getRentalId()));
        
        // Verificar si el usuario es el arrendatario
        if (!rental.getRenter().getId().equals(userId)) {
            throw new UnauthorizedOperationException("Solo el arrendatario puede extender el alquiler");
        }
        
        // Verificar si el alquiler se puede extender
        if (!rentalDomainService.canExtendRental(rental, request.getNewEndDateTime())) {
            throw new InvalidRentalOperationException("No se puede extender el alquiler");
        }
        
        // Verificar disponibilidad del vehículo para la extensión
        if (rentalRepository.existsActiveRentalForVehicle(rental.getVehicle().getId(), rental.getEndDateTime(), request.getNewEndDateTime())) {
            throw new VehicleNotAvailableException("El vehículo ya está reservado para las nuevas fechas");
        }
        
        // Calcular precio adicional
        BigDecimal additionalPrice = rentalDomainService.calculateRentalPrice(
                rental.getVehicle(), 
                rental.getRenter(), 
                rental.getEndDateTime(), 
                request.getNewEndDateTime()
        );
        
        // Actualizar el alquiler
        rental.setExtendedUntil(request.getNewEndDateTime());
        rental.setTotalPrice(rental.getTotalPrice().add(additionalPrice));
        
        // Si se proporciona un paymentId, marcar como pagado
        if (request.getPaymentId() != null && !request.getPaymentId().isEmpty()) {
            // Aquí se podría guardar un registro adicional del pago de la extensión
            rental.setPaid(true);
        } else {
            // Si no hay pago, la extensión queda pendiente de pago
            rental.setPaid(false);
        }
        
        Rental updatedRental = rentalRepository.save(rental);
        return rentalDtoMapper.toDto(updatedRental);
    }
    
    /**
     * Registra la devolución de un vehículo.
     *
     * @param request Datos de la devolución
     * @param userId ID del usuario que realiza la operación
     * @return DTO del alquiler finalizado
     */
    @Transactional
    public RentalDto returnVehicle(ReturnVehicleRequest request, Long userId) {
        Rental rental = rentalRepository.findById(request.getRentalId())
                .orElseThrow(() -> new ResourceNotFoundException("Alquiler", "id", request.getRentalId()));
        
        // Verificar si el usuario es el arrendatario o el propietario
        boolean isRenter = rental.getRenter().getId().equals(userId);
        boolean isOwner = rental.getVehicle().getOwner().getId().equals(userId);
        
        if (!isRenter && !isOwner) {
            throw new UnauthorizedOperationException("Solo el arrendatario o el propietario pueden registrar la devolución");
        }
        
        // Fecha de devolución actual si no se proporciona
        LocalDateTime returnDateTime = request.getReturnDateTime() != null ? 
                request.getReturnDateTime() : LocalDateTime.now();
        
        try {
            // Registrar la devolución
            Rental returnedRental = rentalDomainService.returnVehicle(rental, returnDateTime);
            
            // Verificar si se debe suspender al usuario por devolución tardía
            if (returnedRental.isLateReturn() && rentalDomainService.shouldBanUser(returnedRental, rental.getRenter())) {
                userDomainService.banUser(rental.getRenter());
                userRepository.save(rental.getRenter());
            }
            
            // Actualizar la fecha del último alquiler del vehículo
            Vehicle vehicle = rental.getVehicle();
            vehicle.setLastRentalEnd(returnDateTime);
            vehicleRepository.save(vehicle);
            
            Rental savedRental = rentalRepository.save(returnedRental);
            return rentalDtoMapper.toDto(savedRental);
        } catch (InvalidRentalOperationException e) {
            throw new InvalidOperationException(e.getMessage());
        }
    }
    
    /**
     * Obtiene un alquiler por su ID.
     *
     * @param rentalId ID del alquiler
     * @return DTO del alquiler
     */
    @Transactional(readOnly = true)
    public RentalDto getRentalById(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Alquiler", "id", rentalId));
        
        return rentalDtoMapper.toDto(rental);
    }
    
    /**
     * Obtiene todos los alquileres de un arrendatario.
     *
     * @param renterId ID del arrendatario
     * @return Lista de DTOs de alquiler
     */
    @Transactional(readOnly = true)
    public List<RentalDto> getRentalsByRenterId(Long renterId) {
        return rentalRepository.findByRenterId(renterId)
                .stream()
                .map(rentalDtoMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene todos los alquileres de un vehículo.
     *
     * @param vehicleId ID del vehículo
     * @return Lista de DTOs de alquiler
     */
    @Transactional(readOnly = true)
    public List<RentalDto> getRentalsByVehicleId(Long vehicleId) {
        return rentalRepository.findByVehicleId(vehicleId)
                .stream()
                .map(rentalDtoMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene todos los alquileres de vehículos de un propietario.
     *
     * @param ownerId ID del propietario
     * @return Lista de DTOs de alquiler
     */
    @Transactional(readOnly = true)
    public List<RentalDto> getRentalsByVehicleOwnerId(Long ownerId) {
        return rentalRepository.findByVehicleOwnerId(ownerId)
                .stream()
                .map(rentalDtoMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Cancela un alquiler.
     *
     * @param rentalId ID del alquiler
     * @param userId ID del usuario que realiza la operación
     * @return DTO del alquiler cancelado
     */
    @Transactional
    public RentalDto cancelRental(Long rentalId, Long userId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Alquiler", "id", rentalId));
        
        // Verificar si el usuario es el arrendatario o el propietario
        boolean isRenter = rental.getRenter().getId().equals(userId);
        boolean isOwner = rental.getVehicle().getOwner().getId().equals(userId);
        
        if (!isRenter && !isOwner) {
            throw new UnauthorizedOperationException("Solo el arrendatario o el propietario pueden cancelar el alquiler");
        }
        
        // Verificar si el alquiler se puede cancelar
        if ("ACTIVE".equals(rental.getStatus()) || "COMPLETED".equals(rental.getStatus())) {
            throw new InvalidOperationException("No se puede cancelar un alquiler activo o completado");
        }
        
        rental.setStatus("CANCELLED");
        
        Rental cancelledRental = rentalRepository.save(rental);
        return rentalDtoMapper.toDto(cancelledRental);
    }
}