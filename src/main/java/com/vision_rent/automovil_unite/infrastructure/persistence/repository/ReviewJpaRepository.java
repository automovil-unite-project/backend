package com.vision_rent.automovil_unite.infrastructure.persistence.repository;


import com.vision_rent.automovil_unite.infrastructure.persistence.entity.ReviewJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad ReviewJpaEntity.
 */
@Repository
public interface ReviewJpaRepository extends JpaRepository<ReviewJpaEntity, Long> {
    
    List<ReviewJpaEntity> findByReviewerId(Long reviewerId);
    
    List<ReviewJpaEntity> findByReviewedId(Long reviewedId);
    
    List<ReviewJpaEntity> findByVehicleId(Long vehicleId);
    
    List<ReviewJpaEntity> findByRentalId(Long rentalId);
    
    List<ReviewJpaEntity> findByType(String type);
    
    @Query("SELECT r FROM ReviewJpaEntity r WHERE r.rental.id = :rentalId AND r.type = 'VEHICLE_REVIEW'")
    Optional<ReviewJpaEntity> findVehicleReviewByRentalId(@Param("rentalId") Long rentalId);
    
    @Query("SELECT r FROM ReviewJpaEntity r WHERE r.rental.id = :rentalId AND r.type = 'USER_REVIEW'")
    Optional<ReviewJpaEntity> findUserReviewByRentalId(@Param("rentalId") Long rentalId);
    
    @Query("SELECT AVG(r.rating) FROM ReviewJpaEntity r WHERE r.reviewed.id = :userId")
    Double getAverageRatingForUser(@Param("userId") Long userId);
    
    @Query("SELECT AVG(r.rating) FROM ReviewJpaEntity r WHERE r.vehicle.id = :vehicleId")
    Double getAverageRatingForVehicle(@Param("vehicleId") Long vehicleId);
}