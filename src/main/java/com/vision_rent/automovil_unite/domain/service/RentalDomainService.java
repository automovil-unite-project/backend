package com.vision_rent.automovil_unite.domain.service;



import com.vision_rent.automovil_unite.domain.entity.Rental;
import com.vision_rent.automovil_unite.domain.entity.User;
import com.vision_rent.automovil_unite.domain.entity.Vehicle;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Servicio de dominio para operaciones relacionadas con alquileres.
 */
@Service
public interface RentalDomainService {
    
    /**
     * Verifica si un vehículo está disponible para alquilar en un rango de fechas.
     * 
     * @param vehicle Vehículo a verificar
     * @param startDateTime Fecha y hora de inicio del alquiler
     * @param endDateTime Fecha y hora de fin del alquiler
     * @return true si el vehículo está disponible, false en caso contrario
     */
    boolean isVehicleAvailable(Vehicle vehicle, LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    /**
     * Calcula el precio total del alquiler considerando descuentos y penalizaciones.
     * 
     * @param vehicle Vehículo a alquilar
     * @param renter Usuario que alquila
     * @param startDateTime Fecha y hora de inicio
     * @param endDateTime Fecha y hora de fin
     * @return Precio total calculado
     */
    BigDecimal calculateRentalPrice(Vehicle vehicle, User renter, LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    /**
     * Aplica un descuento a un alquiler si el usuario es elegible.
     * 
     * @param rental Alquiler al que se aplicará el descuento
     * @param renter Usuario que alquila
     * @return Alquiler con el descuento aplicado
     */
    Rental applyDiscount(Rental rental, User renter);
    
    /**
     * Verifica si un alquiler puede ser extendido.
     * 
     * @param rental Alquiler a extender
     * @param newEndDateTime Nueva fecha y hora de fin
     * @return true si el alquiler puede ser extendido, false en caso contrario
     */
    boolean canExtendRental(Rental rental, LocalDateTime newEndDateTime);
    
    /**
     * Calcula la penalización por devolución tardía.
     * 
     * @param rental Alquiler con devolución tardía
     * @return Monto de la penalización
     */
    BigDecimal calculateLateFee(Rental rental);
    
    /**
     * Registra la devolución de un vehículo.
     * 
     * @param rental Alquiler a finalizar
     * @param returnDateTime Fecha y hora de devolución
     * @return Alquiler actualizado
     */
    Rental returnVehicle(Rental rental, LocalDateTime returnDateTime);
    
    /**
     * Determina si un usuario debe ser temporalmente suspendido por devolución tardía.
     * 
     * @param rental Alquiler con devolución tardía
     * @param renter Usuario que alquiló
     * @return true si el usuario debe ser suspendido, false en caso contrario
     */
    boolean shouldBanUser(Rental rental, User renter);
}