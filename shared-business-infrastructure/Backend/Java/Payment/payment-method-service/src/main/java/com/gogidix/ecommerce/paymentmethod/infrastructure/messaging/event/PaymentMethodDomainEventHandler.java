package com.gogidix.ecommerce.paymentmethod.infrastructure.messaging.event;

import com.gogidix.ecommerce.paymentmethod.domain.event.PaymentMethodCreatedEvent;
import com.gogidix.ecommerce.paymentmethod.domain.event.PaymentMethodUpdatedEvent;
import com.gogidix.ecommerce.paymentmethod.domain.event.PaymentMethodDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(PaymentMethodDomainEventHandler.class);

    @EventListener
    public void handleCreated(PaymentMethodCreatedEvent event) {
        log.info("PaymentMethod created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(PaymentMethodUpdatedEvent event) {
        log.info("PaymentMethod updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(PaymentMethodDeletedEvent event) {
        log.info("PaymentMethod deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
