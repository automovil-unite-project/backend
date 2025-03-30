package com.vision_rent.automovil_unite.application.service;

import com.vision_rent.automovil_unite.application.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Servicio para enviar notificaciones en tiempo real utilizando WebSocket.
 */
@Service
@RequiredArgsConstructor
public class NotificationWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    
    /**
     * Envía una notificación en tiempo real a un usuario específico.
     *
     * @param userId ID del usuario destinatario
     * @param notification Notificación a enviar
     */
    public void sendNotificationToUser(Long userId, NotificationDto notification) {
        // El destino es una cola específica para el usuario
        messagingTemplate.convertAndSendToUser(
                userId.toString(), 
                "/queue/notifications", 
                notification
        );
        
        // También enviamos un contador actualizado
        sendUnreadNotificationCount(userId, Math.toIntExact(notification.getUserId()));
    }
    
    /**
     * Envía el contador de notificaciones no leídas a un usuario.
     *
     * @param userId ID del usuario destinatario
     * @param count Número de notificaciones no leídas
     */
    public void sendUnreadNotificationCount(Long userId, int count) {
        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/queue/notification-count",
                count
        );
    }
    
    /**
     * Envía una notificación a todos los usuarios.
     *
     * @param notification Notificación a enviar
     */
    public void sendGlobalNotification(NotificationDto notification) {
        messagingTemplate.convertAndSend("/topic/global-notifications", notification);
    }
}