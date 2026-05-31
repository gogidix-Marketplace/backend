package com.gogidix.sales.forecast.domain.port.out;

import com.gogidix.sales.forecast.domain.event.*;

import java.util.List;

/**
 * Event Publisher Interface (Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    /**
     * Publishes a forecast created event
     */
    void publishForecastCreated(ForecastCreatedEvent event);

    /**
     * Publishes a forecast updated event
     */
    void publishForecastUpdated(ForecastUpdatedEvent event);

    /**
     * Publishes a forecast approved event
     */
    void publishForecastApproved(ForecastApprovedEvent event);

    /**
     * Publishes a forecast line item event
     */
    void publishLineItemEvent(ForecastLineItemEvent event);

    /**
     * Publishes multiple events
     */
    void publishAll(List<Object> events);

    /**
     * Checks if the publisher is ready
     */
    boolean isReady();
}
