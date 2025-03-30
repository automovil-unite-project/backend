package com.vision_rent.automovil_unite.infrastructure.persistence.entity;

import jakarta.persistence.*;
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
 * Entidad JPA que representa un vehículo en la base de datos.
 */
@Entity
@Table(name = "vehicles")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleJpaEntity extends BaseJpaEntity {
    
    @Column(nullable = false)
    private String brand;
    
    @Column(nullable = false)
    private String model;
    
    @Column(nullable = false)
    private int year;
    
    @Column(name = "license_plate", nullable = false, unique = true)
    private String licensePlate;
    
    @Column(nullable = false)
    private String color;
    
    @Column(nullable = false)
    private String transmission;
    
    @Column(name = "fuel_type", nullable = false)
    private String fuelType;
    
    @Column(nullable = false)
    private int seats;
    
    @Column(nullable = false)
    private String category;
    
    @Column(name = "price_per_day", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerDay;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserJpaEntity owner;
    
    @Column(nullable = false)
    private boolean available;
    
    @Column(name = "rent_count", nullable = false)
    private int rentCount;
    
    @Column(name = "average_rating")
    private Float averageRating;
    
    @ElementCollection
    @CollectionTable(name = "vehicle_photos", joinColumns = @JoinColumn(name = "vehicle_id"))
    @Column(name = "photo_url", nullable = false)
    @Builder.Default
    private List<String> photoUrls = new ArrayList<>();
    
    @Column(name = "last_rental_end")
    private LocalDateTime lastRentalEnd;
}