package com.vision_rent.automovil_unite.infrastructure.persistence.repository;


import com.vision_rent.automovil_unite.infrastructure.persistence.entity.PaymentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad PaymentJpaEntity.
 */
@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentJpaEntity, Long> {
    
    Optional<PaymentJpaEntity> findByExternalId(String externalId);
    
    List<PaymentJpaEntity> findByRentalId(Long rentalId);
    
    List<PaymentJpaEntity> findByPayerId(Long payerId);
    
    List<PaymentJpaEntity> findByStatus(String status);
}