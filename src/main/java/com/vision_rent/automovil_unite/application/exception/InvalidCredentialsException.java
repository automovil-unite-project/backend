package com.vision_rent.automovil_unite.application.exception;

/**
 * Excepción lanzada cuando las credenciales de autenticación son inválidas.
 */
public class InvalidCredentialsException extends RuntimeException {
    
    public InvalidCredentialsException(String message) {
        super(message);
    }
}