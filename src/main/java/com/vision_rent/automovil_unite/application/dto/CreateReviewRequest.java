package com.vision_rent.automovil_unite.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitud de creación de reseña.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequest {

    @NotNull(message = "El ID del alquiler es obligatorio")
    private Long rentalId;

    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    private Float rating;

    @Size(max = 500, message = "El comentario debe tener máximo 500 caracteres")
    private String comment;

    @NotNull(message = "El tipo de reseña es obligatorio")
    private String type; // USER_REVIEW, VEHICLE_REVIEW;
}