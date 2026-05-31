package com.gogidix.dashboard.realtime.domain.port.out;

import com.gogidix.dashboard.realtime.domain.model.*;
import java.util.List;
import java.util.Optional;

/**
 * Real-Time Data Repository Port
 * 
 * Secondary port for persistence operations
 * Defines data access operations for real-time data streams
 */
public interface RealTimeDataRepositoryPort {

    /**
     * Save a real-time data stream
     */
    RealTimeDataStream save(RealTimeDataStream stream);

    /**
     * Find stream by ID
     */
    Optional<RealTimeDataStream> findById(StreamId streamId);

    /**
     * Find all active streams
     */
    List<RealTimeDataStream> findActiveStreams();

    /**
     * Find streams by domain
     */
    List<RealTimeDataStream> findByDomain(String domain);

    /**
     * Find streams by status
     */
    List<RealTimeDataStream> findByStatus(StreamStatus status);

    /**
     * Check if stream exists
     */
    boolean existsById(StreamId streamId);

    /**
     * Delete stream by ID
     */
    void deleteById(StreamId streamId);

    /**
     * Update stream status
     */
    void updateStreamStatus(StreamId streamId, StreamStatus status);
}