package com.vision_rent.automovil_unite.infrastructure.persistence.mapper;


import com.vision_rent.automovil_unite.domain.entity.Report;
import com.vision_rent.automovil_unite.infrastructure.persistence.entity.ReportJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para convertir entre Report (dominio) y ReportJpaEntity (persistencia).
 */
@Mapper(uses = {RentalMapper.class, UserMapper.class})
public interface ReportMapper {
    
    ReportMapper INSTANCE = Mappers.getMapper(ReportMapper.class);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "rental", source = "rental")
    @Mapping(target = "reporter", source = "reporter")
    @Mapping(target = "reported", source = "reported")
    @Mapping(target = "resolvedBy", source = "resolvedBy")
    Report toDomain(ReportJpaEntity reportJpaEntity);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "rental", source = "rental")
    @Mapping(target = "reporter", source = "reporter")
    @Mapping(target = "reported", source = "reported")
    @Mapping(target = "resolvedBy", source = "resolvedBy")
    ReportJpaEntity toJpaEntity(Report report);
}