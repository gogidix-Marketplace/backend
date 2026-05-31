package com.gogidix.aiservices.aisalesforecastingservice.infrastructure.messaging.event;

import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastType;
import java.time.Instant;
import java.util.Set;

/**
 * Base event class for segment-related domain events.
 */
public record ForecastEvent(

        String eventId,

        EventType eventType,

        String aggregateId,

        String tenantId,

        String userId,

        Instant timestamp,

        ForecastEventData data,

        String correlationId

) {

    public enum EventType {
        SEGMENT_CREATED,
        SEGMENT_UPDATED,
        SEGMENT_DELETED,
        CUSTOMERS_ADDED,
        CUSTOMERS_REMOVED,
        SEGMENT_ANALYZED
    }

    public record ForecastEventData(
            String segmentId,
            String name,
            String description,
            ForecastType segmentType,
            int customerCount,
            Set<String> customerIds,
            boolean active,
            Long version
    ) {
    }

    /**
     * Create a new segment event.
     */
    public static ForecastEvent create(
            EventType eventType,
            String aggregateId,
            String tenantId,
            String userId,
            ForecastEventData data,
            String correlationId
    ) {
        return new ForecastEvent(
                java.util.UUID.randomUUID().toString(),
                eventType,
                aggregateId,
                tenantId,
                userId,
                Instant.now(),
                data,
                correlationId
        );
    }

    /**
     * Create a segment created event.
     */
    public static ForecastEvent segmentCreated(
            String segmentId,
            String name,
            String description,
            ForecastType segmentType,
            int customerCount,
            String tenantId,
            String userId,
            String correlationId
    ) {
        ForecastEventData data = new ForecastEventData(
                segmentId, name, description, segmentType, customerCount, Set.of(), true, 0L
        );
        return create(EventType.SEGMENT_CREATED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a segment updated event.
     */
    public static ForecastEvent segmentUpdated(
            String segmentId,
            String name,
            String description,
            ForecastType segmentType,
            int customerCount,
            boolean active,
            Long version,
            String tenantId,
            String userId,
            String correlationId
    ) {
        ForecastEventData data = new ForecastEventData(
                segmentId, name, description, segmentType, customerCount, Set.of(), active, version
        );
        return create(EventType.SEGMENT_UPDATED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a segment deleted event.
     */
    public static ForecastEvent segmentDeleted(
            String segmentId,
            String tenantId,
            String userId,
            String correlationId
    ) {
        ForecastEventData data = new ForecastEventData(
                segmentId, null, null, null, 0, Set.of(), false, null
        );
        return create(EventType.SEGMENT_DELETED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a customers added event.
     */
    public static ForecastEvent customersAdded(
            String segmentId,
            Set<String> customerIds,
            int newForecastModelCount,
            String tenantId,
            String userId,
            String correlationId
    ) {
        ForecastEventData data = new ForecastEventData(
                segmentId, null, null, null, newForecastModelCount, customerIds, true, null
        );
        return create(EventType.CUSTOMERS_ADDED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a customers removed event.
     */
    public static ForecastEvent customersRemoved(
            String segmentId,
            Set<String> customerIds,
            int newForecastModelCount,
            String tenantId,
            String userId,
            String correlationId
    ) {
        ForecastEventData data = new ForecastEventData(
                segmentId, null, null, null, newForecastModelCount, customerIds, true, null
        );
        return create(EventType.CUSTOMERS_REMOVED, segmentId, tenantId, userId, data, correlationId);
    }
}
