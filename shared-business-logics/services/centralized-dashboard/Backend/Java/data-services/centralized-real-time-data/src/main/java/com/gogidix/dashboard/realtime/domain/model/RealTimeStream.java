package com.gogidix.dashboard.realtime.domain.model;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Real-Time Stream Domain Entity
 * 
 * Core business entity for real-time data streaming
 * Implements WebSocket streaming logic and backpressure management
 */
public class RealTimeStream {
    
    private final StreamId id;
    private final String streamName;
    private final StreamType streamType;
    private final StreamConfiguration configuration;
    private final LocalDateTime createdAt;
    private StreamStatus status;
    private final AtomicLong messagesProcessed;
    private final AtomicLong bytesTransferred;
    private final Map<String, StreamSubscriber> subscribers;
    private LocalDateTime lastActivityAt;
    private StreamHealth health;
    
    private RealTimeStream(Builder builder) {
        this.id = Objects.requireNonNull(builder.id, "Stream ID is required");
        this.streamName = Objects.requireNonNull(builder.streamName, "Stream name is required");
        this.streamType = Objects.requireNonNull(builder.streamType, "Stream type is required");
        this.configuration = Objects.requireNonNull(builder.configuration, "Configuration is required");
        this.createdAt = LocalDateTime.now();
        this.status = StreamStatus.INITIALIZING;
        this.messagesProcessed = new AtomicLong(0);
        this.bytesTransferred = new AtomicLong(0);
        this.subscribers = new ConcurrentHashMap<>();
        this.lastActivityAt = LocalDateTime.now();
        this.health = calculateHealth();
    }
    
    /**
     * Process incoming real-time message
     */
    public StreamMessage processMessage(Map<String, Object> data) {
        validateStreamActive();
        
        StreamMessage message = StreamMessage.builder()
            .withStreamId(id)
            .withData(data)
            .withTimestamp(LocalDateTime.now())
            .withSequenceNumber(messagesProcessed.incrementAndGet())
            .build();
        
        // Apply transformations
        if (configuration.hasTransformations()) {
            message = configuration.applyTransformations(message);
        }
        
        // Apply filters
        if (configuration.hasFilters() && !configuration.passesFilters(message)) {
            return null;
        }
        
        // Update metrics
        bytesTransferred.addAndGet(message.getPayloadSize());
        lastActivityAt = LocalDateTime.now();
        
        // Broadcast to subscribers
        broadcastToSubscribers(message);
        
        return message;
    }
    
    /**
     * Add subscriber to stream
     */
    public void addSubscriber(StreamSubscriber subscriber) {
        Objects.requireNonNull(subscriber, "Subscriber cannot be null");
        
        if (subscribers.size() >= configuration.getMaxSubscribers()) {
            throw new IllegalStateException("Maximum subscribers limit reached: " + 
                configuration.getMaxSubscribers());
        }
        
        subscribers.put(subscriber.getId(), subscriber);
        subscriber.onSubscribe(this);
    }
    
    /**
     * Remove subscriber from stream
     */
    public void removeSubscriber(String subscriberId) {
        StreamSubscriber subscriber = subscribers.remove(subscriberId);
        if (subscriber != null) {
            subscriber.onUnsubscribe(this);
        }
    }
    
    /**
     * Broadcast message to all subscribers
     */
    private void broadcastToSubscribers(StreamMessage message) {
        subscribers.values().parallelStream().forEach(subscriber -> {
            try {
                if (subscriber.isActive()) {
                    subscriber.onMessage(message);
                }
            } catch (Exception e) {
                handleSubscriberError(subscriber, e);
            }
        });
    }
    
    /**
     * Handle subscriber error
     */
    private void handleSubscriberError(StreamSubscriber subscriber, Exception error) {
        subscriber.incrementErrorCount();
        
        if (subscriber.getErrorCount() > configuration.getMaxErrorsPerSubscriber()) {
            removeSubscriber(subscriber.getId());
        }
    }
    
    /**
     * Check if stream requires backpressure
     */
    public boolean requiresBackpressure() {
        long currentRate = calculateMessageRate();
        return currentRate > configuration.getMaxMessagesPerSecond() ||
               getBufferUtilization() > 0.8;
    }
    
    /**
     * Calculate current message rate
     */
    public long calculateMessageRate() {
        Duration uptime = Duration.between(createdAt, LocalDateTime.now());
        if (uptime.getSeconds() == 0) return 0;
        return messagesProcessed.get() / uptime.getSeconds();
    }
    
