package com.vision_rent.automovil_unite.domain.exception;

/**
 * Excepción lanzada cuando no se encuentra una entidad en el repositorio.
 */
public class EntityNotFoundException extends DomainException {
    
    public EntityNotFoundException(String entityName, String identifier) {
        super(String.format("No se encontró %s con identificador: %s", entityName, identifier));
    }
    
    public EntityNotFoundException(String entityName, Long id) {
        super(String.format("No se encontró %s con id: %d", entityName, id));
    }
}