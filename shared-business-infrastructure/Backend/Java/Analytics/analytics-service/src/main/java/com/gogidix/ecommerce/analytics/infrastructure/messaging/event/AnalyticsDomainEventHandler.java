package com.gogidix.ecommerce.analytics.infrastructure.messaging.event;

import com.gogidix.ecommerce.analytics.domain.event.AnalyticsCreatedEvent;
import com.gogidix.ecommerce.analytics.domain.event.AnalyticsUpdatedEvent;
import com.gogidix.ecommerce.analytics.domain.event.AnalyticsDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsDomainEventHandler.class);

    @EventListener
    public void handleCreated(AnalyticsCreatedEvent event) {
        log.info("Analytics created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(AnalyticsUpdatedEvent event) {
        log.info("Analytics updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(AnalyticsDeletedEvent event) {
        log.info("Analytics deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
