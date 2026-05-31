package com.gogidix.sysadmin.incidentmanagement.domain.port;

import com.gogidix.sysadmin.incidentmanagement.domain.event.*;

public interface DomainEventPublisher {
    void publishIncidentCreated(IncidentCreatedEvent event);
    void publishIncidentUpdated(IncidentUpdatedEvent event);
    void publishIncidentDeleted(IncidentDeletedEvent event);
}
