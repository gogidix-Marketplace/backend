package com.gogidix.courier.gpstrackingservice.infrastructure.messaging.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Map;

/**
 * External event representation for GPS tracking events.
 * This is the format sent to Kafka.
 */
@Schema(description = "GPS Tracking event for Kafka messaging")
public record GpsTrackingEvent(

        @JsonProperty("event_id")
        @Schema(description = "Unique event ID", example = "evt-123")
        String eventId,

        @JsonProperty("event_type")
        @Schema(description = "Event type", example = "LocationUpdated")
        String eventType,

        @JsonProperty("aggregate_id")
        @Schema(description = "Aggregate ID", example = "agg-456")
        String aggregateId,

        @JsonProperty("tenant_id")
        @Schema(description = "Tenant ID", example = "tenant-001")
        String tenantId,

        @JsonProperty("occurred_at")
        @Schema(description = "Event timestamp", example = "2025-02-20T10:30:00Z")
        Instant occurredAt,

        @JsonProperty("data")
        @Schema(description = "Event data")
        Map<String, Object> data
) {
}
