package com.gogidix.ecommerce.sms.infrastructure.messaging.event;

import com.gogidix.ecommerce.sms.domain.event.SmsCreatedEvent;
import com.gogidix.ecommerce.sms.domain.event.SmsUpdatedEvent;
import com.gogidix.ecommerce.sms.domain.event.SmsDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SmsDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(SmsDomainEventHandler.class);

    @EventListener
    public void handleCreated(SmsCreatedEvent event) {
        log.info("Sms created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(SmsUpdatedEvent event) {
        log.info("Sms updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(SmsDeletedEvent event) {
        log.info("Sms deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
