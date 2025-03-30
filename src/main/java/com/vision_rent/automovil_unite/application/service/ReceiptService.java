package com.vision_rent.automovil_unite.application.service;

import com.vision_rent.automovil_unite.application.dto.ReceiptDto;
import com.vision_rent.automovil_unite.application.exception.InvalidOperationException;
import com.vision_rent.automovil_unite.application.exception.ResourceNotFoundException;
import com.vision_rent.automovil_unite.application.mapper.ReceiptDtoMapper;
import com.vision_rent.automovil_unite.domain.entity.Payment;
import com.vision_rent.automovil_unite.domain.entity.Receipt;
import com.vision_rent.automovil_unite.domain.entity.Rental;
import com.vision_rent.automovil_unite.domain.repository.PaymentRepository;
import com.vision_rent.automovil_unite.domain.repository.ReceiptRepository;
import com.vision_rent.automovil_unite.domain.repository.RentalRepository;
import com.vision_rent.automovil_unite.domain.service.ReceiptDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de boletas/recibos.
 */
@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final PaymentRepository paymentRepository;
    private final RentalRepository rentalRepository;
    private final ReceiptDomainService receiptDomainService;
    private final NotificationService notificationService;
    private final ReceiptDtoMapper receiptDtoMapper;
    
    /**
     * Genera una boleta para un pago.
     *
     * @param paymentId ID del pago
     * @return DTO de la boleta generada
     */
    @Transactional
    public ReceiptDto generateReceipt(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Pago", "id", paymentId));
        
        // Verificar que el pago está completado
        if (!"COMPLETED".equals(payment.getStatus())) {
            throw new InvalidOperationException("No se puede generar una boleta para un pago no completado");
        }
        
        Rental rental = payment.getRental();
        
        // Verificar si ya existe una boleta para este pago
        List<Receipt> existingReceipts = receiptRepository.findByPaymentId(paymentId);
        if (!existingReceipts.isEmpty()) {
            return receiptDtoMapper.toDto(existingReceipts.get(0));
        }
        
        // Generar la boleta
        Receipt receipt = receiptDomainService.generateReceipt(payment, rental);
        Receipt savedReceipt = receiptRepository.save(receipt);
        
        // Enviar notificación
        notificationService.notifyReceiptGenerated(savedReceipt);
        
        return receiptDtoMapper.toDto(savedReceipt);
    }
    
    /**
     * Obtiene una boleta por su ID.
     *
     * @param receiptId ID de la boleta
     * @param userId ID del usuario que solicita
     * @return DTO de la boleta
     */
    @Transactional(readOnly = true)
    public ReceiptDto getReceiptById(Long receiptId, Long userId) {
        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new ResourceNotFoundException("Boleta", "id", receiptId));
        
        // Verificar que el usuario es el arrendatario o el propietario
        boolean isRenter = receipt.getRenter().getId().equals(userId);
        boolean isOwner = receipt.getOwner().getId().equals(userId);
        
        if (!isRenter && !isOwner) {
            throw new InvalidOperationException("No tiene permiso para acceder a esta boleta");
        }
        
        return receiptDtoMapper.toDto(receipt);
    }
    
    /**
     * Obtiene una boleta por su número.
     *
     * @param receiptNumber Número de la boleta
     * @param userId ID del usuario que solicita
     * @return DTO de la boleta
     */
    @Transactional(readOnly = true)
    public ReceiptDto getReceiptByNumber(String receiptNumber, Long userId) {
        Receipt receipt = receiptRepository.findByReceiptNumber(receiptNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Boleta", "número", receiptNumber));
        
        // Verificar que el usuario es el arrendatario o el propietario
        boolean isRenter = receipt.getRenter().getId().equals(userId);
        boolean isOwner = receipt.getOwner().getId().equals(userId);
        
        if (!isRenter && !isOwner) {
            throw new InvalidOperationException("No tiene permiso para acceder a esta boleta");
        }
        
        return receiptDtoMapper.toDto(receipt);
    }
    
    /**
     * Obtiene todas las boletas generadas para un usuario como arrendatario.
     *
     * @param userId ID del usuario
     * @return Lista de DTOs de boleta
     */
    @Transactional(readOnly = true)
    public List<ReceiptDto> getReceiptsByRenterId(Long userId) {
        return receiptRepository.findByRenterId(userId)
                .stream()
                .map(receiptDtoMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene todas las boletas generadas para un usuario como propietario.
     *
     * @param userId ID del usuario
     * @return Lista de DTOs de boleta
     */
    @Transactional(readOnly = true)
    public List<ReceiptDto> getReceiptsByOwnerId(Long userId) {
        return receiptRepository.findByOwnerId(userId)
                .stream()
                .map(receiptDtoMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene todas las boletas generadas para un alquiler.
     *
     * @param rentalId ID del alquiler
     * @param userId ID del usuario que solicita
     * @return Lista de DTOs de boleta
     */
    @Transactional(readOnly = true)
    public List<ReceiptDto> getReceiptsByRentalId(Long rentalId, Long userId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Alquiler", "id", rentalId));
        
        // Verificar que el usuario es el arrendatario o el propietario
        boolean isRenter = rental.getRenter().getId().equals(userId);
        boolean isOwner = rental.getVehicle().getOwner().getId().equals(userId);
        
        if (!isRenter && !isOwner) {
            throw new InvalidOperationException("No tiene permiso para acceder a las boletas de este alquiler");
        }
        
        return receiptRepository.findByRentalId(rentalId)
                .stream()
                .map(receiptDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}