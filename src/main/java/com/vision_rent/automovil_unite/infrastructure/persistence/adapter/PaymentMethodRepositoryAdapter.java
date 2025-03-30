package com.vision_rent.automovil_unite.infrastructure.persistence.adapter;

import com.vision_rent.automovil_unite.domain.entity.PaymentMethod;
import com.vision_rent.automovil_unite.domain.repository.PaymentMethodRepository;
import com.vision_rent.automovil_unite.infrastructure.persistence.mapper.PaymentMethodMapper;
import com.vision_rent.automovil_unite.infrastructure.persistence.repository.PaymentMethodJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador para el repositorio de métodos de pago que implementa la interfaz de dominio.
 */
@Component
@RequiredArgsConstructor
public class PaymentMethodRepositoryAdapter implements PaymentMethodRepository {

    private final PaymentMethodJpaRepository paymentMethodJpaRepository;
    private final PaymentMethodMapper paymentMethodMapper = PaymentMethodMapper.INSTANCE;
    
    @Override
    public PaymentMethod save(PaymentMethod paymentMethod) {
        var paymentMethodJpaEntity = paymentMethodMapper.toJpaEntity(paymentMethod);
        var savedEntity = paymentMethodJpaRepository.save(paymentMethodJpaEntity);
        return paymentMethodMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<PaymentMethod> findById(Long id) {
        return paymentMethodJpaRepository.findById(id)
                .map(paymentMethodMapper::toDomain);
    }

    @Override
    public List<PaymentMethod> findByUserId(Long userId) {
        return paymentMethodJpaRepository.findByUserId(userId)
                .stream()
                .map(paymentMethodMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PaymentMethod> findDefaultByUserId(Long userId) {
        return paymentMethodJpaRepository.findDefaultByUserId(userId)
                .map(paymentMethodMapper::toDomain);
    }

    @Override
    public void delete(PaymentMethod paymentMethod) {
        paymentMethodJpaRepository.deleteById(paymentMethod.getId());
    }

    @Override
    public boolean existsByUserIdAndAlias(Long userId, String alias) {
        return paymentMethodJpaRepository.existsByUserIdAndAlias(userId, alias);
    }
}