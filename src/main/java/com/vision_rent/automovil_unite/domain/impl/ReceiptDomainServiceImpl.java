package com.vision_rent.automovil_unite.domain.impl;



import com.vision_rent.automovil_unite.application.exception.InvalidOperationException;
import com.vision_rent.automovil_unite.domain.entity.Payment;
import com.vision_rent.automovil_unite.domain.entity.Receipt;
import com.vision_rent.automovil_unite.domain.entity.Rental;
import com.vision_rent.automovil_unite.domain.service.ReceiptDomainService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Implementación del servicio de dominio para operaciones relacionadas con boletas/recibos.
 */
@Service
public class ReceiptDomainServiceImpl implements ReceiptDomainService {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.18"); // 18% IGV
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    @Override
    public Receipt generateReceipt(Payment payment, Rental rental) {
        if (!"COMPLETED".equals(payment.getStatus())) {
            throw new InvalidOperationException("No se puede generar una boleta para un pago no completado");
        }
        
        // Calcular el número de días de alquiler
        int rentalDays = calculateRentalDays(rental);
        
        // Crear la boleta
        Receipt receipt = new Receipt();
        receipt.setReceiptNumber(generateReceiptNumber());
        receipt.setPayment(payment);
        receipt.setRental(rental);
        receipt.setRenter(rental.getRenter());
        receipt.setOwner(rental.getVehicle().getOwner());
        receipt.setVehicle(rental.getVehicle());
        receipt.setIssueDate(LocalDateTime.now());
        
        // Calcular montos
        BigDecimal subtotal = payment.getAmount().divide(BigDecimal.ONE.add(TAX_RATE), 2, RoundingMode.HALF_UP);
        BigDecimal taxAmount = payment.getAmount().subtract(subtotal);
        
        receipt.setSubtotal(subtotal);
        receipt.setTaxAmount(taxAmount);
        receipt.setTotalAmount(payment.getAmount());
        receipt.setRentalDays(rentalDays);
        receipt.setPricePerDay(rental.getVehicle().getPricePerDay());
        receipt.setCurrency(payment.getCurrency());
        receipt.setStatus("ISSUED");
        
        // Generar PDF (simulado)
        receipt.setPdfUrl(generateReceiptPdf(receipt));
        
        return receipt;
    }

    @Override
    public BigDecimal calculateTaxAmount(BigDecimal subtotal) {
        return subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public Receipt cancelReceipt(Receipt receipt) {
        if ("CANCELLED".equals(receipt.getStatus()) || "REFUNDED".equals(receipt.getStatus())) {
            throw new InvalidOperationException("La boleta ya ha sido cancelada o reembolsada");
        }
        
        receipt.setStatus("CANCELLED");
        return receipt;
    }

    @Override
    public String generateReceiptNumber() {
        // Formato: YYYYMMDD-XXXXX (fecha actual + 5 dígitos aleatorios)
        String datePart = LocalDateTime.now().format(DATE_FORMATTER);
        String randomPart = String.format("%05d", (int) (Math.random() * 100000));
        
        return datePart + "-" + randomPart;
    }

    @Override
    public String generateReceiptPdf(Receipt receipt) {
        // En un sistema real, aquí se generaría un PDF real y se almacenaría en un servicio de almacenamiento
        // Para esta simulación, simplemente generamos una URL ficticia
        String filename = "receipt_" + receipt.getReceiptNumber().replace("-", "_") + ".pdf";
        return "receipts/" + receipt.getRenter().getId() + "/" + filename;
    }
    
    /**
     * Calcula el número de días de alquiler.
     */
    private int calculateRentalDays(Rental rental) {
        Duration duration = Duration.between(rental.getStartDateTime(), rental.getEndDateTime());
        long hours = duration.toHours();
        
        // Si son menos de 24 horas, se considera 1 día
        if (hours <= 24) {
            return 1;
        }
        
        // Para periodos más largos, calcular basado en días (redondeando hacia arriba)
        return (int) Math.ceil((double) hours / 24);
    }
}