package com.gogidix.sysadmin.accesscontrol.domain.port;

import com.gogidix.sysadmin.accesscontrol.domain.event.*;

public interface DomainEventPublisher {
    void publishAccessPolicyCreated(AccessPolicyCreatedEvent event);
    void publishAccessPolicyUpdated(AccessPolicyUpdatedEvent event);
    void publishAccessPolicyDeleted(AccessPolicyDeletedEvent event);
}
