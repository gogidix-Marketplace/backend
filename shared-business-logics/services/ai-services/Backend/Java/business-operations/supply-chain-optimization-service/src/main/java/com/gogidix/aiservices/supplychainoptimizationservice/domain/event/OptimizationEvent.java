package com.gogidix.aiservices.supplychainoptimizationservice.domain.event;

import lombok.Builder;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Builder
public class OptimizationEvent {
    private final UUID eventId;
    private final UUID requestId;
    private final OptimizationEventType eventType;
    private final String description;
    private final Instant timestamp;
    @Builder.Default
    private final Map<String, Object> metadata = Map.of();

    private OptimizationEvent(UUID eventId, UUID requestId, OptimizationEventType eventType,
                           String description, Instant timestamp, Map<String, Object> metadata) {
        this.eventId = eventId != null ? eventId : UUID.randomUUID();
        this.requestId = requestId;
        this.eventType = eventType;
        this.description = description;
        this.timestamp = timestamp != null ? timestamp : Instant.now();
        this.metadata = metadata != null ? metadata : Map.of();
    }

    public UUID getEventId() {
        return eventId;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public OptimizationEventType getEventType() {
        return eventType;
    }

    public String getDescription() {
        return description;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OptimizationEvent that = (OptimizationEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }
}
