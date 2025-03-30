package com.vision_rent.automovil_unite.infrastructure.persistence.adapter;

import com.vision_rent.automovil_unite.domain.entity.Receipt;
import com.vision_rent.automovil_unite.domain.repository.ReceiptRepository;
import com.vision_rent.automovil_unite.infrastructure.persistence.mapper.ReceiptMapper;
import com.vision_rent.automovil_unite.infrastructure.persistence.repository.ReceiptJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador para el repositorio de boletas/recibos que implementa la interfaz de dominio.
 */
@Component
@RequiredArgsConstructor
public class ReceiptRepositoryAdapter implements ReceiptRepository {

    private final ReceiptJpaRepository receiptJpaRepository;
    private final ReceiptMapper receiptMapper = ReceiptMapper.INSTANCE;
    
    @Override
    public Receipt save(Receipt receipt) {
        var receiptJpaEntity = receiptMapper.toJpaEntity(receipt);
        var savedEntity = receiptJpaRepository.save(receiptJpaEntity);
        return receiptMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Receipt> findById(Long id) {
        return receiptJpaRepository.findById(id)
                .map(receiptMapper::toDomain);
    }

    @Override
    public Optional<Receipt> findByReceiptNumber(String receiptNumber) {
        return receiptJpaRepository.findByReceiptNumber(receiptNumber)
                .map(receiptMapper::toDomain);
    }

    @Override
    public List<Receipt> findByRenterId(Long renterId) {
        return receiptJpaRepository.findByRenterId(renterId)
                .stream()
                .map(receiptMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Receipt> findByOwnerId(Long ownerId) {
        return receiptJpaRepository.findByOwnerId(ownerId)
                .stream()
                .map(receiptMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Receipt> findByRentalId(Long rentalId) {
        return receiptJpaRepository.findByRentalId(rentalId)
                .stream()
                .map(receiptMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Receipt> findByPaymentId(Long paymentId) {
        return receiptJpaRepository.findByPaymentId(paymentId)
                .stream()
                .map(receiptMapper::toDomain)
                .collect(Collectors.toList());
    }
}