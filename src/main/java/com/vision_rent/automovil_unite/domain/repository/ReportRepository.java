package com.vision_rent.automovil_unite.domain.repository;



import com.vision_rent.automovil_unite.domain.entity.Report;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad Report en el dominio.
 * Define las operaciones permitidas de persistencia para los reportes.
 */
public interface ReportRepository {
    Report save(Report report);
    Optional<Report> findById(Long id);
    List<Report> findByReporterId(Long reporterId);
    List<Report> findByReportedId(Long reportedId);
    List<Report> findByRentalId(Long rentalId);
    List<Report> findByStatus(String status);
    int countByReportedId(Long reportedId);
    boolean existsByRentalId(Long rentalId);
}