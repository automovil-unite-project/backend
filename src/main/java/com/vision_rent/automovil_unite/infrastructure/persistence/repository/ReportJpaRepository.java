package com.vision_rent.automovil_unite.infrastructure.persistence.repository;


import com.vision_rent.automovil_unite.infrastructure.persistence.entity.ReportJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad ReportJpaEntity.
 */
@Repository
public interface ReportJpaRepository extends JpaRepository<ReportJpaEntity, Long> {
    
    List<ReportJpaEntity> findByReporterId(Long reporterId);
    
    List<ReportJpaEntity> findByReportedId(Long reportedId);
    
    List<ReportJpaEntity> findByRentalId(Long rentalId);
    
    List<ReportJpaEntity> findByStatus(String status);
    
    int countByReportedId(Long reportedId);
    
    boolean existsByRentalId(Long rentalId);
}