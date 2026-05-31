package com.gogidix.dashboard.realtime.domain.model;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Stream Configuration Value Object
 * 
 * Defines configuration parameters for real-time streams
 */
public class StreamConfiguration {
    
    private final int maxSubscribers;
    private final long maxMessagesPerSecond;
    private final long bufferSize;
    private final Duration maxInactivityDuration;
    private final long maxErrorRate;
    private final int maxErrorsPerSubscriber;
    private final List<Function<StreamMessage, StreamMessage>> transformations;
    private final List<Predicate<StreamMessage>> filters;
    private final BackpressureStrategy backpressureStrategy;
    private final CompressionType compressionType;
    private final boolean enableMetrics;
    private final Duration metricReportingInterval;
    
    private StreamConfiguration(Builder builder) {
        this.maxSubscribers = builder.maxSubscribers;
        this.maxMessagesPerSecond = builder.maxMessagesPerSecond;
        this.bufferSize = builder.bufferSize;
        this.maxInactivityDuration = builder.maxInactivityDuration;
        this.maxErrorRate = builder.maxErrorRate;
        this.maxErrorsPerSubscriber = builder.maxErrorsPerSubscriber;
        this.transformations = List.copyOf(builder.transformations);
        this.filters = List.copyOf(builder.filters);
        this.backpressureStrategy = builder.backpressureStrategy;
        this.compressionType = builder.compressionType;
        this.enableMetrics = builder.enableMetrics;
        this.metricReportingInterval = builder.metricReportingInterval;
        
        validateConfiguration();
    }
    
