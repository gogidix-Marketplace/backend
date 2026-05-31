package com.gogidix.monitoring.alertmanagementservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Domain model for an alert.
 * Represents a generated alert from a rule trigger.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    /**
     * Unique identifier for the alert.
     */
    private String id;

    /**
     * Tenant identifier.
     */
    private String tenantId;

    /**
     * Associated alert rule ID.
     */
    private String ruleId;

    /**
     * Alert rule name.
     */
    private String ruleName;

    /**
     * Service name that triggered the alert.
     */
    private String serviceName;

    /**
     * Metric name that triggered the alert.
     */
    private String metricName;

    /**
     * Alert severity.
     */
    private AlertRule.AlertSeverity severity;

    /**
     * Alert status.
     */
    @Builder.Default
    private AlertStatus status = AlertStatus.OPEN;

    /**
     * Alert message.
     */
    private String message;

    /**
     * Value that triggered the alert.
     */
    private Double triggerValue;

    /**
     * Threshold value.
     */
    private Double threshold;

    /**
     * When the alert was triggered.
     */
    private Instant triggeredAt;

    /**
     * When the alert was acknowledged (null if not acknowledged).
     */
    private Instant acknowledgedAt;

    /**
     * User who acknowledged the alert.
     */
    private String acknowledgedBy;

    /**
     * Acknowledgment comment.
     */
    private String acknowledgmentComment;

    /**
     * When the alert was resolved (null if not resolved).
     */
    private Instant resolvedAt;

    /**
     * User who resolved the alert.
     */
    private String resolvedBy;

    /**
     * Resolution comment.
     */
    private String resolutionComment;

    /**
     * Notification status.
     */
    @Builder.Default
    private NotificationStatus notificationStatus = NotificationStatus.PENDING;

    /**
     * Number of notification attempts.
     */
    @Builder.Default
    private Integer notificationAttempts = 0;

    /**
     * Additional context/data.
     */
    private Map<String, Object> context;

    /**
     * Tags.
     */
    private Map<String, String> tags;

    /**
     * Timestamp when created.
     */
    private Instant createdAt;

    /**
     * Timestamp when updated.
     */
    private Instant updatedAt;

    /**
     * Alert status enumeration.
     */
    public enum AlertStatus {
        OPEN,
        ACKNOWLEDGED,
        RESOLVED,
        SUPPRESSED,
        CLOSED
    }

    /**
     * Notification status enumeration.
     */
    public enum NotificationStatus {
        PENDING,
        SENT,
        FAILED,
        RETRYING
    }

    /**
     * Acknowledges the alert.
     */
    public void acknowledge(String userId, String comment) {
        this.status = AlertStatus.ACKNOWLEDGED;
        this.acknowledgedAt = Instant.now();
        this.acknowledgedBy = userId;
        this.acknowledgmentComment = comment;
        this.updatedAt = Instant.now();
    }

    /**
     * Resolves the alert.
     */
    public void resolve(String userId, String comment) {
        this.status = AlertStatus.RESOLVED;
        this.resolvedAt = Instant.now();
        this.resolvedBy = userId;
        this.resolutionComment = comment;
        this.updatedAt = Instant.now();
    }

    /**
     * Marks the alert as resolved (automatic).
     */
    public void autoResolve() {
        if (this.status == AlertStatus.OPEN || this.status == AlertStatus.ACKNOWLEDGED) {
            this.status = AlertStatus.RESOLVED;
            this.resolvedAt = Instant.now();
            this.resolvedBy = "system";
            this.resolutionComment = "Automatically resolved";
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Checks if the alert is open.
     */
    public boolean isOpen() {
        return status == AlertStatus.OPEN;
    }

    /**
     * Checks if the alert is acknowledged.
     */
    public boolean isAcknowledged() {
        return status == AlertStatus.ACKNOWLEDGED;
    }

    /**
     * Checks if the alert is resolved.
     */
    public boolean isResolved() {
        return status == AlertStatus.RESOLVED;
    }

    /**
     * Checks if notifications were sent successfully.
     */
    public boolean isNotificationSent() {
        return notificationStatus == NotificationStatus.SENT;
    }
}
