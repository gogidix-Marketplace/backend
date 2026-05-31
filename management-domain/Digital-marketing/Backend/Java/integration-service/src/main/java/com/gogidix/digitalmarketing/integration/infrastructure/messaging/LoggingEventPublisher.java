package com.gogidix.digitalmarketing.integration.infrastructure.messaging;

import com.gogidix.digitalmarketing.integration.domain.event.*;
import com.gogidix.digitalmarketing.integration.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishIntegrationCreated(IntegrationCreatedEvent event) {
        log.info("Integration created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishIntegrationUpdated(IntegrationUpdatedEvent event) {
        log.info("Integration updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishIntegrationDeleted(IntegrationDeletedEvent event) {
        log.info("Integration deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
