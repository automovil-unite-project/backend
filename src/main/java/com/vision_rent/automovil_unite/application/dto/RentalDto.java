package com.vision_rent.automovil_unite.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para transferir datos de alquiler entre capas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalDto {
    private Long id;
    private VehicleDto vehicle;
    private UserDto renter;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime actualReturnDateTime;
    private BigDecimal totalPrice;
    private BigDecimal securityDeposit;
    private String status;
    private String paymentId;
    private boolean paid;
    private Float renterRating;
    private String renterReview;
    private Float vehicleRating;
    private String vehicleReview;
    private boolean renterReported;
    private String renterReportReason;
    private boolean lateReturn;
    private boolean discountApplied;
    private BigDecimal discountAmount;
    private BigDecimal lateReturnFee;
    private LocalDateTime extendedUntil;
}