package com.gogidix.courier.gpstrackingservice.application.service;

import com.gogidix.courier.gpstrackingservice.domain.event.DomainEvent;

/**
 * Event publisher for GPS tracking domain events.
 */
public interface GpsTrackingEventPublisher {

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
