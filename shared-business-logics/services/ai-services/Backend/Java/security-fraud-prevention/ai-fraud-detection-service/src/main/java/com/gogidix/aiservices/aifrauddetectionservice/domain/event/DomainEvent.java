package com.gogidix.aiservices.aifrauddetectionservice.domain.event;

import java.time.Instant;

/**
 * Base interface for all domain events in the fraud detection system.
 * Domain events represent something that happened in the domain that
 * domain experts care about.
 *
 * Following Domain-Driven Design (DDD) principles, domain events:
 * - Are immutable
 * - Have a discrete occurrence in time
 * - Describe a business-relevant state change
 * - Are named in the past tense (e.g., FraudDetected, PatternAdded)
 */
public interface DomainEvent {

    /**
     * Gets the unique identifier for this event instance.
     *
     * @return The event ID
     */
    String getEventId();

    /**
     * Gets the timestamp when this event occurred.
     *
     * @return The occurrence timestamp
     */
    Instant getOccurredAt();

    /**
     * Gets the ID of the aggregate that generated this event.
     *
     * @return The aggregate ID
     */
    String getAggregateId();

    /**
     * Gets the type/name of this event for serialization and routing purposes.
     * Default implementation returns the simple class name.
     *
     * @return The event type
     */
    default String getEventType() {
        return this.getClass().getSimpleName();
    }

    /**
     * Gets the aggregate type that generated this event.
     * Default implementation extracts it from the package name.
     *
     * @return The aggregate type
     */
    default String getAggregateType() {
        String className = this.getClass().getName();
        // Extract from package: com.gogidix...domain.event.{EventName}
        // Return the event name without "Event" suffix
        String simpleName = this.getClass().getSimpleName();
        return simpleName.endsWith("Event")
            ? simpleName.substring(0, simpleName.length() - "Event".length())
            : simpleName;
    }
}
