package com.vision_rent.automovil_unite.infrastructure.persistence.adapter;

import com.vision_rent.automovil_unite.domain.entity.Payment;
import com.vision_rent.automovil_unite.domain.repository.PaymentRepository;
import com.vision_rent.automovil_unite.infrastructure.persistence.mapper.PaymentMapper;
import com.vision_rent.automovil_unite.infrastructure.persistence.repository.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador para el repositorio de pagos que implementa la interfaz de dominio.
 */
@Component
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;
    private final PaymentMapper paymentMapper = PaymentMapper.INSTANCE;
    
    @Override
    public Payment save(Payment payment) {
        var paymentJpaEntity = paymentMapper.toJpaEntity(payment);
        var savedEntity = paymentJpaRepository.save(paymentJpaEntity);
        return paymentMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return paymentJpaRepository.findById(id)
                .map(paymentMapper::toDomain);
    }

    @Override
    public Optional<Payment> findByExternalId(String externalId) {
        return paymentJpaRepository.findByExternalId(externalId)
                .map(paymentMapper::toDomain);
    }

    @Override
    public List<Payment> findByRentalId(Long rentalId) {
        return paymentJpaRepository.findByRentalId(rentalId)
                .stream()
                .map(paymentMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Payment> findByPayerId(Long payerId) {
        return paymentJpaRepository.findByPayerId(payerId)
                .stream()
                .map(paymentMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Payment> findByStatus(String status) {
        return paymentJpaRepository.findByStatus(status)
                .stream()
                .map(paymentMapper::toDomain)
                .collect(Collectors.toList());
    }
}