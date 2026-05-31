package com.gogidix.ecommerce.oceanshipping.infrastructure.messaging.event;

import com.gogidix.ecommerce.oceanshipping.domain.event.OceanShippingCreatedEvent;
import com.gogidix.ecommerce.oceanshipping.domain.event.OceanShippingUpdatedEvent;
import com.gogidix.ecommerce.oceanshipping.domain.event.OceanShippingDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OceanShippingDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(OceanShippingDomainEventHandler.class);

    @EventListener
    public void handleCreated(OceanShippingCreatedEvent event) {
        log.info("OceanShipping created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(OceanShippingUpdatedEvent event) {
        log.info("OceanShipping updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(OceanShippingDeletedEvent event) {
        log.info("OceanShipping deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
