package com.gogidix.sysadmin.environment.infrastructure.messaging;

import com.gogidix.sysadmin.environment.domain.event.*;
import com.gogidix.sysadmin.environment.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishEnvironmentCreated(EnvironmentCreatedEvent event) {
        log.info("Environment created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishEnvironmentUpdated(EnvironmentUpdatedEvent event) {
        log.info("Environment updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishEnvironmentDeleted(EnvironmentDeletedEvent event) {
        log.info("Environment deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
