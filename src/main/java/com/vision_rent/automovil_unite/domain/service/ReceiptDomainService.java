package com.vision_rent.automovil_unite.domain.service;


import com.vision_rent.automovil_unite.domain.entity.Payment;
import com.vision_rent.automovil_unite.domain.entity.Receipt;
import com.vision_rent.automovil_unite.domain.entity.Rental;
import org.springframework.stereotype.Service;

/**
 * Servicio de dominio para operaciones relacionadas con boletas/recibos.
 */
@Service
public interface ReceiptDomainService {
    
    /**
     * Genera una boleta para un pago.
     * 
     * @param payment Pago realizado
     * @param rental Alquiler relacionado
     * @return Boleta generada
     */
    Receipt generateReceipt(Payment payment, Rental rental);
    
    /**
     * Calcula el monto de impuestos para una boleta.
     * 
     * @param subtotal Subtotal de la boleta
     * @return Monto de impuestos
     */
    java.math.BigDecimal calculateTaxAmount(java.math.BigDecimal subtotal);
    
    /**
     * Cancela una boleta.
     * 
     * @param receipt Boleta a cancelar
     * @return Boleta cancelada
     */
    Receipt cancelReceipt(Receipt receipt);
    
    /**
     * Genera el número de boleta.
     * 
     * @return Número de boleta generado
     */
    String generateReceiptNumber();
    
    /**
     * Genera un PDF para la boleta.
     * 
     * @param receipt Boleta
     * @return URL del PDF generado
     */
    String generateReceiptPdf(Receipt receipt);
}