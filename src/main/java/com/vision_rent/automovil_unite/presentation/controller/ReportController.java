package com.vision_rent.automovil_unite.presentation.controller;


import com.vision_rent.automovil_unite.application.dto.CreateReportRequest;
import com.vision_rent.automovil_unite.application.dto.ReportDto;
import com.vision_rent.automovil_unite.application.service.ReportService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador para operaciones relacionadas con reportes.
 */
@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ReportController {

    private final ReportService reportService;
    
    /**
     * Crea un nuevo reporte.
     *
     * @param userDetails Detalles del usuario autenticado
     * @param request Datos del reporte
     * @return DTO del reporte creado
     */
    @PostMapping
    public ResponseEntity<ReportDto> createReport(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CreateReportRequest request) {
        return ResponseEntity.ok(reportService.createReport(userDetails.getUserId(), request));
    }
    
    /**
     * Obtiene un reporte por su ID.
     *
     * @param id ID del reporte
     * @return DTO del reporte
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportDto> getReportById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }
    
    /**
     * Actualiza el estado de un reporte (solo admin).
     *
     * @param id ID del reporte
     * @param userDetails Detalles del usuario autenticado
     * @param status Nuevo estado
     * @param resolution Resolución del reporte
     * @return DTO del reporte actualizado
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ReportDto> updateReportStatus(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam String status,
            @RequestParam String resolution) {
        return ResponseEntity.ok(reportService.updateReportStatus(
                id, status, resolution, userDetails.getUserId()));
    }
    
    /**
     * Obtiene todos los reportes creados por el usuario autenticado.
     *
     * @param userDetails Detalles del usuario autenticado
     * @return Lista de DTOs de reporte
     */
    @GetMapping("/my-reports")
    public ResponseEntity<List<ReportDto>> getMyReports(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(reportService.getReportsByReporterId(userDetails.getUserId()));
    }
    
    /**
     * Obtiene todos los reportes recibidos por el usuario autenticado.
     *
     * @param userDetails Detalles del usuario autenticado
     * @return Lista de DTOs de reporte
     */
    @GetMapping("/reports-about-me")
    public ResponseEntity<List<ReportDto>> getReportsAboutMe(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(reportService.getReportsByReportedId(userDetails.getUserId()));
    }
    
    /**
     * Obtiene todos los reportes de un usuario.
     *
     * @param userId ID del usuario
     * @return Lista de DTOs de reporte
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReportDto>> getReportsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(reportService.getReportsByReportedId(userId));
    }
    
    /**
     * Obtiene todos los reportes de un alquiler.
     *
     * @param rentalId ID del alquiler
     * @return Lista de DTOs de reporte
     */
    @GetMapping("/rental/{rentalId}")
    public ResponseEntity<List<ReportDto>> getReportsByRentalId(@PathVariable Long rentalId) {
        return ResponseEntity.ok(reportService.getReportsByRentalId(rentalId));
    }
    
    /**
     * Obtiene todos los reportes con un estado específico (solo admin).
     *
     * @param status Estado de los reportes
     * @return Lista de DTOs de reporte
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReportDto>> getReportsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(reportService.getReportsByStatus(status));
    }
}