package com.gogidix.sysadmin.securitymonitoring.infrastructure.messaging;

import com.gogidix.sysadmin.securitymonitoring.domain.event.*;
import com.gogidix.sysadmin.securitymonitoring.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishSecurityEventCreated(SecurityEventCreatedEvent event) {
        log.info("SecurityEvent created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSecurityEventUpdated(SecurityEventUpdatedEvent event) {
        log.info("SecurityEvent updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSecurityEventDeleted(SecurityEventDeletedEvent event) {
        log.info("SecurityEvent deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
