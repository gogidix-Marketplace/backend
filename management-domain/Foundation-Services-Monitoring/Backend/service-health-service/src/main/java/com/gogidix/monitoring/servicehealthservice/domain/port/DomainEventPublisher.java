package com.gogidix.monitoring.servicehealthservice.domain.port;

import com.gogidix.monitoring.servicehealthservice.domain.event.*;

public interface DomainEventPublisher {
    void publishServiceDependencyCreated(ServiceDependencyCreatedEvent event);
    void publishServiceDependencyUpdated(ServiceDependencyUpdatedEvent event);
    void publishServiceDependencyDeleted(ServiceDependencyDeletedEvent event);
    void publishServiceHealthStatusCreated(ServiceHealthStatusCreatedEvent event);
    void publishServiceHealthStatusUpdated(ServiceHealthStatusUpdatedEvent event);
    void publishServiceHealthStatusDeleted(ServiceHealthStatusDeletedEvent event);
    void publishServiceUptimeCreated(ServiceUptimeCreatedEvent event);
    void publishServiceUptimeUpdated(ServiceUptimeUpdatedEvent event);
    void publishServiceUptimeDeleted(ServiceUptimeDeletedEvent event);
}
