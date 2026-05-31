package com.gogidix.finance.budgetmanagement.infrastructure.messaging;

import com.gogidix.finance.budgetmanagement.domain.event.*;
import com.gogidix.finance.budgetmanagement.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishBudgetCreated(BudgetCreatedEvent event) {
        log.info("Budget created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBudgetUpdated(BudgetUpdatedEvent event) {
        log.info("Budget updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBudgetDeleted(BudgetDeletedEvent event) {
        log.info("Budget deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBudgetAllocationCreated(BudgetAllocationCreatedEvent event) {
        log.info("BudgetAllocation created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBudgetAllocationUpdated(BudgetAllocationUpdatedEvent event) {
        log.info("BudgetAllocation updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBudgetAllocationDeleted(BudgetAllocationDeletedEvent event) {
        log.info("BudgetAllocation deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBudgetPeriodCreated(BudgetPeriodCreatedEvent event) {
        log.info("BudgetPeriod created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBudgetPeriodUpdated(BudgetPeriodUpdatedEvent event) {
        log.info("BudgetPeriod updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBudgetPeriodDeleted(BudgetPeriodDeletedEvent event) {
        log.info("BudgetPeriod deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
