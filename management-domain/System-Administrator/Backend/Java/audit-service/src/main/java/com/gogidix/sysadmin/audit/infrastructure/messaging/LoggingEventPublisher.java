package com.gogidix.sysadmin.audit.infrastructure.messaging;

import com.gogidix.sysadmin.audit.domain.event.*;
import com.gogidix.sysadmin.audit.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishAuditLogCreated(AuditLogCreatedEvent event) {
        log.info("AuditLog created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAuditLogUpdated(AuditLogUpdatedEvent event) {
        log.info("AuditLog updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAuditLogDeleted(AuditLogDeletedEvent event) {
        log.info("AuditLog deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
