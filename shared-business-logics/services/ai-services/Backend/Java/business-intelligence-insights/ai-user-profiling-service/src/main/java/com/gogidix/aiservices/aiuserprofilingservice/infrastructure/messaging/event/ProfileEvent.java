package com.gogidix.aiservices.aiuserprofilingservice.infrastructure.messaging.event;

import com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileType;
import java.time.Instant;
import java.util.Set;

/**
 * Base event class for segment-related domain events.
 */
public record ProfileEvent(

        String eventId,

        EventType eventType,

        String aggregateId,

        String tenantId,

        String userId,

        Instant timestamp,

        ProfileEventData data,

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

    public record ProfileEventData(
            String segmentId,
            String name,
            String description,
            ProfileType segmentType,
            int customerCount,
            Set<String> customerIds,
            boolean active,
            Long version
    ) {
    }

    /**
     * Create a new segment event.
     */
    public static ProfileEvent create(
            EventType eventType,
            String aggregateId,
            String tenantId,
            String userId,
            ProfileEventData data,
            String correlationId
    ) {
        return new ProfileEvent(
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
    public static ProfileEvent segmentCreated(
            String segmentId,
            String name,
            String description,
            ProfileType segmentType,
            int customerCount,
            String tenantId,
            String userId,
            String correlationId
    ) {
        ProfileEventData data = new ProfileEventData(
                segmentId, name, description, segmentType, customerCount, Set.of(), true, 0L
        );
        return create(EventType.SEGMENT_CREATED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a segment updated event.
     */
    public static ProfileEvent segmentUpdated(
            String segmentId,
            String name,
            String description,
            ProfileType segmentType,
            int customerCount,
            boolean active,
            Long version,
            String tenantId,
            String userId,
            String correlationId
    ) {
        ProfileEventData data = new ProfileEventData(
                segmentId, name, description, segmentType, customerCount, Set.of(), active, version
        );
        return create(EventType.SEGMENT_UPDATED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a segment deleted event.
     */
    public static ProfileEvent segmentDeleted(
            String segmentId,
            String tenantId,
            String userId,
            String correlationId
    ) {
        ProfileEventData data = new ProfileEventData(
                segmentId, null, null, null, 0, Set.of(), false, null
        );
        return create(EventType.SEGMENT_DELETED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a customers added event.
     */
    public static ProfileEvent customersAdded(
            String segmentId,
            Set<String> customerIds,
            int newUserCount,
            String tenantId,
            String userId,
            String correlationId
    ) {
        ProfileEventData data = new ProfileEventData(
                segmentId, null, null, null, newUserCount, customerIds, true, null
        );
        return create(EventType.CUSTOMERS_ADDED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a customers removed event.
     */
    public static ProfileEvent customersRemoved(
            String segmentId,
            Set<String> customerIds,
            int newUserCount,
            String tenantId,
            String userId,
            String correlationId
    ) {
        ProfileEventData data = new ProfileEventData(
                segmentId, null, null, null, newUserCount, customerIds, true, null
        );
        return create(EventType.CUSTOMERS_REMOVED, segmentId, tenantId, userId, data, correlationId);
    }
}
