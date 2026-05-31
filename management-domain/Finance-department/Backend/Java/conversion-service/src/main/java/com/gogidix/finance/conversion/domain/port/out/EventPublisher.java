package com.gogidix.finance.conversion.domain.port.out;

import com.gogidix.finance.conversion.domain.event.ConversionCompletedEvent;

import java.util.List;

/**
 * Event Publisher Interface (Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    /**
     * Publishes a conversion event
     */
    void publish(ConversionCompletedEvent event);

    /**
     * Publishes multiple events
     */
    void publishAll(List<Object> events);

    /**
     * Checks if the publisher is ready
     */
    boolean isReady();
}
