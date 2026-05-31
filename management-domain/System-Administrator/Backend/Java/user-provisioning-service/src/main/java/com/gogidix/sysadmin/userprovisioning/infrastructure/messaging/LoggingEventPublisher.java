package com.gogidix.sysadmin.userprovisioning.infrastructure.messaging;

import com.gogidix.sysadmin.userprovisioning.domain.event.*;
import com.gogidix.sysadmin.userprovisioning.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishUserAccountCreated(UserAccountCreatedEvent event) {
        log.info("UserAccount created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishUserAccountUpdated(UserAccountUpdatedEvent event) {
        log.info("UserAccount updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishUserAccountDeleted(UserAccountDeletedEvent event) {
        log.info("UserAccount deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
