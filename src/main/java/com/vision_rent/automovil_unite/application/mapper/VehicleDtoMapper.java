package com.vision_rent.automovil_unite.application.mapper;


import com.vision_rent.automovil_unite.application.dto.CreateVehicleRequest;
import com.vision_rent.automovil_unite.application.dto.VehicleDto;
import com.vision_rent.automovil_unite.domain.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para convertir entre Vehicle (dominio) y VehicleDto (aplicación).
 */
@Mapper(componentModel = "spring", uses = {UserDtoMapper.class})
public interface VehicleDtoMapper {
    
    VehicleDtoMapper INSTANCE = Mappers.getMapper(VehicleDtoMapper.class);
    
    VehicleDto toDto(Vehicle vehicle);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "available", constant = "true")
    @Mapping(target = "rentCount", constant = "0")
    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "photoUrls", ignore = true)
    @Mapping(target = "lastRentalEnd", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Vehicle toEntity(CreateVehicleRequest createVehicleRequest);
    
    Vehicle toEntity(VehicleDto vehicleDto);
}