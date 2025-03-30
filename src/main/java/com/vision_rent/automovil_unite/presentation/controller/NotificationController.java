package com.vision_rent.automovil_unite.presentation.controller;

import com.vision_rent.automovil_unite.application.dto.NotificationDto;
import com.vision_rent.automovil_unite.application.service.NotificationService;
import com.vision_rent.automovil_unite.infrastructure.security.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Controlador para operaciones relacionadas con notificaciones.
 */
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    
    /**
     * Obtiene todas las notificaciones del usuario autenticado.
     *
     * @param userDetails Detalles del usuario autenticado
     * @return Lista de DTOs de notificación
     */
    @GetMapping
    public ResponseEntity<List<NotificationDto>> getMyNotifications(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(userDetails.getUserId()));
    }
    
    /**
     * Obtiene las notificaciones no leídas del usuario autenticado.
     *
     * @param userDetails Detalles del usuario autenticado
     * @return Lista de DTOs de notificación
     */
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDto>> getMyUnreadNotifications(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(notificationService.getUnreadNotificationsByUserId(userDetails.getUserId()));
    }
    
    /**
     * Cuenta las notificaciones no leídas del usuario autenticado.
     *
     * @param userDetails Detalles del usuario autenticado
     * @return Mapa con el contador
     */
    @GetMapping("/unread/count")
    public ResponseEntity<Map<String, Integer>> countMyUnreadNotifications(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        int count = notificationService.countUnreadNotifications(userDetails.getUserId());
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }
    
    /**
     * Marca una notificación como leída.
     *
     * @param id ID de la notificación
     * @param userDetails Detalles del usuario autenticado
     * @return Respuesta vacía
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        notificationService.markAsRead(id, userDetails.getUserId());
        return ResponseEntity.ok().build();
    }
    
    /**
     * Marca todas las notificaciones del usuario autenticado como leídas.
     *
     * @param userDetails Detalles del usuario autenticado
     * @return Respuesta vacía
     */
    @PutMapping("/mark-all-read")
    public ResponseEntity<Void> markAllAsRead(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        notificationService.markAllAsRead(userDetails.getUserId());
        return ResponseEntity.ok().build();
    }
}