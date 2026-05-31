package com.gogidix.transaction.monitoring.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Alert entity for tracking monitoring alerts.
 * Converted from MongoDB to PostgreSQL JPA.
 */
@Entity
@Table(name = "alerts", indexes = {
    @Index(name = "idx_alert_transaction_id", columnList = "transaction_id"),
    @Index(name = "idx_alert_type", columnList = "alert_type"),
    @Index(name = "idx_alert_severity", columnList = "severity"),
    @Index(name = "idx_alert_status", columnList = "status"),
    @Index(name = "idx_alert_created_at", columnList = "created_at")
})
@Data
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Column(name = "transaction_id")
    private UUID transactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", length = 50)
    private AlertType alertType;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", length = 50)
    private AlertSeverity severity;

    @Column(name = "title", length = 500, nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "metric_name", length = 255)
    private String metricName;

    @Column(name = "threshold_value", length = 255)
    private String thresholdValue;

    @Column(name = "actual_value", length = 255)
    private String actualValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    @Builder.Default
    private AlertStatus status = AlertStatus.OPEN;

    @Column(name = "acknowledged_by", length = 255)
    private String acknowledgedBy;

    @Column(name = "acknowledged_at")
    private LocalDateTime acknowledgedAt;

    @Column(name = "resolved_by", length = 255)
    private String resolvedBy;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "resolution_notes", columnDefinition = "TEXT")
    private String resolutionNotes;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = AlertStatus.OPEN;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum AlertType {
        PERFORMANCE,
        ERROR_RATE,
        TIMEOUT,
        BUSINESS_RULE,
        SECURITY,
        COMPLIANCE,
        SYSTEM_HEALTH,
        CUSTOM
    }

    public enum AlertSeverity {
        INFO,
        WARNING,
        ERROR,
        CRITICAL
    }

    public enum AlertStatus {
        OPEN,
        ACKNOWLEDGED,
        RESOLVED,
        SUPPRESSED
    }

    /**
     * Acknowledge the alert
     */
    public void acknowledge(String acknowledgedBy) {
        this.status = AlertStatus.ACKNOWLEDGED;
        this.acknowledgedBy = acknowledgedBy;
        this.acknowledgedAt = LocalDateTime.now();
    }

    /**
     * Resolve the alert
     */
    public void resolve(String resolvedBy, String resolutionNotes) {
        this.status = AlertStatus.RESOLVED;
        this.resolvedBy = resolvedBy;
        this.resolvedAt = LocalDateTime.now();
        this.resolutionNotes = resolutionNotes;
    }

    /**
     * Suppress the alert
     */
    public void suppress() {
        this.status = AlertStatus.SUPPRESSED;
    }

    /**
     * Check if alert is open
     */
    public boolean isOpen() {
        return status == AlertStatus.OPEN;
    }

    /**
     * Check if alert is critical
     */
    public boolean isCritical() {
        return severity == AlertSeverity.CRITICAL;
    }
}
