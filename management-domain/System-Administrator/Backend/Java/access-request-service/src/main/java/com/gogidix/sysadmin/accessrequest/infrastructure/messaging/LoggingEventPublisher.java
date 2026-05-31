package com.gogidix.sysadmin.accessrequest.infrastructure.messaging;

import com.gogidix.sysadmin.accessrequest.domain.event.*;
import com.gogidix.sysadmin.accessrequest.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishAccessRequestCreated(AccessRequestCreatedEvent event) {
        log.info("AccessRequest created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAccessRequestUpdated(AccessRequestUpdatedEvent event) {
        log.info("AccessRequest updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAccessRequestDeleted(AccessRequestDeletedEvent event) {
        log.info("AccessRequest deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
