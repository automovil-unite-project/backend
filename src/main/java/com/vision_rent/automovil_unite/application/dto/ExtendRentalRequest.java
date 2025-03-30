package com.vision_rent.automovil_unite.application.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para solicitud de extensión de alquiler.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtendRentalRequest {
    
    @NotNull(message = "El ID del alquiler es obligatorio")
    private Long rentalId;
    
    @NotNull(message = "La nueva fecha de fin es obligatoria")
    @Future(message = "La nueva fecha de fin debe ser en el futuro")
    private LocalDateTime newEndDateTime;
    
    private String paymentId;
}