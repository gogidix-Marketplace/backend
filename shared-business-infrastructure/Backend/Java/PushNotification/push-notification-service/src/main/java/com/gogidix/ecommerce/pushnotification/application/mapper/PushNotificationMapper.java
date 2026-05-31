package com.gogidix.ecommerce.pushnotification.application.mapper;

import com.gogidix.ecommerce.pushnotification.application.dto.CreatePushNotificationRequest;
import com.gogidix.ecommerce.pushnotification.application.dto.UpdatePushNotificationRequest;
import com.gogidix.ecommerce.pushnotification.application.dto.PushNotificationResponse;
import com.gogidix.ecommerce.pushnotification.domain.model.PushNotification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PushNotificationMapper {

    public PushNotificationResponse toResponse(PushNotification entity) {
        return new PushNotificationResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.isActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getDeviceToken(),
            entity.getPlatform(),
            entity.getTitle(),
            entity.getPayload()
        );
    }

    public PushNotification toEntity(CreatePushNotificationRequest request) {
        PushNotification entity = new PushNotification();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setType(request.getType());
        entity.setActive(true);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    public void updateFromRequest(PushNotification entity, UpdatePushNotificationRequest request) {
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getType() != null) entity.setType(request.getType());
        if (request.getIsActive() != null) entity.setActive(request.getIsActive());
        entity.setUpdatedAt(LocalDateTime.now());
    }
}