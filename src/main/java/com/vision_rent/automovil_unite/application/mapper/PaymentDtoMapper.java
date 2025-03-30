package com.vision_rent.automovil_unite.application.mapper;

import com.vision_rent.automovil_unite.application.dto.PaymentDto;
import com.vision_rent.automovil_unite.domain.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper para convertir entre Payment (dominio) y PaymentDto (aplicación).
 */
@Mapper(componentModel = "spring", uses = {UserDtoMapper.class})
public abstract class PaymentDtoMapper {

    @Autowired
    protected PaymentMethodDtoMapper paymentMethodDtoMapper;

    @Mapping(target = "rentalId", source = "rental.id")
    @Mapping(target = "paymentMethod", expression = "java(paymentMethodDtoMapper.toDto(payment.getPaymentMethod()))")
    public abstract PaymentDto toDto(Payment payment);

    @Mapping(target = "rental", ignore = true)
    @Mapping(target = "paymentMethod", ignore = true)
    public abstract Payment toEntity(PaymentDto paymentDto);
}