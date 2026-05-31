package com.gogidix.courier.assignmentservice.application.service;

import com.gogidix.courier.assignmentservice.domain.event.DomainEvent;

/**
 * Interface for publishing assignment domain events.
 */
public interface AssignmentEventPublisher {

    /**
     * Publish a domain event.
     *
     * @param event the event to publish
     */
    void publish(DomainEvent event);

    /**
     * Publish multiple domain events.
     *
     * @param events the events to publish
     */
    void publishAll(java.util.List<DomainEvent> events);
}
