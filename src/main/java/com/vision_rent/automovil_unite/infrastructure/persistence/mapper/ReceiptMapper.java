package com.vision_rent.automovil_unite.infrastructure.persistence.mapper;

import com.vision_rent.automovil_unite.domain.entity.Receipt;
import com.vision_rent.automovil_unite.infrastructure.persistence.entity.ReceiptJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para convertir entre Receipt (dominio) y ReceiptJpaEntity (persistencia).
 */
@Mapper(uses = {PaymentMapper.class, RentalMapper.class, UserMapper.class, VehicleMapper.class})
public interface ReceiptMapper {
    
    ReceiptMapper INSTANCE = Mappers.getMapper(ReceiptMapper.class);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "payment", source = "payment")
    @Mapping(target = "rental", source = "rental")
    @Mapping(target = "renter", source = "renter")
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "vehicle", source = "vehicle")
    Receipt toDomain(ReceiptJpaEntity receiptJpaEntity);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "payment", source = "payment")
    @Mapping(target = "rental", source = "rental")
    @Mapping(target = "renter", source = "renter")
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "vehicle", source = "vehicle")
    ReceiptJpaEntity toJpaEntity(Receipt receipt);
}