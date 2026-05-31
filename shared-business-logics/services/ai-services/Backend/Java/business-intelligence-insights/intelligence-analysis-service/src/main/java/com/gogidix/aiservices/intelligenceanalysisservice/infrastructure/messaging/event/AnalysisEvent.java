package com.gogidix.aiservices.intelligenceanalysisservice.infrastructure.messaging.event;

import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisType;
import java.time.Instant;
import java.util.Set;

/**
 * Base event class for segment-related domain events.
 */
public record AnalysisEvent(

        String eventId,

        EventType eventType,

        String aggregateId,

        String tenantId,

        String userId,

        Instant timestamp,

        AnalysisEventData data,

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

    public record AnalysisEventData(
            String segmentId,
            String name,
            String description,
            AnalysisType segmentType,
            int customerCount,
            Set<String> customerIds,
            boolean active,
            Long version
    ) {
    }

    /**
     * Create a new segment event.
     */
    public static AnalysisEvent create(
            EventType eventType,
            String aggregateId,
            String tenantId,
            String userId,
            AnalysisEventData data,
            String correlationId
    ) {
        return new AnalysisEvent(
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
    public static AnalysisEvent segmentCreated(
            String segmentId,
            String name,
            String description,
            AnalysisType segmentType,
            int customerCount,
            String tenantId,
            String userId,
            String correlationId
    ) {
        AnalysisEventData data = new AnalysisEventData(
                segmentId, name, description, segmentType, customerCount, Set.of(), true, 0L
        );
        return create(EventType.SEGMENT_CREATED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a segment updated event.
     */
    public static AnalysisEvent segmentUpdated(
            String segmentId,
            String name,
            String description,
            AnalysisType segmentType,
            int customerCount,
            boolean active,
            Long version,
            String tenantId,
            String userId,
            String correlationId
    ) {
        AnalysisEventData data = new AnalysisEventData(
                segmentId, name, description, segmentType, customerCount, Set.of(), active, version
        );
        return create(EventType.SEGMENT_UPDATED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a segment deleted event.
     */
    public static AnalysisEvent segmentDeleted(
            String segmentId,
            String tenantId,
            String userId,
            String correlationId
    ) {
        AnalysisEventData data = new AnalysisEventData(
                segmentId, null, null, null, 0, Set.of(), false, null
        );
        return create(EventType.SEGMENT_DELETED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a customers added event.
     */
    public static AnalysisEvent customersAdded(
            String segmentId,
            Set<String> customerIds,
            int newIntelligenceReportCount,
            String tenantId,
            String userId,
            String correlationId
    ) {
        AnalysisEventData data = new AnalysisEventData(
                segmentId, null, null, null, newIntelligenceReportCount, customerIds, true, null
        );
        return create(EventType.CUSTOMERS_ADDED, segmentId, tenantId, userId, data, correlationId);
    }

    /**
     * Create a customers removed event.
     */
    public static AnalysisEvent customersRemoved(
            String segmentId,
            Set<String> customerIds,
            int newIntelligenceReportCount,
            String tenantId,
            String userId,
            String correlationId
    ) {
        AnalysisEventData data = new AnalysisEventData(
                segmentId, null, null, null, newIntelligenceReportCount, customerIds, true, null
        );
        return create(EventType.CUSTOMERS_REMOVED, segmentId, tenantId, userId, data, correlationId);
    }
}
