package com.gogidix.ecommerce.storecredit.infrastructure.messaging.event;

import com.gogidix.ecommerce.storecredit.domain.event.StoreCreditCreatedEvent;
import com.gogidix.ecommerce.storecredit.domain.event.StoreCreditUpdatedEvent;
import com.gogidix.ecommerce.storecredit.domain.event.StoreCreditDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StoreCreditDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(StoreCreditDomainEventHandler.class);

    @EventListener
    public void handleCreated(StoreCreditCreatedEvent event) {
        log.info("StoreCredit created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(StoreCreditUpdatedEvent event) {
        log.info("StoreCredit updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(StoreCreditDeletedEvent event) {
        log.info("StoreCredit deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
