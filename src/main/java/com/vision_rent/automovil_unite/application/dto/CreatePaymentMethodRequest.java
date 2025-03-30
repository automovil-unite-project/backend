package com.vision_rent.automovil_unite.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitud de creación de método de pago.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentMethodRequest {
    
    @NotBlank(message = "El tipo de pago es obligatorio")
    @Pattern(regexp = "^(CREDIT_CARD|DEBIT_CARD|PAYPAL)$", message = "Tipo de pago inválido")
    private String type;
    
    @NotBlank(message = "El proveedor es obligatorio")
    @Pattern(regexp = "^(VISA|MASTERCARD|AMERICAN_EXPRESS|PAYPAL)$", message = "Proveedor inválido")
    private String provider;
    
    @NotBlank(message = "El alias es obligatorio")
    private String alias;
    
    // Para tarjetas de crédito y débito
    private String cardNumber;
    
    private String expiryDate;
    
    private String cvv;
    
    private String cardholderName;
    
    // Para PayPal
    @Email(message = "Email de PayPal inválido")
    private String paypalEmail;
    
    private Boolean setAsDefault;
}