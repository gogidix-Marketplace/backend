package com.gogidix.customersupport.phonesupport.infrastructure.messaging;

import com.gogidix.customersupport.phonesupport.domain.event.*;
import com.gogidix.customersupport.phonesupport.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishCallQueueCreated(CallQueueCreatedEvent event) {
        log.info("CallQueue created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCallQueueUpdated(CallQueueUpdatedEvent event) {
        log.info("CallQueue updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCallQueueDeleted(CallQueueDeletedEvent event) {
        log.info("CallQueue deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPhoneCallCreated(PhoneCallCreatedEvent event) {
        log.info("PhoneCall created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPhoneCallUpdated(PhoneCallUpdatedEvent event) {
        log.info("PhoneCall updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPhoneCallDeleted(PhoneCallDeletedEvent event) {
        log.info("PhoneCall deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
