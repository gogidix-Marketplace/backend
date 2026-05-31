package com.gogidix.customersupport.ticketmanagement.domain.port;

import com.gogidix.customersupport.ticketmanagement.domain.event.*;

public interface DomainEventPublisher {
    void publishTicketCreated(TicketCreatedEvent event);
    void publishTicketUpdated(TicketUpdatedEvent event);
    void publishTicketDeleted(TicketDeletedEvent event);
    void publishTicketAuditLogCreated(TicketAuditLogCreatedEvent event);
    void publishTicketAuditLogUpdated(TicketAuditLogUpdatedEvent event);
    void publishTicketAuditLogDeleted(TicketAuditLogDeletedEvent event);
}
