package com.gogidix.ecommerce.communication.infrastructure.messaging.event;

import com.gogidix.ecommerce.communication.domain.event.CommunicationCreatedEvent;
import com.gogidix.ecommerce.communication.domain.event.CommunicationUpdatedEvent;
import com.gogidix.ecommerce.communication.domain.event.CommunicationDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CommunicationDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(CommunicationDomainEventHandler.class);

    @EventListener
    public void handleCreated(CommunicationCreatedEvent event) {
        log.info("Communication created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(CommunicationUpdatedEvent event) {
        log.info("Communication updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(CommunicationDeletedEvent event) {
        log.info("Communication deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
