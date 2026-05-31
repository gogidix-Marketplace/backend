package com.gogidix.finance.accountsreceivable.domain.port.out;

import com.gogidix.finance.accountsreceivable.domain.event.CustomerRegisteredEvent;
import com.gogidix.finance.accountsreceivable.domain.event.InvoiceGeneratedEvent;
import com.gogidix.finance.accountsreceivable.domain.event.PaymentReceivedEvent;

import java.util.List;

/**
 * Event Publisher Interface (Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    /**
     * Publishes an invoice event
     */
    void publish(InvoiceGeneratedEvent event);

    /**
     * Publishes a payment event
     */
    void publish(PaymentReceivedEvent event);

    /**
     * Publishes a customer event
     */
    void publish(CustomerRegisteredEvent event);

    /**
     * Publishes multiple events
     */
    void publishAll(List<Object> events);

    /**
     * Checks if the publisher is ready
     */
    boolean isReady();
}
