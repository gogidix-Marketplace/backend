package com.gogidix.sysadmin.securitymonitoring.domain.port;

import com.gogidix.sysadmin.securitymonitoring.domain.event.*;

public interface DomainEventPublisher {
    void publishSecurityEventCreated(SecurityEventCreatedEvent event);
    void publishSecurityEventUpdated(SecurityEventUpdatedEvent event);
    void publishSecurityEventDeleted(SecurityEventDeletedEvent event);
}
