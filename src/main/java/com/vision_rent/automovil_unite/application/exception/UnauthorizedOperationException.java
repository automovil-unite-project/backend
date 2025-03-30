package com.vision_rent.automovil_unite.application.exception;

/**
 * Excepción lanzada cuando un usuario intenta realizar una operación para la cual no está autorizado.
 */
public class UnauthorizedOperationException extends RuntimeException {
    
    public UnauthorizedOperationException(String message) {
        super(message);
    }
}