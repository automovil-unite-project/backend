package com.vision_rent.automovil_unite.infrastructure.persistence.mapper;


import com.vision_rent.automovil_unite.domain.entity.Vehicle;
import com.vision_rent.automovil_unite.infrastructure.persistence.entity.VehicleJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para convertir entre Vehicle (dominio) y VehicleJpaEntity (persistencia).
 */
@Mapper(uses = {UserMapper.class})
public interface VehicleMapper {
    
    VehicleMapper INSTANCE = Mappers.getMapper(VehicleMapper.class);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "owner", source = "owner")
    Vehicle toDomain(VehicleJpaEntity vehicleJpaEntity);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "owner", source = "owner")
    VehicleJpaEntity toJpaEntity(Vehicle vehicle);
}