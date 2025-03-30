package com.vision_rent.automovil_unite.infrastructure.persistence.mapper;


import com.vision_rent.automovil_unite.domain.entity.Review;
import com.vision_rent.automovil_unite.infrastructure.persistence.entity.ReviewJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para convertir entre Review (dominio) y ReviewJpaEntity (persistencia).
 */
@Mapper(uses = {RentalMapper.class, UserMapper.class, VehicleMapper.class})
public interface ReviewMapper {
    
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "rental", source = "rental")
    @Mapping(target = "reviewer", source = "reviewer")
    @Mapping(target = "reviewed", source = "reviewed")
    @Mapping(target = "vehicle", source = "vehicle")
    Review toDomain(ReviewJpaEntity reviewJpaEntity);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "rental", source = "rental")
    @Mapping(target = "reviewer", source = "reviewer")
    @Mapping(target = "reviewed", source = "reviewed")
    @Mapping(target = "vehicle", source = "vehicle")
    ReviewJpaEntity toJpaEntity(Review review);
}