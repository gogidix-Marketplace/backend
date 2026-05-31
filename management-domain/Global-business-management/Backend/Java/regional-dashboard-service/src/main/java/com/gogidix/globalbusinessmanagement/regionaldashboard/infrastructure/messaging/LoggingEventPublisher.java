package com.gogidix.globalbusinessmanagement.regionaldashboard.infrastructure.messaging;

import com.gogidix.globalbusinessmanagement.regionaldashboard.domain.event.*;
import com.gogidix.globalbusinessmanagement.regionaldashboard.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishDashboardConfigCreated(DashboardConfigCreatedEvent event) {
        log.info("DashboardConfig created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDashboardConfigUpdated(DashboardConfigUpdatedEvent event) {
        log.info("DashboardConfig updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDashboardConfigDeleted(DashboardConfigDeletedEvent event) {
        log.info("DashboardConfig deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRegionalDashboardCreated(RegionalDashboardCreatedEvent event) {
        log.info("RegionalDashboard created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRegionalDashboardUpdated(RegionalDashboardUpdatedEvent event) {
        log.info("RegionalDashboard updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRegionalDashboardDeleted(RegionalDashboardDeletedEvent event) {
        log.info("RegionalDashboard deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishWidgetCreated(WidgetCreatedEvent event) {
        log.info("Widget created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishWidgetUpdated(WidgetUpdatedEvent event) {
        log.info("Widget updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishWidgetDeleted(WidgetDeletedEvent event) {
        log.info("Widget deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
