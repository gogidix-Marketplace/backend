package com.gogidix.customersupport.slamanagement.domain.port;

import com.gogidix.customersupport.slamanagement.domain.event.*;

public interface DomainEventPublisher {
    void publishSLABreachCreated(SLABreachCreatedEvent event);
    void publishSLABreachUpdated(SLABreachUpdatedEvent event);
    void publishSLABreachDeleted(SLABreachDeletedEvent event);
    void publishSLAPolicyCreated(SLAPolicyCreatedEvent event);
    void publishSLAPolicyUpdated(SLAPolicyUpdatedEvent event);
    void publishSLAPolicyDeleted(SLAPolicyDeletedEvent event);
}
