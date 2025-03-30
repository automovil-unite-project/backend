package com.vision_rent.automovil_unite.domain.repository;



import com.vision_rent.automovil_unite.domain.entity.Receipt;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad Receipt en el dominio.
 */
public interface ReceiptRepository {
    Receipt save(Receipt receipt);
    Optional<Receipt> findById(Long id);
    Optional<Receipt> findByReceiptNumber(String receiptNumber);
    List<Receipt> findByRenterId(Long renterId);
    List<Receipt> findByOwnerId(Long ownerId);
    List<Receipt> findByRentalId(Long rentalId);
    List<Receipt> findByPaymentId(Long paymentId);
}