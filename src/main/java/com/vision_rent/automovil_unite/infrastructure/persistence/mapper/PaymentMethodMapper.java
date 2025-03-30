package com.vision_rent.automovil_unite.infrastructure.persistence.mapper;


import com.vision_rent.automovil_unite.domain.entity.PaymentMethod;
import com.vision_rent.automovil_unite.infrastructure.persistence.entity.PaymentMethodJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para convertir entre PaymentMethod (dominio) y PaymentMethodJpaEntity (persistencia).
 */
@Mapper(uses = {UserMapper.class})
public interface PaymentMethodMapper {
    
    PaymentMethodMapper INSTANCE = Mappers.getMapper(PaymentMethodMapper.class);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "user", source = "user")
    PaymentMethod toDomain(PaymentMethodJpaEntity paymentMethodJpaEntity);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "user", source = "user")
    PaymentMethodJpaEntity toJpaEntity(PaymentMethod paymentMethod);
}