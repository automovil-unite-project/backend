package com.vision_rent.automovil_unite.application.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para solicitud de creación de alquiler.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRentalRequest {
    
    @NotNull(message = "El ID del vehículo es obligatorio")
    private Long vehicleId;
    
    @NotNull(message = "La fecha de inicio es obligatoria")
    @Future(message = "La fecha de inicio debe ser en el futuro")
    private LocalDateTime startDateTime;
    
    @NotNull(message = "La fecha de fin es obligatoria")
    @Future(message = "La fecha de fin debe ser en el futuro")
    private LocalDateTime endDateTime;
    
    private String paymentId;
}