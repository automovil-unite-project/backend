package com.vision_rent.automovil_unite.application.service;


import com.vision_rent.automovil_unite.application.dto.CreateReportRequest;
import com.vision_rent.automovil_unite.application.dto.ReportDto;
import com.vision_rent.automovil_unite.application.exception.InvalidOperationException;
import com.vision_rent.automovil_unite.application.exception.ResourceNotFoundException;
import com.vision_rent.automovil_unite.application.exception.UnauthorizedOperationException;
import com.vision_rent.automovil_unite.application.mapper.ReportDtoMapper;
import com.vision_rent.automovil_unite.domain.entity.Rental;
import com.vision_rent.automovil_unite.domain.entity.Report;
import com.vision_rent.automovil_unite.domain.entity.User;
import com.vision_rent.automovil_unite.domain.repository.RentalRepository;
import com.vision_rent.automovil_unite.domain.repository.ReportRepository;
import com.vision_rent.automovil_unite.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de reportes.
 */
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final ReportDtoMapper reportDtoMapper = ReportDtoMapper.INSTANCE;
    
    /**
     * Crea un nuevo reporte.
     *
     * @param reporterId ID del usuario que crea el reporte
     * @param request Datos del reporte
     * @return DTO del reporte creado
     */
    @Transactional
    public ReportDto createReport(Long reporterId, CreateReportRequest request) {
        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", reporterId));
        
        User reported = userRepository.findById(request.getReportedId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getReportedId()));
        
        Rental rental = rentalRepository.findById(request.getRentalId())
                .orElseThrow(() -> new ResourceNotFoundException("Alquiler", "id", request.getRentalId()));
        
        // Verificar si el alquiler está completado
        if (!"COMPLETED".equals(rental.getStatus())) {
            throw new InvalidOperationException("Solo se pueden reportar alquileres completados");
        }
        
        // Verificar si el usuario es parte del alquiler (arrendatario o propietario)
        boolean isRenter = rental.getRenter().getId().equals(reporterId);
        boolean isOwner = rental.getVehicle().getOwner().getId().equals(reporterId);
        
        if (!isRenter && !isOwner) {
            throw new UnauthorizedOperationException("Solo el arrendatario o el propietario pueden crear reportes");
        }
        
        // Verificar si el usuario reportado es parte del alquiler
        boolean isReportedRenter = rental.getRenter().getId().equals(request.getReportedId());
        boolean isReportedOwner = rental.getVehicle().getOwner().getId().equals(request.getReportedId());
        
        if (!isReportedRenter && !isReportedOwner) {
            throw new InvalidOperationException("Solo se puede reportar a usuarios involucrados en el alquiler");
        }
        
        // Verificar que no se esté auto-reportando
        if (reporterId.equals(request.getReportedId())) {
            throw new InvalidOperationException("No puede reportarse a sí mismo");
        }
        
        // Verificar si ya existe un reporte para este alquiler
        if (reportRepository.existsByRentalId(request.getRentalId())) {
            throw new InvalidOperationException("Ya existe un reporte para este alquiler");
        }
        
        // Crear el reporte
        Report report = new Report();
        report.setRental(rental);
        report.setReporter(reporter);
        report.setReported(reported);
        report.setReason(request.getReason());
        report.setDescription(request.getDescription());
        report.setStatus("PENDING");
        
        // Si el propietario reporta al arrendatario, marcarlo en el alquiler
        if (isOwner && isReportedRenter) {
            rental.setRenterReported(true);
            rental.setRenterReportReason(request.getReason());
            rentalRepository.save(rental);
        }
        
        // Incrementar el contador de reportes del usuario reportado
        userRepository.incrementReportCount(reported.getId());
        
        Report savedReport = reportRepository.save(report);
        return reportDtoMapper.toDto(savedReport);
    }
    
    /**
     * Obtiene un reporte por su ID.
     *
     * @param reportId ID del reporte
     * @return DTO del reporte
     */
    @Transactional(readOnly = true)
    public ReportDto getReportById(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte", "id", reportId));
        
        return reportDtoMapper.toDto(report);
    }
    
    /**
     * Obtiene todos los reportes creados por un usuario.
     *
     * @param reporterId ID del autor de los reportes
     * @return Lista de DTOs de reporte
     */
    @Transactional(readOnly = true)
    public List<ReportDto> getReportsByReporterId(Long reporterId) {
        return reportRepository.findByReporterId(reporterId)
                .stream()
                .map(reportDtoMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene todos los reportes recibidos por un usuario.
     *
     * @param reportedId ID del usuario reportado
     * @return Lista de DTOs de reporte
     */
    @Transactional(readOnly = true)
    public List<ReportDto> getReportsByReportedId(Long reportedId) {
        return reportRepository.findByReportedId(reportedId)
                .stream()
                .map(reportDtoMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene todos los reportes de un alquiler.
     *
     * @param rentalId ID del alquiler
     * @return Lista de DTOs de reporte
     */
    @Transactional(readOnly = true)
    public List<ReportDto> getReportsByRentalId(Long rentalId) {
        return reportRepository.findByRentalId(rentalId)
                .stream()
                .map(reportDtoMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene todos los reportes con un estado específico.
     *
     * @param status Estado de los reportes
     * @return Lista de DTOs de reporte
     */
    @Transactional(readOnly = true)
    public List<ReportDto> getReportsByStatus(String status) {
        return reportRepository.findByStatus(status)
                .stream()
                .map(reportDtoMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Actualiza el estado de un reporte (solo admin).
     *
     * @param reportId ID del reporte
     * @param status Nuevo estado
     * @param resolution Resolución del reporte
     * @param resolvedByUserId ID del administrador que resuelve
     * @return DTO del reporte actualizado
     */
    @Transactional
    public ReportDto updateReportStatus(Long reportId, String status, String resolution, Long resolvedByUserId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte", "id", reportId));
        
        User resolvedBy = userRepository.findById(resolvedByUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", resolvedByUserId));
        
        // Verificar si el usuario es administrador (debería hacerse con anotaciones de seguridad)
        if (!resolvedBy.isAdmin()) {
            throw new UnauthorizedOperationException("Solo los administradores pueden actualizar el estado de los reportes");
        }
        
        report.setStatus(status);
        report.setResolution(resolution);
        report.setResolvedBy(resolvedBy);
        
        Report updatedReport = reportRepository.save(report);
        return reportDtoMapper.toDto(updatedReport);
    }
}