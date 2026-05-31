package com.gogidix.sales.notification.application.dto.response;

import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;
import com.gogidix.sales.notification.domain.valueobject.NotificationPriority;
import com.gogidix.sales.notification.domain.valueobject.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Notification Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {

    private String id;
    private String notificationId;
    private String tenantId;
    private String userId;
    private List<String> recipientIds;
    private List<RecipientInfoDto> recipients;
    private NotificationChannel channel;
    private String subject;
    private String content;
    private String htmlContent;
    private String templateId;
    private NotificationStatus status;
    private Boolean isRead;
    private Instant readAt;
    private String readBy;
    private NotificationPriority priority;
    private Instant scheduledAt;
    private Instant sentAt;
    private Instant deliveredAt;
    private String externalMessageId;
    private String category;
    private String actionType;
    private String actionUrl;
    private Map<String, String> metadata;
    private String groupId;
    private Boolean isBatched;
    private Instant expiresAt;
    private Integer retryCount;
    private String errorMessage;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipientInfoDto {
        private String recipientId;
        private String recipientName;
        private String recipientType;
        private String emailAddress;
        private String phoneNumber;
        private String deviceToken;
        private String webhookUrl;
    }
}
