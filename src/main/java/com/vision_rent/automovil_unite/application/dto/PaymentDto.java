package com.vision_rent.automovil_unite.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para transferir datos de pago entre capas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long id;
    private String externalId;
    private Long rentalId;
    private UserDto payer;
    private PaymentMethodDto paymentMethod;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String type;
    private LocalDateTime paymentDate;
    private String receiptUrl;
    private String failureReason;
}