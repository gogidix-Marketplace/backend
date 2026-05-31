package com.gogidix.shared.infrastructure.services.communication.notification.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Notification domain entity.
 * <p>
 * Represents a notification (email, SMS, push) to be sent.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;

    @Indexed
    private TenantId tenantId;

    @Indexed
    private String userId;

    private NotificationType type;

    private NotificationChannel channel;

    private String recipient;

    private String subject;

    private String body;

    private String templateId;

    private Map<String, Object> templateVariables;

    private NotificationStatus status;

    private String errorMessage;

    private Integer retryCount;

    private LocalDateTime scheduledAt;

    private LocalDateTime sentAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * Notification types.
     */
    public enum NotificationType {
        WELCOME,
        PASSWORD_RESET,
        EMAIL_VERIFICATION,
        ACCOUNT_LOCKED,
        ACCOUNT_UNLOCKED,
        INVITATION,
        ALERT,
        MARKETING,
        TRANSACTIONAL,
        SYSTEM
    }

    /**
     * Notification channels.
     */
    public enum NotificationChannel {
        EMAIL,
        SMS,
        PUSH,
        IN_APP
    }

    /**
     * Notification status.
     */
    public enum NotificationStatus {
        PENDING,
        SENDING,
        SENT,
        FAILED,
        RETRYING,
        CANCELLED
    }

    /**
     * Marks the notification as sent.
     */
    public void markAsSent() {
        this.status = NotificationStatus.SENT;
        this.sentAt = LocalDateTime.now();
    }

    /**
     * Marks the notification as failed.
     *
     * @param errorMessage the error message
     */
    public void markAsFailed(String errorMessage) {
        this.status = NotificationStatus.FAILED;
        this.errorMessage = errorMessage;
    }

    /**
     * Increments the retry count.
     */
    public void incrementRetryCount() {
        this.retryCount = this.retryCount == null ? 1 : this.retryCount + 1;
        this.status = NotificationStatus.RETRYING;
    }
}
