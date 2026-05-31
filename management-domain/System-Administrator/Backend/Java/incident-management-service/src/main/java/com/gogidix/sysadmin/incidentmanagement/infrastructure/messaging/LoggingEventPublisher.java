package com.gogidix.sysadmin.incidentmanagement.infrastructure.messaging;

import com.gogidix.sysadmin.incidentmanagement.domain.event.*;
import com.gogidix.sysadmin.incidentmanagement.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishIncidentCreated(IncidentCreatedEvent event) {
        log.info("Incident created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishIncidentUpdated(IncidentUpdatedEvent event) {
        log.info("Incident updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishIncidentDeleted(IncidentDeletedEvent event) {
        log.info("Incident deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
