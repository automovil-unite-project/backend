package com.vision_rent.automovil_unite.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Entidad de dominio que representa una notificación para un usuario.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntity {
    private User user;
    private String title;
    private String message;
    private String type; // RENTAL_CREATED, PAYMENT_RECEIVED, RENTAL_ENDING, VEHICLE_RETURNED, etc.
    private String relatedEntityType; // RENTAL, PAYMENT, RECEIPT, REVIEW, etc.
    private Long relatedEntityId;
    private Boolean read;
    private LocalDateTime readAt;
    private String deepLink; // URL para navegar directamente en la aplicación
}