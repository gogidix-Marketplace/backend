package com.gogidix.aiservices.aifrauddetectionservice.domain.event;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event emitted when an existing fraud pattern is updated.
 * This event tracks changes to pattern definitions, thresholds, and status.
 */
@Value
@Builder
public class PatternUpdatedEvent implements DomainEvent {

    /**
     * Unique identifier for this event instance.
     */
    String eventId;

    /**
     * ID of the pattern that was updated.
     */
    String patternId;

    /**
     * Old pattern value/state before update.
     */
    String oldPattern;

    /**
     * New pattern value/state after update.
     */
    String newPattern;

    /**
     * User or system that performed the update.
     */
    String updatedBy;

    /**
     * Type of update (e.g., "DEFINITION", "THRESHOLD", "STATUS", "ACTIVATION").
     */
    String updateType;

    /**
     * When the update occurred.
     */
    Instant occurredAt;

    /**
     * ID of the aggregate that generated this event.
     */
    String aggregateId;

    /**
     * Tenant ID for multi-tenancy support.
     */
    String tenantId;

    /**
     * Field that was updated.
     */
    String updatedField;

    /**
     * Previous confidence score (if applicable).
     */
    Double oldConfidenceScore;

    /**
     * New confidence score (if applicable).
     */
    Double newConfidenceScore;

    @Override
    public String getEventType() {
        return "PatternUpdated";
    }

    @Override
    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public String getAggregateId() {
        return aggregateId;
    }

    @Override
    public String getAggregateType() {
        return "FraudPattern";
    }

    /**
     * Creates a new PatternUpdatedEvent with generated ID and timestamp.
     */
    public static PatternUpdatedEvent create(String patternId, String oldPattern,
                                             String newPattern, String updatedBy) {
        return PatternUpdatedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .patternId(patternId)
                .oldPattern(oldPattern)
                .newPattern(newPattern)
                .updatedBy(updatedBy)
                .occurredAt(Instant.now())
                .aggregateId(patternId)
                .build();
    }

    /**
     * Creates a pattern update event for activation/deactivation.
     */
    public static PatternUpdatedEvent createStatusChange(String patternId, boolean wasActive,
                                                         boolean isActive, String updatedBy) {
        return PatternUpdatedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .patternId(patternId)
                .oldPattern(wasActive ? "Active" : "Inactive")
                .newPattern(isActive ? "Active" : "Inactive")
                .updatedBy(updatedBy)
                .updateType(isActive ? "ACTIVATION" : "DEACTIVATION")
                .occurredAt(Instant.now())
                .aggregateId(patternId)
                .build();
    }

    /**
     * Checks if this update represents an activation.
     */
    public boolean isActivation() {
        return "ACTIVATION".equalsIgnoreCase(updateType) ||
               ("Active".equalsIgnoreCase(newPattern) && "Inactive".equalsIgnoreCase(oldPattern));
    }

    /**
     * Checks if this update represents a deactivation.
     */
    public boolean isDeactivation() {
        return "DEACTIVATION".equalsIgnoreCase(updateType) ||
               ("Inactive".equalsIgnoreCase(newPattern) && "Active".equalsIgnoreCase(oldPattern));
    }

    /**
     * Checks if confidence score was increased.
     */
    public boolean isConfidenceIncreased() {
        return oldConfidenceScore != null && newConfidenceScore != null &&
               newConfidenceScore > oldConfidenceScore;
    }
}
