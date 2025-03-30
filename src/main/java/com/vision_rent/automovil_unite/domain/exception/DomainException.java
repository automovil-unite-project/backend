package com.vision_rent.automovil_unite.domain.exception;

/**
 * Excepción base para todas las excepciones del dominio.
 */
public class DomainException extends RuntimeException {
    
    public DomainException(String message) {
        super(message);
    }
    
    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}