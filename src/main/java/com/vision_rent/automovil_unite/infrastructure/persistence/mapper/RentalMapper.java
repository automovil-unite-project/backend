package com.vision_rent.automovil_unite.infrastructure.persistence.mapper;


import com.vision_rent.automovil_unite.domain.entity.Rental;
import com.vision_rent.automovil_unite.infrastructure.persistence.entity.RentalJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para convertir entre Rental (dominio) y RentalJpaEntity (persistencia).
 */
@Mapper(uses = {VehicleMapper.class, UserMapper.class})
public interface RentalMapper {
    
    RentalMapper INSTANCE = Mappers.getMapper(RentalMapper.class);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "vehicle", source = "vehicle")
    @Mapping(target = "renter", source = "renter")
    Rental toDomain(RentalJpaEntity rentalJpaEntity);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "vehicle", source = "vehicle")
    @Mapping(target = "renter", source = "renter")
    RentalJpaEntity toJpaEntity(Rental rental);
}