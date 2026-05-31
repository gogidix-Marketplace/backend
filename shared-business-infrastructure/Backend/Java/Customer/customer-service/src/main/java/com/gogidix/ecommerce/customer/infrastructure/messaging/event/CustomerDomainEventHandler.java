package com.gogidix.ecommerce.customer.infrastructure.messaging.event;

import com.gogidix.ecommerce.customer.domain.event.CustomerCreatedEvent;
import com.gogidix.ecommerce.customer.domain.event.CustomerUpdatedEvent;
import com.gogidix.ecommerce.customer.domain.event.CustomerDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CustomerDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomerDomainEventHandler.class);

    @EventListener
    public void handleCreated(CustomerCreatedEvent event) {
        log.info("Customer created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(CustomerUpdatedEvent event) {
        log.info("Customer updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(CustomerDeletedEvent event) {
        log.info("Customer deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
