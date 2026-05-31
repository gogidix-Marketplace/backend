package com.gogidix.ecommerce.loyalty.infrastructure.messaging.event;

import com.gogidix.ecommerce.loyalty.domain.event.LoyaltyCreatedEvent;
import com.gogidix.ecommerce.loyalty.domain.event.LoyaltyUpdatedEvent;
import com.gogidix.ecommerce.loyalty.domain.event.LoyaltyDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(LoyaltyDomainEventHandler.class);

    @EventListener
    public void handleCreated(LoyaltyCreatedEvent event) {
        log.info("Loyalty created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(LoyaltyUpdatedEvent event) {
        log.info("Loyalty updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(LoyaltyDeletedEvent event) {
        log.info("Loyalty deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
