package com.gogidix.monitoring.alertmanagementservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Domain model for alert rules.
 * Defines conditions under which alerts should be generated.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertRule {

    /**
     * Unique identifier for the alert rule.
     */
    private String id;

    /**
     * Tenant identifier.
     */
    private String tenantId;

    /**
     * Rule name.
     */
    private String name;

    /**
     * Rule description.
     */
    private String description;

    /**
     * Whether the rule is enabled.
     */
    @Builder.Default
    private Boolean enabled = true;

    /**
     * Service name to monitor (null for all services).
     */
    private String serviceName;

    /**
     * Metric name to monitor.
     */
    private String metricName;

    /**
     * Alert condition type.
     */
    private ConditionType conditionType;

    /**
     * Threshold value.
     */
    private Double threshold;

    /**
     * Comparison operator.
     */
    private ComparisonOperator operator;

    /**
     * Duration in seconds that condition must be met.
     */
    private Integer durationSeconds;

    /**
     * Severity level.
     */
    private AlertSeverity severity;

    /**
     * Notification channels.
     */
    private List<NotificationChannel> notificationChannels;

    /**
     * Recipients for notifications.
     */
    private List<String> recipients;

    /**
     * Cooldown period in seconds (minimum time between alerts).
     */
    @Builder.Default
    private Integer cooldownSeconds = 300;

    /**
     * Custom message template.
     */
    private String messageTemplate;

    /**
     * Additional metadata.
     */
    private Map<String, Object> metadata;

    /**
     * Tags for categorization.
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
     * User who created the rule.
     */
    private String createdBy;

    /**
     * User who last updated the rule.
     */
    private String updatedBy;

    /**
     * Condition type enumeration.
     */
    public enum ConditionType {
        THRESHOLD,
        ANOMALY_DETECTION,
        RATE_OF_CHANGE,
        MISSING_DATA,
        PREDICTIVE
    }

    /**
     * Comparison operator enumeration.
     */
    public enum ComparisonOperator {
        GREATER_THAN,
        LESS_THAN,
        EQUAL_TO,
        NOT_EQUAL_TO,
        GREATER_THAN_OR_EQUAL,
        LESS_THAN_OR_EQUAL
    }

    /**
     * Alert severity enumeration.
     */
    public enum AlertSeverity {
        CRITICAL,
        HIGH,
        MEDIUM,
        LOW,
        INFO
    }

    /**
     * Notification channel enumeration.
     */
    public enum NotificationChannel {
        EMAIL,
        SMS,
        WEBHOOK,
        SLACK,
        PAGERDUTY,
        INCIDENT_MANAGEMENT
    }

    /**
     * Evaluates if a value triggers this alert rule.
     */
    public boolean evaluate(Double value) {
        if (value == null || operator == null || threshold == null) {
            return false;
        }

        return switch (operator) {
            case GREATER_THAN -> value > threshold;
            case LESS_THAN -> value < threshold;
            case EQUAL_TO -> Math.abs(value - threshold) < 0.0001;
            case NOT_EQUAL_TO -> Math.abs(value - threshold) >= 0.0001;
            case GREATER_THAN_OR_EQUAL -> value >= threshold;
            case LESS_THAN_OR_EQUAL -> value <= threshold;
        };
    }

    /**
     * Checks if the rule is applicable for a service.
     */
    public boolean isApplicableForService(String service) {
        return serviceName == null || serviceName.isBlank() || serviceName.equals(service);
    }
}
