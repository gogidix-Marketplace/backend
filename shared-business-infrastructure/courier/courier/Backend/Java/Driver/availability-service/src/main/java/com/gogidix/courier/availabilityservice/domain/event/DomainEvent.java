package com.gogidix.courier.availabilityservice.domain.event;

import java.time.Instant;

/**
 * Base interface for domain events.
 */
public interface DomainEvent {

    /**
     * Get the event ID.
     *
     * @return the unique event identifier
     */
    String getEventId();

    /**
     * Get the aggregate ID that generated this event.
     *
     * @return the aggregate ID
     */
    String getAggregateId();

    /**
     * Get the event type.
     *
     * @return the event type
     */
    String getEventType();

    /**
     * Get the timestamp when the event occurred.
     *
     * @return the timestamp
     */
    Instant getTimestamp();
}
