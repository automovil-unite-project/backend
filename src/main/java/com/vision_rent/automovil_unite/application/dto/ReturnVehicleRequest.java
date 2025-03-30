package com.vision_rent.automovil_unite.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para solicitud de devolución de vehículo.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnVehicleRequest {
    
    @NotNull(message = "El ID del alquiler es obligatorio")
    private Long rentalId;
    
    private LocalDateTime returnDateTime;
}