package com.vision_rent.automovil_unite.domain.exception;

/**
 * Excepción lanzada cuando se intenta utilizar un método de pago inválido.
 */
public class InvalidPaymentMethodException extends DomainException {
    
    public InvalidPaymentMethodException(String message) {
        super(message);
    }
    
    public InvalidPaymentMethodException(String message, Throwable cause) {
        super(message, cause);
    }
}