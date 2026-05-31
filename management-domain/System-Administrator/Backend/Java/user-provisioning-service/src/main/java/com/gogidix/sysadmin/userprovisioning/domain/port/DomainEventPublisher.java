package com.gogidix.sysadmin.userprovisioning.domain.port;

import com.gogidix.sysadmin.userprovisioning.domain.event.*;

public interface DomainEventPublisher {
    void publishUserAccountCreated(UserAccountCreatedEvent event);
    void publishUserAccountUpdated(UserAccountUpdatedEvent event);
    void publishUserAccountDeleted(UserAccountDeletedEvent event);
}
