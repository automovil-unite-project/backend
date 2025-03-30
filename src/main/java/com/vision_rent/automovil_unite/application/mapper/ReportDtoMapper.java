package com.vision_rent.automovil_unite.application.mapper;


import com.vision_rent.automovil_unite.application.dto.CreateReportRequest;
import com.vision_rent.automovil_unite.application.dto.ReportDto;
import com.vision_rent.automovil_unite.domain.entity.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para convertir entre Report (dominio) y ReportDto (aplicación).
 */
@Mapper(uses = {RentalDtoMapper.class, UserDtoMapper.class})
public interface ReportDtoMapper {
    
    ReportDtoMapper INSTANCE = Mappers.getMapper(ReportDtoMapper.class);
    
    @Mapping(target = "rentalId", source = "rental.id")
    ReportDto toDto(Report report);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rental", ignore = true)
    @Mapping(target = "reporter", ignore = true)
    @Mapping(target = "reported", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "resolution", ignore = true)
    @Mapping(target = "resolvedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Report toEntity(CreateReportRequest createReportRequest);
    
    @Mapping(target = "rental", ignore = true)
    Report toEntity(ReportDto reportDto);
}