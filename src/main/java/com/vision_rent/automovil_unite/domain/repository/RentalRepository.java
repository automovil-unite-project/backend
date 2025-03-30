package com.vision_rent.automovil_unite.domain.repository;

import com.vision_rent.automovil_unite.domain.entity.Rental;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad Rental en el dominio.
 * Define las operaciones permitidas de persistencia para los alquileres.
 */
public interface RentalRepository {
    Rental save(Rental rental);
    Optional<Rental> findById(Long id);
    List<Rental> findByRenterId(Long renterId);
    List<Rental> findByVehicleId(Long vehicleId);
    List<Rental> findByVehicleOwnerId(Long ownerId);
    List<Rental> findActiveRentals();
    List<Rental> findLateRentals();
    List<Rental> findCompletedRentals();
    List<Rental> findByStatus(String status);
    List<Rental> findActiveRentalsByVehicleId(Long vehicleId);
    boolean existsActiveRentalForVehicle(Long vehicleId, LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<Rental> findActiveRentalsEndingBefore(LocalDateTime dateTime);
}