package com.vision_rent.automovil_unite.domain.valueobject;

/**
 * Enumerado que representa los roles disponibles en el sistema.
 */
public enum Role {
    RENTER("RENTER"),       // Arrendatario (el que alquila)
    OWNER("OWNER"),         // Arrendador (el dueño)
    ADMIN("ADMIN");         // Administrador

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}