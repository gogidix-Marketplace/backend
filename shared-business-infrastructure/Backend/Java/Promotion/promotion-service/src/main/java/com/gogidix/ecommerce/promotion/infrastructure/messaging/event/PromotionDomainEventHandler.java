package com.gogidix.ecommerce.promotion.infrastructure.messaging.event;

import com.gogidix.ecommerce.promotion.domain.event.PromotionCreatedEvent;
import com.gogidix.ecommerce.promotion.domain.event.PromotionUpdatedEvent;
import com.gogidix.ecommerce.promotion.domain.event.PromotionDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PromotionDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(PromotionDomainEventHandler.class);

    @EventListener
    public void handleCreated(PromotionCreatedEvent event) {
        log.info("Promotion created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(PromotionUpdatedEvent event) {
        log.info("Promotion updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(PromotionDeletedEvent event) {
        log.info("Promotion deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
