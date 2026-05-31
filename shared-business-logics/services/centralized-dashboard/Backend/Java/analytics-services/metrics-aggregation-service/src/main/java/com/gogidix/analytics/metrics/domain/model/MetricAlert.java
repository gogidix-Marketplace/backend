package com.gogidix.analytics.metrics.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "metric_alerts", indexes = {
    @Index(name = "idx_alert_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_alert_metric_name", columnList = "metric_name"),
    @Index(name = "idx_alert_status", columnList = "status"),
    @Index(name = "idx_alert_enabled", columnList = "enabled")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "alert_name", nullable = false, length = 255)
    private String alertName;

    @Column(name = "metric_name", nullable = false, length = 255)
    private String metricName;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_type", nullable = false, length = 50)
    private ConditionType conditionType;

    @Column(name = "threshold_value", precision = 19, scale = 6)
    private BigDecimal thresholdValue;

    @Column(name = "evaluation_window_seconds")
    private Integer evaluationWindowSeconds;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", length = 20)
    @Builder.Default
    private AlertSeverity severity = AlertSeverity.WARNING;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    @Builder.Default
    private AlertStatus status = AlertStatus.ACTIVE;

    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    @Column(name = "notification_channels", columnDefinition = "TEXT")
    private String notificationChannels;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    @Column(name = "last_triggered_at")
    private LocalDateTime lastTriggeredAt;

    @Column(name = "trigger_count", nullable = false)
    @Builder.Default
    private Integer triggerCount = 0;

    @Column(name = "cooldown_seconds")
    @Builder.Default
    private Integer cooldownSeconds = 300;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public boolean isCooldownPeriod() {
        return lastTriggeredAt != null &&
            LocalDateTime.now().isBefore(lastTriggeredAt.plusSeconds(cooldownSeconds));
    }

    public void markAsTriggered() {
        this.lastTriggeredAt = LocalDateTime.now();
        this.triggerCount++;
    }

    public enum ConditionType {
        GREATER_THAN,
        LESS_THAN,
        EQUALS,
        NOT_EQUALS,
        GREATER_THAN_OR_EQUAL,
        LESS_THAN_OR_EQUAL,
        RATE_INCREASES_BY,
        RATE_DECREASES_BY
    }

    public enum AlertSeverity {
        INFO,
        WARNING,
        ERROR,
        CRITICAL
    }

    public enum AlertStatus {
        ACTIVE,
        PAUSED,
        TRIGGERED,
        RESOLVED,
        DISABLED
    }
}
