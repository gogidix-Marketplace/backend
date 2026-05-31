package com.gogidix.shared.courier.notification.application.dto;

import com.gogidix.shared.courier.notification.domain.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for notification
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {

    private String notificationId;
    private String tenantId;
    private String recipientId;
    private String recipientType;
    private String notificationType;
    private String title;
    private String message;
    private String actionUrl;
    private String actionLabel;
    private Map<String, Object> metadata;
    private String priority;
    private String channel;
    private Notification.NotificationStatus status;
    private LocalDateTime sentAt;
    private LocalDateTime readAt;
    private LocalDateTime createdAt;
}
