package com.vision_rent.automovil_unite.application.service;


import com.vision_rent.automovil_unite.application.dto.CreateReviewRequest;
import com.vision_rent.automovil_unite.application.dto.ReviewDto;
import com.vision_rent.automovil_unite.application.exception.InvalidOperationException;
import com.vision_rent.automovil_unite.application.exception.ResourceNotFoundException;
import com.vision_rent.automovil_unite.application.exception.UnauthorizedOperationException;
import com.vision_rent.automovil_unite.application.mapper.ReviewDtoMapper;
import com.vision_rent.automovil_unite.domain.entity.Rental;
import com.vision_rent.automovil_unite.domain.entity.Review;
import com.vision_rent.automovil_unite.domain.entity.User;
import com.vision_rent.automovil_unite.domain.entity.Vehicle;
import com.vision_rent.automovil_unite.domain.repository.RentalRepository;
import com.vision_rent.automovil_unite.domain.repository.ReviewRepository;
import com.vision_rent.automovil_unite.domain.repository.UserRepository;
import com.vision_rent.automovil_unite.domain.repository.VehicleRepository;
import com.vision_rent.automovil_unite.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de reseñas.
 */
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final UserDomainService userDomainService;
    private final ReviewDtoMapper reviewDtoMapper = ReviewDtoMapper.INSTANCE;
    
    /**
     * Crea una nueva reseña.
     *
     * @param reviewerId ID del usuario que crea la reseña
     * @param request Datos de la reseña
     * @return DTO de la reseña creada
     */
    @Transactional
    public ReviewDto createReview(Long reviewerId, CreateReviewRequest request) {
        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", reviewerId));
        
        Rental rental = rentalRepository.findById(request.getRentalId())
                .orElseThrow(() -> new ResourceNotFoundException("Alquiler", "id", request.getRentalId()));
        
        // Verificar si el alquiler está completado
        if (!"COMPLETED".equals(rental.getStatus())) {
            throw new InvalidOperationException("Solo se pueden revisar alquileres completados");
        }
        
        // Verificar si el usuario es parte del alquiler (arrendatario o propietario)
        boolean isRenter = rental.getRenter().getId().equals(reviewerId);
        boolean isOwner = rental.getVehicle().getOwner().getId().equals(reviewerId);
        
        if (!isRenter && !isOwner) {
            throw new UnauthorizedOperationException("Solo el arrendatario o el propietario pueden escribir reseñas");
        }
        
        // Verificar si ya existe una reseña del mismo tipo para este alquiler
        if ("USER_REVIEW".equals(request.getType())) {
            if (reviewRepository.findUserReviewByRentalId(request.getRentalId()).isPresent()) {
                throw new InvalidOperationException("Ya existe una reseña de usuario para este alquiler");
            }
            
            // Verificar si el usuario es el propietario del vehículo (solo él puede reseñar al arrendatario)
            if (!isOwner) {
                throw new UnauthorizedOperationException("Solo el propietario puede reseñar al arrendatario");
            }
        } else if ("VEHICLE_REVIEW".equals(request.getType())) {
            if (reviewRepository.findVehicleReviewByRentalId(request.getRentalId()).isPresent()) {
                throw new InvalidOperationException("Ya existe una reseña de vehículo para este alquiler");
            }
            
            // Verificar si el usuario es el arrendatario (solo él puede reseñar al vehículo)
            if (!isRenter) {
                throw new UnauthorizedOperationException("Solo el arrendatario puede reseñar el vehículo");
            }
        } else {
            throw new InvalidOperationException("Tipo de reseña inválido");
        }
        
        // Crear la reseña
        Review review = new Review();
        review.setRental(rental);
        review.setReviewer(reviewer);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setType(request.getType());
        
        // Configurar los objetos relacionados según el tipo de reseña
        if ("USER_REVIEW".equals(request.getType())) {
            review.setReviewed(rental.getRenter());
            
            // Actualizar la calificación del arrendatario
            User renter = rental.getRenter();
            userDomainService.updateUserRating(renter, request.getRating());
            userRepository.save(renter);
            
            // Actualizar el alquiler con la calificación del arrendatario
            rental.setRenterRating(request.getRating());
            rental.setRenterReview(request.getComment());
            rentalRepository.save(rental);
        } else {
            review.setVehicle(rental.getVehicle());
            
            // Actualizar la calificación del vehículo
            Vehicle vehicle = rental.getVehicle();
            vehicle.updateRating(request.getRating());
            vehicleRepository.save(vehicle);
            
            // Actualizar el alquiler con la calificación del vehículo
            rental.setVehicleRating(request.getRating());
            rental.setVehicleReview(request.getComment());
            rentalRepository.save(rental);
        }
        
        Review savedReview = reviewRepository.save(review);
        return reviewDtoMapper.toDto(savedReview);
    }
    
    /**
     * Obtiene una reseña por su ID.
     *
     * @param reviewId ID de la reseña
     * @return DTO de la reseña
     */
    @Transactional(readOnly = true)
    public ReviewDto getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña", "id", reviewId));
        
        return reviewDtoMapper.toDto(review);
    }
    
    /**
     * Obtiene todas las reseñas escritas por un usuario.
     *
     * @param reviewerId ID del autor de las reseñas
     * @return Lista de DTOs de reseña
     */
    @Transactional(readOnly = true)
    public List<ReviewDto> getReviewsByReviewerId(Long reviewerId) {
        return reviewRepository.findByReviewerId(reviewerId)
                .stream()
                .map(reviewDtoMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene todas las reseñas recibidas por un usuario.
     *
     * @param reviewedId ID del usuario reseñado
     * @return Lista de DTOs de reseña
     */
    @Transactional(readOnly = true)
    public List<ReviewDto> getReviewsByReviewedId(Long reviewedId) {
        return reviewRepository.findByReviewedId(reviewedId)
                .stream()
                .map(reviewDtoMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene todas las reseñas de un vehículo.
     *
     * @param vehicleId ID del vehículo
     * @return Lista de DTOs de reseña
     */
    @Transactional(readOnly = true)
    public List<ReviewDto> getReviewsByVehicleId(Long vehicleId) {
        return reviewRepository.findByVehicleId(vehicleId)
                .stream()
                .map(reviewDtoMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene todas las reseñas de un alquiler.
     *
     * @param rentalId ID del alquiler
     * @return Lista de DTOs de reseña
     */
    @Transactional(readOnly = true)
    public List<ReviewDto> getReviewsByRentalId(Long rentalId) {
        return reviewRepository.findByRentalId(rentalId)
                .stream()
                .map(reviewDtoMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Actualiza una reseña.
     *
     * @param reviewId ID de la reseña
     * @param reviewDto Datos actualizados de la reseña
     * @param userId ID del usuario que realiza la operación
     * @return DTO de la reseña actualizada
     */
    @Transactional
    public ReviewDto updateReview(Long reviewId, ReviewDto reviewDto, Long userId) {
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña", "id", reviewId));
        
        // Verificar si el usuario es el autor de la reseña
        if (!existingReview.getReviewer().getId().equals(userId)) {
            throw new UnauthorizedOperationException("Solo el autor puede actualizar la reseña");
        }
        
        // Actualizar campos editables
        existingReview.setRating(reviewDto.getRating());
        existingReview.setComment(reviewDto.getComment());
        
        // Actualizar calificaciones relacionadas
        if (existingReview.isUserReview()) {
            User reviewed = existingReview.getReviewed();
            userDomainService.updateUserRating(reviewed, reviewDto.getRating());
            userRepository.save(reviewed);
            
            // Actualizar el alquiler
            Rental rental = existingReview.getRental();
            rental.setRenterRating(reviewDto.getRating());
            rental.setRenterReview(reviewDto.getComment());
            rentalRepository.save(rental);
        } else if (existingReview.isVehicleReview()) {
            Vehicle vehicle = existingReview.getVehicle();
            vehicle.updateRating(reviewDto.getRating());
            vehicleRepository.save(vehicle);
            
            // Actualizar el alquiler
            Rental rental = existingReview.getRental();
            rental.setVehicleRating(reviewDto.getRating());
            rental.setVehicleReview(reviewDto.getComment());
            rentalRepository.save(rental);
        }
        
        Review updatedReview = reviewRepository.save(existingReview);
        return reviewDtoMapper.toDto(updatedReview);
    }
    
    /**
     * Elimina una reseña.
     *
     * @param reviewId ID de la reseña
     * @param userId ID del usuario que realiza la operación

    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña", "id", reviewId));
        
        // Verificar si el usuario es el autor de la reseña
        if (!review.getReviewer().getId().equals(userId)) {
            throw new UnauthorizedOperationException("Solo el autor puede eliminar la reseña");
        }
        
        // Eliminar la reseña
        reviewRepository.delete(review);
        
        // Actualizar el alquiler
        Rental rental = review.getRental();
        if (review.isUserReview()) {
            rental.setRenterRating(null);
            rental.setRenterReview(null);
        } else if (review.isVehicleReview()) {
            rental.setVehicleRating(null);
            rental.setVehicleReview(null);
        }
        rentalRepository.save(rental);
        
        // Aquí se podría recalcular el promedio de calificaciones del usuario o vehículo
    }
     */
}