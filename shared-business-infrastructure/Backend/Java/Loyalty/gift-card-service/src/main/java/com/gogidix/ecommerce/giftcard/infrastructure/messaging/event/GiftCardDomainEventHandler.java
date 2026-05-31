package com.gogidix.ecommerce.giftcard.infrastructure.messaging.event;

import com.gogidix.ecommerce.giftcard.domain.event.GiftCardCreatedEvent;
import com.gogidix.ecommerce.giftcard.domain.event.GiftCardUpdatedEvent;
import com.gogidix.ecommerce.giftcard.domain.event.GiftCardDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class GiftCardDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(GiftCardDomainEventHandler.class);

    @EventListener
    public void handleCreated(GiftCardCreatedEvent event) {
        log.info("GiftCard created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(GiftCardUpdatedEvent event) {
        log.info("GiftCard updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(GiftCardDeletedEvent event) {
        log.info("GiftCard deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
