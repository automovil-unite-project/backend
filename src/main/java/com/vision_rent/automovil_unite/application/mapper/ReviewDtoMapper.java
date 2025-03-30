package com.vision_rent.automovil_unite.application.mapper;


import com.vision_rent.automovil_unite.application.dto.CreateReviewRequest;
import com.vision_rent.automovil_unite.application.dto.ReviewDto;
import com.vision_rent.automovil_unite.domain.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para convertir entre Review (dominio) y ReviewDto (aplicación).
 */
@Mapper(uses = {RentalDtoMapper.class, UserDtoMapper.class, VehicleDtoMapper.class})
public interface ReviewDtoMapper {
    
    ReviewDtoMapper INSTANCE = Mappers.getMapper(ReviewDtoMapper.class);
    
    @Mapping(target = "rentalId", source = "rental.id")
    ReviewDto toDto(Review review);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rental", ignore = true)
    @Mapping(target = "reviewer", ignore = true)
    @Mapping(target = "reviewed", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Review toEntity(CreateReviewRequest createReviewRequest);
    
    @Mapping(target = "rental", ignore = true)
    Review toEntity(ReviewDto reviewDto);
}