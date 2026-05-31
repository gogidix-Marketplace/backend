package com.gogidix.sales.notification.domain.model;

import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;
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
import java.util.Map;

/**
 * Notification Delivery Domain Entity
 * Tracks delivery status for each notification recipient
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "notification_deliveries")
@CompoundIndex(def = "{'tenantId': 1, 'notificationId': 1, 'recipientId': 1}")
@CompoundIndex(def = "{'tenantId': 1, 'recipientId': 1, 'status': 1, 'createdAt': -1}")
public class NotificationDelivery extends AuditableEntity {

    @Indexed
    private String deliveryId;

    @Indexed
    private String tenantId;

    @Indexed
    private String notificationId;

    @Indexed
    private String recipientId;

    private String recipientName;

    private String recipientType;

    @Indexed
    private NotificationChannel channel;

    private String recipientAddress; // email, phone, device token, etc.

    @Indexed
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

    /**
     * Creates a new notification delivery record
     */
    public static NotificationDelivery create(String tenantId, String notificationId,
                                              String recipientId, String recipientName,
                                              NotificationChannel channel, String recipientAddress) {
        return NotificationDelivery.builder()
                .deliveryId(java.util.UUID.randomUUID().toString())
                .tenantId(tenantId)
                .notificationId(notificationId)
                .recipientId(recipientId)
                .recipientName(recipientName)
                .channel(channel)
                .recipientAddress(recipientAddress)
                .status(NotificationStatus.PENDING)
                .retryCount(0)
                .deliveryAttempts(0)
                .build();
    }

    /**
     * Marks the delivery as sent
     */
    public void markAsSent(String externalMessageId) {
        this.status = NotificationStatus.SENT;
        this.sentAt = Instant.now();
        this.externalMessageId = externalMessageId;
        this.deliveryAttempts++;
    }

    /**
     * Marks the delivery as delivered
     */
    public void markAsDelivered() {
        this.status = NotificationStatus.DELIVERED;
        this.deliveredAt = Instant.now();
    }

    /**
     * Marks the delivery as read
     */
    public void markAsRead() {
        this.status = NotificationStatus.READ;
        this.readAt = Instant.now();
    }

    /**
     * Marks the delivery as failed
     */
    public void markAsFailed(String errorMessage, Instant nextRetryAt) {
        this.status = NotificationStatus.FAILED;
        this.failedAt = Instant.now();
        this.errorMessage = errorMessage;
        this.retryCount++;
        this.deliveryAttempts++;
        this.nextRetryAt = nextRetryAt;
    }

    /**
     * Marks the delivery as bounced
     */
    public void markAsBounced(String errorMessage) {
        this.status = NotificationStatus.BOUNCED;
        this.failedAt = Instant.now();
        this.errorMessage = errorMessage;
    }

    /**
     * Checks if the delivery can be retried
     */
    public boolean canRetry() {
        return this.status == NotificationStatus.FAILED &&
               this.retryCount < 3 &&
               this.nextRetryAt != null &&
               Instant.now().isAfter(this.nextRetryAt);
    }

    /**
     * Checks if the delivery is in a terminal state
     */
    public boolean isTerminal() {
        return this.status == NotificationStatus.DELIVERED ||
               this.status == NotificationStatus.READ ||
               this.status == NotificationStatus.BOUNCED ||
               this.status == NotificationStatus.ARCHIVED;
    }

    /**
     * Adds provider metadata
     */
    public void addProviderMetadata(String key, String value) {
        if (this.providerMetadata == null) {
            this.providerMetadata = new java.util.HashMap<>();
        }
        this.providerMetadata.put(key, value);
    }
}
