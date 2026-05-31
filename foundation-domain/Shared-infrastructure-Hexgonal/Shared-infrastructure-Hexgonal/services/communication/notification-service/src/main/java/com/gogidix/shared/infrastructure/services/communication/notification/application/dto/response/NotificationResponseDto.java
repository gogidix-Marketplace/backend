package com.gogidix.shared.infrastructure.services.communication.notification.application.dto.response;

import com.gogidix.shared.infrastructure.services.communication.notification.domain.model.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Notification response DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {

    private String id;
    private String tenantId;
    private String userId;
    private Notification.NotificationType type;
    private Notification.NotificationChannel channel;
    private String recipient;
    private String subject;
    private String body;
    private String templateId;
    private Map<String, Object> templateVariables;
    private Notification.NotificationStatus status;
    private String errorMessage;
    private Integer retryCount;
    private LocalDateTime scheduledAt;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
