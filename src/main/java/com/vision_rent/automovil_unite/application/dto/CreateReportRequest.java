package com.vision_rent.automovil_unite.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitud de creación de reporte.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReportRequest {
    
    @NotNull(message = "El ID del alquiler es obligatorio")
    private Long rentalId;
    
    @NotNull(message = "El ID del usuario reportado es obligatorio")
    private Long reportedId;
    
    @NotBlank(message = "El motivo del reporte es obligatorio")
    private String reason;
    
    @Size(max = 1000, message = "La descripción debe tener máximo 1000 caracteres")
    private String description;
}