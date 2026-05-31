package com.gogidix.finance.accountspayable.domain.port;

import com.gogidix.finance.accountspayable.domain.event.*;

public interface DomainEventPublisher {
    void publishInvoiceCreated(InvoiceCreatedEvent event);
    void publishInvoiceUpdated(InvoiceUpdatedEvent event);
    void publishInvoiceDeleted(InvoiceDeletedEvent event);
    void publishPaymentCreated(PaymentCreatedEvent event);
    void publishPaymentUpdated(PaymentUpdatedEvent event);
    void publishPaymentDeleted(PaymentDeletedEvent event);
    void publishPaymentScheduleCreated(PaymentScheduleCreatedEvent event);
    void publishPaymentScheduleUpdated(PaymentScheduleUpdatedEvent event);
    void publishPaymentScheduleDeleted(PaymentScheduleDeletedEvent event);
    void publishVendorCreated(VendorCreatedEvent event);
    void publishVendorUpdated(VendorUpdatedEvent event);
    void publishVendorDeleted(VendorDeletedEvent event);
    void publishVendorTermCreated(VendorTermCreatedEvent event);
    void publishVendorTermUpdated(VendorTermUpdatedEvent event);
    void publishVendorTermDeleted(VendorTermDeletedEvent event);
    void publishInvoiceApprovalEvent(InvoiceApprovedEvent event);
    void publishInvoiceEvent(InvoiceCreatedEvent event);
    boolean isReady();
}
