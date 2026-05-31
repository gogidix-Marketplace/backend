package com.gogidix.finance.currency.domain.port.out;

import java.util.List;

import com.gogidix.finance.currency.domain.event.DomainEvent;

/**
 * Output Port - Event Publisher
 * Interface for publishing domain events
 * Following hexagonal architecture principles
 */
public interface EventPublisher {

    /**
     * Publish a domain event
     */
    void publish(DomainEvent event);

    /**
     * Publish multiple domain events
     */
    void publishAll(java.util.List<DomainEvent> events);

    /**
     * Check if publisher is ready
     */
    boolean isReady();
}
