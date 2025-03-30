package com.vision_rent.automovil_unite.infrastructure.persistence.mapper;

import com.vision_rent.automovil_unite.domain.entity.Payment;
import com.vision_rent.automovil_unite.infrastructure.persistence.entity.PaymentJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para convertir entre Payment (dominio) y PaymentJpaEntity (persistencia).
 */
@Mapper(uses = {RentalMapper.class, UserMapper.class, PaymentMethodMapper.class})
public interface PaymentMapper {
    
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "rental", source = "rental")
    @Mapping(target = "payer", source = "payer")
    @Mapping(target = "paymentMethod", source = "paymentMethod")
    Payment toDomain(PaymentJpaEntity paymentJpaEntity);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "rental", source = "rental")
    @Mapping(target = "payer", source = "payer")
    @Mapping(target = "paymentMethod", source = "paymentMethod")
    PaymentJpaEntity toJpaEntity(Payment payment);
}