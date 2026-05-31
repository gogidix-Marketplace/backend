package com.gogidix.universal.tracking.domain.port.out;

import com.gogidix.universal.tracking.domain.model.TrackingEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for Tracking Event repository operations.
 */
public interface TrackingEventRepositoryPort {

    /**
     * Save a tracking event
     */
    TrackingEvent save(TrackingEvent event);

    /**
     * Save multiple events
     */
    List<TrackingEvent> saveAll(List<TrackingEvent> events);

    /**
     * Find event by ID
     */
    Optional<TrackingEvent> findById(UUID id);

    /**
     * Find events by session ID
     */
    List<TrackingEvent> findBySessionId(String sessionId);

    /**
     * Find events by tenant ID with pagination
     */
    Page<TrackingEvent> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find events by filters
     */
    Page<TrackingEvent> findByFilters(
        String tenantId,
        String eventType,
        String sessionId,
        String userId,
        String source,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Boolean processed,
        Pageable pageable
    );

    /**
     * Find unprocessed events
     */
    List<TrackingEvent> findUnprocessedEvents(Pageable pageable);

    /**
     * Find unprocessed events with limit
     */
    default List<TrackingEvent> findUnprocessedEvents(int limit) {
        return findUnprocessedEvents(org.springframework.data.domain.PageRequest.of(0, limit));
    }

    /**
     * Count events by type and tenant
     */
    Long countByEventTypeAndTenantId(String eventType, String tenantId);

    /**
     * Count events by date range and tenant
     */
    Long countByTimestampBetweenAndTenantId(LocalDateTime start, LocalDateTime end, String tenantId);

    /**
     * Delete events older than specified date
     */
    void deleteByTimestampBefore(LocalDateTime date);

    /**
     * Delete by ID
     */
    void deleteById(UUID id);
}
