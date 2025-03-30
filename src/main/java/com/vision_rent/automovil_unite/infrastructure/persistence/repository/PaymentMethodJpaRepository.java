package com.vision_rent.automovil_unite.infrastructure.persistence.repository;


import com.vision_rent.automovil_unite.infrastructure.persistence.entity.PaymentMethodJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad PaymentMethodJpaEntity.
 */
@Repository
public interface PaymentMethodJpaRepository extends JpaRepository<PaymentMethodJpaEntity, Long> {
    
    List<PaymentMethodJpaEntity> findByUserId(Long userId);
    
    @Query("SELECT pm FROM PaymentMethodJpaEntity pm WHERE pm.user.id = :userId AND pm.isDefault = true")
    Optional<PaymentMethodJpaEntity> findDefaultByUserId(@Param("userId") Long userId);
    
    boolean existsByUserIdAndAlias(Long userId, String alias);
}