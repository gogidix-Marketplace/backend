package com.gogidix.courier.tenantservice.infrastructure.messaging.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Map;

/**
 * Kafka event representation for tenant events.
 */
@Schema(description="Kafka event for tenant changes")
public record TenantEvent(

        @JsonProperty("event_id")
        @Schema(description="Unique event ID")
        String eventId,

        @JsonProperty("event_type")
        @Schema(description="Event type", example = "TenantCreatedEvent")
        String eventType,

        @JsonProperty("aggregate_id")
        @Schema(description="Aggregate ID", example = "550e8400-e29b-41d4-a716-446655440000")
        String aggregateId,

        @JsonProperty("tenant_id")
        @Schema(description="Tenant ID", example = "tenant-001")
        String tenantId,

        @JsonProperty("tenant_name")
        @Schema(description="Tenant name", example = "Acme Courier")
        String tenantName,

        @JsonProperty("status")
        @Schema(description="Tenant status", example = "ACTIVE")
        String status,

        @JsonProperty("data")
        @Schema(description="Additional event data")
        Map<String, Object> data,

        @JsonProperty("occurred_at")
        @Schema(description="Event timestamp")
        Instant occurredAt
) {
    public static TenantEvent fromDomainEvent(
            String eventType,
            String aggregateId,
            String tenantId,
            String tenantName,
            String status,
            Map<String, Object> data,
            Instant occurredAt
    ) {
        return new TenantEvent(
                java.util.UUID.randomUUID().toString(),
                eventType,
                aggregateId,
                tenantId,
                tenantName,
                status,
                data,
                occurredAt
        );
    }
}
