package com.gogidix.ecommerce.paymentgateway.infrastructure.messaging.event;

import com.gogidix.ecommerce.paymentgateway.domain.event.PaymentGatewayCreatedEvent;
import com.gogidix.ecommerce.paymentgateway.domain.event.PaymentGatewayUpdatedEvent;
import com.gogidix.ecommerce.paymentgateway.domain.event.PaymentGatewayDeletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayDomainEventHandler {

    @EventListener
    public void handleCreated(PaymentGatewayCreatedEvent event) {
    }

    @EventListener
    public void handleUpdated(PaymentGatewayUpdatedEvent event) {
    }

    @EventListener
    public void handleDeleted(PaymentGatewayDeletedEvent event) {
    }
}