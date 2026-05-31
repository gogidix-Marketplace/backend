package com.gogidix.sales.notification.domain.port.out;

import com.gogidix.sales.notification.domain.event.NotificationDeliveredEvent;
import com.gogidix.sales.notification.domain.event.NotificationFailedEvent;
import com.gogidix.sales.notification.domain.event.NotificationReadEvent;
import com.gogidix.sales.notification.domain.event.NotificationSentEvent;

import java.util.List;

/**
 * Event Publisher (Output Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    void publish(NotificationSentEvent event);

    void publish(NotificationReadEvent event);

    void publish(NotificationDeliveredEvent event);

    void publish(NotificationFailedEvent event);

    void publishAll(List<Object> events);

    boolean isReady();
}
