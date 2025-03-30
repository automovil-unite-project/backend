package com.vision_rent.automovil_unite.infrastructure.persistence.adapter;


import com.vision_rent.automovil_unite.domain.entity.Review;
import com.vision_rent.automovil_unite.domain.repository.ReviewRepository;
import com.vision_rent.automovil_unite.infrastructure.persistence.mapper.ReviewMapper;
import com.vision_rent.automovil_unite.infrastructure.persistence.repository.ReviewJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador para el repositorio de reseñas que implementa la interfaz de dominio.
 */
@Component
@RequiredArgsConstructor
public class ReviewRepositoryAdapter implements ReviewRepository {

    private final ReviewJpaRepository reviewJpaRepository;
    private final ReviewMapper reviewMapper = ReviewMapper.INSTANCE;
    
    @Override
    public Review save(Review review) {
        var reviewJpaEntity = reviewMapper.toJpaEntity(review);
        var savedEntity = reviewJpaRepository.save(reviewJpaEntity);
        return reviewMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Review> findById(Long id) {
        return reviewJpaRepository.findById(id)
                .map(reviewMapper::toDomain);
    }

    @Override
    public List<Review> findByReviewerId(Long reviewerId) {
        return reviewJpaRepository.findByReviewerId(reviewerId)
                .stream()
                .map(reviewMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Review> findByReviewedId(Long reviewedId) {
        return reviewJpaRepository.findByReviewedId(reviewedId)
                .stream()
                .map(reviewMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Review> findByVehicleId(Long vehicleId) {
        return reviewJpaRepository.findByVehicleId(vehicleId)
                .stream()
                .map(reviewMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Review> findByRentalId(Long rentalId) {
        return reviewJpaRepository.findByRentalId(rentalId)
                .stream()
                .map(reviewMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Review> findByType(String type) {
        return reviewJpaRepository.findByType(type)
                .stream()
                .map(reviewMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Review> findVehicleReviewByRentalId(Long rentalId) {
        return reviewJpaRepository.findVehicleReviewByRentalId(rentalId)
                .map(reviewMapper::toDomain);
    }

    @Override
    public Optional<Review> findUserReviewByRentalId(Long rentalId) {
        return reviewJpaRepository.findUserReviewByRentalId(rentalId)
                .map(reviewMapper::toDomain);
    }

    @Override
    public double getAverageRatingForUser(Long userId) {
        Double averageRating = reviewJpaRepository.getAverageRatingForUser(userId);
        return averageRating != null ? averageRating : 0.0;
    }

    @Override
    public double getAverageRatingForVehicle(Long vehicleId) {
        Double averageRating = reviewJpaRepository.getAverageRatingForVehicle(vehicleId);
        return averageRating != null ? averageRating : 0.0;
    }
}