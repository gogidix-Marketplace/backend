package com.gogidix.ecommerce.pushnotification.application.mapper;

import com.gogidix.ecommerce.pushnotification.application.dto.*;
import com.gogidix.ecommerce.pushnotification.domain.model.PushNotification;
import org.springframework.stereotype.Component;

@Component
public class PushNotificationMapper {

    public PushNotificationResponse toResponse(PushNotification entity) {
        if (entity == null) return null;
        return new PushNotificationResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getDeviceToken(),
            entity.getPlatform(),
            entity.getMessageBody(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public PushNotification toEntity(CreatePushNotificationRequest request) {
        PushNotification entity = new PushNotification();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setDeviceToken(request.deviceToken());
        entity.setPlatform(request.platform());
        entity.setMessageBody(request.messageBody());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(PushNotification entity, UpdatePushNotificationRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.deviceToken() != null) entity.setDeviceToken(request.deviceToken());
        if (request.platform() != null) entity.setPlatform(request.platform());
        if (request.messageBody() != null) entity.setMessageBody(request.messageBody());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}
