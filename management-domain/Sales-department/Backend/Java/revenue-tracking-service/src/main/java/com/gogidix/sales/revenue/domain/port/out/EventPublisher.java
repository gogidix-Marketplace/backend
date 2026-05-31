package com.gogidix.sales.revenue.domain.port.out;

import com.gogidix.sales.revenue.domain.event.ARRUpdatedEvent;
import com.gogidix.sales.revenue.domain.event.MRRUpdatedEvent;
import com.gogidix.sales.revenue.domain.event.RevenueRecognizedEvent;

import java.util.List;

/**
 * Event Publisher Interface (Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    /**
     * Publishes a revenue event
     */
    void publish(RevenueRecognizedEvent event);

    /**
     * Publishes an MRR event
     */
    void publish(MRRUpdatedEvent event);

    /**
     * Publishes an ARR event
     */
    void publish(ARRUpdatedEvent event);

    /**
     * Publishes multiple events
     */
    void publishAll(List<Object> events);

    /**
     * Checks if the publisher is ready
     */
    boolean isReady();
}
