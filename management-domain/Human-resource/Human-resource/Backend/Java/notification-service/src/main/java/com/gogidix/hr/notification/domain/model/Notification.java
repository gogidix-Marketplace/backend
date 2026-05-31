package com.gogidix.hr.notification.domain.model;

import com.gogidix.hr.notification.domain.enums.NotificationChannel;
import com.gogidix.hr.notification.domain.enums.NotificationPriority;
import com.gogidix.hr.notification.domain.enums.NotificationStatus;
import com.gogidix.hr.notification.domain.enums.NotificationType;
import com.gogidix.hr.notification.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Notification Domain Entity
 * Represents a notification to be sent to recipients
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "notifications")
public class Notification extends BaseEntity {

    @Indexed(unique = true)
    private String notificationCode;

    @Indexed
    private String tenantId;

    @Indexed
    private NotificationType type;

    private String title;
    private String subject;
    private String body;
    private String htmlBody;

    @Indexed
    private NotificationStatus status;

    @Indexed
    private NotificationPriority priority;

    @Indexed
    private NotificationChannel channel;

    @Indexed
    private String senderId;

    private String senderName;
    private String senderEmail;

    @Indexed
    private List<String> recipientIds;

    @Builder.Default
    private List<RecipientDetail> recipientDetails = new ArrayList<>();

    @Indexed
    private List<String> recipientGroupIds;

    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Indexed
    private LocalDateTime scheduledAt;

    private LocalDateTime sentAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime readAt;

    @Indexed
    private LocalDate expiryDate;

    private String templateId;
    @Builder.Default
    private Map<String, Object> templateData = new HashMap<>();

    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    @Builder.Default
    private List<String> attachmentIds = new ArrayList<>();

    private String correlationId;
    private String referenceId;
    private String referenceType;

    @Indexed
    private Boolean isRead;

    @Indexed
    private Boolean isArchived;

    private Integer retryCount;
    private Integer maxRetries;

    @Indexed
    private String errorCode;

    private String errorMessage;

    @Builder.Default
    private List<String> deliveryIds = new ArrayList<>();

    /**
     * Recipient detail
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipientDetail {
        private String recipientId;
        private String recipientName;
        private String recipientEmail;
        private String recipientPhone;
        private String deliveryStatus;
        private LocalDateTime deliveryTime;
        private Boolean read;
        private LocalDateTime readTime;
    }

    /**
     * Creates a new notification
     */
    public static Notification create(String tenantId, NotificationType type,
                                       NotificationChannel channel, String title,
                                       String subject, String body, String senderId) {
        String notificationCode = generateNotificationCode(tenantId, type);

        Notification notification = new Notification();
        notification.setTenantId(tenantId);
        notification.setType(type);
        notification.setChannel(channel);
        notification.setTitle(title);
        notification.setSubject(subject);
        notification.setBody(body);
        notification.setNotificationCode(notificationCode);
        notification.setSenderId(senderId);
        notification.setStatus(NotificationStatus.DRAFT);
        notification.setPriority(NotificationPriority.NORMAL);
        notification.setIsRead(false);
        notification.setIsArchived(false);
        notification.setRetryCount(0);
        notification.setMaxRetries(3);
        notification.setRecipientIds(new ArrayList<>());
        notification.setRecipientDetails(new ArrayList<>());
        notification.setRecipientGroupIds(new ArrayList<>());
        notification.setTags(new ArrayList<>());
        notification.setTemplateData(new HashMap<>());
        notification.setMetadata(new HashMap<>());
        notification.setAttachmentIds(new ArrayList<>());
        notification.setDeliveryIds(new ArrayList<>());

        return notification;
    }

    /**
     * Schedules notification
     */
    public void schedule(LocalDateTime scheduledAt) {
        if (this.status != NotificationStatus.DRAFT) {
            throw new IllegalStateException("Can only schedule draft notifications");
        }
        this.status = NotificationStatus.SCHEDULED;
        this.scheduledAt = scheduledAt;
    }

