package com.vision_rent.automovil_unite.application.service;

import com.vision_rent.automovil_unite.application.dto.CreateVehicleRequest;
import com.vision_rent.automovil_unite.application.dto.VehicleDto;
import com.vision_rent.automovil_unite.application.exception.ResourceNotFoundException;
import com.vision_rent.automovil_unite.application.exception.UnauthorizedOperationException;
import com.vision_rent.automovil_unite.application.mapper.UserDtoMapper;
import com.vision_rent.automovil_unite.application.mapper.VehicleDtoMapper;
import com.vision_rent.automovil_unite.domain.entity.User;
import com.vision_rent.automovil_unite.domain.entity.Vehicle;
import com.vision_rent.automovil_unite.domain.repository.UserRepository;
import com.vision_rent.automovil_unite.domain.repository.VehicleRepository;
import com.vision_rent.automovil_unite.domain.service.UserDomainService;
import com.vision_rent.automovil_unite.infrastructure.storage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de vehículos.
 */
@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final UserDomainService userDomainService;
    private final FileStorageService fileStorageService;
    private final VehicleDtoMapper vehicleDtoMapper;
    private final UserDtoMapper userDtoMapper;

    /**
     * Crea un nuevo vehículo.
     *
     * @param ownerId ID del propietario
     * @param request Datos del vehículo
     * @return DTO del vehículo creado
     */
    @Transactional
    public VehicleDto createVehicle(Long ownerId, CreateVehicleRequest request) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", ownerId));

        // Verificar si el usuario puede publicar un vehículo
        if (!userDomainService.canPublishVehicle(owner)) {
            throw new UnauthorizedOperationException("El usuario no tiene permisos para publicar vehículos");
        }

        Vehicle vehicle = vehicleDtoMapper.toEntity(request);
        vehicle.setOwner(owner);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return vehicleDtoMapper.toDto(savedVehicle);
    }


    @Transactional
    public void verifyUserEmail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId));
        user.setEmailVerified(true);
        userRepository.save(user);
    }

    /**
     * Busca vehículos por marca.
     *
     * @param brand Marca a buscar
     * @return Lista de DTOs de vehículo
     */
    @Transactional(readOnly = true)
    public List<VehicleDto> findByBrand(String brand) {
        return vehicleRepository.findByBrand(brand)
                .stream()
                .map(vehicleDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un vehículo por su ID.
     *
     * @param vehicleId ID del vehículo
     * @return DTO del vehículo
     */
    @Transactional(readOnly = true)
    public VehicleDto getVehicleById(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo", "id", vehicleId));

        return vehicleDtoMapper.toDto(vehicle);
    }

    /**
     * Obtiene todos los vehículos de un propietario.
     *
     * @param ownerId ID del propietario
     * @return Lista de DTOs de vehículo
     */
    @Transactional(readOnly = true)
    public List<VehicleDto> getVehiclesByOwnerId(Long ownerId) {
        return vehicleRepository.findByOwnerId(ownerId)
                .stream()
                .map(vehicleDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los vehículos disponibles.
     *
     * @return Lista de DTOs de vehículo
     */
    @Transactional(readOnly = true)
    public List<VehicleDto> getAvailableVehicles() {
        return vehicleRepository.findAvailable()
                .stream()
                .map(vehicleDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los vehículos disponibles en un rango de fechas.
     *
     * @param startDateTime Fecha y hora de inicio
     * @param endDateTime Fecha y hora de fin
     * @return Lista de DTOs de vehículo
     */
    @Transactional(readOnly = true)
    public List<VehicleDto> getAvailableVehiclesInDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return vehicleRepository.findAvailableInDateRange(startDateTime, endDateTime)
                .stream()
                .map(vehicleDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los vehículos más alquilados.
     *
     * @param limit Límite de resultados
     * @return Lista de DTOs de vehículo
     */
    @Transactional(readOnly = true)
    public List<VehicleDto> getMostRentedVehicles(int limit) {
        return vehicleRepository.findMostRented(limit)
                .stream()
                .map(vehicleDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los vehículos mejor calificados.
     *
     * @param limit Límite de resultados
     * @return Lista de DTOs de vehículo
     */
    @Transactional(readOnly = true)
    public List<VehicleDto> getTopRatedVehicles(int limit) {
        return vehicleRepository.findTopRated(limit)
                .stream()
                .map(vehicleDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene las marcas más populares.
     *
     * @param limit Límite de resultados
     * @return Lista de marcas
     */
    @Transactional(readOnly = true)
    public List<String> getMostPopularBrands(int limit) {
        return vehicleRepository.findMostPopularBrands(limit);
    }

    /**
     * Actualiza los datos de un vehículo.
     *
     * @param vehicleId ID del vehículo
     * @param vehicleDto Datos actualizados del vehículo
     * @param userId ID del usuario que realiza la operación
     * @return DTO del vehículo actualizado
     */
    @Transactional
    public VehicleDto updateVehicle(Long vehicleId, VehicleDto vehicleDto, Long userId) {
        Vehicle existingVehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo", "id", vehicleId));

        // Verificar si el usuario es el propietario del vehículo
        if (!existingVehicle.getOwner().getId().equals(userId)) {
            throw new UnauthorizedOperationException("Solo el propietario puede actualizar el vehículo");
        }

        // Actualizar campos editables
        existingVehicle.setBrand(vehicleDto.getBrand());
        existingVehicle.setModel(vehicleDto.getModel());
        existingVehicle.setYear(vehicleDto.getYear());
        existingVehicle.setColor(vehicleDto.getColor());
        existingVehicle.setTransmission(vehicleDto.getTransmission());
        existingVehicle.setFuelType(vehicleDto.getFuelType());
        existingVehicle.setSeats(vehicleDto.getSeats());
        existingVehicle.setCategory(vehicleDto.getCategory());
        existingVehicle.setPricePerDay(vehicleDto.getPricePerDay());
        existingVehicle.setDescription(vehicleDto.getDescription());
        existingVehicle.setAvailable(vehicleDto.isAvailable());

        Vehicle updatedVehicle = vehicleRepository.save(existingVehicle);
        return vehicleDtoMapper.toDto(updatedVehicle);
    }

    /**
     * Sube una foto para un vehículo.
     *
     * @param vehicleId ID del vehículo
     * @param file Archivo de imagen
     * @param userId ID del usuario que realiza la operación
     * @return DTO del vehículo actualizado
     */
    @Transactional
    public VehicleDto uploadVehiclePhoto(Long vehicleId, MultipartFile file, Long userId) throws IOException {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo", "id", vehicleId));

        // Verificar si el usuario es el propietario del vehículo
        if (!vehicle.getOwner().getId().equals(userId)) {
            throw new UnauthorizedOperationException("Solo el propietario puede subir fotos del vehículo");
        }

        String fileName = fileStorageService.storeFile(file, "vehicles/" + vehicleId + "/photos");
        vehicle.addPhoto(fileName);

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return vehicleDtoMapper.toDto(updatedVehicle);
    }

    /**
     * Elimina un vehículo.
     *
     * @param vehicleId ID del vehículo
     * @param userId ID del usuario que realiza la operación
     */
    @Transactional
    public void deleteVehicle(Long vehicleId, Long userId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo", "id", vehicleId));

        // Verificar si el usuario es el propietario del vehículo
        if (!vehicle.getOwner().getId().equals(userId)) {
            throw new UnauthorizedOperationException("Solo el propietario puede eliminar el vehículo");
        }

        // TODO: Verificar si el vehículo tiene alquileres activos antes de eliminar

        vehicleRepository.delete(vehicle);
    }
}