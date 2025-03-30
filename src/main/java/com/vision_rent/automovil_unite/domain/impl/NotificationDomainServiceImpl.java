package com.vision_rent.automovil_unite.domain.impl;


import com.vision_rent.automovil_unite.domain.entity.*;
import com.vision_rent.automovil_unite.domain.service.NotificationDomainService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Implementación del servicio de dominio para operaciones relacionadas con notificaciones.
 */
@Service
public class NotificationDomainServiceImpl implements NotificationDomainService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    @Override
    public Notification createRentalCreatedNotification(Rental rental, User recipient) {
        String title = "Nuevo alquiler creado";
        String message = String.format("Se ha creado un nuevo alquiler para el vehículo %s %s. " +
                        "Inicio: %s, Fin: %s",
                rental.getVehicle().getBrand(),
                rental.getVehicle().getModel(),
                rental.getStartDateTime().format(DATE_FORMATTER),
                rental.getEndDateTime().format(DATE_FORMATTER));
                
        return createNotification(recipient, title, message, "RENTAL_CREATED", "RENTAL", rental.getId());
    }

    @Override
    public Notification createPaymentReceivedNotification(Payment payment, User recipient) {
        String title = "Pago recibido";
        String message = String.format("Se ha recibido un pago de %s %s por el alquiler del vehículo %s %s.",
                payment.getAmount(),
                payment.getCurrency(),
                payment.getRental().getVehicle().getBrand(),
                payment.getRental().getVehicle().getModel());
                
        return createNotification(recipient, title, message, "PAYMENT_RECEIVED", "PAYMENT", payment.getId());
    }

    @Override
    public Notification createRentalEndingNotification(Rental rental, User recipient, int hoursRemaining) {
        String title = "Alquiler por finalizar";
        String message = String.format("Su alquiler del vehículo %s %s finalizará en %d horas. " +
                        "Fecha de fin: %s",
                rental.getVehicle().getBrand(),
                rental.getVehicle().getModel(),
                hoursRemaining,
                rental.getEndDateTime().format(DATE_FORMATTER));
                
        return createNotification(recipient, title, message, "RENTAL_ENDING", "RENTAL", rental.getId());
    }

    @Override
    public Notification createVehicleReturnedNotification(Rental rental, User recipient) {
        String title = "Vehículo devuelto";
        String message = String.format("El vehículo %s %s ha sido devuelto. " +
                        "Fecha de devolución: %s",
                rental.getVehicle().getBrand(),
                rental.getVehicle().getModel(),
                rental.getActualReturnDateTime().format(DATE_FORMATTER));
                
        return createNotification(recipient, title, message, "VEHICLE_RETURNED", "RENTAL", rental.getId());
    }

    @Override
    public Notification createReviewReceivedNotification(Rental rental, User recipient, Float rating) {
        String title = "Nueva reseña recibida";
        String message = String.format("Has recibido una nueva reseña con calificación %.1f estrellas " +
                        "por el alquiler del vehículo %s %s.",
                rating,
                rental.getVehicle().getBrand(),
                rental.getVehicle().getModel());
                
        return createNotification(recipient, title, message, "REVIEW_RECEIVED", "RENTAL", rental.getId());
    }

    @Override
    public Notification createReceiptGeneratedNotification(Receipt receipt, User recipient) {
        String title = "Boleta generada";
        String message = String.format("Se ha generado la boleta N° %s por el alquiler del vehículo %s %s. " +
                        "Monto total: %s %s.",
                receipt.getReceiptNumber(),
                receipt.getVehicle().getBrand(),
                receipt.getVehicle().getModel(),
                receipt.getTotalAmount(),
                receipt.getCurrency());
                
        return createNotification(recipient, title, message, "RECEIPT_GENERATED", "RECEIPT", receipt.getId());
    }
    
    /**
     * Método helper para crear una notificación.
     */
    private Notification createNotification(User recipient, String title, String message, 
                                          String type, String entityType, Long entityId) {
        Notification notification = new Notification();
        notification.setUser(recipient);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRelatedEntityType(entityType);
        notification.setRelatedEntityId(entityId);
        notification.setRead(false);
        notification.setDeepLink("/app/" + entityType.toLowerCase() + "/" + entityId);
        
        return notification;
    }
}