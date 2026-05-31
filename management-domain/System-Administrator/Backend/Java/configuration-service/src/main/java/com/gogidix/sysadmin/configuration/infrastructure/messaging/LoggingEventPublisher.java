package com.gogidix.sysadmin.configuration.infrastructure.messaging;

import com.gogidix.sysadmin.configuration.domain.event.*;
import com.gogidix.sysadmin.configuration.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishConfigurationCreated(ConfigurationCreatedEvent event) {
        log.info("Configuration created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishConfigurationUpdated(ConfigurationUpdatedEvent event) {
        log.info("Configuration updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishConfigurationDeleted(ConfigurationDeletedEvent event) {
        log.info("Configuration deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
