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

/**
 * Entidad JPA que representa un alquiler en la base de datos.
 */
@Entity
@Table(name = "rentals")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RentalJpaEntity extends BaseJpaEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private VehicleJpaEntity vehicle;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_id", nullable = false)
    private UserJpaEntity renter;
    
    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;
    
    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;
    
    @Column(name = "actual_return_date_time")
    private LocalDateTime actualReturnDateTime;
    
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;
    
    @Column(name = "security_deposit", precision = 10, scale = 2)
    private BigDecimal securityDeposit;
    
    @Column(nullable = false)
    private String status;
    
    @Column(name = "payment_id")
    private String paymentId;
    
    @Column(nullable = false)
    private boolean paid;
    
    @Column(name = "renter_rating")
    private Float renterRating;
    
    @Column(name = "renter_review", columnDefinition = "TEXT")
    private String renterReview;
    
    @Column(name = "vehicle_rating")
    private Float vehicleRating;
    
    @Column(name = "vehicle_review", columnDefinition = "TEXT")
    private String vehicleReview;
    
    @Column(name = "renter_reported", nullable = false)
    private boolean renterReported;
    
    @Column(name = "renter_report_reason", columnDefinition = "TEXT")
    private String renterReportReason;
    
    @Column(name = "late_return", nullable = false)
    private boolean lateReturn;
    
    @Column(name = "discount_applied", nullable = false)
    private boolean discountApplied;
    
    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount;
    
    @Column(name = "late_return_fee", precision = 10, scale = 2)
    private BigDecimal lateReturnFee;
    
    @Column(name = "extended_until")
    private LocalDateTime extendedUntil;
}