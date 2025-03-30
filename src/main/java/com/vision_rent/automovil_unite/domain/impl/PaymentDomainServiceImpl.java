package com.vision_rent.automovil_unite.domain.impl;



import com.vision_rent.automovil_unite.domain.entity.Payment;
import com.vision_rent.automovil_unite.domain.entity.PaymentMethod;
import com.vision_rent.automovil_unite.domain.entity.Rental;
import com.vision_rent.automovil_unite.domain.entity.User;
import com.vision_rent.automovil_unite.domain.exception.InvalidPaymentMethodException;
import com.vision_rent.automovil_unite.domain.exception.PaymentProcessingException;
import com.vision_rent.automovil_unite.domain.service.PaymentDomainService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Implementación del servicio de dominio para operaciones relacionadas con pagos.
 */
@Service
public class PaymentDomainServiceImpl implements PaymentDomainService {

    private static final BigDecimal SECURITY_DEPOSIT_RATE = new BigDecimal("0.30"); // 30% del total
    private static final String DEFAULT_CURRENCY = "PEN";
    
    @Override
    public Payment processPayment(Rental rental, PaymentMethod paymentMethod, BigDecimal amount, String paymentType) {
        if (!validatePaymentMethod(paymentMethod)) {
            throw new InvalidPaymentMethodException("El método de pago no es válido o está expirado");
        }
        
        // En un sistema real, aquí se integraría con un gateway de pago
        // Este es un simulador básico
        
        Payment payment = new Payment();
        payment.setRental(rental);
        payment.setPayer(rental.getRenter());
        payment.setPaymentMethod(paymentMethod);
        payment.setAmount(amount);
        payment.setCurrency(DEFAULT_CURRENCY);
        payment.setStatus("COMPLETED"); // En un sistema real, podría ser PENDING inicialmente
        payment.setType(paymentType);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setExternalId("SIM-" + System.currentTimeMillis()); // Simulado
        
        return payment;
    }

    @Override
    public Payment refundPayment(Payment payment, BigDecimal amount, String reason) {
        if (!"COMPLETED".equals(payment.getStatus())) {
            throw new PaymentProcessingException("Solo se pueden reembolsar pagos completados");
        }
        
        if (amount.compareTo(payment.getAmount()) > 0) {
            throw new PaymentProcessingException("El monto a reembolsar no puede ser mayor al monto pagado");
        }
        
        // En un sistema real, aquí se integraría con un gateway de pago para el reembolso
        
        Payment refundPayment = new Payment();
        refundPayment.setRental(payment.getRental());
        refundPayment.setPayer(payment.getPayer());
        refundPayment.setPaymentMethod(payment.getPaymentMethod());
        refundPayment.setAmount(amount.negate()); // Monto negativo para indicar reembolso
        refundPayment.setCurrency(payment.getCurrency());
        refundPayment.setStatus("COMPLETED");
        refundPayment.setType("REFUND");
        refundPayment.setPaymentDate(LocalDateTime.now());
        refundPayment.setExternalId("REF-" + payment.getExternalId());
        
        // Actualizar el estado del pago original
        payment.setStatus("REFUNDED");
        
        return refundPayment;
    }

    @Override
    public boolean validatePaymentMethod(PaymentMethod paymentMethod) {
        // Para tarjetas, verificar fecha de expiración
        if ("CREDIT_CARD".equals(paymentMethod.getType()) || "DEBIT_CARD".equals(paymentMethod.getType())) {
            // En un sistema real, esto requeriría desencriptar la fecha
            // Aquí simplemente simulamos que es válido
            return true;
        }
        
        // Para PayPal, verificar que el email esté presente
        if ("PAYPAL".equals(paymentMethod.getType())) {
            return paymentMethod.getPaypalEmail() != null && !paymentMethod.getPaypalEmail().isEmpty();
        }
        
        return false;
    }

    @Override
    public BigDecimal calculateRentalPaymentAmount(Rental rental) {
        if (rental.getStartDateTime() == null || rental.getEndDateTime() == null) {
            return BigDecimal.ZERO;
        }
        
        // Calcular días de alquiler (mínimo 1 día)
        Duration duration = Duration.between(rental.getStartDateTime(), rental.getEndDateTime());
        long hours = duration.toHours();
        
        // Si son menos de 24 horas, se cobra por día completo
        if (hours <= 24) {
            return rental.getVehicle().getPricePerDay();
        }
        
        // Para periodos más largos, calcular basado en días (redondeando hacia arriba)
        BigDecimal days = new BigDecimal(Math.ceil((double) hours / 24));
        return rental.getVehicle().getPricePerDay().multiply(days).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateSecurityDepositAmount(Rental rental) {
        BigDecimal rentalAmount = calculateRentalPaymentAmount(rental);
        return rentalAmount.multiply(SECURITY_DEPOSIT_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean hasDefaultPaymentMethod(User user) {
        // Esta implementación depende del repositorio, así que es solo un placeholder
        // En la implementación real, se consultaría el repositorio
        return false;
    }
}