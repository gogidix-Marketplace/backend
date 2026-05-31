package com.gogidix.shared.messaging.domain.handler;

import com.gogidix.shared.messaging.domain.event.DomainEvent;

import java.util.Map;

/**
 * Interface for handling domain events.
 * Implementations process events and manage acknowledgment/rejection.
 *
 * @param <T> The type of domain event this handler processes
 */
public interface EventHandler<T extends DomainEvent> {

    /**
     * Handle the event with the given context
     *
     * @param event   The event to handle
     * @param context The event context
     * @throws EventHandlingException if handling fails
     */
    void handle(T event, EventContext context) throws EventHandlingException;

    /**
     * Get the event type this handler processes
     *
     * @return The event class
     */
    Class<T> getEventType();

    /**
     * Get the priority of this handler (lower = higher priority)
     *
     * @return The priority value
     */
    default int getPriority() {
        return 100;
    }

    /**
     * Check if this handler should always handle events
     *
     * @return true if handler should always process events
     */
    default boolean isAlwaysHandle() {
        return false;
    }

    /**
     * Check if this handler can handle the given event class
     *
     * @param eventClass The event class to check
     * @return true if this handler can process the event
     */
    default boolean canHandle(Class<?> eventClass) {
        return getEventType().isAssignableFrom(eventClass);
    }

    /**
     * Event context providing metadata and control operations
     */
    interface EventContext {

        /**
         * Get the topic this event was received from
         */
        String getTopic();

        /**
         * Get the partition number
         */
        Integer getPartition();

        /**
         * Get the offset of the message
         */
        Long getOffset();

        /**
         * Get the message headers
         */
        Map<String, Object> getHeaders();

        /**
         * Acknowledge successful processing
         */
        void acknowledge();

        /**
         * Reject the message
         */
        void reject();

        /**
         * Reject the message with requeue option
         *
         * @param requeue Whether to requeue the message
         */
        void reject(boolean requeue);

        /**
         * Publish a new event from within the handler
         *
         * @param event The event to publish
         */
        void publishEvent(DomainEvent event);

        /**
         * Create a tracing span
         *
         * @param operationName The operation name
         * @return A closeable span
         */
        AutoCloseable createSpan(String operationName);
    }

    /**
     * Exception thrown when event handling fails
     */
    class EventHandlingException extends Exception {

        private final String eventType;
        private final String eventId;

        public EventHandlingException(String message, String eventType, String eventId) {
            super(message);
            this.eventType = eventType;
            this.eventId = eventId;
        }

        public EventHandlingException(String message, String eventType, String eventId, Throwable cause) {
            super(message, cause);
            this.eventType = eventType;
            this.eventId = eventId;
        }

        public String getEventType() {
            return eventType;
        }

        public String getEventId() {
            return eventId;
        }
    }
}
