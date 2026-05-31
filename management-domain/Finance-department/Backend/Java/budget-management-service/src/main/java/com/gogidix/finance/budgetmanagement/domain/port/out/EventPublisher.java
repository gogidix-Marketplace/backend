package com.gogidix.finance.budgetmanagement.domain.port.out;

import com.gogidix.finance.budgetmanagement.domain.event.BudgetApprovedEvent;
import com.gogidix.finance.budgetmanagement.domain.event.BudgetCreatedEvent;
import com.gogidix.finance.budgetmanagement.domain.event.BudgetModifiedEvent;

import java.util.List;

/**
 * Event Publisher Interface (Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    /**
     * Publishes a budget created event
     */
    void publish(BudgetCreatedEvent event);

    /**
     * Publishes a budget approved event
     */
    void publish(BudgetApprovedEvent event);

    /**
     * Publishes a budget modified event
     */
    void publish(BudgetModifiedEvent event);

    /**
     * Publishes multiple events
     */
    void publishAll(List<Object> events);

    /**
     * Checks if the publisher is ready
     */
    boolean isReady();
}
