package com.vision_rent.automovil_unite.application.exception;

/**
 * Excepción lanzada cuando se intenta realizar una operación inválida.
 */
public class InvalidOperationException extends RuntimeException {
    
    public InvalidOperationException(String message) {
        super(message);
    }
}