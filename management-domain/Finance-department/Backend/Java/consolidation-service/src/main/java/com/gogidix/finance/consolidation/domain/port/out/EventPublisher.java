package com.gogidix.finance.consolidation.domain.port.out;

import com.gogidix.finance.consolidation.domain.event.ConsolidationCompletedEvent;
import com.gogidix.finance.consolidation.domain.event.ConsolidationFailedEvent;

import java.util.List;

/**
 * Event Publisher Interface (Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    /**
     * Publishes a consolidation completed event
     */
    void publish(ConsolidationCompletedEvent event);

    /**
     * Publishes a consolidation failed event
     */
    void publish(ConsolidationFailedEvent event);

    /**
     * Publishes multiple events
     */
    void publishAll(List<Object> events);

    /**
     * Checks if the publisher is ready
     */
    boolean isReady();
}
