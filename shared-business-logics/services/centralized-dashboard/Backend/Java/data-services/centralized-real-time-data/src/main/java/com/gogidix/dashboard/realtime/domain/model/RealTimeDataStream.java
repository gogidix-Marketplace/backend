package com.gogidix.dashboard.realtime.domain.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import java.time.Duration;

/**
 * Real-Time Data Stream Domain Entity
 * 
 * Core business entity for real-time data streaming with comprehensive stream management
 * Pure domain logic following hexagonal architecture principles
 */
public class RealTimeDataStream {
    
    private final StreamId streamId;
    private final String streamName;
    private final StreamType streamType;
    private final DataSourceConnection dataSource;
    private final StreamConfiguration configuration;
    private final StreamStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastActivityAt;
    private final List<StreamConsumer> consumers;
    private final StreamMetrics metrics;
    private final BackpressureStrategy backpressureStrategy;
    private final QualityOfService qualityOfService;
    private final Map<String, String> metadata;
    
    // Business constructor with validation
    public RealTimeDataStream(StreamId streamId, String streamName, StreamType streamType,
                             DataSourceConnection dataSource, StreamConfiguration configuration,
                             StreamStatus status, LocalDateTime createdAt,
                             LocalDateTime lastActivityAt, List<StreamConsumer> consumers,
                             StreamMetrics metrics, BackpressureStrategy backpressureStrategy,
                             QualityOfService qualityOfService, Map<String, String> metadata) {
        
        validateStreamData(streamName, dataSource, configuration);
        
        this.streamId = Objects.requireNonNull(streamId, "Stream ID cannot be null");
        this.streamName = streamName;
        this.streamType = Objects.requireNonNull(streamType, "Stream type cannot be null");
        this.dataSource = dataSource;
        this.configuration = configuration;
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created timestamp cannot be null");
        this.lastActivityAt = lastActivityAt;
        this.consumers = new ArrayList<>(consumers);
        this.metrics = Objects.requireNonNull(metrics, "Stream metrics cannot be null");
        this.backpressureStrategy = Objects.requireNonNull(backpressureStrategy, "Backpressure strategy cannot be null");
        this.qualityOfService = Objects.requireNonNull(qualityOfService, "QoS cannot be null");
        this.metadata = Map.copyOf(metadata);
    }
    
    // Business validation
    private void validateStreamData(String streamName, DataSourceConnection dataSource, 
                                   StreamConfiguration configuration) {
        if (streamName == null || streamName.trim().isEmpty()) {
            throw new IllegalArgumentException("Stream name cannot be empty");
        }
        if (dataSource == null) {
            throw new IllegalArgumentException("Data source connection cannot be null");
        }
        if (configuration == null) {
            throw new IllegalArgumentException("Stream configuration cannot be null");
        }
    }
    
    /**
     * Business Logic: Check if stream is healthy
     */
    public boolean isHealthy() {
        return status == StreamStatus.ACTIVE &&
               dataSource.isConnected() &&
               !hasBackpressureIssues() &&
               isWithinLatencyThresholds() &&
               consumers.stream().allMatch(StreamConsumer::isHealthy);
    }
    
    /**
     * Business Logic: Check for backpressure issues
     */
    public boolean hasBackpressureIssues() {
        return metrics.getBufferUtilization() > configuration.getMaxBufferUtilization() ||
               metrics.getProcessingLag().compareTo(configuration.getMaxProcessingLag()) > 0;
    }
    
    /**
     * Business Logic: Check if within latency thresholds
     */
    public boolean isWithinLatencyThresholds() {
        long currentLatencyMillis = metrics.getAverageLatency();
        long maxLatencyMillis = qualityOfService.getMaxLatency();
        return currentLatencyMillis >= 0 && currentLatencyMillis <= maxLatencyMillis;
    }
    
    /**
     * Business Logic: Calculate stream health score (0-100)
     */
    public double calculateHealthScore() {
        double score = 100.0;
        
        // Deduct for inactive status
        if (status != StreamStatus.ACTIVE) {
            score -= 50;
        }
        
        // Deduct for connection issues
        if (!dataSource.isConnected()) {
            score -= 30;
        }
        
        // Deduct for backpressure
        if (hasBackpressureIssues()) {
            score -= 20;
        }
        
        // Deduct for latency issues
        if (!isWithinLatencyThresholds()) {
            score -= 15;
        }
        
        // Deduct for unhealthy consumers
        long unhealthyConsumers = consumers.stream()
            .mapToLong(consumer -> consumer.isHealthy() ? 0 : 1)
            .sum();
        
        if (!consumers.isEmpty()) {
            double consumerHealthRatio = 1.0 - ((double) unhealthyConsumers / consumers.size());
            score *= consumerHealthRatio;
        }
        
        return Math.max(0, score);
    }
    