    /**
     * Get buffer utilization percentage
     */
    public double getBufferUtilization() {
        // Simulated buffer calculation
        long currentMessages = messagesProcessed.get();
        long maxBuffer = configuration.getBufferSize();
        return Math.min(1.0, (double) currentMessages / maxBuffer);
    }
    
    /**
     * Calculate stream health
     */
    private StreamHealth calculateHealth() {
        if (status != StreamStatus.ACTIVE) {
            return StreamHealth.DEGRADED;
        }
        
        // Check for stale stream
        Duration inactivity = Duration.between(lastActivityAt, LocalDateTime.now());
        if (inactivity.compareTo(configuration.getMaxInactivityDuration()) > 0) {
            return StreamHealth.UNHEALTHY;
        }
        
        // Check error rate
        long errorRate = calculateErrorRate();
        if (errorRate > configuration.getMaxErrorRate()) {
            return StreamHealth.DEGRADED;
        }
        
        // Check backpressure
        if (requiresBackpressure()) {
            return StreamHealth.WARNING;
        }
        
        return StreamHealth.HEALTHY;
    }
    
    /**
     * Calculate error rate across all subscribers
     */
    private long calculateErrorRate() {
        return subscribers.values().stream()
            .mapToLong(StreamSubscriber::getErrorCount)
            .sum();
    }
    
    /**
     * Validate stream is active
     */
    private void validateStreamActive() {
        if (status != StreamStatus.ACTIVE) {
            throw new IllegalStateException("Stream is not active: " + status);
        }
    }
    
    /**
     * Start the stream
     */
    public void start() {
        if (status != StreamStatus.INITIALIZING && status != StreamStatus.STOPPED) {
            throw new IllegalStateException("Cannot start stream in status: " + status);
        }
        status = StreamStatus.ACTIVE;
        lastActivityAt = LocalDateTime.now();
    }
    
    /**
     * Stop the stream
     */
    public void stop() {
        status = StreamStatus.STOPPED;
        subscribers.values().forEach(subscriber -> subscriber.onStreamStop(this));
        subscribers.clear();
    }
    
    /**
     * Pause the stream
     */
    public void pause() {
        if (status != StreamStatus.ACTIVE) {
            throw new IllegalStateException("Cannot pause inactive stream");
        }
        status = StreamStatus.PAUSED;
    }
    
    /**
     * Resume the stream
     */
    public void resume() {
        if (status != StreamStatus.PAUSED) {
            throw new IllegalStateException("Cannot resume non-paused stream");
        }
        status = StreamStatus.ACTIVE;
        lastActivityAt = LocalDateTime.now();
    }
    
    /**
     * Get stream metrics
     */
    public StreamMetrics getMetrics() {
        return StreamMetrics.builder()
            .withMessagesProcessed(messagesProcessed.get())
            .withBytesTransferred(bytesTransferred.get())
            .withSubscriberCount(subscribers.size())
            .withMessageRate(calculateMessageRate())
            .withBufferUtilization(getBufferUtilization())
            .withHealth(calculateHealth())
            .withUptime(Duration.between(createdAt, LocalDateTime.now()))
            .build();
    }
    
    // Getters
    public StreamId getId() { return id; }
    public String getStreamName() { return streamName; }
    public StreamType getStreamType() { return streamType; }
    public StreamConfiguration getConfiguration() { return configuration; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public StreamStatus getStatus() { return status; }
    public long getMessagesProcessed() { return messagesProcessed.get(); }
    public long getBytesTransferred() { return bytesTransferred.get(); }
    public int getSubscriberCount() { return subscribers.size(); }
    public LocalDateTime getLastActivityAt() { return lastActivityAt; }
    public StreamHealth getHealth() { return calculateHealth(); }
    
    // Builder
    public static class Builder {
        private StreamId id;
        private String streamName;
        private StreamType streamType;
        private StreamConfiguration configuration;
        
        public Builder withId(StreamId id) {
            this.id = id;
            return this;
        }
        
        public Builder withStreamName(String streamName) {
            this.streamName = streamName;
            return this;
        }
        
        public Builder withStreamType(StreamType streamType) {
            this.streamType = streamType;
            return this;
        }
        
        public Builder withConfiguration(StreamConfiguration configuration) {
            this.configuration = configuration;
            return this;
        }
        
        public RealTimeStream build() {
            return new RealTimeStream(this);
        }
    }
}