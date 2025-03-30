package com.vision_rent.automovil_unite.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para solicitud de creación de vehículo.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVehicleRequest {
    
    @NotBlank(message = "La marca es obligatoria")
    private String brand;
    
    @NotBlank(message = "El modelo es obligatorio")
    private String model;
    
    @Min(value = 1900, message = "El año debe ser mayor a 1900")
    @Max(value = 2100, message = "El año debe ser menor a 2100")
    private int year;
    
    @NotBlank(message = "La placa es obligatoria")
    @Pattern(regexp = "^[A-Z0-9-]{5,10}$", message = "Formato de placa inválido")
    private String licensePlate;
    
    @NotBlank(message = "El color es obligatorio")
    private String color;
    
    @NotBlank(message = "El tipo de transmisión es obligatorio")
    private String transmission;
    
    @NotBlank(message = "El tipo de combustible es obligatorio")
    private String fuelType;
    
    @Min(value = 1, message = "El número de asientos debe ser al menos 1")
    @Max(value = 20, message = "El número de asientos debe ser máximo 20")
    private int seats;
    
    @NotBlank(message = "La categoría es obligatoria")
    private String category;
    
    @NotNull(message = "El precio por día es obligatorio")
    @Positive(message = "El precio por día debe ser positivo")
    private BigDecimal pricePerDay;
    
    @Size(max = 1000, message = "La descripción debe tener máximo 1000 caracteres")
    private String description;
}