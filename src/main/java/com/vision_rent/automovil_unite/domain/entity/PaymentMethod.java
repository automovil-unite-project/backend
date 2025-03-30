package com.vision_rent.automovil_unite.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Entidad de dominio que representa un método de pago de un usuario.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod extends BaseEntity {
    private User user;
    private String type; // CREDIT_CARD, DEBIT_CARD, PAYPAL
    private String provider; // VISA, MASTERCARD, PAYPAL, etc.
    private String alias;
    
    // Campos encriptados (sólo para tarjetas)
    private String encryptedCardNumber; // Solo últimos 4 dígitos en claro
    private String encryptedExpiryDate;
    private String tokenizedData; // Token proporcionado por el gateway de pago
    
    // Para PayPal
    private String paypalEmail;
    
    private Boolean isDefault;
}