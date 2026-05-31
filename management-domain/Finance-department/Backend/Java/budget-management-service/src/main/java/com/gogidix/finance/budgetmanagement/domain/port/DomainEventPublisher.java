package com.gogidix.finance.budgetmanagement.domain.port;

import com.gogidix.finance.budgetmanagement.domain.event.*;

public interface DomainEventPublisher {
    void publishBudgetCreated(BudgetCreatedEvent event);
    void publishBudgetUpdated(BudgetUpdatedEvent event);
    void publishBudgetDeleted(BudgetDeletedEvent event);
    void publishBudgetAllocationCreated(BudgetAllocationCreatedEvent event);
    void publishBudgetAllocationUpdated(BudgetAllocationUpdatedEvent event);
    void publishBudgetAllocationDeleted(BudgetAllocationDeletedEvent event);
    void publishBudgetPeriodCreated(BudgetPeriodCreatedEvent event);
    void publishBudgetPeriodUpdated(BudgetPeriodUpdatedEvent event);
    void publishBudgetPeriodDeleted(BudgetPeriodDeletedEvent event);
}
