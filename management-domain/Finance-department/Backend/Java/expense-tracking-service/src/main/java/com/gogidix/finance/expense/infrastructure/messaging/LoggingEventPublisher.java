package com.gogidix.finance.expense.infrastructure.messaging;

import com.gogidix.finance.expense.domain.event.*;
import com.gogidix.finance.expense.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishExpenseCreated(ExpenseCreatedEvent event) {
        log.info("Expense created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishExpenseUpdated(ExpenseUpdatedEvent event) {
        log.info("Expense updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishExpenseDeleted(ExpenseDeletedEvent event) {
        log.info("Expense deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
