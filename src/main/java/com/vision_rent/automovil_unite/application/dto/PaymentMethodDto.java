package com.vision_rent.automovil_unite.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferir datos de método de pago entre capas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDto {
    private Long id;
    private Long userId;
    private String type; // CREDIT_CARD, DEBIT_CARD, PAYPAL
    private String provider; // VISA, MASTERCARD, PAYPAL, etc.
    private String alias;
    
    // Datos parciales para UI (la info sensible queda oculta)
    private String maskedCardNumber; // Por ejemplo: **** **** **** 1234
    private String expiryDateFormatted; // Por ejemplo: MM/YY
    
    // Solo para PayPal
    private String paypalEmail;
    
    private Boolean isDefault;
}