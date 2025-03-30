package com.vision_rent.automovil_unite.application.mapper;


import com.vision_rent.automovil_unite.application.dto.CreateRentalRequest;
import com.vision_rent.automovil_unite.application.dto.RentalDto;
import com.vision_rent.automovil_unite.domain.entity.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para convertir entre Rental (dominio) y RentalDto (aplicación).
 */
@Mapper(uses = {VehicleDtoMapper.class, UserDtoMapper.class})
public interface RentalDtoMapper {
    
    RentalDtoMapper INSTANCE = Mappers.getMapper(RentalDtoMapper.class);
    
    RentalDto toDto(Rental rental);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "renter", ignore = true)
    @Mapping(target = "actualReturnDateTime", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "securityDeposit", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "paid", constant = "false")
    @Mapping(target = "renterRating", ignore = true)
    @Mapping(target = "renterReview", ignore = true)
    @Mapping(target = "vehicleRating", ignore = true)
    @Mapping(target = "vehicleReview", ignore = true)
    @Mapping(target = "renterReported", constant = "false")
    @Mapping(target = "renterReportReason", ignore = true)
    @Mapping(target = "lateReturn", constant = "false")
    @Mapping(target = "discountApplied", constant = "false")
    @Mapping(target = "discountAmount", ignore = true)
    @Mapping(target = "lateReturnFee", ignore = true)
    @Mapping(target = "extendedUntil", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Rental toEntity(CreateRentalRequest createRentalRequest);
    
    Rental toEntity(RentalDto rentalDto);
}