    /**
     * Business Logic: Determine if stream needs scaling
     */
    public boolean needsScaling() {
        double bufferUtilization = metrics.getBufferUtilization();
        double cpuUtilization = metrics.getCpuUtilization();
        long throughputRate = metrics.getThroughputRate();
        
        return bufferUtilization > 80.0 ||
               cpuUtilization > 85.0 ||
               throughputRate > configuration.getMaxThroughputRate() * 0.9;
    }
    
    /**
     * Business Logic: Calculate recommended scaling factor
     */
    public double calculateRecommendedScalingFactor() {
        if (!needsScaling()) {
            return 1.0;
        }
        
        double bufferFactor = metrics.getBufferUtilization() / 70.0; // Target 70% utilization
        double cpuFactor = metrics.getCpuUtilization() / 70.0;
        double throughputFactor = (double) metrics.getThroughputRate() / 
                                 (configuration.getMaxThroughputRate() * 0.7);
        
        return Math.max(Math.max(bufferFactor, cpuFactor), throughputFactor);
    }
    
    /**
     * Business Logic: Check if stream should trigger alerts
     */
    public boolean shouldTriggerAlert() {
        return !isHealthy() ||
               metrics.getErrorRate() > 0.01 || // More than 1% error rate
               hasDataQualityIssues() ||
               isStreamStale();
    }
    
    /**
     * Business Logic: Check for data quality issues
     */
    public boolean hasDataQualityIssues() {
        return metrics.getDataQualityScore() < qualityOfService.getMinDataQualityScore();
    }
    
    /**
     * Business Logic: Check if stream is stale
     */
    public boolean isStreamStale() {
        if (lastActivityAt == null) {
            return true;
        }
        
        Duration staleness = Duration.between(lastActivityAt, LocalDateTime.now());
        Duration maxStaleness = configuration.getMaxStaleness();
        
        return staleness.compareTo(maxStaleness) > 0;
    }
    
    /**
     * Business Logic: Get stream priority based on type and QoS
     */
    public StreamPriority getStreamPriority() {
        if (streamType == StreamType.CRITICAL_ALERTS) {
            return StreamPriority.CRITICAL;
        } else if (streamType == StreamType.REAL_TIME_DASHBOARD) {
            return StreamPriority.HIGH;
        }

        long maxLatencyMillis = qualityOfService.getMaxLatency();
        if (maxLatencyMillis < 1000) {
            return StreamPriority.HIGH;
        } else if (maxLatencyMillis < 5000) {
            return StreamPriority.MEDIUM;
        } else {
            return StreamPriority.LOW;
        }
    }
    
    /**
     * Business Logic: Calculate resource requirements
     */
    public ResourceRequirements calculateResourceRequirements() {
        long expectedThroughput = configuration.getExpectedThroughputRate();
        int consumerCount = consumers.size();
        
        // Base resource calculation
        double cpuCores = Math.max(1.0, expectedThroughput / 10000.0); // 1 core per 10k messages/sec
        long memoryMB = Math.max(512, expectedThroughput / 100); // 1MB per 100 messages/sec
        long diskMB = configuration.getBufferSize() * 2; // 2x buffer size for safety
        
        // Adjust for consumers
        cpuCores += consumerCount * 0.1;
        memoryMB += consumerCount * 64;
        
        // Adjust for stream type
        if (streamType.requiresHighResources()) {
            cpuCores *= 1.5;
            memoryMB *= 2;
        }
        
        return new ResourceRequirements(cpuCores, memoryMB, diskMB);
    }
    
    /**
     * Business Logic: Generate performance insights
     */
    public List<String> generatePerformanceInsights() {
        List<String> insights = new ArrayList<>();
        
        if (hasBackpressureIssues()) {
            insights.add("Backpressure detected - consider increasing buffer size or consumer parallelism");
        }
        
        if (!isWithinLatencyThresholds()) {
            insights.add("Latency threshold exceeded - review processing logic and resource allocation");
        }
        
        if (needsScaling()) {
            double factor = calculateRecommendedScalingFactor();
            insights.add(String.format("Stream overloaded - recommend scaling by %.1fx", factor));
        }
        
        if (hasDataQualityIssues()) {
            insights.add("Data quality issues detected - review data source and validation rules");
        }
        
        if (isStreamStale()) {
            insights.add("Stream appears stale - check data source connectivity and flow");
        }
        
        double errorRate = metrics.getErrorRate();
        if (errorRate > 0.001) {
            insights.add(String.format("Error rate elevated (%.3f%%) - investigate error patterns", errorRate * 100));
        }
        
        return insights;
    }
    
