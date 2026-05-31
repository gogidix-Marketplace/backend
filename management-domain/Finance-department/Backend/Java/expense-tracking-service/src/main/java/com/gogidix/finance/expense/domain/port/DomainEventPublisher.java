package com.gogidix.finance.expense.domain.port;

import com.gogidix.finance.expense.domain.event.*;

public interface DomainEventPublisher {
    void publishExpenseCreated(ExpenseCreatedEvent event);
    void publishExpenseUpdated(ExpenseUpdatedEvent event);
    void publishExpenseDeleted(ExpenseDeletedEvent event);
}
