package com.vision_rent.automovil_unite.presentation.controller;


import com.vision_rent.automovil_unite.application.dto.CreateReviewRequest;
import com.vision_rent.automovil_unite.application.dto.ReviewDto;
import com.vision_rent.automovil_unite.application.service.ReviewService;
import com.vision_rent.automovil_unite.infrastructure.security.impl.UserDetailsImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador para operaciones relacionadas con reseñas.
 */
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ReviewController {

    private final ReviewService reviewService;
    
    /**
     * Crea una nueva reseña.
     *
     * @param userDetails Detalles del usuario autenticado
     * @param request Datos de la reseña
     * @return DTO de la reseña creada
     */
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CreateReviewRequest request) {
        return ResponseEntity.ok(reviewService.createReview(userDetails.getUserId(), request));
    }
    
    /**
     * Obtiene una reseña por su ID.
     *
     * @param id ID de la reseña
     * @return DTO de la reseña
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }
    
    /**
     * Actualiza una reseña.
     *
     * @param id ID de la reseña
     * @param userDetails Detalles del usuario autenticado
     * @param reviewDto Datos actualizados de la reseña
     * @return DTO de la reseña actualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody ReviewDto reviewDto) {
        return ResponseEntity.ok(reviewService.updateReview(id, reviewDto, userDetails.getUserId()));
    }

    /**
     * Elimina una reseña.
     *
     * @param id ID de la reseña
     * @param userDetails Detalles del usuario autenticado
     * @return Respuesta vacía

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewService.deleteReview(id, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }
     */

    /**
     * Obtiene todas las reseñas escritas por el usuario autenticado.
     *
     * @param userDetails Detalles del usuario autenticado
     * @return Lista de DTOs de reseña
     */
    @GetMapping("/my-reviews")
    public ResponseEntity<List<ReviewDto>> getMyReviews(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(reviewService.getReviewsByReviewerId(userDetails.getUserId()));
    }
    
    /**
     * Obtiene todas las reseñas recibidas por el usuario autenticado.
     *
     * @param userDetails Detalles del usuario autenticado
     * @return Lista de DTOs de reseña
     */
    @GetMapping("/reviews-about-me")
    public ResponseEntity<List<ReviewDto>> getReviewsAboutMe(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(reviewService.getReviewsByReviewedId(userDetails.getUserId()));
    }
    
    /**
     * Obtiene todas las reseñas de un usuario.
     *
     * @param userId ID del usuario
     * @return Lista de DTOs de reseña
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getReviewsByReviewedId(userId));
    }
    
    /**
     * Obtiene todas las reseñas de un vehículo.
     *
     * @param vehicleId ID del vehículo
     * @return Lista de DTOs de reseña
     */
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByVehicleId(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(reviewService.getReviewsByVehicleId(vehicleId));
    }
    
    /**
     * Obtiene todas las reseñas de un alquiler.
     *
     * @param rentalId ID del alquiler
     * @return Lista de DTOs de reseña
     */
    @GetMapping("/rental/{rentalId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByRentalId(@PathVariable Long rentalId) {
        return ResponseEntity.ok(reviewService.getReviewsByRentalId(rentalId));
    }
}