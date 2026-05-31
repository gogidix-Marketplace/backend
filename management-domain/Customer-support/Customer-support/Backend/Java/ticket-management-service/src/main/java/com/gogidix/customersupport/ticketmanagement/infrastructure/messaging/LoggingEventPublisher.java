package com.gogidix.customersupport.ticketmanagement.infrastructure.messaging;

import com.gogidix.customersupport.ticketmanagement.domain.event.*;
import com.gogidix.customersupport.ticketmanagement.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishTicketCreated(TicketCreatedEvent event) {
        log.info("Ticket created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishTicketUpdated(TicketUpdatedEvent event) {
        log.info("Ticket updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishTicketDeleted(TicketDeletedEvent event) {
        log.info("Ticket deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishTicketAuditLogCreated(TicketAuditLogCreatedEvent event) {
        log.info("TicketAuditLog created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishTicketAuditLogUpdated(TicketAuditLogUpdatedEvent event) {
        log.info("TicketAuditLog updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishTicketAuditLogDeleted(TicketAuditLogDeletedEvent event) {
        log.info("TicketAuditLog deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
