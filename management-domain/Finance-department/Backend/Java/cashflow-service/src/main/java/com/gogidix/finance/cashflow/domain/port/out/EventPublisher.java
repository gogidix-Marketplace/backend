package com.gogidix.finance.cashflow.domain.port.out;

import com.gogidix.finance.cashflow.domain.event.CashflowForecastGeneratedEvent;
import com.gogidix.finance.cashflow.domain.event.CashflowItemRecordedEvent;

import java.util.List;

/**
 * Event Publisher Interface (Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    /**
     * Publishes a cashflow item event
     */
    void publish(CashflowItemRecordedEvent event);

    /**
     * Publishes a cashflow forecast event
     */
    void publish(CashflowForecastGeneratedEvent event);

    /**
     * Publishes multiple events
     */
    void publishAll(List<Object> events);

    /**
     * Checks if the publisher is ready
     */
    boolean isReady();
}
