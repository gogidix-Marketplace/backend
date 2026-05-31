package com.gogidix.monitoring.servicehealthservice.infrastructure.messaging;

import com.gogidix.monitoring.servicehealthservice.domain.event.*;
import com.gogidix.monitoring.servicehealthservice.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishServiceDependencyCreated(ServiceDependencyCreatedEvent event) {
        log.info("ServiceDependency created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishServiceDependencyUpdated(ServiceDependencyUpdatedEvent event) {
        log.info("ServiceDependency updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishServiceDependencyDeleted(ServiceDependencyDeletedEvent event) {
        log.info("ServiceDependency deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishServiceHealthStatusCreated(ServiceHealthStatusCreatedEvent event) {
        log.info("ServiceHealthStatus created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishServiceHealthStatusUpdated(ServiceHealthStatusUpdatedEvent event) {
        log.info("ServiceHealthStatus updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishServiceHealthStatusDeleted(ServiceHealthStatusDeletedEvent event) {
        log.info("ServiceHealthStatus deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishServiceUptimeCreated(ServiceUptimeCreatedEvent event) {
        log.info("ServiceUptime created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishServiceUptimeUpdated(ServiceUptimeUpdatedEvent event) {
        log.info("ServiceUptime updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishServiceUptimeDeleted(ServiceUptimeDeletedEvent event) {
        log.info("ServiceUptime deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
