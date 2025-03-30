package com.vision_rent.automovil_unite.domain.service;


import com.vision_rent.automovil_unite.domain.entity.*;
import org.springframework.stereotype.Service;

/**
 * Servicio de dominio para operaciones relacionadas con notificaciones.
 */

@Service
public interface NotificationDomainService {
    
    /**
     * Crea una notificación de alquiler creado.
     * 
     * @param rental Alquiler creado
     * @param recipient Usuario que recibirá la notificación
     * @return Notificación creada
     */
    Notification createRentalCreatedNotification(Rental rental, User recipient);
    
    /**
     * Crea una notificación de pago recibido.
     * 
     * @param payment Pago recibido
     * @param recipient Usuario que recibirá la notificación
     * @return Notificación creada
     */
    Notification createPaymentReceivedNotification(Payment payment, User recipient);
    
    /**
     * Crea una notificación de alquiler por finalizar.
     * 
     * @param rental Alquiler por finalizar
     * @param recipient Usuario que recibirá la notificación
     * @param hoursRemaining Horas restantes para la finalización
     * @return Notificación creada
     */
    Notification createRentalEndingNotification(Rental rental, User recipient, int hoursRemaining);
    
    /**
     * Crea una notificación de vehículo devuelto.
     * 
     * @param rental Alquiler finalizado
     * @param recipient Usuario que recibirá la notificación
     * @return Notificación creada
     */
    Notification createVehicleReturnedNotification(Rental rental, User recipient);
    
    /**
     * Crea una notificación de reseña recibida.
     * 
     * @param rental Alquiler reseñado
     * @param recipient Usuario que recibirá la notificación
     * @param rating Calificación recibida
     * @return Notificación creada
     */
    Notification createReviewReceivedNotification(Rental rental, User recipient, Float rating);
    
    /**
     * Crea una notificación de boleta generada.
     * 
     * @param receipt Boleta generada
     * @param recipient Usuario que recibirá la notificación
     * @return Notificación creada
     */
    Notification createReceiptGeneratedNotification(Receipt receipt, User recipient);
}