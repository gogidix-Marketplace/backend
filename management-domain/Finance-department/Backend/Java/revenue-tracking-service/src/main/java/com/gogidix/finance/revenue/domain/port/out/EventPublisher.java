package com.gogidix.finance.revenue.domain.port.out;

import com.gogidix.finance.revenue.domain.event.RevenueForecastGeneratedEvent;
import com.gogidix.finance.revenue.domain.event.RevenueRecognizedEvent;
import com.gogidix.finance.revenue.domain.event.RevenueStreamEvent;

import java.util.List;

/**
 * Event Publisher Interface (Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    /**
     * Publishes a revenue event
     */
    void publishRevenueEvent(RevenueRecognizedEvent event);

    /**
     * Publishes a revenue stream event
     */
    void publishStreamEvent(RevenueStreamEvent event);

    /**
     * Publishes a forecast event
     */
    void publishForecastEvent(RevenueForecastGeneratedEvent event);

    /**
     * Publishes multiple events
     */
    void publishAll(List<Object> events);

    /**
     * Checks if the publisher is ready
     */
    boolean isReady();
}
