package com.vision_rent.automovil_unite.application.mapper;

import com.vision_rent.automovil_unite.application.dto.ReceiptDto;
import com.vision_rent.automovil_unite.domain.entity.Receipt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para convertir entre Receipt (dominio) y ReceiptDto (aplicación).
 */
@Mapper(componentModel = "spring", uses = {UserDtoMapper.class, VehicleDtoMapper.class})
public interface ReceiptDtoMapper {
    
    ReceiptDtoMapper INSTANCE = Mappers.getMapper(ReceiptDtoMapper.class);
    
    @Mapping(target = "paymentId", source = "payment.id")
    @Mapping(target = "rentalId", source = "rental.id")
    ReceiptDto toDto(Receipt receipt);
    
    @Mapping(target = "payment", ignore = true)
    @Mapping(target = "rental", ignore = true)
    Receipt toEntity(ReceiptDto receiptDto);
}