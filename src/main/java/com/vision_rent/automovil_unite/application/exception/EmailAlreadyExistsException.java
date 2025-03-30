package com.vision_rent.automovil_unite.application.exception;

/**
 * Excepción lanzada cuando se intenta registrar un usuario con un email que ya existe.
 */
public class EmailAlreadyExistsException extends RuntimeException {
    
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}