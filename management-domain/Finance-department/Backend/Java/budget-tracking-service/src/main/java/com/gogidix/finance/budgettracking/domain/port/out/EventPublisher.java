package com.gogidix.finance.budgettracking.domain.port.out;

import com.gogidix.finance.budgettracking.domain.event.BudgetThresholdExceededEvent;
import com.gogidix.finance.budgettracking.domain.event.BudgetTransactionRecordedEvent;

import java.util.List;

/**
 * Event Publisher Interface (Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    /**
     * Publishes a budget transaction event
     */
    void publish(BudgetTransactionRecordedEvent event);

    /**
     * Publishes a budget threshold event
     */
    void publish(BudgetThresholdExceededEvent event);

    /**
     * Publishes multiple events
     */
    void publishAll(List<Object> events);

    /**
     * Checks if the publisher is ready
     */
    boolean isReady();
}
