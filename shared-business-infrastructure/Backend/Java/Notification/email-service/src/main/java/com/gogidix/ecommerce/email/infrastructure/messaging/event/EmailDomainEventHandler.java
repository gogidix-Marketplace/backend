package com.gogidix.ecommerce.email.infrastructure.messaging.event;

import com.gogidix.ecommerce.email.domain.event.EmailCreatedEvent;
import com.gogidix.ecommerce.email.domain.event.EmailUpdatedEvent;
import com.gogidix.ecommerce.email.domain.event.EmailDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EmailDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(EmailDomainEventHandler.class);

    @EventListener
    public void handleCreated(EmailCreatedEvent event) {
        log.info("Email created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(EmailUpdatedEvent event) {
        log.info("Email updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(EmailDeletedEvent event) {
        log.info("Email deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
