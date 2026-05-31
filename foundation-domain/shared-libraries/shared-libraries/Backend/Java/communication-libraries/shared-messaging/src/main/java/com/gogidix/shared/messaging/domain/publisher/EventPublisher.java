package com.gogidix.shared.messaging.domain.publisher;

import com.gogidix.shared.messaging.domain.event.DomainEvent;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * Interface for publishing domain events.
 * Supports synchronous, asynchronous, batch, and scheduled publishing.
 */
public interface EventPublisher {

    /**
     * Publish an event synchronously
     *
     * @param event The event to publish
     * @param <T>   The event type
     */
    <T extends DomainEvent> void publish(T event);

    /**
     * Publish an event asynchronously
     *
     * @param event The event to publish
     * @param <T>   The event type
     * @return A future that completes when publishing is done
     */
    <T extends DomainEvent> CompletableFuture<Void> publishAsync(T event);

    /**
     * Publish an event to a specific topic synchronously
     *
     * @param topic The topic to publish to
     * @param event The event to publish
     * @param <T>   The event type
     */
    <T extends DomainEvent> void publishToTopic(String topic, T event);

    /**
     * Publish an event to a specific topic asynchronously
     *
     * @param topic The topic to publish to
     * @param event The event to publish
     * @param <T>   The event type
     * @return A future that completes when publishing is done
     */
    <T extends DomainEvent> CompletableFuture<Void> publishToTopicAsync(String topic, T event);

    /**
     * Publish an event with delivery options
     *
     * @param event   The event to publish
     * @param options The delivery options
     * @param <T>     The event type
     */
    <T extends DomainEvent> void publish(T event, DeliveryOptions options);

    /**
     * Publish an event with delivery options asynchronously
     *
     * @param event   The event to publish
     * @param options The delivery options
     * @param <T>     The event type
     * @return A future that completes when publishing is done
     */
    <T extends DomainEvent> CompletableFuture<Void> publishAsync(T event, DeliveryOptions options);

    /**
     * Publish a batch of events
     *
     * @param events The events to publish
     */
    void publishBatch(DomainEvent... events);

    /**
     * Publish a batch of events asynchronously
     *
     * @param events The events to publish
     * @return A future that completes when publishing is done
     */
    CompletableFuture<Void> publishBatchAsync(DomainEvent... events);

    /**
     * Publish an event transactionally
     *
     * @param event The event to publish
     * @param <T>   The event type
     */
    <T extends DomainEvent> void publishTransactional(T event);

    /**
     * Publish an event with a delay
     *
     * @param event        The event to publish
     * @param delaySeconds Delay in seconds
     * @param <T>          The event type
     */
    <T extends DomainEvent> void publishDelayed(T event, long delaySeconds);

    /**
     * Schedule an event for a specific time
     *
     * @param event          The event to schedule
     * @param scheduledTime  The time to publish the event
     * @param <T>            The event type
     */
    <T extends DomainEvent> void scheduleEvent(T event, LocalDateTime scheduledTime);

    /**
     * Delivery options for publishing events
     */
    class DeliveryOptions {

        private final DeliveryMode deliveryMode;
        private final int timeoutMs;
        private final int retryCount;
        private final boolean requireAck;

        private DeliveryOptions(DeliveryMode deliveryMode, int timeoutMs, int retryCount, boolean requireAck) {
            this.deliveryMode = deliveryMode;
            this.timeoutMs = timeoutMs;
            this.retryCount = retryCount;
            this.requireAck = requireAck;
        }

        public static DeliveryOptions reliable() {
            return new DeliveryOptions(DeliveryMode.AT_LEAST_ONCE, 5000, 3, true);
        }

        public static DeliveryOptions fastAndLoose() {
            return new DeliveryOptions(DeliveryMode.AT_MOST_ONCE, 1000, 0, false);
        }

        public static DeliveryOptions exactlyOnce() {
            return new DeliveryOptions(DeliveryMode.EXACTLY_ONCE, 10000, 5, true);
        }

        public DeliveryMode getDeliveryMode() {
            return deliveryMode;
        }

        public int getTimeoutMs() {
            return timeoutMs;
        }

        public int getRetryCount() {
            return retryCount;
        }

        public boolean isRequireAck() {
            return requireAck;
        }

        /**
         * Delivery mode enum
         */
        public enum DeliveryMode {
            AT_MOST_ONCE,
            AT_LEAST_ONCE,
            EXACTLY_ONCE
        }
    }
}
