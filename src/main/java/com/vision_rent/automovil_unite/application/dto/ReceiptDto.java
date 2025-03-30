package com.vision_rent.automovil_unite.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para transferir datos de boleta/recibo entre capas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDto {
    private Long id;
    private String receiptNumber;
    private Long paymentId;
    private Long rentalId;
    private UserDto renter;
    private UserDto owner;
    private VehicleDto vehicle;
    private LocalDateTime issueDate;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private Integer rentalDays;
    private BigDecimal pricePerDay;
    private String currency;
    private String status;
    private String pdfUrl;
}