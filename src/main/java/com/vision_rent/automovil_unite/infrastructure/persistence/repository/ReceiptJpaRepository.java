package com.vision_rent.automovil_unite.infrastructure.persistence.repository;


import com.vision_rent.automovil_unite.infrastructure.persistence.entity.ReceiptJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad ReceiptJpaEntity.
 */
@Repository
public interface ReceiptJpaRepository extends JpaRepository<ReceiptJpaEntity, Long> {
    
    Optional<ReceiptJpaEntity> findByReceiptNumber(String receiptNumber);
    
    List<ReceiptJpaEntity> findByRenterId(Long renterId);
    
    List<ReceiptJpaEntity> findByOwnerId(Long ownerId);
    
    List<ReceiptJpaEntity> findByRentalId(Long rentalId);
    
    List<ReceiptJpaEntity> findByPaymentId(Long paymentId);
}