package com.gogidix.finance.bankreconciliation.domain.port.out;

import com.gogidix.finance.bankreconciliation.domain.event.BankStatementImportedEvent;
import com.gogidix.finance.bankreconciliation.domain.event.ReconciliationCompletedEvent;
import com.gogidix.finance.bankreconciliation.domain.event.ReconciliationDiscrepancyEvent;

import java.util.List;

/**
 * Event Publisher Interface (Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    /**
     * Publishes a reconciliation completed event
     */
    void publish(ReconciliationCompletedEvent event);

    /**
     * Publishes a reconciliation discrepancy event
     */
    void publish(ReconciliationDiscrepancyEvent event);

    /**
     * Publishes a bank statement imported event
     */
    void publish(BankStatementImportedEvent event);

    /**
     * Publishes multiple events
     */
    void publishAll(List<Object> events);

    /**
     * Checks if the publisher is ready
     */
    boolean isReady();
}
