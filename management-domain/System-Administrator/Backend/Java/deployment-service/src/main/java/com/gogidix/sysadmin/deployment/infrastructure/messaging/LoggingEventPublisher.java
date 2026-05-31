package com.gogidix.sysadmin.deployment.infrastructure.messaging;

import com.gogidix.sysadmin.deployment.domain.event.*;
import com.gogidix.sysadmin.deployment.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishDeploymentCreated(DeploymentCreatedEvent event) {
        log.info("Deployment created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDeploymentUpdated(DeploymentUpdatedEvent event) {
        log.info("Deployment updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDeploymentDeleted(DeploymentDeletedEvent event) {
        log.info("Deployment deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
