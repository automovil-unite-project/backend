package com.vision_rent.automovil_unite.infrastructure.persistence.repository;


import com.vision_rent.automovil_unite.domain.valueobject.Role;
import com.vision_rent.automovil_unite.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad UserJpaEntity.
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
    
    Optional<UserJpaEntity> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM UserJpaEntity u WHERE :role MEMBER OF u.roles")
    List<UserJpaEntity> findByRole(@Param("role") Role role);
    
    @Modifying
    @Query("UPDATE UserJpaEntity u SET u.averageRating = :rating WHERE u.id = :userId")
    void updateRating(@Param("userId") Long userId, @Param("rating") Float rating);
    
    @Modifying
    @Query("UPDATE UserJpaEntity u SET u.reportCount = u.reportCount + 1 WHERE u.id = :userId")
    void incrementReportCount(@Param("userId") Long userId);
    
    @Query("SELECT u FROM UserJpaEntity u WHERE :role MEMBER OF u.roles ORDER BY u.averageRating DESC NULLS LAST")
    List<UserJpaEntity> findTopRatedRenters(@Param("role") Role role);
}