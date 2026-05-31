package com.gogidix.platform.metering.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Quota alert entity.
 *
 * Generated when quota thresholds are exceeded.
 */
@Entity
@Table(name = "quota_alerts", indexes = {
    @Index(name = "idx_quota_alerts_tenant", columnList = "tenant_id"),
    @Index(name = "idx_quota_alerts_quota", columnList = "quota_id"),
    @Index(name = "idx_quota_alerts_created", columnList = "created_at"),
    @Index(name = "idx_quota_alerts_acknowledged", columnList = "is_acknowledged")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotaAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    /**
     * Reference to quota definition
     */
    @Column(name = "quota_id", nullable = false)
    private String quotaId;

    /**
     * Alert type
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", nullable = false, length = 50)
    private AlertType alertType;

    /**
     * Severity level
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false, length = 50)
    private Severity severity;

    /**
     * Current usage when alert was generated
     */
    @Column(name = "current_usage", nullable = false, precision = 20, scale = 4)
    private BigDecimal currentUsage;

    /**
     * Limit value
     */
    @Column(name = "limit_value", nullable = false, precision = 20, scale = 4)
    private BigDecimal limitValue;

    /**
     * Percentage of limit used
     */
    @Column(name = "percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal percentage;

    /**
     * Alert message
     */
    @Lob
    @Column(name = "message", nullable = false)
    private String message;

    /**
     * Recommended action
     */
    @Lob
    @Column(name = "recommended_action")
    private String recommendedAction;

    /**
     * Is acknowledged
     */
    @Column(name = "is_acknowledged", nullable = false)
    @Builder.Default
    private boolean isAcknowledged = false;

    /**
     * Who acknowledged
     */
    @Column(name = "acknowledged_by", length = 255)
    private String acknowledgedBy;

    /**
     * When acknowledged
     */
    @Column(name = "acknowledged_at")
    private LocalDateTime acknowledgedAt;

    /**
     * Notifications sent
     */
    @Column(name = "notification_sent", nullable = false)
    @Builder.Default
    private boolean notificationSent = false;

    /**
     * Which channels were used
     */
    @Column(name = "notification_channels")
    private String[] notificationChannels;

    /**
     * Period start
     */
    @Column(name = "period_start", nullable = false)
    private LocalDateTime periodStart;

    /**
     * Period end
     */
    @Column(name = "period_end", nullable = false)
    private LocalDateTime periodEnd;

    /**
     * Additional metadata
     */
    @Convert(converter = com.gogidix.platform.metering.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "metadata")
    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /**
     * Acknowledge alert
     */
    public void acknowledge(String acknowledgedBy) {
        this.isAcknowledged = true;
        this.acknowledgedBy = acknowledgedBy;
        this.acknowledgedAt = LocalDateTime.now();
    }

    /**
     * Mark notification as sent
     */
    public void markNotificationSent(String[] channels) {
        this.notificationSent = true;
        this.notificationChannels = channels;
    }

    public enum AlertType {
        SOFT_LIMIT,
        HARD_LIMIT,
        FORECAST
    }

    public enum Severity {
        INFO,
        WARNING,
        CRITICAL
    }
}
