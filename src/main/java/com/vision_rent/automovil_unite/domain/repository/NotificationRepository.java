package com.vision_rent.automovil_unite.domain.repository;



import com.vision_rent.automovil_unite.domain.entity.Notification;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad Notification en el dominio.
 */
public interface NotificationRepository {
    Notification save(Notification notification);
    Optional<Notification> findById(Long id);
    List<Notification> findByUserId(Long userId);
    List<Notification> findUnreadByUserId(Long userId);
    List<Notification> findByUserIdAndType(Long userId, String type);
    int countUnreadByUserId(Long userId);
    void markAsRead(Long notificationId);
    void markAllAsReadForUser(Long userId);
}