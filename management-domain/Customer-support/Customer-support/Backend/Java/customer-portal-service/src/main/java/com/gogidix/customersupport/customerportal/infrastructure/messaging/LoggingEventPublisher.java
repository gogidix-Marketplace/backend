package com.gogidix.customersupport.customerportal.infrastructure.messaging;

import com.gogidix.customersupport.customerportal.domain.event.*;
import com.gogidix.customersupport.customerportal.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishCustomerProfileCreated(CustomerProfileCreatedEvent event) {
        log.info("CustomerProfile created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCustomerProfileUpdated(CustomerProfileUpdatedEvent event) {
        log.info("CustomerProfile updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCustomerProfileDeleted(CustomerProfileDeletedEvent event) {
        log.info("CustomerProfile deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishTicketHistoryCreated(TicketHistoryCreatedEvent event) {
        log.info("TicketHistory created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishTicketHistoryUpdated(TicketHistoryUpdatedEvent event) {
        log.info("TicketHistory updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishTicketHistoryDeleted(TicketHistoryDeletedEvent event) {
        log.info("TicketHistory deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
