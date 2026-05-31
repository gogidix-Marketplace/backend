package com.gogidix.ecommerce.paymentgateway.infrastructure.messaging.event;

import com.gogidix.ecommerce.paymentgateway.domain.event.PaymentGatewayCreatedEvent;
import com.gogidix.ecommerce.paymentgateway.domain.event.PaymentGatewayUpdatedEvent;
import com.gogidix.ecommerce.paymentgateway.domain.event.PaymentGatewayDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(PaymentGatewayDomainEventHandler.class);

    @EventListener
    public void handleCreated(PaymentGatewayCreatedEvent event) {
        log.info("PaymentGateway created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(PaymentGatewayUpdatedEvent event) {
        log.info("PaymentGateway updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(PaymentGatewayDeletedEvent event) {
        log.info("PaymentGateway deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
