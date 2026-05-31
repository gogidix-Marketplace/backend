package com.gogidix.digitalmarketing.integration.domain.port;

import com.gogidix.digitalmarketing.integration.domain.event.*;

public interface DomainEventPublisher {
    void publishIntegrationCreated(IntegrationCreatedEvent event);
    void publishIntegrationUpdated(IntegrationUpdatedEvent event);
    void publishIntegrationDeleted(IntegrationDeletedEvent event);
}
