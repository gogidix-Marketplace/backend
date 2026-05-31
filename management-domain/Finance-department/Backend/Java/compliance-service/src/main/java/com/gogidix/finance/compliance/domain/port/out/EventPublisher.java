package com.gogidix.finance.compliance.domain.port.out;

import com.gogidix.finance.compliance.domain.event.ComplianceCheckCompletedEvent;
import com.gogidix.finance.compliance.domain.event.ComplianceViolationEvent;

import java.util.List;

/**
 * Event Publisher Interface (Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    /**
     * Publishes a compliance violation event
     */
    void publish(ComplianceViolationEvent event);

    /**
     * Publishes a compliance check completed event
     */
    void publish(ComplianceCheckCompletedEvent event);

    /**
     * Publishes multiple events
     */
    void publishAll(List<Object> events);

    /**
     * Checks if the publisher is ready
     */
    boolean isReady();
}
