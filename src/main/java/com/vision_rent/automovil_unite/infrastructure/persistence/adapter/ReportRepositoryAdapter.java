package com.vision_rent.automovil_unite.infrastructure.persistence.adapter;


import com.vision_rent.automovil_unite.domain.entity.Report;
import com.vision_rent.automovil_unite.domain.repository.ReportRepository;
import com.vision_rent.automovil_unite.infrastructure.persistence.mapper.ReportMapper;
import com.vision_rent.automovil_unite.infrastructure.persistence.repository.ReportJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador para el repositorio de reportes que implementa la interfaz de dominio.
 */
@Component
@RequiredArgsConstructor
public class ReportRepositoryAdapter implements ReportRepository {

    private final ReportJpaRepository reportJpaRepository;
    private final ReportMapper reportMapper = ReportMapper.INSTANCE;
    
    @Override
    public Report save(Report report) {
        var reportJpaEntity = reportMapper.toJpaEntity(report);
        var savedEntity = reportJpaRepository.save(reportJpaEntity);
        return reportMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Report> findById(Long id) {
        return reportJpaRepository.findById(id)
                .map(reportMapper::toDomain);
    }

    @Override
    public List<Report> findByReporterId(Long reporterId) {
        return reportJpaRepository.findByReporterId(reporterId)
                .stream()
                .map(reportMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Report> findByReportedId(Long reportedId) {
        return reportJpaRepository.findByReportedId(reportedId)
                .stream()
                .map(reportMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Report> findByRentalId(Long rentalId) {
        return reportJpaRepository.findByRentalId(rentalId)
                .stream()
                .map(reportMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Report> findByStatus(String status) {
        return reportJpaRepository.findByStatus(status)
                .stream()
                .map(reportMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public int countByReportedId(Long reportedId) {
        return reportJpaRepository.countByReportedId(reportedId);
    }

    @Override
    public boolean existsByRentalId(Long rentalId) {
        return reportJpaRepository.existsByRentalId(rentalId);
    }
}