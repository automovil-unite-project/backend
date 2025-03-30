package com.vision_rent.automovil_unite.domain.exception;

/**
 * Excepción lanzada cuando ocurre un error al procesar un pago.
 */
public class PaymentProcessingException extends DomainException {
    
    public PaymentProcessingException(String message) {
        super(message);
    }
    
    public PaymentProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}