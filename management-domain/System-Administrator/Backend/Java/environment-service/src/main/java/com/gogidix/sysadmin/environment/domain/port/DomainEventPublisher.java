package com.gogidix.sysadmin.environment.domain.port;

import com.gogidix.sysadmin.environment.domain.event.*;

public interface DomainEventPublisher {
    void publishEnvironmentCreated(EnvironmentCreatedEvent event);
    void publishEnvironmentUpdated(EnvironmentUpdatedEvent event);
    void publishEnvironmentDeleted(EnvironmentDeletedEvent event);
}
