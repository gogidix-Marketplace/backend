package com.gogidix.sales.notification.application.mapper;

import com.gogidix.sales.notification.domain.model.Notification;
import com.gogidix.sales.notification.application.dto.request.NotificationRequestDto;
import com.gogidix.sales.notification.application.dto.response.NotificationResponseDto;
import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;
import com.gogidix.sales.notification.domain.valueobject.NotificationPriority;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public Notification toEntity(NotificationRequestDto dto) {
        return Notification.builder()
            .tenantId(dto.getTenantId())
            .userId(dto.getRecipientId())
            .channel(dto.getChannel() != null ? NotificationChannel.valueOf(dto.getChannel()) : null)
            .subject(dto.getSubject())
            .content(dto.getContent())
            .priority(dto.getPriority() != null ? NotificationPriority.valueOf(dto.getPriority()) : null)
            .templateId(dto.getTemplateId())
            .build();
    }

    public NotificationResponseDto toResponseDto(Notification entity) {
        return NotificationResponseDto.builder()
            .id(entity.getId())
            .notificationId(entity.getNotificationId())
            .tenantId(entity.getTenantId())
            .userId(entity.getUserId())
            .channel(entity.getChannel())
            .subject(entity.getSubject())
            .content(entity.getContent())
            .status(entity.getStatus())
            .priority(entity.getPriority())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}