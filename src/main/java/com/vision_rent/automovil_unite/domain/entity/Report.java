package com.vision_rent.automovil_unite.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Entidad de dominio que representa un reporte realizado por un usuario.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Report extends BaseEntity {
    private Rental rental;
    private User reporter;
    private User reported;
    private String reason;
    private String description;
    private String status;  // PENDING, REVIEWED, RESOLVED, DISMISSED
    private String resolution;
    private User resolvedBy;
}