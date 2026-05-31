package com.gogidix.aiservices.aiproductrecommendationservice.infrastructure.messaging.event;

import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationType;
import java.time.Instant;
import java.util.Set;

/**
 * Base event class for segment-related domain events.
 */
public record RecommendationEvent(

        String eventId,

        EventType eventType,

        String aggregateId,

        String tenantId,

        String userId,

        Instant timestamp,

        RecommendationEventData data,

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

    public record RecommendationEventData(
            String segmentId,
            String name,
            String description,
            RecommendationType segmentType,
            int customerCount,
            Set<String> customerIds,
            boolean active,
            Long version
    ) {
    }

    /**
     * Create a new segment event.
     */
    public static RecommendationEvent create(
            EventType eventType,
            String aggregateId,
            String tenantId,
            String userId,
            RecommendationEventData data,
            String correlationId
    ) {
        return new RecommendationEvent(
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
    public static RecommendationEvent segmentCreated(
            String segmentId,
            String name,
            String description,
            RecommendationType segmentType,
            int customerCount,
            String tenantId,
            String userId,
            String correlationId
    ) {
        RecommendationEventData data = new RecommendationEventData(
                segmentId, name, description, segmentType, customerCount, Set.of(), true, 0L
        );
        return create(EventType.SEGMENT_CREATED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a segment updated event.
     */
    public static RecommendationEvent segmentUpdated(
            String segmentId,
            String name,
            String description,
            RecommendationType segmentType,
            int customerCount,
            boolean active,
            Long version,
            String tenantId,
            String userId,
            String correlationId
    ) {
        RecommendationEventData data = new RecommendationEventData(
                segmentId, name, description, segmentType, customerCount, Set.of(), active, version
        );
        return create(EventType.SEGMENT_UPDATED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a segment deleted event.
     */
    public static RecommendationEvent segmentDeleted(
            String segmentId,
            String tenantId,
            String userId,
            String correlationId
    ) {
        RecommendationEventData data = new RecommendationEventData(
                segmentId, null, null, null, 0, Set.of(), false, null
        );
        return create(EventType.SEGMENT_DELETED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a customers added event.
     */
    public static RecommendationEvent customersAdded(
            String segmentId,
            Set<String> customerIds,
            int newProductCount,
            String tenantId,
            String userId,
            String correlationId
    ) {
        RecommendationEventData data = new RecommendationEventData(
                segmentId, null, null, null, newProductCount, customerIds, true, null
        );
        return create(EventType.CUSTOMERS_ADDED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a customers removed event.
     */
    public static RecommendationEvent customersRemoved(
            String segmentId,
            Set<String> customerIds,
            int newProductCount,
            String tenantId,
            String userId,
            String correlationId
    ) {
        RecommendationEventData data = new RecommendationEventData(
                segmentId, null, null, null, newProductCount, customerIds, true, null
        );
        return create(EventType.CUSTOMERS_REMOVED, segmentId, tenantId, userId, data, correlationId);
    }
}
