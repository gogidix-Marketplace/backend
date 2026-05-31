package com.gogidix.aiservices.aifrauddetectionservice.domain.event;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event emitted when a system threshold is breached.
 * This event is critical for monitoring and alerting.
 */
@Value
@Builder
public class ThresholdBreachedEvent implements DomainEvent {

    /**
     * Unique identifier for this event instance.
     */
    String eventId;

    /**
     * Name of the threshold that was breached.
     */
    String thresholdName;

    /**
     * The actual value that breached the threshold.
     */
    double actualValue;

    /**
     * The threshold value that was exceeded.
     */
    double thresholdValue;

    /**
     * Severity level of the breach.
     */
    String severity;

    /**
     * Pattern ID if this is related to a fraud pattern.
     */
    String patternId;

    /**
     * Tenant ID for multi-tenancy support.
     */
    String tenantId;

    /**
     * When the threshold was breached.
     */
    Instant occurredAt;

    /**
     * ID of the aggregate that generated this event.
     */
    String aggregateId;

    /**
     * Additional context about the breach.
     */
    String context;

    /**
     * The amount by which the threshold was exceeded.
     */
    Double excessAmount;

    /**
     * Percentage above the threshold.
     */
    Double percentageExcess;

    @Override
    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public String getAggregateId() {
        return aggregateId;
    }

    @Override
    public String getEventType() {
        return "ThresholdBreached";
    }

    @Override
    public String getAggregateType() {
        return patternId != null ? "FraudPattern" : "System";
    }

    /**
     * Creates a new ThresholdBreachedEvent with generated ID and timestamp.
     */
    public static ThresholdBreachedEvent create(String thresholdName, double actualValue,
                                                double thresholdValue, String severity) {
        var builder = ThresholdBreachedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .thresholdName(thresholdName)
                .actualValue(actualValue)
                .thresholdValue(thresholdValue)
                .severity(severity)
                .occurredAt(Instant.now());

        // Calculate excess amounts
        if (thresholdValue > 0) {
            double excess = actualValue - thresholdValue;
            double percentage = (excess / thresholdValue) * 100.0;
            builder.excessAmount(excess).percentageExcess(percentage);
        }

        return builder.aggregateId(thresholdName).build();
    }

    /**
     * Checks if this is a critical severity breach.
     */
    public boolean isCritical() {
        return "CRITICAL".equalsIgnoreCase(severity);
    }

    /**
     * Checks if this is a high severity breach.
     */
    public boolean isHighSeverity() {
        return "HIGH".equalsIgnoreCase(severity) || "CRITICAL".equalsIgnoreCase(severity);
    }

    /**
     * Checks if this is a low severity breach.
     */
    public boolean isLowSeverity() {
        return "LOW".equalsIgnoreCase(severity);
    }

    /**
     * Gets the breach percentage.
     */
    public double getBreachPercentage() {
        if (percentageExcess != null) {
            return percentageExcess;
        }
        if (thresholdValue > 0) {
            return ((actualValue - thresholdValue) / thresholdValue) * 100.0;
        }
        return 0.0;
    }
}
