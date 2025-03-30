package com.vision_rent.automovil_unite.presentation.controller;


import com.vision_rent.automovil_unite.application.dto.CreateVehicleRequest;
import com.vision_rent.automovil_unite.application.dto.VehicleDto;
import com.vision_rent.automovil_unite.application.service.VehicleService;
import com.vision_rent.automovil_unite.infrastructure.security.impl.UserDetailsImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador para operaciones relacionadas con vehículos.
 */
@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class VehicleController {

    private final VehicleService vehicleService;
    
    /**
     * Crea un nuevo vehículo.
     *
     * @param userDetails Detalles del usuario autenticado
     * @param request Datos del vehículo
     * @return DTO del vehículo creado
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<VehicleDto> createVehicle(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CreateVehicleRequest request) {
        return ResponseEntity.ok(vehicleService.createVehicle(userDetails.getUserId(), request));
    }
    
    /**
     * Obtiene un vehículo por su ID.
     *
     * @param id ID del vehículo
     * @return DTO del vehículo
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleDto> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }
    
    /**
     * Obtiene todos los vehículos de un propietario.
     *
     * @param userDetails Detalles del usuario autenticado
     * @return Lista de DTOs de vehículo
     */
    @GetMapping("/my-vehicles")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<List<VehicleDto>> getMyVehicles(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(vehicleService.getVehiclesByOwnerId(userDetails.getUserId()));
    }
    
    /**
     * Actualiza un vehículo.
     *
     * @param id ID del vehículo
     * @param userDetails Detalles del usuario autenticado
     * @param vehicleDto Datos actualizados del vehículo
     * @return DTO del vehículo actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<VehicleDto> updateVehicle(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody VehicleDto vehicleDto) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, vehicleDto, userDetails.getUserId()));
    }
    
    /**
     * Sube una foto para un vehículo.
     *
     * @param id ID del vehículo
     * @param userDetails Detalles del usuario autenticado
     * @param file Archivo de imagen
     * @return DTO del vehículo actualizado
     * @throws IOException Si ocurre un error al subir la imagen
     */
    @PostMapping(value = "/{id}/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<VehicleDto> uploadVehiclePhoto(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(vehicleService.uploadVehiclePhoto(id, file, userDetails.getUserId()));
    }
    
    /**
     * Elimina un vehículo.
     *
     * @param id ID del vehículo
     * @param userDetails Detalles del usuario autenticado
     * @return Respuesta vacía
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        vehicleService.deleteVehicle(id, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Obtiene todos los vehículos disponibles.
     *
     * @return Lista de DTOs de vehículo
     */
    @GetMapping("/available")
    public ResponseEntity<List<VehicleDto>> getAvailableVehicles() {
        return ResponseEntity.ok(vehicleService.getAvailableVehicles());
    }
    
    /**
     * Obtiene los vehículos disponibles en un rango de fechas.
     *
     * @param startDateTime Fecha y hora de inicio
     * @param endDateTime Fecha y hora de fin
     * @return Lista de DTOs de vehículo
     */
    @GetMapping("/available-in-date-range")
    public ResponseEntity<List<VehicleDto>> getAvailableVehiclesInDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {
        return ResponseEntity.ok(vehicleService.getAvailableVehiclesInDateRange(startDateTime, endDateTime));
    }
    
    /**
     * Obtiene los vehículos más alquilados.
     *
     * @param limit Límite de resultados
     * @return Lista de DTOs de vehículo
     */
    @GetMapping("/most-rented")
    public ResponseEntity<List<VehicleDto>> getMostRentedVehicles(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(vehicleService.getMostRentedVehicles(limit));
    }
    
    /**
     * Obtiene los vehículos mejor calificados.
     *
     * @param limit Límite de resultados
     * @return Lista de DTOs de vehículo
     */
    @GetMapping("/top-rated")
    public ResponseEntity<List<VehicleDto>> getTopRatedVehicles(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(vehicleService.getTopRatedVehicles(limit));
    }
    
    /**
     * Obtiene las marcas más populares.
     *
     * @param limit Límite de resultados
     * @return Lista de marcas
     */
    @GetMapping("/popular-brands")
    public ResponseEntity<List<String>> getMostPopularBrands(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(vehicleService.getMostPopularBrands(limit));
    }
    
    /**
     * Obtiene los vehículos por marca.
     *
     * @param brand Marca de los vehículos
     * @return Lista de DTOs de vehículo
     */
    @GetMapping("/by-brand")
    public ResponseEntity<List<VehicleDto>> getVehiclesByBrand(
            @RequestParam String brand) {
        return ResponseEntity.ok(vehicleService.findByBrand(brand));
    }
}