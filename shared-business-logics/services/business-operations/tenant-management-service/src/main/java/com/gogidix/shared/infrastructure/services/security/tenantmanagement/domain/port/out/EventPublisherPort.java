package com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.port.out;

import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.event.DomainEvent;

/**
 * Port for publishing domain events.
 * Implementations will publish to event buses like Kafka.
 */
public interface EventPublisherPort {
    /**
     * Publish a domain event.
     *
     * @param event the event to publish
     */
    void publish(DomainEvent event);
}
