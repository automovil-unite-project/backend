package com.vision_rent.automovil_unite.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad de dominio que representa un pago realizado.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {
    private String externalId; // ID proporcionado por el gateway de pago
    private Rental rental;
    private User payer;
    private PaymentMethod paymentMethod;
    private BigDecimal amount;
    private String currency; // Por defecto "PEN"
    private String status; // PENDING, COMPLETED, FAILED, REFUNDED
    private String type; // RENTAL, EXTENSION, SECURITY_DEPOSIT
    private LocalDateTime paymentDate;
    private String receiptUrl; // URL a la boleta/recibo generado
    private String failureReason;
}