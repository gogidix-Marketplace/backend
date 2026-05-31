package com.gogidix.transaction.status.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Subscriber entity for tracking WebSocket connections.
 * Converted from MongoDB to PostgreSQL JPA.
 */
@Entity
@Table(name = "subscribers", indexes = {
    @Index(name = "idx_subscriber_user_id", columnList = "user_id"),
    @Index(name = "idx_subscriber_session_id", columnList = "session_id"),
    @Index(name = "idx_subscriber_connection_id", columnList = "connection_id"),
    @Index(name = "idx_subscriber_transaction_filter", columnList = "transaction_filter"),
    @Index(name = "idx_subscriber_status", columnList = "status"),
    @Index(name = "idx_subscriber_last_activity", columnList = "last_activity"),
    @Index(name = "idx_subscriber_connected_at", columnList = "connected_at")
})
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Column(name = "user_id", length = 255)
    private String userId;

    @Column(name = "session_id", length = 255)
    private String sessionId;

    @Column(name = "connection_id", length = 255, nullable = false)
    private String connectionId;

    @Column(name = "transaction_filter", length = 500)
    private String transactionFilter;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    @Builder.Default
    private SubscriptionStatus status = SubscriptionStatus.ACTIVE;

    @Column(name = "filter_expression", columnDefinition = "TEXT")
    private String filterExpression;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;

    @Column(name = "last_activity")
    private LocalDateTime lastActivity;

    @CreatedDate
    @Column(name = "connected_at", nullable = false, updatable = false)
    private LocalDateTime connectedAt;

    @Column(name = "disconnected_at")
    private LocalDateTime disconnectedAt;

    @Version
    @Column(name = "version")
    private Long version;

    /**
     * Lifecycle callback before persist
     */
    @PrePersist
    protected void onCreate() {
        connectedAt = LocalDateTime.now();
        if (status == null) {
            status = SubscriptionStatus.ACTIVE;
        }
    }

    /**
     * Lifecycle callback before update
     */
    @PreUpdate
    protected void onUpdate() {
        if (status == SubscriptionStatus.DISCONNECTED || status == SubscriptionStatus.EXPIRED) {
            if (disconnectedAt == null) {
                disconnectedAt = LocalDateTime.now();
            }
        }
    }

    public enum SubscriptionStatus {
        ACTIVE,
        PAUSED,
        DISCONNECTED,
        EXPIRED
    }

    /**
     * Check if subscription is active
     */
    public boolean isActive() {
        return status == SubscriptionStatus.ACTIVE;
    }

    /**
     * Update last activity timestamp
     */
    public void updateLastActivity() {
        this.lastActivity = LocalDateTime.now();
    }

    /**
     * Mark as disconnected
     */
    public void markAsDisconnected() {
        this.status = SubscriptionStatus.DISCONNECTED;
        this.disconnectedAt = LocalDateTime.now();
    }

    /**
     * Mark as expired
     */
    public void markAsExpired() {
        this.status = SubscriptionStatus.EXPIRED;
        this.disconnectedAt = LocalDateTime.now();
    }
}
