package com.gogidix.courier.tenantservice.application.service;

import com.gogidix.courier.tenantservice.domain.event.DomainEvent;

/**
 * Interface for publishing domain events.
 */
public interface TenantEventPublisher {

    /**
     * Publish a domain event.
     *
     * @param event the event to publish
     */
    void publish(DomainEvent event);
}