    /**
     * Validate configuration parameters
     */
    private void validateConfiguration() {
        if (maxSubscribers <= 0) {
            throw new IllegalArgumentException("Max subscribers must be positive");
        }
        if (maxMessagesPerSecond <= 0) {
            throw new IllegalArgumentException("Max messages per second must be positive");
        }
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("Buffer size must be positive");
        }
        if (maxInactivityDuration.isNegative() || maxInactivityDuration.isZero()) {
            throw new IllegalArgumentException("Max inactivity duration must be positive");
        }
        if (maxErrorRate < 0) {
            throw new IllegalArgumentException("Max error rate cannot be negative");
        }
        if (maxErrorsPerSubscriber < 0) {
            throw new IllegalArgumentException("Max errors per subscriber cannot be negative");
        }
    }
    
    /**
     * Check if configuration has transformations
     */
    public boolean hasTransformations() {
        return !transformations.isEmpty();
    }
    
    /**
     * Apply transformations to message
     */
    public StreamMessage applyTransformations(StreamMessage message) {
        StreamMessage transformed = message;
        for (Function<StreamMessage, StreamMessage> transformation : transformations) {
            transformed = transformation.apply(transformed);
        }
        return transformed;
    }
    
    /**
     * Check if configuration has filters
     */
    public boolean hasFilters() {
        return !filters.isEmpty();
    }
    
    /**
     * Check if message passes all filters
     */
    public boolean passesFilters(StreamMessage message) {
        return filters.stream().allMatch(filter -> filter.test(message));
    }
    
    /**
     * Get optimal buffer size based on message rate
     */
    public long getOptimalBufferSize() {
        return Math.max(bufferSize, maxMessagesPerSecond * 10); // 10 second buffer
    }
    
    /**
     * Calculate backpressure threshold
     */
    public double getBackpressureThreshold() {
        return backpressureStrategy.getThreshold();
    }
    
    /**
     * Determine if compression should be applied
     */
    public boolean shouldCompress(long payloadSize) {
        return compressionType != CompressionType.NONE && 
               payloadSize > compressionType.getMinSizeForCompression();
    }
    
    // Getters
    public int getMaxSubscribers() { return maxSubscribers; }
    public long getMaxMessagesPerSecond() { return maxMessagesPerSecond; }
    public long getBufferSize() { return bufferSize; }
    public Duration getMaxInactivityDuration() { return maxInactivityDuration; }
    public long getMaxErrorRate() { return maxErrorRate; }
    public int getMaxErrorsPerSubscriber() { return maxErrorsPerSubscriber; }
    public List<Function<StreamMessage, StreamMessage>> getTransformations() { return transformations; }
    public List<Predicate<StreamMessage>> getFilters() { return filters; }
    public BackpressureStrategy getBackpressureStrategy() { return backpressureStrategy; }
    public CompressionType getCompressionType() { return compressionType; }
    public boolean isEnableMetrics() { return enableMetrics; }
    public Duration getMetricReportingInterval() { return metricReportingInterval; }

    /**
     * Get expected throughput rate
     */
    public long getExpectedThroughputRate() {
        return maxMessagesPerSecond;
    }

    /**
     * Get max throughput rate
     */
    public long getMaxThroughputRate() {
        return maxMessagesPerSecond;
    }

    /**
     * Get max staleness duration
     */
    public Duration getMaxStaleness() {
        return maxInactivityDuration;
    }

    /**
     * Get max buffer utilization (0.0 to 1.0)
     */
    public double getMaxBufferUtilization() {
        return 0.8; // 80% max buffer utilization
    }

    /**
     * Get max processing lag duration
     */
    public Duration getMaxProcessingLag() {
        return Duration.ofSeconds(5); // 5 second max lag
    }

    /**
     * Create default configuration for dashboard streams
     */
    public static StreamConfiguration defaultDashboardConfig() {
        return new Builder()
            .withMaxSubscribers(1000)
            .withMaxMessagesPerSecond(10000)
            .withBufferSize(100000)
            .withMaxInactivityDuration(Duration.ofMinutes(5))
            .withMaxErrorRate(100)
            .withMaxErrorsPerSubscriber(10)
            .withBackpressureStrategy(BackpressureStrategy.DROP_OLDEST)
            .withCompressionType(CompressionType.GZIP)
            .withMetricsEnabled(true)
            .withMetricReportingInterval(Duration.ofSeconds(30))
            .build();
    }
    
    /**
     * Create high-throughput configuration
     */
    public static StreamConfiguration highThroughputConfig() {
        return new Builder()
            .withMaxSubscribers(10000)
            .withMaxMessagesPerSecond(100000)
            .withBufferSize(1000000)
            .withMaxInactivityDuration(Duration.ofMinutes(1))
            .withMaxErrorRate(1000)
            .withMaxErrorsPerSubscriber(50)
            .withBackpressureStrategy(BackpressureStrategy.REJECT_NEW)
            .withCompressionType(CompressionType.LZ4)
            .withMetricsEnabled(true)
            .withMetricReportingInterval(Duration.ofSeconds(10))
            .build();
    }
    
    // Builder
    public static class Builder {
        private int maxSubscribers = 100;
        private long maxMessagesPerSecond = 1000;
        private long bufferSize = 10000;
        private Duration maxInactivityDuration = Duration.ofMinutes(10);
        private long maxErrorRate = 10;
        private int maxErrorsPerSubscriber = 5;
        private List<Function<StreamMessage, StreamMessage>> transformations = List.of();
        private List<Predicate<StreamMessage>> filters = List.of();
        private BackpressureStrategy backpressureStrategy = BackpressureStrategy.BUFFER;
        private CompressionType compressionType = CompressionType.NONE;
        private boolean enableMetrics = true;
        private Duration metricReportingInterval = Duration.ofMinutes(1);
        
        public Builder withMaxSubscribers(int maxSubscribers) {
            this.maxSubscribers = maxSubscribers;
            return this;
        }
        
        public Builder withMaxMessagesPerSecond(long maxMessagesPerSecond) {
            this.maxMessagesPerSecond = maxMessagesPerSecond;
            return this;
        }
        
        public Builder withBufferSize(long bufferSize) {
            this.bufferSize = bufferSize;
            return this;
        }
        
        public Builder withMaxInactivityDuration(Duration maxInactivityDuration) {
            this.maxInactivityDuration = maxInactivityDuration;
            return this;
        }
        
        public Builder withMaxErrorRate(long maxErrorRate) {
            this.maxErrorRate = maxErrorRate;
            return this;
        }
        
        public Builder withMaxErrorsPerSubscriber(int maxErrorsPerSubscriber) {
            this.maxErrorsPerSubscriber = maxErrorsPerSubscriber;
            return this;
        }
        
        public Builder withTransformations(List<Function<StreamMessage, StreamMessage>> transformations) {
            this.transformations = transformations;
            return this;
        }
        
        public Builder withFilters(List<Predicate<StreamMessage>> filters) {
            this.filters = filters;
            return this;
        }
        
        public Builder withBackpressureStrategy(BackpressureStrategy strategy) {
            this.backpressureStrategy = strategy;
            return this;
        }
        
        public Builder withCompressionType(CompressionType compressionType) {
            this.compressionType = compressionType;
            return this;
        }
        
        public Builder withMetricsEnabled(boolean enabled) {
            this.enableMetrics = enabled;
            return this;
        }
        
        public Builder withMetricReportingInterval(Duration interval) {
            this.metricReportingInterval = interval;
            return this;
        }
        
        public StreamConfiguration build() {
            return new StreamConfiguration(this);
        }
    }
}