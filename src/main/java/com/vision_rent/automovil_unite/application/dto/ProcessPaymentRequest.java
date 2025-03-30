package com.vision_rent.automovil_unite.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitud de procesamiento de pago.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentRequest {
    
    @NotNull(message = "El ID del alquiler es obligatorio")
    private Long rentalId;
    
    @NotNull(message = "El ID del método de pago es obligatorio")
    private Long paymentMethodId;
    
    private String paymentType; // RENTAL, EXTENSION, SECURITY_DEPOSIT
    
    // Para pagos con tarjeta que requieren CVV adicional
    private String cvv;
}