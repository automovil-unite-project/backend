package com.vision_rent.automovil_unite.application.service;

import com.vision_rent.automovil_unite.application.dto.*;
import com.vision_rent.automovil_unite.application.exception.InvalidOperationException;
import com.vision_rent.automovil_unite.application.exception.ResourceNotFoundException;
import com.vision_rent.automovil_unite.application.mapper.PaymentDtoMapper;
import com.vision_rent.automovil_unite.domain.entity.Payment;
import com.vision_rent.automovil_unite.domain.entity.PaymentMethod;
import com.vision_rent.automovil_unite.domain.entity.Receipt;
import com.vision_rent.automovil_unite.domain.entity.Rental;
import com.vision_rent.automovil_unite.domain.entity.User;
import com.vision_rent.automovil_unite.domain.exception.InvalidPaymentMethodException;
import com.vision_rent.automovil_unite.domain.exception.PaymentProcessingException;
import com.vision_rent.automovil_unite.domain.repository.PaymentMethodRepository;
import com.vision_rent.automovil_unite.domain.repository.PaymentRepository;
import com.vision_rent.automovil_unite.domain.repository.RentalRepository;
import com.vision_rent.automovil_unite.domain.repository.UserRepository;
import com.vision_rent.automovil_unite.domain.service.PaymentDomainService;
import com.vision_rent.automovil_unite.domain.service.ReceiptDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de pagos.
 */
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final PaymentDomainService paymentDomainService;
    private final ReceiptDomainService receiptDomainService;
    private final NotificationService notificationService;
    private final PaymentDtoMapper paymentDtoMapper;
    
    /**
     * Procesa un pago para un alquiler.
     *
     * @param userId ID del usuario que realiza el pago
     * @param request Datos del pago
     * @return DTO del pago realizado
     */
    @Transactional
    public PaymentDto processPayment(Long userId, ProcessPaymentRequest request) {
        User payer = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId));
        
        Rental rental = rentalRepository.findById(request.getRentalId())
                .orElseThrow(() -> new ResourceNotFoundException("Alquiler", "id", request.getRentalId()));
        
        // Verificar que el usuario es el arrendatario
        if (!rental.getRenter().getId().equals(userId)) {
            throw new InvalidOperationException("Solo el arrendatario puede realizar el pago");
        }
        
        // Verificar que el alquiler está pendiente de pago
        if (!"PENDING".equals(rental.getStatus()) && !"CONFIRMED".equals(rental.getStatus())) {
            throw new InvalidOperationException("El alquiler no está en estado válido para realizar el pago");
        }
        
        // Obtener el método de pago
        PaymentMethod paymentMethod = paymentMethodRepository.findById(request.getPaymentMethodId())
                .orElseThrow(() -> new ResourceNotFoundException("Método de pago", "id", request.getPaymentMethodId()));
        
        // Verificar que el método de pago pertenece al usuario
        if (!paymentMethod.getUser().getId().equals(userId)) {
            throw new InvalidOperationException("El método de pago no pertenece al usuario");
        }
        
        // Determinar el tipo de pago
        String paymentType = request.getPaymentType();
        if (paymentType == null) {
            paymentType = "RENTAL"; // Por defecto
        }
        
        try {
            // Calcular el monto según el tipo de pago
            java.math.BigDecimal amount;
            if ("RENTAL".equals(paymentType)) {
                amount = paymentDomainService.calculateRentalPaymentAmount(rental);
            } else if ("SECURITY_DEPOSIT".equals(paymentType)) {
                amount = paymentDomainService.calculateSecurityDepositAmount(rental);
            } else if ("EXTENSION".equals(paymentType)) {
                // El monto para extensión debería calcularse en base a la duración adicional
                // Este cálculo debería estar en otro lugar, pero lo simplificamos para este ejemplo
                amount = rental.getTotalPrice();
            } else {
                throw new InvalidOperationException("Tipo de pago inválido");
            }
            
            // Procesar el pago
            Payment payment = paymentDomainService.processPayment(rental, paymentMethod, amount, paymentType);
            
            // Si el pago es exitoso, actualizar el estado del alquiler
            if ("COMPLETED".equals(payment.getStatus())) {
                if ("PENDING".equals(rental.getStatus())) {
                    rental.setStatus("CONFIRMED");
                    rental.setPaid(true);
                }
                rentalRepository.save(rental);
                
                // Generar la boleta
                Receipt receipt = receiptDomainService.generateReceipt(payment, rental);
                payment.setReceiptUrl(receipt.getPdfUrl());
                payment = paymentRepository.save(payment);
                
                // Enviar notificaciones
                notificationService.notifyPaymentReceived(payment, rental.getRenter().getId(), rental.getVehicle().getOwner().getId());
            }
            
            return paymentDtoMapper.toDto(payment);
            
        } catch (InvalidPaymentMethodException | PaymentProcessingException e) {
            throw new InvalidOperationException(e.getMessage());
        }
    }
    
    /**
     * Obtiene un pago por su ID.
     *
     * @param paymentId ID del pago
     * @param userId ID del usuario que solicita
     * @return DTO del pago
     */
    @Transactional(readOnly = true)
    public PaymentDto getPaymentById(Long paymentId, Long userId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Pago", "id", paymentId));
        
        // Verificar que el usuario es el pagador o el dueño del vehículo
        boolean isPayer = payment.getPayer().getId().equals(userId);
        boolean isOwner = payment.getRental().getVehicle().getOwner().getId().equals(userId);
        
        if (!isPayer && !isOwner) {
            throw new InvalidOperationException("No tiene permiso para acceder a este pago");
        }
        
        return paymentDtoMapper.toDto(payment);
    }
    
    /**
     * Obtiene todos los pagos realizados por un usuario.
     *
     * @param userId ID del usuario
     * @return Lista de DTOs de pago
     */
    @Transactional(readOnly = true)
    public List<PaymentDto> getPaymentsByUserId(Long userId) {
        return paymentRepository.findByPayerId(userId)
                .stream()
                .map(paymentDtoMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene todos los pagos de un alquiler.
     *
     * @param rentalId ID del alquiler
     * @param userId ID del usuario que solicita
     * @return Lista de DTOs de pago
     */
    @Transactional(readOnly = true)
    public List<PaymentDto> getPaymentsByRentalId(Long rentalId, Long userId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Alquiler", "id", rentalId));
        
        // Verificar que el usuario es el arrendatario o el dueño del vehículo
        boolean isRenter = rental.getRenter().getId().equals(userId);
        boolean isOwner = rental.getVehicle().getOwner().getId().equals(userId);
        
        if (!isRenter && !isOwner) {
            throw new InvalidOperationException("No tiene permiso para acceder a los pagos de este alquiler");
        }
        
        return paymentRepository.findByRentalId(rentalId)
                .stream()
                .map(paymentDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}