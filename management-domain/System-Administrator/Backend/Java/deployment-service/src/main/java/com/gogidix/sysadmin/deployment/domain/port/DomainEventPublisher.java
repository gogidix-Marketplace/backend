package com.gogidix.sysadmin.deployment.domain.port;

import com.gogidix.sysadmin.deployment.domain.event.*;

public interface DomainEventPublisher {
    void publishDeploymentCreated(DeploymentCreatedEvent event);
    void publishDeploymentUpdated(DeploymentUpdatedEvent event);
    void publishDeploymentDeleted(DeploymentDeletedEvent event);
}
