package com.vision_rent.automovil_unite.infrastructure.persistence.repository;


import com.vision_rent.automovil_unite.infrastructure.persistence.entity.NotificationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para la entidad NotificationJpaEntity.
 */
@Repository
public interface NotificationJpaRepository extends JpaRepository<NotificationJpaEntity, Long> {
    
    List<NotificationJpaEntity> findByUserId(Long userId);
    
    List<NotificationJpaEntity> findByUserIdAndReadFalse(Long userId);
    
    List<NotificationJpaEntity> findByUserIdAndType(Long userId, String type);
    
    int countByUserIdAndReadFalse(Long userId);
    
    @Modifying
    @Query("UPDATE NotificationJpaEntity n SET n.read = true, n.readAt = :now WHERE n.id = :notificationId")
    void markAsRead(@Param("notificationId") Long notificationId, @Param("now") LocalDateTime now);
    
    @Modifying
    @Query("UPDATE NotificationJpaEntity n SET n.read = true, n.readAt = :now WHERE n.user.id = :userId AND n.read = false")
    void markAllAsReadForUser(@Param("userId") Long userId, @Param("now") LocalDateTime now);
}