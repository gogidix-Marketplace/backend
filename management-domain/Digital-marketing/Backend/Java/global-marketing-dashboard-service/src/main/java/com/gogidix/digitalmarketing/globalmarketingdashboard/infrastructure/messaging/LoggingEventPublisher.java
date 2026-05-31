package com.gogidix.digitalmarketing.globalmarketingdashboard.infrastructure.messaging;

import com.gogidix.digitalmarketing.globalmarketingdashboard.domain.event.*;
import com.gogidix.digitalmarketing.globalmarketingdashboard.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishMarketingDashboardCreated(MarketingDashboardCreatedEvent event) {
        log.info("MarketingDashboard created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMarketingDashboardUpdated(MarketingDashboardUpdatedEvent event) {
        log.info("MarketingDashboard updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMarketingDashboardDeleted(MarketingDashboardDeletedEvent event) {
        log.info("MarketingDashboard deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
