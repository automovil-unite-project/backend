package com.vision_rent.automovil_unite.infrastructure.persistence.repository;


import com.vision_rent.automovil_unite.infrastructure.persistence.entity.RentalJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para la entidad RentalJpaEntity.
 */
@Repository
public interface RentalJpaRepository extends JpaRepository<RentalJpaEntity, Long> {
    
    List<RentalJpaEntity> findByRenterId(Long renterId);
    
    List<RentalJpaEntity> findByVehicleId(Long vehicleId);
    
    @Query("SELECT r FROM RentalJpaEntity r WHERE r.vehicle.owner.id = :ownerId")
    List<RentalJpaEntity> findByVehicleOwnerId(@Param("ownerId") Long ownerId);
    
    @Query("SELECT r FROM RentalJpaEntity r WHERE r.status = 'ACTIVE'")
    List<RentalJpaEntity> findActiveRentals();
    
    @Query("SELECT r FROM RentalJpaEntity r WHERE r.status = 'ACTIVE' AND r.endDateTime < CURRENT_TIMESTAMP")
    List<RentalJpaEntity> findLateRentals();
    
    @Query("SELECT r FROM RentalJpaEntity r WHERE r.status = 'COMPLETED'")
    List<RentalJpaEntity> findCompletedRentals();
    
    List<RentalJpaEntity> findByStatus(String status);
    
    @Query("SELECT r FROM RentalJpaEntity r WHERE r.vehicle.id = :vehicleId AND r.status = 'ACTIVE'")
    List<RentalJpaEntity> findActiveRentalsByVehicleId(@Param("vehicleId") Long vehicleId);
    
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM RentalJpaEntity r " +
           "WHERE r.vehicle.id = :vehicleId AND r.status IN ('CONFIRMED', 'ACTIVE') " +
           "AND ((r.startDateTime <= :endDateTime AND r.endDateTime >= :startDateTime) " +
           "OR (r.extendedUntil IS NOT NULL AND r.startDateTime <= :endDateTime AND r.extendedUntil >= :startDateTime))")
    boolean existsActiveRentalForVehicle(
            @Param("vehicleId") Long vehicleId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);
    
    @Query("SELECT r FROM RentalJpaEntity r WHERE r.status = 'ACTIVE' AND r.endDateTime <= :dateTime")
    List<RentalJpaEntity> findActiveRentalsEndingBefore(@Param("dateTime") LocalDateTime dateTime);
}