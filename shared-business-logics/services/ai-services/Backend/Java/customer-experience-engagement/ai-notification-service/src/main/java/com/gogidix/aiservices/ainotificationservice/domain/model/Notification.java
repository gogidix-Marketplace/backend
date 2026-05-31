package com.gogidix.aiservices.ainotificationservice.domain.model;

import lombok.Builder;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Builder
public class Notification {
    private final String notificationId;
    private final String recipientId;
    private final NotificationType type;
    private final String subject;
    private final String content;
    private final Map<String, String> metadata;
    private final String templateId;
    private NotificationStatus status;
    private final NotificationPriority priority;
    private final Instant scheduledFor;
    private final Instant createdAt;
    private Instant sentAt;
    private final Instant expiresAt;
    private final Integer retryCount;
    private final String errorMessage;
    private final String language;

    public Notification(String notificationId, String recipientId, NotificationType type,
                       String subject, String content, Map<String, String> metadata,
                       String templateId, NotificationStatus status, NotificationPriority priority,
                       Instant scheduledFor, Instant createdAt, Instant sentAt,
                       Instant expiresAt, Integer retryCount, String errorMessage, String language) {
        if (notificationId == null || notificationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Notification ID cannot be null or empty");
        }
        if (recipientId == null || recipientId.trim().isEmpty()) {
            throw new IllegalArgumentException("Recipient ID cannot be null or empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("Notification type cannot be null");
        }
        this.notificationId = notificationId;
        this.recipientId = recipientId;
        this.type = type;
        this.subject = subject;
        this.content = content;
        this.metadata = metadata;
        this.templateId = templateId;
        this.status = status != null ? status : NotificationStatus.PENDING;
        this.priority = priority != null ? priority : NotificationPriority.NORMAL;
        this.scheduledFor = scheduledFor;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.sentAt = sentAt;
        this.expiresAt = expiresAt;
        this.retryCount = retryCount != null ? retryCount : 0;
        this.errorMessage = errorMessage;
        this.language = language;
    }

    public static Notification create(String recipientId, NotificationType type,
                                     String subject, String content) {
        return new Notification(
                UUID.randomUUID().toString(),
                recipientId,
                type,
                subject,
                content,
                null,
                null,
                NotificationStatus.PENDING,
                NotificationPriority.NORMAL,
                null,
                Instant.now(),
                null,
                null,
                0,
                null,
                null
        );
    }

    public String getNotificationId() { return notificationId; }
    public String getRecipientId() { return recipientId; }
    public NotificationType getType() { return type; }
    public String getSubject() { return subject; }
    public String getContent() { return content; }
    public Map<String, String> getMetadata() { return metadata; }
    public String getTemplateId() { return templateId; }
    public NotificationStatus getStatus() { return status; }
    public NotificationPriority getPriority() { return priority; }
    public Instant getScheduledFor() { return scheduledFor; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getSentAt() { return sentAt; }
    public Instant getExpiresAt() { return expiresAt; }
    public Integer getRetryCount() { return retryCount; }
    public String getErrorMessage() { return errorMessage; }
    public String getLanguage() { return language; }

    public void markAsSent() {
        this.status = NotificationStatus.SENT;
        this.sentAt = Instant.now();
    }

    public void markAsDelivered() {
        this.status = NotificationStatus.DELIVERED;
    }

    public void markAsFailed(String error) {
        this.status = NotificationStatus.FAILED;
    }

    public void markAsCancelled() {
        this.status = NotificationStatus.CANCELLED;
    }

    public boolean isPending() { return status == NotificationStatus.PENDING; }
    public boolean isSent() { return status == NotificationStatus.SENT; }
    public boolean isDelivered() { return status == NotificationStatus.DELIVERED; }
    public boolean isFailed() { return status == NotificationStatus.FAILED; }

    public boolean canRetry() {
        return isFailed() && retryCount < 3;
    }

    public boolean isScheduled() {
        return scheduledFor != null && scheduledFor.isAfter(Instant.now());
    }

    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(Instant.now());
    }

    public Notification withContent(String newContent) {
        return new Notification(
                notificationId, recipientId, type, subject, newContent, metadata,
                templateId, status, priority, scheduledFor, createdAt, sentAt,
                expiresAt, retryCount, errorMessage, language
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(notificationId, that.notificationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notificationId);
    }
}
