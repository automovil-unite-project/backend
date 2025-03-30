package com.vision_rent.automovil_unite.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para transferir datos de vehículo entre capas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {
    private Long id;
    private String brand;
    private String model;
    private int year;
    private String licensePlate;
    private String color;
    private String transmission;
    private String fuelType;
    private int seats;
    private String category;
    private BigDecimal pricePerDay;
    private String description;
    private UserDto owner;
    private boolean available;
    private int rentCount;
    private Float averageRating;
    private List<String> photoUrls;
    private LocalDateTime lastRentalEnd;
}