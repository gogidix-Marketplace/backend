package com.gogidix.ecommerce.paymentmethod.infrastructure.messaging.event;

import com.gogidix.ecommerce.paymentmethod.domain.event.PaymentMethodCreatedEvent;
import com.gogidix.ecommerce.paymentmethod.domain.event.PaymentMethodUpdatedEvent;
import com.gogidix.ecommerce.paymentmethod.domain.event.PaymentMethodDeletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodDomainEventHandler {

    @EventListener
    public void handleCreated(PaymentMethodCreatedEvent event) {
    }

    @EventListener
    public void handleUpdated(PaymentMethodUpdatedEvent event) {
    }

    @EventListener
    public void handleDeleted(PaymentMethodDeletedEvent event) {
    }
}