package com.vision_rent.automovil_unite.infrastructure.storage;

/**
 * Excepción lanzada cuando ocurre un error en el almacenamiento de archivos.
 */
public class StorageException extends RuntimeException {
    
    public StorageException(String message) {
        super(message);
    }
    
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}