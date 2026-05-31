package com.gogidix.finance.accountspayable.infrastructure.messaging;

import com.gogidix.finance.accountspayable.domain.event.*;
import com.gogidix.finance.accountspayable.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


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
    @Override
    public void publishVendorCreated(VendorCreatedEvent event) {
        log.info("Vendor created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishVendorUpdated(VendorUpdatedEvent event) {
        log.info("Vendor updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishVendorDeleted(VendorDeletedEvent event) {
        log.info("Vendor deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishVendorTermCreated(VendorTermCreatedEvent event) {
        log.info("VendorTerm created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishVendorTermUpdated(VendorTermUpdatedEvent event) {
        log.info("VendorTerm updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishVendorTermDeleted(VendorTermDeletedEvent event) {
        log.info("VendorTerm deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishInvoiceApprovalEvent(InvoiceApprovedEvent event) {
        log.info("Invoice approved: tenant={}, invoiceId={}", event.getTenantId(), event.getInvoiceId());
    }
    @Override
    public void publishInvoiceEvent(InvoiceCreatedEvent event) {
        log.info("Invoice event: tenant={}, invoiceId={}", event.getTenantId(), event.getInvoiceId());
    }
    @Override
    public boolean isReady() {
        return true;
    }
}
