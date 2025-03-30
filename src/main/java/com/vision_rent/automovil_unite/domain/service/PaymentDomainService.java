package com.vision_rent.automovil_unite.domain.service;



import com.vision_rent.automovil_unite.domain.entity.Payment;
import com.vision_rent.automovil_unite.domain.entity.PaymentMethod;
import com.vision_rent.automovil_unite.domain.entity.Rental;
import com.vision_rent.automovil_unite.domain.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Servicio de dominio para operaciones relacionadas con pagos.
 */

@Service
public interface PaymentDomainService {
    
    /**
     * Procesa un pago utilizando un método de pago específico.
     * 
     * @param rental Alquiler a pagar
     * @param paymentMethod Método de pago a utilizar
     * @param amount Monto a pagar
     * @param paymentType Tipo de pago (RENTAL, EXTENSION, SECURITY_DEPOSIT)
     * @return Pago procesado
     */
    Payment processPayment(Rental rental, PaymentMethod paymentMethod, BigDecimal amount, String paymentType);
    
    /**
     * Reembolsa un pago.
     * 
     * @param payment Pago a reembolsar
     * @param amount Monto a reembolsar (puede ser parcial)
     * @param reason Motivo del reembolso
     * @return Pago actualizado
     */
    Payment refundPayment(Payment payment, BigDecimal amount, String reason);
    
    /**
     * Valida un método de pago.
     * 
     * @param paymentMethod Método de pago a validar
     * @return true si el método de pago es válido y está activo
     */
    boolean validatePaymentMethod(PaymentMethod paymentMethod);
    
    /**
     * Calcula el monto total a pagar por un alquiler.
     * 
     * @param rental Alquiler
     * @return Monto total
     */
    BigDecimal calculateRentalPaymentAmount(Rental rental);
    
    /**
     * Calcula el monto del depósito de seguridad.
     * 
     * @param rental Alquiler
     * @return Monto del depósito
     */
    BigDecimal calculateSecurityDepositAmount(Rental rental);
    
    /**
     * Verifica si un usuario tiene un método de pago por defecto establecido.
     * 
     * @param user Usuario a verificar
     * @return true si el usuario tiene un método de pago por defecto
     */
    boolean hasDefaultPaymentMethod(User user);
}