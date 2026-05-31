package com.gogidix.sales.communication.domain.port.out;

import com.gogidix.sales.communication.domain.event.ConversationCreatedEvent;
import com.gogidix.sales.communication.domain.event.ConversationUpdatedEvent;
import com.gogidix.sales.communication.domain.event.MessageReadEvent;
import com.gogidix.sales.communication.domain.event.MessageSentEvent;

import java.util.List;

/**
 * Event Publisher Interface (Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    /**
     * Publishes a message event
     */
    void publish(MessageSentEvent event);

    /**
     * Publishes a message read event
     */
    void publish(MessageReadEvent event);

    /**
     * Publishes a conversation created event
     */
    void publish(ConversationCreatedEvent event);

    /**
     * Publishes a conversation updated event
     */
    void publish(ConversationUpdatedEvent event);

    /**
     * Publishes multiple events
     */
    void publishAll(List<Object> events);

    /**
     * Checks if the publisher is ready
     */
    boolean isReady();
}
