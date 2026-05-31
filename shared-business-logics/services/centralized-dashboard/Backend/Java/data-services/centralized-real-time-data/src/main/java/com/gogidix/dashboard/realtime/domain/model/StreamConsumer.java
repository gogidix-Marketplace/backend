package com.gogidix.dashboard.realtime.domain.model;

/**
 * Stream Consumer - consumes messages from a real-time stream
 */
public class StreamConsumer {
    private final String consumerId;
    private final String consumerGroup;
    private boolean active;

    public StreamConsumer(String consumerId, String consumerGroup) {
        this.consumerId = consumerId;
        this.consumerGroup = consumerGroup;
        this.active = false;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public boolean isActive() {
        return active;
    }

    /**
     * Check if consumer is healthy
     */
    public boolean isHealthy() {
        return active;
    }

    public void start() {
        this.active = true;
    }

    public void stop() {
        this.active = false;
    }

    public void consume(StreamMessage message) {
        // Consumption logic
    }
}
