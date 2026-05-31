package com.gogidix.sysadmin.accesscontrol.infrastructure.messaging;

import com.gogidix.sysadmin.accesscontrol.domain.event.*;
import com.gogidix.sysadmin.accesscontrol.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishAccessPolicyCreated(AccessPolicyCreatedEvent event) {
        log.info("AccessPolicy created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAccessPolicyUpdated(AccessPolicyUpdatedEvent event) {
        log.info("AccessPolicy updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAccessPolicyDeleted(AccessPolicyDeletedEvent event) {
        log.info("AccessPolicy deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
