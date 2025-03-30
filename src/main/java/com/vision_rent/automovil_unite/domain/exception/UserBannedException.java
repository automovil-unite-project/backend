package com.vision_rent.automovil_unite.domain.exception;

/**
 * Excepción lanzada cuando un usuario baneado intenta realizar una operación restringida.
 */
public class UserBannedException extends DomainException {
    
    public UserBannedException(Long userId) {
        super(String.format("El usuario con id %d está temporalmente suspendido y no puede realizar esta operación", userId));
    }
}