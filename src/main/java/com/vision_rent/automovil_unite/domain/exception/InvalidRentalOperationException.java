package com.vision_rent.automovil_unite.domain.exception;

/**
 * Excepción lanzada cuando se intenta realizar una operación inválida con un alquiler.
 */
public class InvalidRentalOperationException extends DomainException {
    
    public InvalidRentalOperationException(String message) {
        super(message);
    }
}