package com.gogidix.customersupport.slamanagement.infrastructure.messaging;

import com.gogidix.customersupport.slamanagement.domain.event.*;
import com.gogidix.customersupport.slamanagement.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishSLABreachCreated(SLABreachCreatedEvent event) {
        log.info("SLABreach created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSLABreachUpdated(SLABreachUpdatedEvent event) {
        log.info("SLABreach updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSLABreachDeleted(SLABreachDeletedEvent event) {
        log.info("SLABreach deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSLAPolicyCreated(SLAPolicyCreatedEvent event) {
        log.info("SLAPolicy created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSLAPolicyUpdated(SLAPolicyUpdatedEvent event) {
        log.info("SLAPolicy updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSLAPolicyDeleted(SLAPolicyDeletedEvent event) {
        log.info("SLAPolicy deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
