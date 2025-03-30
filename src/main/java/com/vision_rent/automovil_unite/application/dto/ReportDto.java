package com.vision_rent.automovil_unite.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para transferir datos de reporte entre capas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
    private Long id;
    private Long rentalId;
    private UserDto reporter;
    private UserDto reported;
    private String reason;
    private String description;
    private String status;
    private String resolution;
    private UserDto resolvedBy;
    private LocalDateTime createdAt;
}