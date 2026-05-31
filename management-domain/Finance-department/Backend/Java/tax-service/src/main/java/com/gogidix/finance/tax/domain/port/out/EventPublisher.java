package com.gogidix.finance.tax.domain.port.out;

import com.gogidix.finance.tax.domain.event.TaxCalculationCompletedEvent;
import com.gogidix.finance.tax.domain.event.TaxFilingSubmittedEvent;
import com.gogidix.finance.tax.domain.event.TaxRateUpdatedEvent;

import java.util.List;

/**
 * Event Publisher Interface (Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    /**
     * Publishes a tax rate event
     */
    void publish(TaxRateUpdatedEvent event);

    /**
     * Publishes a tax calculation event
     */
    void publish(TaxCalculationCompletedEvent event);

    /**
     * Publishes a tax filing event
     */
    void publish(TaxFilingSubmittedEvent event);

    /**
     * Publishes multiple events
     */
    void publishAll(List<Object> events);

    /**
     * Checks if the publisher is ready
     */
    boolean isReady();
}
