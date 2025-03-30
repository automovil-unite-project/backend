package com.vision_rent.automovil_unite.domain.repository;



import com.vision_rent.automovil_unite.domain.entity.Payment;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad Payment en el dominio.
 */
public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findById(Long id);
    Optional<Payment> findByExternalId(String externalId);
    List<Payment> findByRentalId(Long rentalId);
    List<Payment> findByPayerId(Long payerId);
    List<Payment> findByStatus(String status);
}