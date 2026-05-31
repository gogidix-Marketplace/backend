package com.gogidix.dashboard.realtime.domain.port.in;

import com.gogidix.dashboard.realtime.domain.model.RealTimeDataStream;
import com.gogidix.dashboard.realtime.domain.model.StreamMessage;
import com.gogidix.dashboard.realtime.domain.model.StreamMetrics;
import com.gogidix.dashboard.realtime.domain.model.StreamConfiguration;
import com.gogidix.dashboard.realtime.domain.model.StreamId;

import java.util.List;
import java.util.Optional;

/**
 * Use case interface for real-time data management.
 */
public interface RealTimeDataManagementUseCase {

    /**
     * Create a new real-time data stream
     */
    RealTimeDataStream createStream(CreateStreamCommand command);

    /**
     * Get stream by ID
     */
    Optional<RealTimeDataStream> getStream(StreamId streamId);

    /**
     * Get all streams for a tenant
     */
    List<RealTimeDataStream> getStreamsByTenant(String tenantId);

    /**
     * Get active streams
     */
    List<RealTimeDataStream> getActiveStreams();

    /**
     * Get streams by domain
     */
    List<RealTimeDataStream> getStreamsByDomain(String domain);

    /**
     * Start a stream
     */
    void startStream(StreamId streamId);

    /**
     * Stop a stream
     */
    void stopStream(StreamId streamId);

    /**
     * Delete a stream
     */
    void deleteStream(StreamId streamId);

    /**
     * Publish a message to a stream
     */
    void publishMessage(StreamId streamId, StreamMessage message);

    /**
     * Push data to a stream
     */
    void pushData(StreamId streamId, Object data);

    /**
     * Get stream metrics
     */
    StreamMetrics getStreamMetrics(StreamId streamId);

    /**
     * Get active subscribers count
     */
    int getActiveSubscribersCount(StreamId streamId);

    /**
     * Command for creating a stream
     */
    record CreateStreamCommand(String streamName, String domain, StreamConfiguration configuration) {}
}
