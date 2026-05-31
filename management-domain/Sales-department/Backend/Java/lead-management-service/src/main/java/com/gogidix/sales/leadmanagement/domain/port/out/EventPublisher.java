package com.gogidix.sales.leadmanagement.domain.port.out;

import com.gogidix.sales.leadmanagement.domain.event.*;

import java.util.List;

/**
 * Event Publisher (Output Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    void publish(LeadCreatedEvent event);

    void publish(LeadAssignedEvent event);

    void publish(LeadQualifiedEvent event);

    void publish(LeadConvertedEvent event);

    void publish(LeadStageChangedEvent event);

    void publish(LeadLostEvent event);

    void publishAll(List<Object> events);

    boolean isReady();
}
