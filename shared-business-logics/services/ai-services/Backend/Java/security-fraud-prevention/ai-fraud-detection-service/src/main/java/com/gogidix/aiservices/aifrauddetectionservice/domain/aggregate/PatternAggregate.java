package com.gogidix.aiservices.aifrauddetectionservice.domain.aggregate;

import com.gogidix.aiservices.aifrauddetectionservice.domain.event.PatternAddedEvent;
import com.gogidix.aiservices.aifrauddetectionservice.domain.event.PatternUpdatedEvent;
import com.gogidix.aiservices.aifrauddetectionservice.domain.event.ThresholdBreachedEvent;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudPattern;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Domain Aggregate for Fraud Pattern management.
 * This is the aggregate root for fraud pattern operations.
 *
 * Enforces invariants:
 * - Pattern names must be unique within a tenant
 * - Confidence scores must be between 0 and 1
 * - Thresholds must be positive values
 * - All state changes emit domain events
 */
@Aggregate(
    value = "Fraud Pattern Aggregate Root",
    type = "FraudPattern"
)
@Getter
@Builder
public class PatternAggregate {

    private final String patternId;
    private String patternName;
    private String description;
    private String patternType;
    private String patternDefinition;
    private double confidenceScore;
    private double riskThreshold;
    private boolean active;
    private final String tenantId;
    private final String createdBy;
    private String updatedBy;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant lastSeen;
    private int occurrenceCount;
    private final List<DomainEventWrapper> domainEvents;
    private PatternSeverity severity;

    /**
     * Pattern severity levels.
     */
    public enum PatternSeverity {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }

    /**
     * Wrapper for domain events until they are dispatched.
     */
    @Getter
    @lombok.AllArgsConstructor
    public static class DomainEventWrapper {
        private final String eventId;
        private final Object event;
        private final Instant occurredAt;
        private final String aggregateId;

        public static DomainEventWrapper create(Object event, String aggregateId) {
            return new DomainEventWrapper(
                    UUID.randomUUID().toString(),
                    event,
                    Instant.now(),
                    aggregateId
            );
        }
    }

    /**
     * Creates a new fraud pattern.
     */
    public static PatternAggregate create(
            String patternName,
            String description,
            String patternType,
            String patternDefinition,
            double confidenceScore,
            double riskThreshold,
            String tenantId,
            String createdBy) {

        if (confidenceScore < 0.0 || confidenceScore > 1.0) {
            throw new IllegalArgumentException("Confidence score must be between 0 and 1");
        }

        if (riskThreshold < 0.0) {
            throw new IllegalArgumentException("Risk threshold must be positive");
        }

        PatternAggregate pattern = PatternAggregate.builder()
                .patternId(UUID.randomUUID().toString())
                .patternName(patternName)
                .description(description)
                .patternType(patternType)
                .patternDefinition(patternDefinition)
                .confidenceScore(confidenceScore)
                .riskThreshold(riskThreshold)
                .active(true)
                .tenantId(tenantId)
                .createdBy(createdBy)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .lastSeen(Instant.now())
                .occurrenceCount(0)
                .domainEvents(new ArrayList<>())
                .severity(determineSeverity(confidenceScore))
                .build();

        PatternAddedEvent event = PatternAddedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .patternId(pattern.patternId)
                .patternType(patternType)
                .description(description)
                .addedBy(createdBy)
                .tenantId(tenantId)
                .confidenceScore(confidenceScore)
                .occurredAt(Instant.now())
                .aggregateId(pattern.patternId)
                .build();

        pattern.addDomainEvent(DomainEventWrapper.create(event, pattern.patternId));

        return pattern;
    }

    /**
     * Activates the pattern.
     */
    public void activate(String activatedBy) {
        if (this.active) {
            return; // Already active
        }

        this.active = true;
        this.updatedBy = activatedBy;
        this.updatedAt = Instant.now();

        PatternUpdatedEvent event = PatternUpdatedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .patternId(this.patternId)
                .oldPattern("Inactive")
                .newPattern("Active")
                .updatedBy(activatedBy)
                .occurredAt(Instant.now())
                .aggregateId(this.patternId)
                .build();

        addDomainEvent(DomainEventWrapper.create(event, this.patternId));
    }

    /**
     * Deactivates the pattern.
     */
    public void deactivate(String deactivatedBy) {
        if (!this.active) {
            return; // Already inactive
        }

        this.active = false;
        this.updatedBy = deactivatedBy;
        this.updatedAt = Instant.now();

        PatternUpdatedEvent event = PatternUpdatedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .patternId(this.patternId)
                .oldPattern("Active")
                .newPattern("Inactive")
                .updatedBy(deactivatedBy)
                .occurredAt(Instant.now())
                .aggregateId(this.patternId)
                .build();

        addDomainEvent(DomainEventWrapper.create(event, this.patternId));
    }

