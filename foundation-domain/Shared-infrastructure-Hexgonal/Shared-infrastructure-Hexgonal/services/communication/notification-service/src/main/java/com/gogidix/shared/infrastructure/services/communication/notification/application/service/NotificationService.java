package com.gogidix.shared.infrastructure.services.communication.notification.application.service;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.communication.notification.application.dto.request.SendNotificationRequestDto;
import com.gogidix.shared.infrastructure.services.communication.notification.application.dto.response.NotificationResponseDto;
import com.gogidix.shared.infrastructure.services.communication.notification.domain.model.Notification;
import com.gogidix.shared.infrastructure.services.communication.notification.domain.port.in.NotificationPort;
import com.gogidix.shared.infrastructure.services.communication.notification.infrastructure.sender.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Notification Service implementation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService implements NotificationPort {

    private final NotificationSender notificationSender;
    private final TenantContextHolder tenantContextHolder;

    @Override
    public NotificationResponseDto sendNotification(SendNotificationRequestDto request) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        log.info("Sending notification: type={}, channel={}, recipient={}",
                request.getType(), request.getChannel(), request.getRecipient());

        Notification notification = Notification.builder()
                .tenantId(new TenantId(tenantId))
                .userId(request.getUserId())
                .type(request.getType())
                .channel(request.getChannel())
                .recipient(request.getRecipient())
                .subject(request.getSubject())
                .body(request.getBody())
                .templateId(request.getTemplateId())
                .templateVariables(request.getTemplateVariables())
                .status(Notification.NotificationStatus.PENDING)
                .retryCount(0)
                .createdAt(LocalDateTime.now())
                .build();

        notificationSender.send(notification);

        return toResponseDto(notification);
    }

    @Override
    public NotificationResponseDto getNotification(String notificationId) {
        // TODO: Implement retrieval from repository
        return null;
    }

    @Override
    public List<NotificationResponseDto> listNotifications(String userId) {
        // TODO: Implement listing from repository
        return List.of();
    }

    @Override
    public NotificationResponseDto retryNotification(String notificationId) {
        // TODO: Implement retry logic
        return null;
    }

    @Override
    public void cancelNotification(String notificationId) {
        // TODO: Implement cancel logic
    }

    private NotificationResponseDto toResponseDto(Notification notification) {
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .tenantId(notification.getTenantId() != null ? notification.getTenantId().getValue() : null)
                .userId(notification.getUserId())
                .type(notification.getType())
                .channel(notification.getChannel())
                .recipient(notification.getRecipient())
                .subject(notification.getSubject())
                .body(notification.getBody())
                .templateId(notification.getTemplateId())
                .templateVariables(notification.getTemplateVariables())
                .status(notification.getStatus())
                .errorMessage(notification.getErrorMessage())
                .retryCount(notification.getRetryCount())
                .scheduledAt(notification.getScheduledAt())
                .sentAt(notification.getSentAt())
                .createdAt(notification.getCreatedAt())
                .updatedAt(notification.getUpdatedAt())
                .build();
    }
}
