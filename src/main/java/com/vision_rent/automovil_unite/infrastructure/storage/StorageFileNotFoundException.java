package com.vision_rent.automovil_unite.infrastructure.storage;

/**
 * Excepción lanzada cuando no se encuentra un archivo solicitado.
 */
public class StorageFileNotFoundException extends StorageException {
    
    public StorageFileNotFoundException(String message) {
        super(message);
    }
    
    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}