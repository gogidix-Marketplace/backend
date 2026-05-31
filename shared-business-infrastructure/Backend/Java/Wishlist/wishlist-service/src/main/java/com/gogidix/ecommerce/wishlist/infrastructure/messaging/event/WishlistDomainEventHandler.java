package com.gogidix.ecommerce.wishlist.infrastructure.messaging.event;

import com.gogidix.ecommerce.wishlist.domain.event.WishlistCreatedEvent;
import com.gogidix.ecommerce.wishlist.domain.event.WishlistUpdatedEvent;
import com.gogidix.ecommerce.wishlist.domain.event.WishlistDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class WishlistDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(WishlistDomainEventHandler.class);

    @EventListener
    public void handleCreated(WishlistCreatedEvent event) {
        log.info("Wishlist created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(WishlistUpdatedEvent event) {
        log.info("Wishlist updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(WishlistDeletedEvent event) {
        log.info("Wishlist deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
