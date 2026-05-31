package com.gogidix.ecommerce.airfreight.infrastructure.messaging.event;

import com.gogidix.ecommerce.airfreight.domain.event.AirFreightCreatedEvent;
import com.gogidix.ecommerce.airfreight.domain.event.AirFreightUpdatedEvent;
import com.gogidix.ecommerce.airfreight.domain.event.AirFreightDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AirFreightDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(AirFreightDomainEventHandler.class);

    @EventListener
    public void handleCreated(AirFreightCreatedEvent event) {
        log.info("AirFreight created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(AirFreightUpdatedEvent event) {
        log.info("AirFreight updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(AirFreightDeletedEvent event) {
        log.info("AirFreight deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
