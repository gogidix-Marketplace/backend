package com.gogidix.aiservices.aicustomersegmentationservice.infrastructure.messaging.event;

import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentType;
import java.time.Instant;
import java.util.Set;

/**
 * Base event class for segment-related domain events.
 */
public record SegmentEvent(

        String eventId,

        EventType eventType,

        String aggregateId,

        String tenantId,

        String userId,

        Instant timestamp,

        SegmentEventData data,

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

    public record SegmentEventData(
            String segmentId,
            String name,
            String description,
            SegmentType segmentType,
            int customerCount,
            Set<String> customerIds,
            boolean active,
            Long version
    ) {
    }

    /**
     * Create a new segment event.
     */
    public static SegmentEvent create(
            EventType eventType,
            String aggregateId,
            String tenantId,
            String userId,
            SegmentEventData data,
            String correlationId
    ) {
        return new SegmentEvent(
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
    public static SegmentEvent segmentCreated(
            String segmentId,
            String name,
            String description,
            SegmentType segmentType,
            int customerCount,
            String tenantId,
            String userId,
            String correlationId
    ) {
        SegmentEventData data = new SegmentEventData(
                segmentId, name, description, segmentType, customerCount, Set.of(), true, 0L
        );
        return create(EventType.SEGMENT_CREATED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a segment updated event.
     */
    public static SegmentEvent segmentUpdated(
            String segmentId,
            String name,
            String description,
            SegmentType segmentType,
            int customerCount,
            boolean active,
            Long version,
            String tenantId,
            String userId,
            String correlationId
    ) {
        SegmentEventData data = new SegmentEventData(
                segmentId, name, description, segmentType, customerCount, Set.of(), active, version
        );
        return create(EventType.SEGMENT_UPDATED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a segment deleted event.
     */
    public static SegmentEvent segmentDeleted(
            String segmentId,
            String tenantId,
            String userId,
            String correlationId
    ) {
        SegmentEventData data = new SegmentEventData(
                segmentId, null, null, null, 0, Set.of(), false, null
        );
        return create(EventType.SEGMENT_DELETED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a customers added event.
     */
    public static SegmentEvent customersAdded(
            String segmentId,
            Set<String> customerIds,
            int newCustomerCount,
            String tenantId,
            String userId,
            String correlationId
    ) {
        SegmentEventData data = new SegmentEventData(
                segmentId, null, null, null, newCustomerCount, customerIds, true, null
        );
        return create(EventType.CUSTOMERS_ADDED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a customers removed event.
     */
    public static SegmentEvent customersRemoved(
            String segmentId,
            Set<String> customerIds,
            int newCustomerCount,
            String tenantId,
            String userId,
            String correlationId
    ) {
        SegmentEventData data = new SegmentEventData(
                segmentId, null, null, null, newCustomerCount, customerIds, true, null
        );
        return create(EventType.CUSTOMERS_REMOVED, segmentId, tenantId, userId, data, correlationId);
    }
}
