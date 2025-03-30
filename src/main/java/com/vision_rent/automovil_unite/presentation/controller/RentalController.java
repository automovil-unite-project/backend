package com.vision_rent.automovil_unite.presentation.controller;


import com.vision_rent.automovil_unite.application.dto.CreateRentalRequest;
import com.vision_rent.automovil_unite.application.dto.ExtendRentalRequest;
import com.vision_rent.automovil_unite.application.dto.RentalDto;
import com.vision_rent.automovil_unite.application.dto.ReturnVehicleRequest;
import com.vision_rent.automovil_unite.application.service.RentalService;
import com.vision_rent.automovil_unite.infrastructure.security.impl.UserDetailsImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador para operaciones relacionadas con alquileres.
 */
@RestController
@RequestMapping("/api/v1/rentals")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class RentalController {

    private final RentalService rentalService;
    
    /**
     * Crea un nuevo alquiler.
     *
     * @param userDetails Detalles del usuario autenticado
     * @param request Datos del alquiler
     * @return DTO del alquiler creado
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_RENTER')")
    public ResponseEntity<RentalDto> createRental(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CreateRentalRequest request) {
        return ResponseEntity.ok(rentalService.createRental(userDetails.getUserId(), request));
    }
    
    /**
     * Obtiene un alquiler por su ID.
     *
     * @param id ID del alquiler
     * @return DTO del alquiler
     */
    @GetMapping("/{id}")
    public ResponseEntity<RentalDto> getRentalById(@PathVariable Long id) {
        return ResponseEntity.ok(rentalService.getRentalById(id));
    }
    
    /**
     * Confirma un alquiler (después del pago).
     *
     * @param id ID del alquiler
     * @param userDetails Detalles del usuario autenticado
     * @param paymentId ID del pago
     * @return DTO del alquiler confirmado
     */
    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasRole('ROLE_RENTER')")
    public ResponseEntity<RentalDto> confirmRental(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody String paymentId) {
        return ResponseEntity.ok(rentalService.confirmRental(id, paymentId, userDetails.getUserId()));
    }
    
    /**
     * Extiende un alquiler.
     *
     * @param userDetails Detalles del usuario autenticado
     * @param request Datos de la extensión
     * @return DTO del alquiler extendido
     */
    @PutMapping("/extend")
    @PreAuthorize("hasRole('ROLE_RENTER')")
    public ResponseEntity<RentalDto> extendRental(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody ExtendRentalRequest request) {
        return ResponseEntity.ok(rentalService.extendRental(request, userDetails.getUserId()));
    }
    
    /**
     * Registra la devolución de un vehículo.
     *
     * @param userDetails Detalles del usuario autenticado
     * @param request Datos de la devolución
     * @return DTO del alquiler finalizado
     */
    @PutMapping("/return")
    public ResponseEntity<RentalDto> returnVehicle(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody ReturnVehicleRequest request) {
        return ResponseEntity.ok(rentalService.returnVehicle(request, userDetails.getUserId()));
    }
    
    /**
     * Cancela un alquiler.
     *
     * @param id ID del alquiler
     * @param userDetails Detalles del usuario autenticado
     * @return DTO del alquiler cancelado
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<RentalDto> cancelRental(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(rentalService.cancelRental(id, userDetails.getUserId()));
    }
    
    /**
     * Obtiene todos los alquileres del usuario autenticado (como arrendatario).
     *
     * @param userDetails Detalles del usuario autenticado
     * @return Lista de DTOs de alquiler
     */
    @GetMapping("/my-rentals")
    @PreAuthorize("hasRole('ROLE_RENTER')")
    public ResponseEntity<List<RentalDto>> getMyRentals(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(rentalService.getRentalsByRenterId(userDetails.getUserId()));
    }
    
    /**
     * Obtiene todos los alquileres de los vehículos del usuario autenticado (como propietario).
     *
     * @param userDetails Detalles del usuario autenticado
     * @return Lista de DTOs de alquiler
     */
    @GetMapping("/my-vehicle-rentals")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<List<RentalDto>> getMyVehicleRentals(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(rentalService.getRentalsByVehicleOwnerId(userDetails.getUserId()));
    }
    
    /**
     * Obtiene todos los alquileres de un vehículo específico.
     *
     * @param vehicleId ID del vehículo
     * @return Lista de DTOs de alquiler
     */
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<RentalDto>> getRentalsByVehicleId(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(rentalService.getRentalsByVehicleId(vehicleId));
    }
}