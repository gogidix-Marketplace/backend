package com.gogidix.dashboard.realtime.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Extension methods for RealTimeDataStream
 * This file provides additional methods needed by the application layer
 */
public class RealTimeDataStreamExtensions {

    /**
     * Get the ID of the stream as a String
     */
    public static String getId(RealTimeDataStream stream) {
        return stream.getStreamId() != null ? stream.getStreamId().getValue() : null;
    }

    /**
     * Get the domain of the stream
     */
    public static String getDomain(RealTimeDataStream stream) {
        Map<String, String> metadata = stream.getMetadata();
        return metadata != null ? metadata.getOrDefault("domain", "UNKNOWN") : "UNKNOWN";
    }

    /**
     * Check if the stream is active
     */
    public static boolean isActive(RealTimeDataStream stream) {
        return stream.getStatus() == StreamStatus.ACTIVE;
    }

    /**
     * Start the stream
     */
    public static void start(RealTimeDataStream stream) {
        // Start the stream - implementation would interact with data source
        if (!isActive(stream)) {
            // Business logic to start streaming
        }
    }

    /**
     * Stop the stream
     */
    public static void stop(RealTimeDataStream stream) {
        // Stop the stream - implementation would disconnect from data source
        if (isActive(stream)) {
            // Business logic to stop streaming
        }
    }

    /**
     * Push data to stream consumers
     */
    public static void pushData(RealTimeDataStream stream, Object data) {
        // Push data to stream consumers
        if (isActive(stream) && data != null) {
            // Business logic to distribute data to consumers
        }
    }

    /**
     * Builder for RealTimeDataStream
     */
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

        public Builder withStreamId(StreamId streamId) {
            this.streamId = streamId;
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

        public Builder withDataSource(DataSourceConnection dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public Builder withConfiguration(StreamConfiguration configuration) {
            this.configuration = configuration;
            return this;
        }

        public Builder withStatus(StreamStatus status) {
            this.status = status;
            return this;
        }

        public Builder withCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder withLastActivityAt(LocalDateTime lastActivityAt) {
            this.lastActivityAt = lastActivityAt;
            return this;
        }

        public Builder withConsumers(List<StreamConsumer> consumers) {
            this.consumers = consumers != null ? new ArrayList<>(consumers) : new ArrayList<>();
            return this;
        }

        public Builder withMetrics(StreamMetrics metrics) {
            this.metrics = metrics;
            return this;
        }

        public Builder withBackpressureStrategy(BackpressureStrategy backpressureStrategy) {
            this.backpressureStrategy = backpressureStrategy;
            return this;
        }

        public Builder withQualityOfService(QualityOfService qualityOfService) {
            this.qualityOfService = qualityOfService;
            return this;
        }

        public Builder withMetadata(Map<String, String> metadata) {
            this.metadata = metadata != null ? Map.copyOf(metadata) : Map.of();
            return this;
        }

        public RealTimeDataStream build() {
            return new RealTimeDataStream(
                streamId, streamName, streamType, dataSource, configuration,
                status, createdAt, lastActivityAt, consumers, metrics,
                backpressureStrategy, qualityOfService, metadata
            );
        }
    }
}
