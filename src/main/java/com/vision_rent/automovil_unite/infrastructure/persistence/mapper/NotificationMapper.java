package com.vision_rent.automovil_unite.infrastructure.persistence.mapper;

import com.vision_rent.automovil_unite.domain.entity.Notification;
import com.vision_rent.automovil_unite.infrastructure.persistence.entity.NotificationJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para convertir entre Notification (dominio) y NotificationJpaEntity (persistencia).
 */
@Mapper(uses = {UserMapper.class})
public interface NotificationMapper {
    
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "user", source = "user")
    Notification toDomain(NotificationJpaEntity notificationJpaEntity);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "user", source = "user")
    NotificationJpaEntity toJpaEntity(Notification notification);
}