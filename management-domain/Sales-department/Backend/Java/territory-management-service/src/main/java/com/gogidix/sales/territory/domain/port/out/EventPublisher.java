package com.gogidix.sales.territory.domain.port.out;

import com.gogidix.sales.territory.domain.event.QuotaUpdatedEvent;
import com.gogidix.sales.territory.domain.event.TerritoryAssignedEvent;
import com.gogidix.sales.territory.domain.event.TerritoryCreatedEvent;

import java.util.List;

/**
 * Event Publisher Interface (Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    /**
     * Publishes a territory event
     */
    void publishTerritoryEvent(TerritoryCreatedEvent event);

    /**
     * Publishes a territory assignment event
     */
    void publishAssignmentEvent(TerritoryAssignedEvent event);

    /**
     * Publishes a quota event
     */
    void publishQuotaEvent(QuotaUpdatedEvent event);

    /**
     * Publishes multiple events
     */
    void publishAll(List<?> events);

    /**
     * Checks if the publisher is ready
     */
    boolean isReady();
}
