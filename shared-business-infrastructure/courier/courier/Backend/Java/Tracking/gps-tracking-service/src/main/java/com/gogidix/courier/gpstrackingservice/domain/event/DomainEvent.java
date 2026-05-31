package com.gogidix.courier.gpstrackingservice.domain.event;

import java.time.Instant;

/**
 * Base interface for all domain events.
 */
public interface DomainEvent {

    /**
     * Get the unique event ID.
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
     * Get when the event occurred.
     *
     * @return the timestamp
     */
    Instant getOccurredAt();

    /**
     * Get the event type.
     *
     * @return the event type
     */
    default String getEventType() {
        return this.getClass().getSimpleName();
    }
}
