package com.gogidix.finance.accountsreceivable.domain.port;

import com.gogidix.finance.accountsreceivable.domain.event.*;

public interface DomainEventPublisher {
    void publishCreditMemoCreated(CreditMemoCreatedEvent event);
    void publishCreditMemoUpdated(CreditMemoUpdatedEvent event);
    void publishCreditMemoDeleted(CreditMemoDeletedEvent event);
    void publishCustomerCreated(CustomerCreatedEvent event);
    void publishCustomerUpdated(CustomerUpdatedEvent event);
    void publishCustomerDeleted(CustomerDeletedEvent event);
    void publishInvoiceCreated(InvoiceCreatedEvent event);
    void publishInvoiceUpdated(InvoiceUpdatedEvent event);
    void publishInvoiceDeleted(InvoiceDeletedEvent event);
    void publishPaymentCreated(PaymentCreatedEvent event);
    void publishPaymentUpdated(PaymentUpdatedEvent event);
    void publishPaymentDeleted(PaymentDeletedEvent event);
    void publishPaymentScheduleCreated(PaymentScheduleCreatedEvent event);
    void publishPaymentScheduleUpdated(PaymentScheduleUpdatedEvent event);
    void publishPaymentScheduleDeleted(PaymentScheduleDeletedEvent event);
}
