package com.gogidix.aiservices.intelligenceanalysisservice.domain.port.out;

import com.gogidix.aiservices.intelligenceanalysisservice.domain.event.DomainEvent;

/**
 * Output port for publishing domain events.
 * Abstracts the event publishing mechanism from the domain.
 */
public interface AnalysisEventPublisherPort {

    /**
     * Publish a domain event.
     *
     * @param event the domain event to publish
     */
    void publish(DomainEvent event);

    /**
     * Publish multiple domain events.
     *
     * @param events the domain events to publish
     */
    void publishAll(java.util.List<DomainEvent> events);

    /**
     * Check if the publisher is ready.
     *
     * @return true if ready, false otherwise
     */
    boolean isReady();
}
