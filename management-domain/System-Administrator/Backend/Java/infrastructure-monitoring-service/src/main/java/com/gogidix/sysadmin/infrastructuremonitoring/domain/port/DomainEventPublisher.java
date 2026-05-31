package com.gogidix.sysadmin.infrastructuremonitoring.domain.port;

import com.gogidix.sysadmin.infrastructuremonitoring.domain.event.*;

public interface DomainEventPublisher {
    void publishInfrastructureMonitoringCreated(InfrastructureMonitoringCreatedEvent event);
    void publishInfrastructureMonitoringUpdated(InfrastructureMonitoringUpdatedEvent event);
    void publishInfrastructureMonitoringDeleted(InfrastructureMonitoringDeletedEvent event);
    void publishMonitoringAlertCreated(MonitoringAlertCreatedEvent event);
    void publishMonitoringAlertUpdated(MonitoringAlertUpdatedEvent event);
    void publishMonitoringAlertDeleted(MonitoringAlertDeletedEvent event);
}
