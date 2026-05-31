package com.gogidix.sales.dealmanagement.domain.port.out;

import com.gogidix.sales.dealmanagement.domain.event.DealCreatedEvent;
import com.gogidix.sales.dealmanagement.domain.event.DealLostEvent;
import com.gogidix.sales.dealmanagement.domain.event.DealStageChangedEvent;
import com.gogidix.sales.dealmanagement.domain.event.DealWonEvent;

import java.util.List;

/**
 * Event Publisher (Output Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    void publish(DealCreatedEvent event);

    void publish(DealStageChangedEvent event);

    void publish(DealWonEvent event);

    void publish(DealLostEvent event);

    void publishAll(List<Object> events);

    boolean isReady();
}
