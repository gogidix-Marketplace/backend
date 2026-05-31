package com.gogidix.aiservices.aiproductrecommendationservice.domain.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Base interface for all domain events.
 * Events represent important state changes in the domain.
 */
public interface DomainEvent {

    /**
     * Get the unique ID of this event.
     *
     * @return the event ID
     */
    String getEventId();

    /**
     * Get the aggregate ID that generated this event.
     *
     * @return the aggregate ID
     */
    String getAggregateId();

    /**
     * Get the tenant ID.
     *
     * @return the tenant ID
     */
    String getTenantId();

    /**
     * Get the timestamp when this event occurred.
     *
     * @return the timestamp
     */
    Instant getOccurredAt();

    /**
     * Get the event type for serialization.
     *
     * @return the event type
     */
    default String getEventType() {
        return this.getClass().getSimpleName();
    }
}
