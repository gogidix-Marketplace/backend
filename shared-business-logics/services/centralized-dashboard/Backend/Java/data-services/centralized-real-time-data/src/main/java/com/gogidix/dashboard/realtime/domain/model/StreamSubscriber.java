package com.gogidix.dashboard.realtime.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Stream Subscriber - represents an entity subscribed to a real-time stream
 */
public class StreamSubscriber {
    private final String id;
    private final String name;
    private boolean active;
    private long errorCount;
    private LocalDateTime subscribedAt;
    private LocalDateTime lastActivityAt;

    public StreamSubscriber(String id, String name) {
        this.id = Objects.requireNonNull(id, "Subscriber ID cannot be null");
        this.name = Objects.requireNonNull(name, "Subscriber name cannot be null");
        this.active = false;
        this.errorCount = 0;
        this.subscribedAt = LocalDateTime.now();
        this.lastActivityAt = LocalDateTime.now();
    }

    /**
     * Called when subscriber is added to a stream
     */
    public void onSubscribe(RealTimeStream stream) {
        this.active = true;
        this.subscribedAt = LocalDateTime.now();
        this.lastActivityAt = LocalDateTime.now();
    }

    /**
     * Called when subscriber is removed from a stream
     */
    public void onUnsubscribe(RealTimeStream stream) {
        this.active = false;
    }

    /**
     * Called when a message is successfully delivered
     */
    public void onMessage(StreamMessage message) {
        this.lastActivityAt = LocalDateTime.now();
    }

    /**
     * Called when an error occurs during message delivery
     */
    public void onError(Throwable error) {
        this.errorCount++;
        this.lastActivityAt = LocalDateTime.now();
    }

    /**
     * Check if subscriber is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Get error count
     */
    public long getErrorCount() {
        return errorCount;
    }

    /**
     * Increment error count
     */
    public void incrementErrorCount() {
        this.errorCount++;
    }

    /**
     * Called when the stream stops
     */
    public void onStreamStop(RealTimeStream stream) {
        this.active = false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getSubscribedAt() {
        return subscribedAt;
    }

    public LocalDateTime getLastActivityAt() {
        return lastActivityAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreamSubscriber that = (StreamSubscriber) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("StreamSubscriber{id='%s', name='%s', active=%s}", id, name, active);
    }
}
