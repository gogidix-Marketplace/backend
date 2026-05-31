package com.gogidix.aiservices.aichurnpredictionservice.infrastructure.messaging.event;

import com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionType;
import java.time.Instant;
import java.util.Set;

/**
 * Base event class for segment-related domain events.
 */
public record PredictionEvent(

        String eventId,

        EventType eventType,

        String aggregateId,

        String tenantId,

        String userId,

        Instant timestamp,

        PredictionEventData data,

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

    public record PredictionEventData(
            String segmentId,
            String name,
            String description,
            PredictionType segmentType,
            int customerCount,
            Set<String> customerIds,
            boolean active,
            Long version
    ) {
    }

    /**
     * Create a new segment event.
     */
    public static PredictionEvent create(
            EventType eventType,
            String aggregateId,
            String tenantId,
            String userId,
            PredictionEventData data,
            String correlationId
    ) {
        return new PredictionEvent(
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
    public static PredictionEvent segmentCreated(
            String segmentId,
            String name,
            String description,
            PredictionType segmentType,
            int customerCount,
            String tenantId,
            String userId,
            String correlationId
    ) {
        PredictionEventData data = new PredictionEventData(
                segmentId, name, description, segmentType, customerCount, Set.of(), true, 0L
        );
        return create(EventType.SEGMENT_CREATED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a segment updated event.
     */
    public static PredictionEvent segmentUpdated(
            String segmentId,
            String name,
            String description,
            PredictionType segmentType,
            int customerCount,
            boolean active,
            Long version,
            String tenantId,
            String userId,
            String correlationId
    ) {
        PredictionEventData data = new PredictionEventData(
                segmentId, name, description, segmentType, customerCount, Set.of(), active, version
        );
        return create(EventType.SEGMENT_UPDATED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a segment deleted event.
     */
    public static PredictionEvent segmentDeleted(
            String segmentId,
            String tenantId,
            String userId,
            String correlationId
    ) {
        PredictionEventData data = new PredictionEventData(
                segmentId, null, null, null, 0, Set.of(), false, null
        );
        return create(EventType.SEGMENT_DELETED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a customers added event.
     */
    public static PredictionEvent customersAdded(
            String segmentId,
            Set<String> customerIds,
            int newCustomerCount,
            String tenantId,
            String userId,
            String correlationId
    ) {
        PredictionEventData data = new PredictionEventData(
                segmentId, null, null, null, newCustomerCount, customerIds, true, null
        );
        return create(EventType.CUSTOMERS_ADDED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a customers removed event.
     */
    public static PredictionEvent customersRemoved(
            String segmentId,
            Set<String> customerIds,
            int newCustomerCount,
            String tenantId,
            String userId,
            String correlationId
    ) {
        PredictionEventData data = new PredictionEventData(
                segmentId, null, null, null, newCustomerCount, customerIds, true, null
        );
        return create(EventType.CUSTOMERS_REMOVED, segmentId, tenantId, userId, data, correlationId);
    }
}
