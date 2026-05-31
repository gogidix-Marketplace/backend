package com.gogidix.centralconfiguration.notificationservice.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Notification aggregate root for config change alerts.
 */
@Entity
@Table(name = "notifications", indexes = {
    @Index(name = "idx_notif_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_notif_status", columnList = "status"),
    @Index(name = "idx_notif_type", columnList = "notification_type")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    @Column(name = "notification_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(name = "channel", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private NotificationChannel channel;

    @Column(name = "recipient", nullable = false, length = 500)
    private String recipient; // Email, webhook URL, or Slack channel

    @Column(name = "subject", length = 500)
    private String subject;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata; // JSON metadata

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    @Builder.Default
    private NotificationStatus status = NotificationStatus.PENDING;

    @Column(name = "retry_count", nullable = false)
    @Builder.Default
    private Integer retryCount = 0;

    @Column(name = "max_retries", nullable = false)
    @Builder.Default
    private Integer maxRetries = 3;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void markAsSent() {
        this.status = NotificationStatus.SENT;
        this.sentAt = LocalDateTime.now();
    }

    public void markAsFailed(String error) {
        this.status = NotificationStatus.FAILED;
        this.errorMessage = error;
    }

    public void incrementRetry() {
        this.retryCount++;
    }

    public boolean canRetry() {
        return retryCount < maxRetries && status == NotificationStatus.FAILED;
    }
}
