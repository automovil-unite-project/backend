package com.vision_rent.automovil_unite.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Entidad de dominio que representa una reseña.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntity {
    private Rental rental;
    private User reviewer;
    private User reviewed;        // Usuario que recibe la reseña (puede ser el arrendatario o dueño)
    private Vehicle vehicle;      // Null si la reseña es para un usuario
    private Float rating;
    private String comment;
    private String type;          // USER_REVIEW, VEHICLE_REVIEW
    
    public boolean isUserReview() {
        return "USER_REVIEW".equals(type);
    }
    
    public boolean isVehicleReview() {
        return "VEHICLE_REVIEW".equals(type);
    }
}