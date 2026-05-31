package com.gogidix.finance.bankreconciliation.domain.port;

import com.gogidix.finance.bankreconciliation.domain.event.*;

public interface DomainEventPublisher {
    void publishBankAccountCreated(BankAccountCreatedEvent event);
    void publishBankAccountUpdated(BankAccountUpdatedEvent event);
    void publishBankAccountDeleted(BankAccountDeletedEvent event);
    void publishBankStatementCreated(BankStatementCreatedEvent event);
    void publishBankStatementUpdated(BankStatementUpdatedEvent event);
    void publishBankStatementDeleted(BankStatementDeletedEvent event);
    void publishBankTransactionCreated(BankTransactionCreatedEvent event);
    void publishBankTransactionUpdated(BankTransactionUpdatedEvent event);
    void publishBankTransactionDeleted(BankTransactionDeletedEvent event);
    void publishReconciliationCreated(ReconciliationCreatedEvent event);
    void publishReconciliationUpdated(ReconciliationUpdatedEvent event);
    void publishReconciliationDeleted(ReconciliationDeletedEvent event);
    void publishReconciliationLineCreated(ReconciliationLineCreatedEvent event);
    void publishReconciliationLineUpdated(ReconciliationLineUpdatedEvent event);
    void publishReconciliationLineDeleted(ReconciliationLineDeletedEvent event);
    void publishStatementLineCreated(StatementLineCreatedEvent event);
    void publishStatementLineUpdated(StatementLineUpdatedEvent event);
    void publishStatementLineDeleted(StatementLineDeletedEvent event);
}
