package com.gogidix.hr.notification.domain.model;

import com.gogidix.hr.notification.domain.enums.NotificationChannel;
import com.gogidix.hr.notification.domain.enums.NotificationStatus;
import com.gogidix.hr.notification.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * NotificationDelivery Domain Entity
 * Tracks delivery status of notifications
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "notification_deliveries")
public class NotificationDelivery extends BaseEntity {

    @Indexed(unique = true)
    private String deliveryCode;

    @Indexed
    private String tenantId;

    @Indexed
    private String notificationId;

    private String notificationCode;

    @Indexed
    private String recipientId;

    private String recipientName;
    private String recipientEmail;
    private String recipientPhone;

    @Indexed
    private NotificationChannel channel;

    private String destination;

    @Indexed
    private NotificationStatus status;

    private LocalDateTime sentAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime readAt;
    private LocalDateTime failedAt;

    private Integer retryCount;
    private Integer maxRetries;

    private String errorCode;
    private String errorMessage;

    @Builder.Default
    private Map<String, Object> providerResponse = new HashMap<>();

    private String provider; // SENDGRID, TWILIO, FIREBASE, etc.
    private String providerMessageId;

    @Indexed
    private String correlationId;

    private Double cost;

    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    /**
     * Creates a new notification delivery
     */
    public static NotificationDelivery create(String tenantId, String notificationId,
                                              String notificationCode, String recipientId,
                                              String recipientName, String recipientEmail,
                                              String recipientPhone, NotificationChannel channel,
                                              String destination) {
        String deliveryCode = generateDeliveryCode(notificationCode, recipientId);

        NotificationDelivery delivery = new NotificationDelivery();
        delivery.setTenantId(tenantId);
        delivery.setNotificationId(notificationId);
        delivery.setNotificationCode(notificationCode);
        delivery.setRecipientId(recipientId);
        delivery.setRecipientName(recipientName);
        delivery.setRecipientEmail(recipientEmail);
        delivery.setRecipientPhone(recipientPhone);
        delivery.setChannel(channel);
        delivery.setDestination(destination);
        delivery.setDeliveryCode(deliveryCode);
        delivery.setStatus(NotificationStatus.SENT);
        delivery.setSentAt(LocalDateTime.now());
        delivery.setRetryCount(0);
        delivery.setMaxRetries(3);
        delivery.setProviderResponse(new HashMap<>());
        delivery.setMetadata(new HashMap<>());

        return delivery;
    }

    /**
     * Marks as delivered
     */
    public void markAsDelivered(String providerMessageId) {
        this.status = NotificationStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
        this.providerMessageId = providerMessageId;
    }

    /**
     * Marks as read
     */
    public void markAsRead() {
        if (this.status != NotificationStatus.DELIVERED) {
            throw new IllegalStateException("Can only mark delivered notifications as read");
        }
        this.status = NotificationStatus.READ;
        this.readAt = LocalDateTime.now();
    }

    /**
     * Marks as failed
     */
    public void markAsFailed(String errorCode, String errorMessage) {
        this.status = NotificationStatus.FAILED;
        this.failedAt = LocalDateTime.now();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
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
     * Sets provider response
     */
    public void setProviderResponse(String provider, String providerMessageId, Map<String, Object> response) {
        this.provider = provider;
        this.providerMessageId = providerMessageId;
        if (response != null) {
            this.providerResponse = response;
        }
    }

    /**
     * Sets cost
     */
    public void setCost(Double cost) {
        this.cost = cost;
    }

    /**
     * Checks if is delivered
     */
    public boolean isDelivered() {
        return this.status == NotificationStatus.DELIVERED || this.status == NotificationStatus.READ;
    }

    /**
     * Checks if is failed
     */
    public boolean isFailed() {
        return this.status == NotificationStatus.FAILED;
    }

    /**
     * Checks if is read
     */
    public boolean isRead() {
        return this.status == NotificationStatus.READ;
    }

    /**
     * Gets delivery time in milliseconds
     */
    public Long getDeliveryTimeMs() {
        if (this.sentAt != null && this.deliveredAt != null) {
            return java.time.Duration.between(this.sentAt, this.deliveredAt).toMillis();
        }
        return null;
    }

    /**
     * Generates delivery code
     */
    private static String generateDeliveryCode(String notificationCode, String recipientId) {
        return "DLV-" + notificationCode.substring(4) + "-" + recipientId.substring(0, 8);
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
