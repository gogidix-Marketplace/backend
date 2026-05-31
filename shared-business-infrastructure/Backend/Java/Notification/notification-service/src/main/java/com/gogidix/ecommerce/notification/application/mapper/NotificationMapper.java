package com.gogidix.ecommerce.notification.application.mapper;

import com.gogidix.ecommerce.notification.application.dto.*;
import com.gogidix.ecommerce.notification.domain.model.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(Notification entity) {
        if (entity == null) return null;
        return new NotificationResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getRecipientId(),
            entity.getNotificationType(),
            entity.getMessage(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public Notification toEntity(CreateNotificationRequest request) {
        Notification entity = new Notification();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setRecipientId(request.recipientId());
        entity.setNotificationType(request.notificationType());
        entity.setMessage(request.message());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(Notification entity, UpdateNotificationRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.recipientId() != null) entity.setRecipientId(request.recipientId());
        if (request.notificationType() != null) entity.setNotificationType(request.notificationType());
        if (request.message() != null) entity.setMessage(request.message());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}
