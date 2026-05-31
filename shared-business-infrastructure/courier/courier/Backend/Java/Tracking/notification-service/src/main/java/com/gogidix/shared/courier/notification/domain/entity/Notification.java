package com.gogidix.shared.courier.notification.domain.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Entity representing a notification
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Document(collection = "notifications")
public class Notification extends BaseEntity {

    @Id
    private String notificationId;

    @Indexed
    private String tenantId;

    @Indexed
    private String recipientId;

    @Indexed
    private String recipientType;

    @Indexed
    private String notificationType;

    private String title;
    private String message;
    private String actionUrl;
    private String actionLabel;
    private Map<String, Object> metadata;

    private String priority;
    private String channel;

    @Indexed
    private NotificationStatus status;

    private LocalDateTime sentAt;
    private LocalDateTime readAt;
    private LocalDateTime createdAt;

    public enum NotificationStatus {
        SENT,
        DELIVERED,
        READ,
        FAILED
    }
}
