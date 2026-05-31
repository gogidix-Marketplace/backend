package com.gogidix.sysadmin.configuration.domain.port;

import com.gogidix.sysadmin.configuration.domain.event.*;

public interface DomainEventPublisher {
    void publishConfigurationCreated(ConfigurationCreatedEvent event);
    void publishConfigurationUpdated(ConfigurationUpdatedEvent event);
    void publishConfigurationDeleted(ConfigurationDeletedEvent event);
}
