package com.gogidix.sales.crm.domain.port.out;

import java.util.List;

/**
 * Event Publisher (Output Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    void publish(Object event);

    void publishAll(List<Object> events);

    boolean isReady();
}
