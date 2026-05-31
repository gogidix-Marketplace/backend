package com.gogidix.sales.notification.application.dto.response;

import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;
import com.gogidix.sales.notification.domain.valueobject.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Notification Delivery Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDeliveryResponseDto {

    private String id;
    private String deliveryId;
    private String tenantId;
    private String notificationId;
    private String recipientId;
    private String recipientName;
    private String recipientType;
    private NotificationChannel channel;
    private String recipientAddress;
    private NotificationStatus status;
    private Instant sentAt;
    private Instant deliveredAt;
    private Instant readAt;
    private Instant failedAt;
    private String errorMessage;
    private Integer retryCount;
    private String externalMessageId;
    private Map<String, String> providerMetadata;
    private Integer deliveryAttempts;
    private Instant nextRetryAt;
    private Instant createdAt;
    private Instant updatedAt;
}
