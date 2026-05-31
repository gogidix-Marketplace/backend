package com.gogidix.customersupport.notification.application.mapper;

import com.gogidix.customersupport.notification.application.dto.NotificationDto;
import com.gogidix.customersupport.notification.domain.model.Notification;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between domain models and DTOs for Notification Service
 */
@Component
public class NotificationMapper {

    /**
     * Convert Notification entity to DTO
     */
    public NotificationDto toDto(Notification entity) {
        if (entity == null) {
            return null;
        }

        return NotificationDto.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .notificationId(entity.getNotificationId())
                .type(entity.getType() != null ? entity.getType().name() : null)
                .recipientId(entity.getRecipientId())
                .recipientEmail(entity.getRecipientEmail())
                .recipientPhone(entity.getRecipientPhone())
                .channel(entity.getChannel() != null ? entity.getChannel().name() : null)
                .status(entity.getStatus() != null ? entity.getStatus().name() : null)
                .subject(entity.getSubject())
                .content(entity.getContent())
                .templateId(entity.getTemplateId())
                .templateData(entity.getTemplateData())
                .priority(entity.getPriority() != null ? entity.getPriority().name() : null)
                .scheduledAt(entity.getScheduledAt())
                .sentAt(entity.getSentAt())
                .deliveredAt(entity.getDeliveredAt())
                .readAt(entity.getReadAt())
                .failedAt(entity.getFailedAt())
                .failureReason(entity.getFailureReason())
                .retryCount(entity.getRetryCount())
                .maxRetries(entity.getMaxRetries())
                .relatedEntityType(entity.getRelatedEntityType())
                .relatedEntityId(entity.getRelatedEntityId())
                .metadata(entity.getMetadata())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
