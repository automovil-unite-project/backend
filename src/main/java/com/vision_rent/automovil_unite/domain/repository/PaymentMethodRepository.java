package com.vision_rent.automovil_unite.domain.repository;



import com.vision_rent.automovil_unite.domain.entity.PaymentMethod;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad PaymentMethod en el dominio.
 */
public interface PaymentMethodRepository {
    PaymentMethod save(PaymentMethod paymentMethod);
    Optional<PaymentMethod> findById(Long id);
    List<PaymentMethod> findByUserId(Long userId);
    Optional<PaymentMethod> findDefaultByUserId(Long userId);
    void delete(PaymentMethod paymentMethod);
    boolean existsByUserIdAndAlias(Long userId, String alias);
}