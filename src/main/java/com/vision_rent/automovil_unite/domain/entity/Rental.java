package com.vision_rent.automovil_unite.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Entidad de dominio que representa un alquiler de vehículo.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Rental extends BaseEntity {
    private Vehicle vehicle;
    private User renter;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime actualReturnDateTime;
    private BigDecimal totalPrice;
    private BigDecimal securityDeposit;
    private String status;  // PENDING, CONFIRMED, ACTIVE, COMPLETED, CANCELLED, LATE
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
    
    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startDateTime) && now.isBefore(endDateTime) && "ACTIVE".equals(status);
    }
    
    public boolean isLate() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(endDateTime) && 
               (actualReturnDateTime == null || now.isAfter(actualReturnDateTime)) && 
               !("COMPLETED".equals(status) || "CANCELLED".equals(status));
    }
    
    public boolean isEligibleForExtension() {
        return isActive() && vehicle.isAvailableForRental();
    }
    
    public boolean isReturnedLate() {
        if (actualReturnDateTime == null || endDateTime == null) {
            return false;
        }
        
        Duration lateTime = Duration.between(endDateTime, actualReturnDateTime);
        return lateTime.toMinutes() > 30;
    }
    
    public BigDecimal calculateTotalPrice() {
        if (startDateTime == null || endDateTime == null) {
            return BigDecimal.ZERO;
        }
        
        long days = Duration.between(startDateTime, endDateTime).toDays();
        if (days < 1) days = 1; // Mínimo un día
        
        BigDecimal basePrice = vehicle.getPricePerDay().multiply(BigDecimal.valueOf(days));
        
        // Aplicar descuento si corresponde
        if (discountApplied && discountAmount != null) {
            basePrice = basePrice.subtract(discountAmount);
        }
        
        // Agregar cargo por devolución tardía si corresponde
        if (lateReturn && lateReturnFee != null) {
            basePrice = basePrice.add(lateReturnFee);
        }
        
        return basePrice;
    }
}