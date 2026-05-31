package com.gogidix.finance.forecasting.domain.port.out;

import com.gogidix.finance.forecasting.domain.event.ForecastApprovedEvent;
import com.gogidix.finance.forecasting.domain.event.ForecastGeneratedEvent;

import java.util.List;

/**
 * Event Publisher Interface (Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    /**
     * Publishes a forecast generated event
     *
     * @param event the forecast generated event to publish
     */
    void publish(ForecastGeneratedEvent event);

    /**
     * Publishes a forecast approved event
     *
     * @param event the forecast approved event to publish
     */
    void publish(ForecastApprovedEvent event);

    /**
     * Publishes multiple events
     *
     * @param events the list of events to publish
     */
    void publishAll(List<Object> events);

    /**
     * Checks if the publisher is ready
     *
     * @return true if ready to publish events
     */
    boolean isReady();

    /**
     * Publishes a generic object event
     *
     * @param event the event object to publish
     * @param topic the topic to publish to
     */
    void publish(Object event, String topic);

    /**
     * Gets the status of the publisher
     *
     * @return status description
     */
    default String getStatus() {
        return isReady() ? "READY" : "NOT_READY";
    }
}
