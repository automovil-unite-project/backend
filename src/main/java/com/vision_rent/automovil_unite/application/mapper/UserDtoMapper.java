package com.vision_rent.automovil_unite.application.mapper;


import com.vision_rent.automovil_unite.application.dto.RegisterRequest;
import com.vision_rent.automovil_unite.application.dto.UserDto;
import com.vision_rent.automovil_unite.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para convertir entre User (dominio) y UserDto (aplicación).
 */
@Mapper(componentModel = "spring")
public interface UserDtoMapper {



    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "profilePhotoUrl", ignore = true)
    @Mapping(target = "idCardPhotoUrl", ignore = true)
    @Mapping(target = "criminalRecordUrl", ignore = true)
    @Mapping(target = "driverLicenseUrl", ignore = true)
    @Mapping(target = "banned", constant = "false")
    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "reportCount", constant = "0")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(RegisterRequest registerRequest);

    User toEntity(UserDto userDto);


}