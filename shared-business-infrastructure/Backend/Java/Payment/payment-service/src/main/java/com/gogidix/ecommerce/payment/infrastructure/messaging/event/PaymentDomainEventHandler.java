package com.gogidix.ecommerce.payment.infrastructure.messaging.event;

import com.gogidix.ecommerce.payment.domain.event.PaymentCreatedEvent;
import com.gogidix.ecommerce.payment.domain.event.PaymentUpdatedEvent;
import com.gogidix.ecommerce.payment.domain.event.PaymentDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(PaymentDomainEventHandler.class);

    @EventListener
    public void handleCreated(PaymentCreatedEvent event) {
        log.info("Payment created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(PaymentUpdatedEvent event) {
        log.info("Payment updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(PaymentDeletedEvent event) {
        log.info("Payment deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
