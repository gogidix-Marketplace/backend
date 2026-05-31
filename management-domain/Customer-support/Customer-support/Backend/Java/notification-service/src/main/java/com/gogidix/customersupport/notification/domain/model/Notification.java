package com.gogidix.customersupport.notification.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "notifications")
public class Notification extends BaseEntity {

    @Field("notification_id")
    @Indexed(unique = true)
    private String notificationId;

    @Field("type")
    @Indexed
    private NotificationType type;

    @Field("recipient_id")
    @Indexed
    private String recipientId;

    @Field("recipient_email")
    private String recipientEmail;

    @Field("recipient_phone")
    private String recipientPhone;

    @Field("channel")
    @Indexed
    private NotificationChannel channel;

    @Field("status")
    @Indexed
    private NotificationStatus status;

    @Field("subject")
    private String subject;

    @Field("content")
    private String content;

    @Field("template_id")
    private String templateId;

    @Field("template_data")
    private Map<String, Object> templateData;

    @Field("priority")
    private NotificationPriority priority;

    @Field("scheduled_at")
    private Instant scheduledAt;

    @Field("sent_at")
    private Instant sentAt;

    @Field("delivered_at")
    private Instant deliveredAt;

    @Field("read_at")
    private Instant readAt;

    @Field("failed_at")
    private Instant failedAt;

    @Field("failure_reason")
    private String failureReason;

    @Field("retry_count")
    private Integer retryCount;

    @Field("max_retries")
    private Integer maxRetries;

    @Field("related_entity_type")
    private String relatedEntityType;

    @Field("related_entity_id")
    private String relatedEntityId;

    @Field("metadata")
    private Map<String, Object> metadata;

    public static Notification create(String tenantId, NotificationType type, String recipientId,
                                      NotificationChannel channel, String subject, String content) {
        Notification notification = new Notification();
        notification.setId(java.util.UUID.randomUUID().toString());
        notification.setTenantId(tenantId);
        notification.setNotificationId(java.util.UUID.randomUUID().toString());
        notification.setType(type);
        notification.setRecipientId(recipientId);
        notification.setChannel(channel);
        notification.setSubject(subject);
        notification.setContent(content);
        notification.setStatus(NotificationStatus.PENDING);
        notification.setPriority(NotificationPriority.NORMAL);
        notification.setRetryCount(0);
        notification.setMaxRetries(3);
        notification.setCreatedAt(Instant.now());
        notification.setUpdatedAt(Instant.now());
        return notification;
    }

    public void markAsSent() {
        this.status = NotificationStatus.SENT;
        this.sentAt = Instant.now();
        this.updateTimestamp();
    }

    public void markAsDelivered() {
        this.status = NotificationStatus.DELIVERED;
        this.deliveredAt = Instant.now();
        this.updateTimestamp();
    }

    public void markAsRead() {
        this.status = NotificationStatus.READ;
        this.readAt = Instant.now();
        this.updateTimestamp();
    }

    public void markAsFailed(String reason) {
        this.status = NotificationStatus.FAILED;
        this.failedAt = Instant.now();
        this.failureReason = reason;
        this.updateTimestamp();
    }

    public void incrementRetry() {
        this.retryCount = (this.retryCount != null ? this.retryCount : 0) + 1;
        this.updateTimestamp();
    }

    public boolean canRetry() {
        return this.retryCount < this.maxRetries;
    }

    public enum NotificationType {
        TICKET_CREATED, TICKET_UPDATED, TICKET_ASSIGNED, TICKET_ESCALATED, TICKET_RESOLVED, TICKET_CLOSED,
        SLA_BREACH_WARNING, SLA_BREACHED,
        CHAT_MESSAGE_RECEIVED, CHAT_ASSIGNED,
        FEEDBACK_REQUEST, SURVEY_INVITATION,
        SYSTEM_ALERT, MAINTENANCE_NOTICE,
        EMAIL_CONFIRMATION, SMS_VERIFICATION
    }

    public enum NotificationChannel {
        EMAIL, SMS, PUSH, IN_APP, WEBHOOK, WHATSAPP
    }

    public enum NotificationStatus {
        PENDING, SENDING, SENT, DELIVERED, READ, FAILED, CANCELLED
    }

    public enum NotificationPriority {
        LOW, NORMAL, HIGH, URGENT
    }
}
