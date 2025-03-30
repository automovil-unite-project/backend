package com.vision_rent.automovil_unite.infrastructure.persistence.adapter;


import com.vision_rent.automovil_unite.domain.entity.Vehicle;
import com.vision_rent.automovil_unite.domain.repository.VehicleRepository;
import com.vision_rent.automovil_unite.infrastructure.persistence.mapper.VehicleMapper;
import com.vision_rent.automovil_unite.infrastructure.persistence.repository.VehicleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador para el repositorio de vehículos que implementa la interfaz de dominio.
 */
@Component
@RequiredArgsConstructor
public class VehicleRepositoryAdapter implements VehicleRepository {

    private final VehicleJpaRepository vehicleJpaRepository;
    private final VehicleMapper vehicleMapper = VehicleMapper.INSTANCE;
    
    @Override
    public Vehicle save(Vehicle vehicle) {
        var vehicleJpaEntity = vehicleMapper.toJpaEntity(vehicle);
        var savedEntity = vehicleJpaRepository.save(vehicleJpaEntity);
        return vehicleMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Vehicle> findById(Long id) {
        return vehicleJpaRepository.findById(id)
                .map(vehicleMapper::toDomain);
    }

    @Override
    public List<Vehicle> findByOwnerId(Long ownerId) {
        return vehicleJpaRepository.findByOwnerId(ownerId)
                .stream()
                .map(vehicleMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Vehicle> findAll() {
        return vehicleJpaRepository.findAll()
                .stream()
                .map(vehicleMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Vehicle> findAvailable() {
        return vehicleJpaRepository.findByAvailableTrue()
                .stream()
                .map(vehicleMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Vehicle> findByBrand(String brand) {
        return vehicleJpaRepository.findByBrand(brand)
                .stream()
                .map(vehicleMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Vehicle> findMostRented(int limit) {
        return vehicleJpaRepository.findMostRented()
                .stream()
                .limit(limit)
                .map(vehicleMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Vehicle> findTopRated(int limit) {
        return vehicleJpaRepository.findTopRated()
                .stream()
                .limit(limit)
                .map(vehicleMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findMostPopularBrands(int limit) {
        return vehicleJpaRepository.findMostPopularBrands()
                .stream()
                .limit(limit)
                .map(result -> (String) result[0])
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Vehicle vehicle) {
        vehicleJpaRepository.deleteById(vehicle.getId());
    }

    @Override
    public void updateRating(Long vehicleId, Float newRating) {
        vehicleJpaRepository.updateRating(vehicleId, newRating);
    }

    @Override
    public void incrementRentCount(Long vehicleId) {
        vehicleJpaRepository.incrementRentCount(vehicleId);
    }

    @Override
    public List<Vehicle> findAvailableInDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return vehicleJpaRepository.findAvailableInDateRange(startDateTime, endDateTime)
                .stream()
                .map(vehicleMapper::toDomain)
                .collect(Collectors.toList());
    }
}