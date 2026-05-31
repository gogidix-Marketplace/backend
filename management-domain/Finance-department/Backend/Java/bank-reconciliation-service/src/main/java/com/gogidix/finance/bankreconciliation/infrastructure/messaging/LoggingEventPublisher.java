package com.gogidix.finance.bankreconciliation.infrastructure.messaging;

import com.gogidix.finance.bankreconciliation.domain.event.*;
import com.gogidix.finance.bankreconciliation.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishBankAccountCreated(BankAccountCreatedEvent event) {
        log.info("BankAccount created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBankAccountUpdated(BankAccountUpdatedEvent event) {
        log.info("BankAccount updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBankAccountDeleted(BankAccountDeletedEvent event) {
        log.info("BankAccount deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBankStatementCreated(BankStatementCreatedEvent event) {
        log.info("BankStatement created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBankStatementUpdated(BankStatementUpdatedEvent event) {
        log.info("BankStatement updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBankStatementDeleted(BankStatementDeletedEvent event) {
        log.info("BankStatement deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBankTransactionCreated(BankTransactionCreatedEvent event) {
        log.info("BankTransaction created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBankTransactionUpdated(BankTransactionUpdatedEvent event) {
        log.info("BankTransaction updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBankTransactionDeleted(BankTransactionDeletedEvent event) {
        log.info("BankTransaction deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishReconciliationCreated(ReconciliationCreatedEvent event) {
        log.info("Reconciliation created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishReconciliationUpdated(ReconciliationUpdatedEvent event) {
        log.info("Reconciliation updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishReconciliationDeleted(ReconciliationDeletedEvent event) {
        log.info("Reconciliation deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishReconciliationLineCreated(ReconciliationLineCreatedEvent event) {
        log.info("ReconciliationLine created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishReconciliationLineUpdated(ReconciliationLineUpdatedEvent event) {
        log.info("ReconciliationLine updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishReconciliationLineDeleted(ReconciliationLineDeletedEvent event) {
        log.info("ReconciliationLine deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishStatementLineCreated(StatementLineCreatedEvent event) {
        log.info("StatementLine created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishStatementLineUpdated(StatementLineUpdatedEvent event) {
        log.info("StatementLine updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishStatementLineDeleted(StatementLineDeletedEvent event) {
        log.info("StatementLine deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
