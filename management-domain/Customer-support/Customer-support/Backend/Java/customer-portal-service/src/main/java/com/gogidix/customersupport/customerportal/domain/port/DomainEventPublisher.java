package com.gogidix.customersupport.customerportal.domain.port;

import com.gogidix.customersupport.customerportal.domain.event.*;

public interface DomainEventPublisher {
    void publishCustomerProfileCreated(CustomerProfileCreatedEvent event);
    void publishCustomerProfileUpdated(CustomerProfileUpdatedEvent event);
    void publishCustomerProfileDeleted(CustomerProfileDeletedEvent event);
    void publishTicketHistoryCreated(TicketHistoryCreatedEvent event);
    void publishTicketHistoryUpdated(TicketHistoryUpdatedEvent event);
    void publishTicketHistoryDeleted(TicketHistoryDeletedEvent event);
}
