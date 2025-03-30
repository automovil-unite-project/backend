package com.vision_rent.automovil_unite.infrastructure.persistence.adapter;

import com.vision_rent.automovil_unite.domain.entity.Notification;
import com.vision_rent.automovil_unite.domain.repository.NotificationRepository;
import com.vision_rent.automovil_unite.infrastructure.persistence.mapper.NotificationMapper;
import com.vision_rent.automovil_unite.infrastructure.persistence.repository.NotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador para el repositorio de notificaciones que implementa la interfaz de dominio.
 */
@Component
@RequiredArgsConstructor
public class NotificationRepositoryAdapter implements NotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;
    private final NotificationMapper notificationMapper = NotificationMapper.INSTANCE;
    
    @Override
    public Notification save(Notification notification) {
        var notificationJpaEntity = notificationMapper.toJpaEntity(notification);
        var savedEntity = notificationJpaRepository.save(notificationJpaEntity);
        return notificationMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationJpaRepository.findById(id)
                .map(notificationMapper::toDomain);
    }

    @Override
    public List<Notification> findByUserId(Long userId) {
        return notificationJpaRepository.findByUserId(userId)
                .stream()
                .map(notificationMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findUnreadByUserId(Long userId) {
        return notificationJpaRepository.findByUserIdAndReadFalse(userId)
                .stream()
                .map(notificationMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> findByUserIdAndType(Long userId, String type) {
        return notificationJpaRepository.findByUserIdAndType(userId, type)
                .stream()
                .map(notificationMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public int countUnreadByUserId(Long userId) {
        return notificationJpaRepository.countByUserIdAndReadFalse(userId);
    }

    @Override
    public void markAsRead(Long notificationId) {
        notificationJpaRepository.markAsRead(notificationId, LocalDateTime.now());
    }

    @Override
    public void markAllAsReadForUser(Long userId) {
        notificationJpaRepository.markAllAsReadForUser(userId, LocalDateTime.now());
    }
}