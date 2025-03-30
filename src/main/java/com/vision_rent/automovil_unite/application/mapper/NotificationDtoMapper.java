package com.vision_rent.automovil_unite.application.mapper;

import com.vision_rent.automovil_unite.application.dto.NotificationDto;
import com.vision_rent.automovil_unite.domain.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para convertir entre Notification (dominio) y NotificationDto (aplicación).
 */
@Mapper(componentModel = "spring", uses = {UserDtoMapper.class})
public interface NotificationDtoMapper {
    
    NotificationDtoMapper INSTANCE = Mappers.getMapper(NotificationDtoMapper.class);
    
    @Mapping(target = "userId", source = "user.id")
    NotificationDto toDto(Notification notification);
    
    @Mapping(target = "user", ignore = true)
    Notification toEntity(NotificationDto notificationDto);
}