    /**
     * Sends notification
     */
    public void send() {
        if (this.status != NotificationStatus.DRAFT && this.status != NotificationStatus.SCHEDULED) {
            throw new IllegalStateException("Can only send draft or scheduled notifications");
        }
        this.status = NotificationStatus.SENT;
        this.sentAt = LocalDateTime.now();
    }

    /**
     * Marks as delivered
     */
    public void markAsDelivered() {
        if (this.status != NotificationStatus.SENT) {
            throw new IllegalStateException("Can only mark sent notifications as delivered");
        }
        this.status = NotificationStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
    }

    /**
     * Marks as read
     */
    public void markAsRead() {
        if (this.status != NotificationStatus.DELIVERED) {
            throw new IllegalStateException("Can only mark delivered notifications as read");
        }
        this.isRead = true;
        this.status = NotificationStatus.READ;
        this.readAt = LocalDateTime.now();
    }

    /**
     * Marks as failed
     */
    public void markAsFailed(String errorCode, String errorMessage) {
        this.status = NotificationStatus.FAILED;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * Archives notification
     */
    public void archive() {
        this.isArchived = true;
        this.status = NotificationStatus.ARCHIVED;
    }

    /**
     * Adds recipient
     */
    public void addRecipient(String recipientId) {
        if (this.recipientIds == null) {
            this.recipientIds = new ArrayList<>();
        }
        if (!this.recipientIds.contains(recipientId)) {
            this.recipientIds.add(recipientId);
        }
    }

    /**
     * Adds recipient group
     */
    public void addRecipientGroup(String groupId) {
        if (this.recipientGroupIds == null) {
            this.recipientGroupIds = new ArrayList<>();
        }
        if (!this.recipientGroupIds.contains(groupId)) {
            this.recipientGroupIds.add(groupId);
        }
    }

    /**
     * Adds recipient detail
     */
    public void addRecipientDetail(RecipientDetail detail) {
        if (this.recipientDetails == null) {
            this.recipientDetails = new ArrayList<>();
        }
        this.recipientDetails.add(detail);
    }

    /**
     * Adds tag
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Adds attachment
     */
    public void addAttachment(String attachmentId) {
        if (this.attachmentIds == null) {
            this.attachmentIds = new ArrayList<>();
        }
        if (!this.attachmentIds.contains(attachmentId)) {
            this.attachmentIds.add(attachmentId);
        }
    }

    /**
     * Sets template
     */
    public void setTemplate(String templateId, Map<String, Object> templateData) {
        this.templateId = templateId;
        if (templateData != null) {
            this.templateData = templateData;
        }
    }

    /**
     * Increments retry count
     */
    public void incrementRetryCount() {
        this.retryCount = (this.retryCount != null ? this.retryCount : 0) + 1;
    }

    /**
     * Checks if max retries reached
     */
    public boolean isMaxRetriesReached() {
        return this.retryCount != null && this.maxRetries != null &&
               this.retryCount >= this.maxRetries;
    }

    /**
     * Checks if is expired
     */
    public boolean isExpired() {
        return this.expiryDate != null && LocalDate.now().isAfter(this.expiryDate);
    }

    /**
     * Adds delivery
     */
    public void addDelivery(String deliveryId) {
        if (this.deliveryIds == null) {
            this.deliveryIds = new ArrayList<>();
        }
        if (!this.deliveryIds.contains(deliveryId)) {
            this.deliveryIds.add(deliveryId);
        }
    }

    /**
     * Generates notification code
     */
    private static String generateNotificationCode(String tenantId, NotificationType type) {
        String prefix = type.name().substring(0, 3).toUpperCase();
        String uniqueId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "NOT-" + prefix + "-" + uniqueId;
    }

    @Builder.Default
    private List<Object> domainEvents = new ArrayList<>();

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
