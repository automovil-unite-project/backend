package com.vision_rent.automovil_unite.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad de dominio que representa un vehículo.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle extends BaseEntity {
    private String brand;
    private String model;
    private int year;
    private String licensePlate;
    private String color;
    private String transmission;  // AUTOMATIC, MANUAL
    private String fuelType;      // GASOLINE, DIESEL, ELECTRIC, HYBRID
    private int seats;
    private String category;      // SEDAN, SUV, HATCHBACK, etc.
    private BigDecimal pricePerDay;
    private String description;
    private User owner;
    private boolean available;
    private int rentCount;
    private Float averageRating;
    private List<String> photoUrls;
    private LocalDateTime lastRentalEnd;
    
    public boolean isAvailableForRental() {
        return available && (lastRentalEnd == null || 
                LocalDateTime.now().isAfter(lastRentalEnd.plusDays(1)));
    }
    
    public void addPhoto(String photoUrl) {
        if (photoUrls == null) {
            photoUrls = new ArrayList<>();
        }
        photoUrls.add(photoUrl);
    }
    
    public void updateRating(Float newRating) {
        if (averageRating == null) {
            averageRating = newRating;
        } else {
            // Actualizar el promedio considerando la nueva calificación
            float totalRating = averageRating * rentCount;
            totalRating += newRating;
            averageRating = totalRating / (rentCount + 1);
        }
    }
}