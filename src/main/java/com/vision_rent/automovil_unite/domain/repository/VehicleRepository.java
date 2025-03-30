package com.vision_rent.automovil_unite.domain.repository;

import com.vision_rent.automovil_unite.domain.entity.Vehicle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad Vehicle en el dominio.
 * Define las operaciones permitidas de persistencia para los vehículos.
 */
public interface VehicleRepository {
    Vehicle save(Vehicle vehicle);

    Optional<Vehicle> findById(Long id);

    List<Vehicle> findByOwnerId(Long ownerId);

    List<Vehicle> findAll();

    List<Vehicle> findAvailable();

    List<Vehicle> findByBrand(String brand);

    List<Vehicle> findMostRented(int limit);

    List<Vehicle> findTopRated(int limit);

    List<String> findMostPopularBrands(int limit);

    void delete(Vehicle vehicle);

    void updateRating(Long vehicleId, Float newRating);

    void incrementRentCount(Long vehicleId);

    List<Vehicle> findAvailableInDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime);
}