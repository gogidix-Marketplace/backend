package com.gogidix.shared.courier.notification.application.mapper;

import com.gogidix.shared.courier.notification.application.command.SendNotificationCommand;
import com.gogidix.shared.courier.notification.application.dto.NotificationDTO;
import com.gogidix.shared.courier.notification.domain.entity.Notification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Mapper between DTOs and domain entities for notifications
 */
@Component
public class NotificationDtoMapper {

    public Notification toEntity(SendNotificationCommand command) {
        return Notification.builder()
                .notificationId(UUID.randomUUID().toString())
                .tenantId(command.getTenantId())
                .recipientId(command.getRecipientId())
                .recipientType(command.getRecipientType())
                .notificationType(command.getNotificationType())
                .title(command.getTitle())
                .message(command.getMessage())
                .actionUrl(command.getActionUrl())
                .actionLabel(command.getActionLabel())
                .metadata(command.getMetadata())
                .priority(command.getPriority() != null ? command.getPriority() : "normal")
                .channel(command.getChannel())
                .status(Notification.NotificationStatus.SENT)
                .sentAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public NotificationDTO toDTO(Notification entity) {
        return NotificationDTO.builder()
                .notificationId(entity.getNotificationId())
                .tenantId(entity.getTenantId())
                .recipientId(entity.getRecipientId())
                .recipientType(entity.getRecipientType())
                .notificationType(entity.getNotificationType())
                .title(entity.getTitle())
                .message(entity.getMessage())
                .actionUrl(entity.getActionUrl())
                .actionLabel(entity.getActionLabel())
                .metadata(entity.getMetadata())
                .priority(entity.getPriority())
                .channel(entity.getChannel())
                .status(entity.getStatus())
                .sentAt(entity.getSentAt())
                .readAt(entity.getReadAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
