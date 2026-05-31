package com.gogidix.sales.notification.domain.model;

import com.gogidix.sales.notification.domain.event.NotificationDeliveredEvent;
import com.gogidix.sales.notification.domain.event.NotificationFailedEvent;
import com.gogidix.sales.notification.domain.event.NotificationReadEvent;
import com.gogidix.sales.notification.domain.event.NotificationSentEvent;
import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;
import com.gogidix.sales.notification.domain.valueobject.NotificationPriority;
import com.gogidix.sales.notification.domain.valueobject.NotificationStatus;
import com.gogidix.sales.notification.shared.base.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Notification Domain Entity
 * Multi-tenant notification with multi-channel delivery support
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "notifications")
@CompoundIndex(def = "{'tenantId': 1, 'userId': 1, 'createdAt': -1}")
@CompoundIndex(def = "{'tenantId': 1, 'status': 1, 'scheduledAt': 1}")
public class Notification extends AuditableEntity {

    @Indexed
    private String notificationId;

    @Indexed
    private String tenantId;

    @Indexed
    private String userId;

    @Indexed
    private List<String> recipientIds;

    private List<RecipientInfo> recipients;

    @Indexed
    private NotificationChannel channel;

    private String subject;

    private String content;

    private String htmlContent;

    private String templateId;

    private Map<String, Object> templateVariables;

    @Indexed
    private NotificationStatus status;

    @Indexed
    private Boolean isRead;

    private Instant readAt;

    private String readBy;

    @Indexed
    private NotificationPriority priority;

    private Instant scheduledAt;

    private Instant sentAt;

    private Instant deliveredAt;

    private String externalMessageId;

    private String category;

    private String actionType;

    private String actionUrl;

    private Map<String, String> metadata;

    private Integer retryCount;

    private String errorMessage;

    private String groupId;

    private Boolean isBatched;

    private Instant expiresAt;

    @Builder.Default
    private List<Object> domainEvents = new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipientInfo {
        private String recipientId;
        private String recipientName;
        private String recipientType;
        private String emailAddress;
        private String phoneNumber;
        private String deviceToken;
        private String webhookUrl;
    }

    /**
     * Creates a new notification
     */
    public static Notification create(String tenantId, String userId,
                                      List<RecipientInfo> recipients,
                                      NotificationChannel channel,
                                      String subject, String content) {
        String notificationId = UUID.randomUUID().toString();

        Notification notification = Notification.builder()
                .notificationId(notificationId)
                .tenantId(tenantId)
                .userId(userId)
                .recipients(recipients != null ? recipients : new ArrayList<>())
                .channel(channel)
                .subject(subject)
                .content(content)
                .status(NotificationStatus.DRAFT)
                .isRead(false)
                .priority(NotificationPriority.NORMAL)
                .retryCount(0)
                .isBatched(false)
                .templateVariables(new HashMap<>())
                .metadata(new HashMap<>())
                .domainEvents(new ArrayList<>())
                .build();

        // Extract recipient IDs
        if (recipients != null) {
            notification.setRecipientIds(recipients.stream()
                    .map(RecipientInfo::getRecipientId)
                    .toList());
        }

        notification.addDomainEvent(NotificationSentEvent.builder()
                .notificationId(notification.getNotificationId())
                .tenantId(tenantId)
                .userId(userId)
                .recipientIds(notification.getRecipientIds())
                .channel(channel.name())
                .subject(subject)
                .priority(notification.getPriority().getLevel())
                .timestamp(Instant.now())
                .eventType("NOTIFICATION_CREATED")
                .build());

        return notification;
    }

    /**
     * Creates a notification from template
     */
    public static Notification createFromTemplate(String tenantId, String userId,
                                                  String templateId,
                                                  Map<String, Object> templateVariables,
                                                  List<RecipientInfo> recipients,
                                                  NotificationChannel channel) {
        Notification notification = create(tenantId, userId, recipients, channel, null, null);
        notification.setTemplateId(templateId);
        notification.setTemplateVariables(templateVariables);
        return notification;
    }

