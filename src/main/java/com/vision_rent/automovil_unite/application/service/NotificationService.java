package com.vision_rent.automovil_unite.application.service;

import com.vision_rent.automovil_unite.application.dto.NotificationDto;
import com.vision_rent.automovil_unite.application.exception.ResourceNotFoundException;
import com.vision_rent.automovil_unite.application.mapper.NotificationDtoMapper;
import com.vision_rent.automovil_unite.domain.entity.Notification;
import com.vision_rent.automovil_unite.domain.entity.Payment;
import com.vision_rent.automovil_unite.domain.entity.Receipt;
import com.vision_rent.automovil_unite.domain.entity.Rental;
import com.vision_rent.automovil_unite.domain.entity.User;
import com.vision_rent.automovil_unite.domain.repository.NotificationRepository;
import com.vision_rent.automovil_unite.domain.repository.RentalRepository;
import com.vision_rent.automovil_unite.domain.repository.UserRepository;
import com.vision_rent.automovil_unite.domain.service.NotificationDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de notificaciones.
 */
@Service

public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;
    private final NotificationDomainService notificationDomainService;
    private final NotificationDtoMapper notificationDtoMapper;
    private final NotificationWebSocketService webSocketService;


    public NotificationService(NotificationRepository notificationRepository,
                               UserRepository userRepository,
                               RentalRepository rentalRepository,
                               NotificationDomainService notificationDomainService,
                               NotificationDtoMapper notificationDtoMapper,
                               NotificationWebSocketService webSocketService) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
        this.notificationDomainService = notificationDomainService;
        this.notificationDtoMapper = notificationDtoMapper;
        this.webSocketService = webSocketService;
    }

    /**
     * Obtiene todas las notificaciones de un usuario.
     *
     * @param userId ID del usuario
     * @return Lista de DTOs de notificación
     */
    @Transactional(readOnly = true)
    public List<NotificationDto> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId)
                .stream()
                .map(notificationDtoMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene las notificaciones no leídas de un usuario.
     *
     * @param userId ID del usuario
     * @return Lista de DTOs de notificación
     */
    @Transactional(readOnly = true)
    public List<NotificationDto> getUnreadNotificationsByUserId(Long userId) {
        return notificationRepository.findUnreadByUserId(userId)
                .stream()
                .map(notificationDtoMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Marca una notificación como leída.
     *
     * @param notificationId ID de la notificación
     * @param userId ID del usuario que solicita
     */
    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación", "id", notificationId));
        
        // Verificar que la notificación pertenece al usuario
        if (!notification.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Notificación", "id", notificationId);
        }
        
        notificationRepository.markAsRead(notificationId);
    }
    
    /**
     * Marca todas las notificaciones de un usuario como leídas.
     *
     * @param userId ID del usuario
     */
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsReadForUser(userId);
    }
    
    /**
     * Cuenta las notificaciones no leídas de un usuario.
     *
     * @param userId ID del usuario
     * @return Número de notificaciones no leídas
     */
    @Transactional(readOnly = true)
    public int countUnreadNotifications(Long userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }
    
    /**
     * Notifica la creación de un alquiler.
     *
     * @param rental Alquiler creado
     */
    @Transactional
    public void notifyRentalCreated(Rental rental) {
        // Notificar al arrendatario
        User renter = rental.getRenter();
        Notification renterNotification = notificationDomainService.createRentalCreatedNotification(rental, renter);
        renterNotification = notificationRepository.save(renterNotification);

        // Enviar notificación en tiempo real al arrendatario
        NotificationDto renterNotificationDto = notificationDtoMapper.toDto(renterNotification);
        webSocketService.sendNotificationToUser(renter.getId(), renterNotificationDto);

        // Notificar al propietario
        User owner = rental.getVehicle().getOwner();
        Notification ownerNotification = notificationDomainService.createRentalCreatedNotification(rental, owner);
        ownerNotification = notificationRepository.save(ownerNotification);

        // Enviar notificación en tiempo real al propietario
        NotificationDto ownerNotificationDto = notificationDtoMapper.toDto(ownerNotification);
        webSocketService.sendNotificationToUser(owner.getId(), ownerNotificationDto);
    }
    
    /**
     * Notifica un pago recibido.
     *
     * @param payment Pago recibido
     * @param renterId ID del arrendatario
     * @param ownerId ID del propietario
     */
    @Transactional
    public void notifyPaymentReceived(Payment payment, Long renterId, Long ownerId) {
        // Notificar al arrendatario
        User renter = userRepository.findById(renterId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", renterId));
        Notification renterNotification = notificationDomainService.createPaymentReceivedNotification(payment, renter);
        renterNotification = notificationRepository.save(renterNotification);

        // Enviar notificación en tiempo real al arrendatario
        NotificationDto renterNotificationDto = notificationDtoMapper.toDto(renterNotification);
        webSocketService.sendNotificationToUser(renterId, renterNotificationDto);

        // Notificar al propietario
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", ownerId));
        Notification ownerNotification = notificationDomainService.createPaymentReceivedNotification(payment, owner);
        ownerNotification = notificationRepository.save(ownerNotification);

        // Enviar notificación en tiempo real al propietario
        NotificationDto ownerNotificationDto = notificationDtoMapper.toDto(ownerNotification);
        webSocketService.sendNotificationToUser(ownerId, ownerNotificationDto);
    }
    
    /**
     * Notifica un alquiler por finalizar.
     *
     * @param rentalId ID del alquiler
     * @param hoursRemaining Horas restantes
     */
    @Transactional
    public void notifyRentalEnding(Long rentalId, int hoursRemaining) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Alquiler", "id", rentalId));
        
        // Notificar solo al arrendatario
        User renter = rental.getRenter();
        Notification notification = notificationDomainService.createRentalEndingNotification(rental, renter, hoursRemaining);
        notificationRepository.save(notification);
    }
    
    /**
     * Notifica la devolución de un vehículo.
     *
     * @param rental Alquiler finalizado
     */
    @Transactional
    public void notifyVehicleReturned(Rental rental) {
        // Notificar al arrendatario
        User renter = rental.getRenter();
        Notification renterNotification = notificationDomainService.createVehicleReturnedNotification(rental, renter);
        notificationRepository.save(renterNotification);
        
        // Notificar al propietario
        User owner = rental.getVehicle().getOwner();
        Notification ownerNotification = notificationDomainService.createVehicleReturnedNotification(rental, owner);
        notificationRepository.save(ownerNotification);
    }
    
    /**
     * Notifica una reseña recibida.
     *
     * @param rental Alquiler reseñado
     * @param recipientId ID del usuario que recibe la reseña
     * @param rating Calificación recibida
     */
    @Transactional
    public void notifyReviewReceived(Rental rental, Long recipientId, Float rating) {
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", recipientId));
        
        Notification notification = notificationDomainService.createReviewReceivedNotification(rental, recipient, rating);
        notificationRepository.save(notification);
    }
    
    /**
     * Notifica una boleta generada.
     *
     * @param receipt Boleta generada
     */
    @Transactional
    public void notifyReceiptGenerated(Receipt receipt) {
        // Notificar al arrendatario
        User renter = receipt.getRenter();
        Notification renterNotification = notificationDomainService.createReceiptGeneratedNotification(receipt, renter);
        notificationRepository.save(renterNotification);
    }


}