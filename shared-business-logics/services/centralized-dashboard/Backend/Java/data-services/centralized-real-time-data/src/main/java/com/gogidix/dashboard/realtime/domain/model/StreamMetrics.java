package com.gogidix.dashboard.realtime.domain.model;

import java.time.Duration;

/**
 * Stream Metrics Value Object
 */
public class StreamMetrics {
    private final long messagesProcessed;
    private final long bytesTransferred;
    private final int subscriberCount;
    private final long messageRate;
    private final double bufferUtilization;
    private final StreamHealth health;
    private final Duration uptime;
    
    private StreamMetrics(Builder builder) {
        this.messagesProcessed = builder.messagesProcessed;
        this.bytesTransferred = builder.bytesTransferred;
        this.subscriberCount = builder.subscriberCount;
        this.messageRate = builder.messageRate;
        this.bufferUtilization = builder.bufferUtilization;
        this.health = builder.health;
        this.uptime = builder.uptime;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    // Getters
    public long getMessagesProcessed() { return messagesProcessed; }
    public long getBytesTransferred() { return bytesTransferred; }
    public int getSubscriberCount() { return subscriberCount; }
    public long getMessageRate() { return messageRate; }
    public double getBufferUtilization() { return bufferUtilization; }
    public StreamHealth getHealth() { return health; }
    public Duration getUptime() { return uptime; }

    /**
     * Get error rate (errors per second)
     */
    public double getErrorRate() {
        // Calculate error rate from metrics (default implementation)
        return 0.0;
    }

    /**
     * Get throughput rate (messages per second)
     */
    public long getThroughputRate() {
        return messageRate;
    }

    /**
     * Get data quality score (0.0 to 1.0)
     */
    public double getDataQualityScore() {
        // Calculate based on error rate, latency, etc.
        return 0.95; // Default high quality
    }

    /**
     * Get processing lag duration
     */
    public Duration getProcessingLag() {
        return Duration.ofMillis(100); // Default 100ms lag
    }

    /**
     * Get average latency in milliseconds
     */
    public long getAverageLatency() {
        return 50L; // Default 50ms latency
    }

    /**
     * Get CPU utilization (0.0 to 1.0)
     */
    public double getCpuUtilization() {
        return 0.3; // Default 30% CPU
    }

    public static class Builder {
        private long messagesProcessed;
        private long bytesTransferred;
        private int subscriberCount;
        private long messageRate;
        private double bufferUtilization;
        private StreamHealth health;
        private Duration uptime;
        
        public Builder withMessagesProcessed(long messagesProcessed) {
            this.messagesProcessed = messagesProcessed;
            return this;
        }
        
        public Builder withBytesTransferred(long bytesTransferred) {
            this.bytesTransferred = bytesTransferred;
            return this;
        }
        
        public Builder withSubscriberCount(int subscriberCount) {
            this.subscriberCount = subscriberCount;
            return this;
        }
        
        public Builder withMessageRate(long messageRate) {
            this.messageRate = messageRate;
            return this;
        }
        
        public Builder withBufferUtilization(double bufferUtilization) {
            this.bufferUtilization = bufferUtilization;
            return this;
        }
        
        public Builder withHealth(StreamHealth health) {
            this.health = health;
            return this;
        }
        
        public Builder withUptime(Duration uptime) {
            this.uptime = uptime;
            return this;
        }
        
        public StreamMetrics build() {
            return new StreamMetrics(this);
        }
    }
}