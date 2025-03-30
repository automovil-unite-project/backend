package com.vision_rent.automovil_unite.infrastructure.persistence.adapter;


import com.vision_rent.automovil_unite.domain.entity.Rental;
import com.vision_rent.automovil_unite.domain.repository.RentalRepository;
import com.vision_rent.automovil_unite.infrastructure.persistence.mapper.RentalMapper;
import com.vision_rent.automovil_unite.infrastructure.persistence.repository.RentalJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador para el repositorio de alquileres que implementa la interfaz de dominio.
 */
@Component
@RequiredArgsConstructor
public class RentalRepositoryAdapter implements RentalRepository {

    private final RentalJpaRepository rentalJpaRepository;
    private final RentalMapper rentalMapper = RentalMapper.INSTANCE;
    
    @Override
    public Rental save(Rental rental) {
        var rentalJpaEntity = rentalMapper.toJpaEntity(rental);
        var savedEntity = rentalJpaRepository.save(rentalJpaEntity);
        return rentalMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Rental> findById(Long id) {
        return rentalJpaRepository.findById(id)
                .map(rentalMapper::toDomain);
    }

    @Override
    public List<Rental> findByRenterId(Long renterId) {
        return rentalJpaRepository.findByRenterId(renterId)
                .stream()
                .map(rentalMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Rental> findByVehicleId(Long vehicleId) {
        return rentalJpaRepository.findByVehicleId(vehicleId)
                .stream()
                .map(rentalMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Rental> findByVehicleOwnerId(Long ownerId) {
        return rentalJpaRepository.findByVehicleOwnerId(ownerId)
                .stream()
                .map(rentalMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Rental> findActiveRentals() {
        return rentalJpaRepository.findActiveRentals()
                .stream()
                .map(rentalMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Rental> findLateRentals() {
        return rentalJpaRepository.findLateRentals()
                .stream()
                .map(rentalMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Rental> findCompletedRentals() {
        return rentalJpaRepository.findCompletedRentals()
                .stream()
                .map(rentalMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Rental> findByStatus(String status) {
        return rentalJpaRepository.findByStatus(status)
                .stream()
                .map(rentalMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Rental> findActiveRentalsByVehicleId(Long vehicleId) {
        return rentalJpaRepository.findActiveRentalsByVehicleId(vehicleId)
                .stream()
                .map(rentalMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsActiveRentalForVehicle(Long vehicleId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return rentalJpaRepository.existsActiveRentalForVehicle(vehicleId, startDateTime, endDateTime);
    }

    @Override
    public List<Rental> findActiveRentalsEndingBefore(LocalDateTime dateTime) {
        return rentalJpaRepository.findActiveRentalsEndingBefore(dateTime)
                .stream()
                .map(rentalMapper::toDomain)
                .collect(Collectors.toList());
    }
}