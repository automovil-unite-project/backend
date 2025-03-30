package com.vision_rent.automovil_unite.domain.exception;

/**
 * Excepción lanzada cuando se intenta alquilar un vehículo que no está disponible.
 */
public class VehicleNotAvailableException extends DomainException {
    
    public VehicleNotAvailableException(Long vehicleId) {
        super(String.format("El vehículo con id %d no está disponible para alquilar en las fechas seleccionadas", vehicleId));
    }
    
    public VehicleNotAvailableException(String message) {
        super(message);
    }
}