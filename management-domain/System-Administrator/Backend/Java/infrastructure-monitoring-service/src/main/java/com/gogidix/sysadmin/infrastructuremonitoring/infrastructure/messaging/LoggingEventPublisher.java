package com.gogidix.sysadmin.infrastructuremonitoring.infrastructure.messaging;

import com.gogidix.sysadmin.infrastructuremonitoring.domain.event.*;
import com.gogidix.sysadmin.infrastructuremonitoring.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishInfrastructureMonitoringCreated(InfrastructureMonitoringCreatedEvent event) {
        log.info("InfrastructureMonitoring created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishInfrastructureMonitoringUpdated(InfrastructureMonitoringUpdatedEvent event) {
        log.info("InfrastructureMonitoring updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishInfrastructureMonitoringDeleted(InfrastructureMonitoringDeletedEvent event) {
        log.info("InfrastructureMonitoring deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMonitoringAlertCreated(MonitoringAlertCreatedEvent event) {
        log.info("MonitoringAlert created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMonitoringAlertUpdated(MonitoringAlertUpdatedEvent event) {
        log.info("MonitoringAlert updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMonitoringAlertDeleted(MonitoringAlertDeletedEvent event) {
        log.info("MonitoringAlert deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
