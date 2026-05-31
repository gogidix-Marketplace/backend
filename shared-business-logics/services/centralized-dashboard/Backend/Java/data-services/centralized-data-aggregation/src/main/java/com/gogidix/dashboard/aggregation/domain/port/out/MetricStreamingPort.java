package com.gogidix.dashboard.aggregation.domain.port.out;

import com.gogidix.dashboard.aggregation.domain.model.*;
import java.util.function.Consumer;

/**
 * Metric Streaming Port - Output Port
 * 
 * Defines the contract for real-time metric streaming operations
 * following hexagonal architecture principles
 */
public interface MetricStreamingPort {
    
    /**
     * Start streaming aggregations
     */
    void startStreaming(String streamId, String metricName, TimeGranularity granularity,
                       AggregationFunction function, Consumer<AggregatedMetric> callback);
    
    /**
     * Stop streaming aggregations
     */
    void stopStreaming(String streamId);
    
    /**
     * Check if stream is active
     */
    boolean isStreamActive(String streamId);
    
    /**
     * Get stream metrics
     */
    StreamMetrics getStreamMetrics(String streamId);
    
    /**
     * Stream Metrics Value Object
     */
    class StreamMetrics {
        private final String streamId;
        private final long messagesProcessed;
        private final long messagesPerSecond;
        private final boolean backpressureEnabled;
        private final boolean isHealthy;
        
        public StreamMetrics(String streamId, long messagesProcessed, long messagesPerSecond,
                           boolean backpressureEnabled, boolean isHealthy) {
            this.streamId = streamId;
            this.messagesProcessed = messagesProcessed;
            this.messagesPerSecond = messagesPerSecond;
            this.backpressureEnabled = backpressureEnabled;
            this.isHealthy = isHealthy;
        }
        
        // Getters
        public String getStreamId() { return streamId; }
        public long getMessagesProcessed() { return messagesProcessed; }
        public long getMessagesPerSecond() { return messagesPerSecond; }
        public boolean isBackpressureEnabled() { return backpressureEnabled; }
        public boolean isHealthy() { return isHealthy; }
    }
}