    /**
     * Business Logic: Calculate cost per message
     */
    public double calculateCostPerMessage() {
        ResourceRequirements requirements = calculateResourceRequirements();
        
        // Base cost calculation (hypothetical cloud costs)
        double hourlyCost = (requirements.getCpuCores() * 0.05) + // $0.05 per CPU core hour
                           (requirements.getMemoryMB() / 1024.0 * 0.01) + // $0.01 per GB hour
                           (requirements.getDiskMB() / 1024.0 * 0.001); // $0.001 per GB hour
        
        long messagesPerHour = metrics.getThroughputRate() * 3600;
        
        if (messagesPerHour == 0) {
            return 0.0;
        }
        
        return hourlyCost / messagesPerHour;
    }
    
    // Getters
    public StreamId getStreamId() { return streamId; }
    public String getStreamName() { return streamName; }
    public StreamType getStreamType() { return streamType; }
    public DataSourceConnection getDataSource() { return dataSource; }
    public StreamConfiguration getConfiguration() { return configuration; }
    public StreamStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastActivityAt() { return lastActivityAt; }
    public List<StreamConsumer> getConsumers() { return new ArrayList<>(consumers); }
    public StreamMetrics getMetrics() { return metrics; }
    public BackpressureStrategy getBackpressureStrategy() { return backpressureStrategy; }
    public QualityOfService getQualityOfService() { return qualityOfService; }
    public Map<String, String> getMetadata() { return Map.copyOf(metadata); }
    public String getId() { return streamId != null ? streamId.getValue() : null; }
    public String getDomain() { return metadata != null ? metadata.getOrDefault("domain", "UNKNOWN") : "UNKNOWN"; }
    public boolean isActive() { return status == StreamStatus.ACTIVE; }

    // Stream lifecycle methods
    public void start() { if (!isActive()) { /* Start streaming */ } }
    public void stop() { if (isActive()) { /* Stop streaming */ } }
    public void pushData(Object data) { if (isActive() && data != null) { /* Push data */ } }

    // Builder pattern
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private StreamId streamId;
        private String streamName;
        private StreamType streamType;
        private DataSourceConnection dataSource;
        private StreamConfiguration configuration;
        private StreamStatus status = StreamStatus.CREATED;
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime lastActivityAt;
        private List<StreamConsumer> consumers = new ArrayList<>();
        private StreamMetrics metrics;
        private BackpressureStrategy backpressureStrategy = BackpressureStrategy.DROP_OLDEST;
        private QualityOfService qualityOfService;
        private Map<String, String> metadata = Map.of();

        public Builder withStreamId(StreamId sid) { this.streamId = sid; return this; }
        public Builder withId(String id) { this.streamId = StreamId.of(id); return this; }
        public Builder withStreamName(String sn) { this.streamName = sn; return this; }
        public Builder withStreamType(StreamType st) { this.streamType = st; return this; }
        public Builder withDataSource(DataSourceConnection ds) { this.dataSource = ds; return this; }
        public Builder withConfiguration(StreamConfiguration c) { this.configuration = c; return this; }
        public Builder withStatus(StreamStatus s) { this.status = s; return this; }
        public Builder withCreatedAt(LocalDateTime ca) { this.createdAt = ca; return this; }
        public Builder withLastActivityAt(LocalDateTime la) { this.lastActivityAt = la; return this; }
        public Builder withConsumers(List<StreamConsumer> c) { this.consumers = c != null ? new ArrayList<>(c) : new ArrayList<>(); return this; }
        public Builder withMetrics(StreamMetrics m) { this.metrics = m; return this; }
        public Builder withBackpressureStrategy(BackpressureStrategy b) { this.backpressureStrategy = b; return this; }
        public Builder withQualityOfService(QualityOfService q) { this.qualityOfService = q; return this; }
        public Builder withMetadata(Map<String, String> m) { this.metadata = m != null ? Map.copyOf(m) : Map.of(); return this; }
        public Builder withDomain(String domain) {
            if (domain != null) {
                java.util.Map<String, String> mutableMetadata = new java.util.HashMap<>(this.metadata);
                mutableMetadata.put("domain", domain);
                this.metadata = Map.copyOf(mutableMetadata);
            }
            return this;
        }

        public RealTimeDataStream build() {
            return new RealTimeDataStream(streamId, streamName, streamType, dataSource, configuration,
                status, createdAt, lastActivityAt, consumers, metrics, backpressureStrategy, qualityOfService, metadata);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RealTimeDataStream that = (RealTimeDataStream) o;
        return Objects.equals(streamId, that.streamId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(streamId);
    }
}

// Supporting classes would be defined here or in separate files
enum StreamPriority {
    CRITICAL, HIGH, MEDIUM, LOW
}

class ResourceRequirements {
    private final double cpuCores;
    private final long memoryMB;
    private final long diskMB;
    
    public ResourceRequirements(double cpuCores, long memoryMB, long diskMB) {
        this.cpuCores = cpuCores;
        this.memoryMB = memoryMB;
        this.diskMB = diskMB;
    }
    
    public double getCpuCores() { return cpuCores; }
    public long getMemoryMB() { return memoryMB; }
    public long getDiskMB() { return diskMB; }
}