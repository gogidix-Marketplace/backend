package com.gogidix.sysadmin.accessrequest.domain.port;

import com.gogidix.sysadmin.accessrequest.domain.event.*;

public interface DomainEventPublisher {
    void publishAccessRequestCreated(AccessRequestCreatedEvent event);
    void publishAccessRequestUpdated(AccessRequestUpdatedEvent event);
    void publishAccessRequestDeleted(AccessRequestDeletedEvent event);
}
