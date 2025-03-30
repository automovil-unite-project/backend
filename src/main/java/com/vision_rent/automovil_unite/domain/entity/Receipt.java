package com.vision_rent.automovil_unite.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad de dominio que representa una boleta/recibo de alquiler.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Receipt extends BaseEntity {
    private String receiptNumber;
    private Payment payment;
    private Rental rental;
    private User renter;
    private User owner;
    private Vehicle vehicle;
    private LocalDateTime issueDate;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private Integer rentalDays;
    private BigDecimal pricePerDay;
    private String currency;
    private String status; // ISSUED, CANCELED, REFUNDED
    private String pdfUrl; // URL al PDF de la boleta
}