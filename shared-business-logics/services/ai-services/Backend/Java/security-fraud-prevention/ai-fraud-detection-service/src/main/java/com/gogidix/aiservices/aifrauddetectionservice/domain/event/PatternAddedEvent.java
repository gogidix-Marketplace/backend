package com.gogidix.aiservices.aifrauddetectionservice.domain.event;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain event emitted when a new fraud pattern is added to the system.
 * This event indicates the creation of a new pattern for fraud detection.
 */
@Value
@Builder
public class PatternAddedEvent implements DomainEvent {

    /**
     * Unique identifier for this event instance.
     */
    String eventId;

    /**
     * ID of the pattern that was added.
     */
    String patternId;

    /**
     * Type/category of the pattern.
     */
    String patternType;

    /**
     * Human-readable description of the pattern.
     */
    String description;

    /**
     * User or system that added the pattern.
     */
    String addedBy;

    /**
     * Tenant ID for multi-tenancy support.
     */
    String tenantId;

    /**
     * Initial confidence score for the pattern.
     */
    Double confidenceScore;

    /**
     * When the pattern was added.
     */
    Instant occurredAt;

    /**
     * ID of the aggregate that generated this event.
     */
    String aggregateId;

    /**
     * Risk threshold associated with the pattern.
     */
    Double riskThreshold;

    /**
     * Additional metadata about the pattern.
     */
    String metadata;

    /**
     * Analysis ID if this was added during an analysis.
     */
    String analysisId;

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
        return "PatternAdded";
    }

    @Override
    public String getAggregateType() {
        return "FraudPattern";
    }

    /**
     * Creates a new PatternAddedEvent with generated ID and timestamp.
     */
    public static PatternAddedEvent create(String patternId, String patternType,
                                           String description, String addedBy) {
        return PatternAddedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .patternId(patternId)
                .patternType(patternType)
                .description(description)
                .addedBy(addedBy)
                .occurredAt(Instant.now())
                .aggregateId(patternId)
                .build();
    }

    /**
     * Checks if this is a high-confidence pattern.
     */
    public boolean isHighConfidence() {
        return confidenceScore != null && confidenceScore >= 0.7;
    }

    /**
     * Checks if this pattern requires immediate review before activation.
     */
    public boolean requiresReview() {
        return confidenceScore != null && confidenceScore >= 0.9;
    }
}
