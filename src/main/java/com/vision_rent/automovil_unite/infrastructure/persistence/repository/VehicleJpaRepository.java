package com.vision_rent.automovil_unite.infrastructure.persistence.repository;


import com.vision_rent.automovil_unite.infrastructure.persistence.entity.VehicleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para la entidad VehicleJpaEntity.
 */
@Repository
public interface VehicleJpaRepository extends JpaRepository<VehicleJpaEntity, Long> {
    
    List<VehicleJpaEntity> findByOwnerId(Long ownerId);
    
    List<VehicleJpaEntity> findByAvailableTrue();
    
    List<VehicleJpaEntity> findByBrand(String brand);
    
    @Query("SELECT v FROM VehicleJpaEntity v ORDER BY v.rentCount DESC")
    List<VehicleJpaEntity> findMostRented();
    
    @Query("SELECT v FROM VehicleJpaEntity v WHERE v.averageRating IS NOT NULL ORDER BY v.averageRating DESC")
    List<VehicleJpaEntity> findTopRated();
    
    @Query("SELECT v.brand, COUNT(v) as count FROM VehicleJpaEntity v GROUP BY v.brand ORDER BY count DESC")
    List<Object[]> findMostPopularBrands();
    
    @Modifying
    @Query("UPDATE VehicleJpaEntity v SET v.averageRating = :rating WHERE v.id = :vehicleId")
    void updateRating(@Param("vehicleId") Long vehicleId, @Param("rating") Float rating);
    
    @Modifying
    @Query("UPDATE VehicleJpaEntity v SET v.rentCount = v.rentCount + 1 WHERE v.id = :vehicleId")
    void incrementRentCount(@Param("vehicleId") Long vehicleId);
    
    @Query("SELECT v FROM VehicleJpaEntity v WHERE v.available = true " +
           "AND v.id NOT IN (SELECT r.vehicle.id FROM RentalJpaEntity r WHERE " +
           "r.status IN ('CONFIRMED', 'ACTIVE') " +
           "AND ((r.startDateTime <= :endDateTime AND r.endDateTime >= :startDateTime) " +
           "OR (r.extendedUntil IS NOT NULL AND r.startDateTime <= :endDateTime AND r.extendedUntil >= :startDateTime)))")
    List<VehicleJpaEntity> findAvailableInDateRange(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);
}