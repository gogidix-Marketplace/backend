package com.gogidix.finance.accountsreceivable.infrastructure.messaging;

import com.gogidix.finance.accountsreceivable.domain.event.*;
import com.gogidix.finance.accountsreceivable.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishCreditMemoCreated(CreditMemoCreatedEvent event) {
        log.info("CreditMemo created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCreditMemoUpdated(CreditMemoUpdatedEvent event) {
        log.info("CreditMemo updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCreditMemoDeleted(CreditMemoDeletedEvent event) {
        log.info("CreditMemo deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCustomerCreated(CustomerCreatedEvent event) {
        log.info("Customer created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCustomerUpdated(CustomerUpdatedEvent event) {
        log.info("Customer updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCustomerDeleted(CustomerDeletedEvent event) {
        log.info("Customer deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishInvoiceCreated(InvoiceCreatedEvent event) {
        log.info("Invoice created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishInvoiceUpdated(InvoiceUpdatedEvent event) {
        log.info("Invoice updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishInvoiceDeleted(InvoiceDeletedEvent event) {
        log.info("Invoice deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPaymentCreated(PaymentCreatedEvent event) {
        log.info("Payment created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPaymentUpdated(PaymentUpdatedEvent event) {
        log.info("Payment updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPaymentDeleted(PaymentDeletedEvent event) {
        log.info("Payment deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPaymentScheduleCreated(PaymentScheduleCreatedEvent event) {
        log.info("PaymentSchedule created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPaymentScheduleUpdated(PaymentScheduleUpdatedEvent event) {
        log.info("PaymentSchedule updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPaymentScheduleDeleted(PaymentScheduleDeletedEvent event) {
        log.info("PaymentSchedule deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
