package com.vision_rent.automovil_unite.domain.repository;



import com.vision_rent.automovil_unite.domain.entity.Review;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad Review en el dominio.
 * Define las operaciones permitidas de persistencia para las reseñas.
 */
public interface ReviewRepository {
    Review save(Review review);
    Optional<Review> findById(Long id);
    List<Review> findByReviewerId(Long reviewerId);
    List<Review> findByReviewedId(Long reviewedId);
    List<Review> findByVehicleId(Long vehicleId);
    List<Review> findByRentalId(Long rentalId);
    List<Review> findByType(String type);
    Optional<Review> findVehicleReviewByRentalId(Long rentalId);
    Optional<Review> findUserReviewByRentalId(Long rentalId);
    double getAverageRatingForUser(Long userId);
    double getAverageRatingForVehicle(Long vehicleId);
}