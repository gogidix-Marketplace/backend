package com.gogidix.finance.accountspayable.domain.port.out;

import com.gogidix.finance.accountspayable.domain.event.InvoiceCreatedEvent;
import com.gogidix.finance.accountspayable.domain.event.InvoiceApprovedEvent;
import com.gogidix.finance.accountspayable.domain.event.PaymentProcessedEvent;
import com.gogidix.finance.accountspayable.domain.event.VendorRegisteredEvent;

import java.util.List;

/**
 * Event Publisher Interface (Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    /**
     * Publishes a vendor event
     */
    void publishVendorEvent(VendorRegisteredEvent event);

    /**
     * Publishes an invoice event
     */
    void publishInvoiceEvent(InvoiceCreatedEvent event);

    /**
     * Publishes an invoice approval event
     */
    void publishInvoiceApprovalEvent(InvoiceApprovedEvent event);

    /**
     * Publishes a payment event
     */
    void publishPaymentEvent(PaymentProcessedEvent event);

    /**
     * Publishes multiple events
     */
    void publishAll(List<Object> events);

    /**
     * Checks if the publisher is ready
     */
    boolean isReady();
}