    /**
     * Sends the notification
     */
    public void send() {
        if (this.status != NotificationStatus.DRAFT && this.status != NotificationStatus.SCHEDULED) {
            throw new IllegalStateException("Can only send draft or scheduled notifications");
        }

        if (this.recipients == null || this.recipients.isEmpty()) {
            throw new IllegalStateException("Notification must have at least one recipient");
        }

        this.status = NotificationStatus.SENDING;
        this.sentAt = Instant.now();

        addDomainEvent(NotificationSentEvent.builder()
                .notificationId(this.notificationId)
                .tenantId(this.tenantId)
                .userId(this.userId)
                .channel(this.channel.name())
                .subject(this.subject)
                .priority(this.priority != null ? this.priority.getLevel() : NotificationPriority.NORMAL.getLevel())
                .timestamp(Instant.now())
                .eventType("NOTIFICATION_SENDING")
                .build());
    }

    /**
     * Marks the notification as sent
     */
    public void markAsSent(String externalMessageId) {
        this.status = NotificationStatus.SENT;
        this.sentAt = Instant.now();
        this.externalMessageId = externalMessageId;

        addDomainEvent(NotificationSentEvent.builder()
                .notificationId(this.notificationId)
                .tenantId(this.tenantId)
                .userId(this.userId)
                .channel(this.channel.name())
                .externalMessageId(externalMessageId)
                .timestamp(Instant.now())
                .eventType("NOTIFICATION_SENT")
                .build());
    }

    /**
     * Marks the notification as delivered
     */
    public void markAsDelivered(String externalMessageId) {
        this.status = NotificationStatus.DELIVERED;
        this.deliveredAt = Instant.now();
        if (externalMessageId != null) {
            this.externalMessageId = externalMessageId;
        }

        addDomainEvent(NotificationDeliveredEvent.builder()
                .notificationId(this.notificationId)
                .tenantId(this.tenantId)
                .userId(this.userId)
                .channel(this.channel.name())
                .deliveredAt(Instant.now())
                .externalMessageId(externalMessageId)
                .eventType("NOTIFICATION_DELIVERED")
                .build());
    }

    /**
     * Marks the notification as read
     */
    public void markAsRead(String readBy) {
        this.isRead = true;
        this.readAt = Instant.now();
        this.readBy = readBy;
        this.status = NotificationStatus.READ;

        addDomainEvent(NotificationReadEvent.builder()
                .notificationId(this.notificationId)
                .tenantId(this.tenantId)
                .userId(this.userId)
                .readAt(Instant.now())
                .eventType("NOTIFICATION_READ")
                .build());
    }

    /**
     * Marks the notification as failed
     */
    public void markAsFailed(String errorMessage) {
        this.status = NotificationStatus.FAILED;
        this.errorMessage = errorMessage;
        this.retryCount++;

        addDomainEvent(NotificationFailedEvent.builder()
                .notificationId(this.notificationId)
                .tenantId(this.tenantId)
                .userId(this.userId)
                .channel(this.channel.name())
                .errorMessage(errorMessage)
                .retryCount(this.retryCount)
                .timestamp(Instant.now())
                .eventType("NOTIFICATION_FAILED")
                .build());
    }

    /**
     * Schedules the notification for later sending
     */
    public void schedule(Instant scheduledAt) {
        if (scheduledAt.isBefore(Instant.now())) {
            throw new IllegalStateException("Scheduled time must be in the future");
        }
        this.status = NotificationStatus.SCHEDULED;
        this.scheduledAt = scheduledAt;
    }

    /**
     * Cancels the notification
     */
    public void cancel() {
        if (this.status == NotificationStatus.SENT ||
            this.status == NotificationStatus.DELIVERED ||
            this.status == NotificationStatus.READ) {
            throw new IllegalStateException("Cannot cancel sent, delivered or read notifications");
        }
        this.status = NotificationStatus.CANCELLED;
    }

    /**
     * Archives the notification
     */
    public void archive() {
        this.status = NotificationStatus.ARCHIVED;
    }

    /**
     * Sets the priority of the notification
     */
    public void setPriorityValue(NotificationPriority priority) {
        this.priority = priority;
    }

    /**
     * Adds metadata to the notification
     */
    public void addMetadata(String key, String value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }

    /**
     * Checks if the notification has expired
     */
    public boolean isExpired() {
        return this.expiresAt != null && Instant.now().isAfter(this.expiresAt);
    }

    /**
     * Checks if the notification can be retried
     */
    public boolean canRetry() {
        return this.status == NotificationStatus.FAILED &&
               this.retryCount < 3 &&
               this.priority != null &&
               this.priority.getLevel() >= NotificationPriority.HIGH.getLevel();
    }

    public void addDomainEvent(Object event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }
}