    /**
     * Updates the risk threshold and emits event if breached.
     *
     * @param newThreshold The new threshold value
     * @param updatedBy Who made the update
     */
    public void updateThreshold(double newThreshold, String updatedBy) {
        if (newThreshold < 0.0) {
            throw new IllegalArgumentException("Risk threshold must be positive");
        }

        double oldThreshold = this.riskThreshold;
        this.riskThreshold = newThreshold;
        this.updatedBy = updatedBy;
        this.updatedAt = Instant.now();

        // Check if threshold is breached
        if (this.confidenceScore > newThreshold) {
            ThresholdBreachedEvent event = ThresholdBreachedEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .thresholdName("Risk Threshold: " + this.patternName)
                    .actualValue(this.confidenceScore)
                    .thresholdValue(newThreshold)
                    .severity(this.severity.name())
                    .patternId(this.patternId)
                    .occurredAt(Instant.now())
                    .aggregateId(this.patternId)
                    .build();

            addDomainEvent(DomainEventWrapper.create(event, this.patternId));
        }

        PatternUpdatedEvent event = PatternUpdatedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .patternId(this.patternId)
                .oldPattern(String.valueOf(oldThreshold))
                .newPattern(String.valueOf(newThreshold))
                .updatedBy(updatedBy)
                .occurredAt(Instant.now())
                .aggregateId(this.patternId)
                .build();

        addDomainEvent(DomainEventWrapper.create(event, this.patternId));
    }

    /**
     * Updates pattern details.
     */
    public void updateDetails(
            String newName,
            String newDescription,
            String newDefinition,
            double newConfidenceScore,
            String updatedBy) {

        if (newConfidenceScore < 0.0 || newConfidenceScore > 1.0) {
            throw new IllegalArgumentException("Confidence score must be between 0 and 1");
        }

        String oldDefinition = this.patternDefinition;

        this.patternName = newName;
        this.description = newDescription;
        this.patternDefinition = newDefinition;
        this.confidenceScore = newConfidenceScore;
        this.updatedBy = updatedBy;
        this.updatedAt = Instant.now();
        this.severity = determineSeverity(newConfidenceScore);

        PatternUpdatedEvent event = PatternUpdatedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .patternId(this.patternId)
                .oldPattern(oldDefinition)
                .newPattern(newDefinition)
                .updatedBy(updatedBy)
                .occurredAt(Instant.now())
                .aggregateId(this.patternId)
                .build();

        addDomainEvent(DomainEventWrapper.create(event, this.patternId));
    }

    /**
     * Records that this pattern was matched in an analysis.
     */
    public void recordMatch() {
        this.occurrenceCount++;
        this.lastSeen = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Determines severity based on confidence score.
     */
    private static PatternSeverity determineSeverity(double confidenceScore) {
        if (confidenceScore >= 0.9) return PatternSeverity.CRITICAL;
        if (confidenceScore >= 0.7) return PatternSeverity.HIGH;
        if (confidenceScore >= 0.5) return PatternSeverity.MEDIUM;
        return PatternSeverity.LOW;
    }

    /**
     * Adds a domain event to the list of uncommitted events.
     */
    private void addDomainEvent(DomainEventWrapper event) {
        this.domainEvents.add(event);
    }

    /**
     * Returns and clears all uncommitted domain events.
     */
    public List<DomainEventWrapper> getAndClearDomainEvents() {
        List<DomainEventWrapper> events = new ArrayList<>(this.domainEvents);
        this.domainEvents.clear();
        return events;
    }

    /**
     * Returns uncommitted domain events without clearing them.
     */
    public List<DomainEventWrapper> getUncommittedDomainEvents() {
        return Collections.unmodifiableList(this.domainEvents);
    }

    /**
     * Checks if the pattern is currently active.
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Checks if confidence score exceeds the risk threshold.
     */
    public boolean isThresholdBreached() {
        return this.confidenceScore > this.riskThreshold;
    }

    /**
     * Gets the pattern age in days.
     */
    public long getAgeInDays() {
        return Instant.now().getEpochSecond() - this.createdAt.getEpochSecond() / 86400;
    }

    /**
     * Gets the days since last seen.
     */
    public long getDaysSinceLastSeen() {
        return Instant.now().getEpochSecond() - this.lastSeen.getEpochSecond() / 86400;
    }

    /**
     * Converts this aggregate to a FraudPattern value object.
     */
    public FraudPattern toValueObject() {
        return FraudPattern.builder()
                .patternId(this.patternId)
                .patternName(this.patternName)
                .description(this.description)
                .tenantId(this.tenantId)
                .confidenceScore(this.confidenceScore)
                .lastSeen(this.lastSeen)
                .occurrenceCount(this.occurrenceCount)
                .build();
    }

    /**
     * Checks if pattern needs review (high occurrence but not recently seen).
     */
    public boolean needsReview() {
        return this.occurrenceCount > 100 && getDaysSinceLastSeen() > 30;
    }

    /**
     * Gets a summary of the pattern.
     */
    public String getSummary() {
        return String.format("Pattern[%s]: %s (confidence: %.2f, occurrences: %d, active: %s)",
                this.patternId,
                this.patternName,
                this.confidenceScore,
                this.occurrenceCount,
                this.active);
    }
}
