package com.gogidix.ecommerce.discount.infrastructure.messaging.event;

import com.gogidix.ecommerce.discount.domain.event.DiscountCreatedEvent;
import com.gogidix.ecommerce.discount.domain.event.DiscountUpdatedEvent;
import com.gogidix.ecommerce.discount.domain.event.DiscountDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DiscountDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(DiscountDomainEventHandler.class);

    @EventListener
    public void handleCreated(DiscountCreatedEvent event) {
        log.info("Discount created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(DiscountUpdatedEvent event) {
        log.info("Discount updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(DiscountDeletedEvent event) {
        log.info("Discount deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
