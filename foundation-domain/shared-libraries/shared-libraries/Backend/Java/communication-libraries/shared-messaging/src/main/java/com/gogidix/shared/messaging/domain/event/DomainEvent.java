package com.gogidix.shared.messaging.domain.event;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Base class for domain events in the Gogidix ecosystem.
 * Provides common fields and methods for event sourcing and messaging.
 */
@Data
@Setter
@Getter
public abstract class DomainEvent {

    private String eventId;
    private String eventType;
    private String aggregateId;
    private String aggregateType;
    private String sourceService;
    private LocalDateTime timestamp;
    private String version = "1.0";
    private long sequenceNumber;
    private EventPriority priority = EventPriority.NORMAL;
    private String userId;
    private String sessionId;
    private String tenantId;
    private String correlationId;
    private String causationId;
    private Map<String, String> tags = new ConcurrentHashMap<>();
    private Map<String, Object> metadata = new ConcurrentHashMap<>();

    private static final AtomicLong sequenceCounter = new AtomicLong(0);

    /**
     * Event priority levels
     */
    public enum EventPriority {
        CRITICAL,
        HIGH,
        NORMAL,
        LOW
    }

    /**
     * Initialize the event with default values
     */
    public void initializeEvent() {
        if (this.eventId == null) {
            this.eventId = UUID.randomUUID().toString();
        }
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
        if (this.sequenceNumber == 0) {
            this.sequenceNumber = sequenceCounter.incrementAndGet();
        }
        if (this.correlationId == null) {
            this.correlationId = UUID.randomUUID().toString();
        }
    }

    /**
     * Validate the event has all required fields
     */
    public void validate() {
        if (eventType == null || eventType.isEmpty()) {
            throw new IllegalStateException("Event type cannot be null or empty");
        }
        if (aggregateId == null || aggregateId.isEmpty()) {
            throw new IllegalStateException("Aggregate ID cannot be null or empty");
        }
        if (aggregateType == null || aggregateType.isEmpty()) {
            throw new IllegalStateException("Aggregate type cannot be null or empty");
        }
        if (sourceService == null || sourceService.isEmpty()) {
            throw new IllegalStateException("Source service cannot be null or empty");
        }
    }

    /**
     * Create a copy of this event
     */
    public abstract DomainEvent createCopy();

    /**
     * Add a tag to the event
     */
    public void addTag(String key, String value) {
        this.tags.put(key, value);
    }

    /**
     * Get a tag value
     */
    public String getTag(String key) {
        return this.tags.get(key);
    }

    /**
     * Add metadata to the event
     */
    public void addMetadata(String key, Object value) {
        this.metadata.put(key, value);
    }

    /**
     * Get metadata value
     */
    public Object getMetadata(String key) {
        return this.metadata.get(key);
    }

    /**
     * Set the event that caused this event
     */
    public void setCausationEvent(DomainEvent causingEvent) {
        if (causingEvent != null) {
            this.causationId = causingEvent.getEventId();
            this.correlationId = causingEvent.getCorrelationId();
        }
    }

    /**
     * Check if this event is related to another event
     */
    public boolean isRelatedTo(DomainEvent other) {
        if (other == null) {
            return false;
        }
        return this.correlationId != null && this.correlationId.equals(other.getCorrelationId());
    }

    /**
     * Check if this event was caused by another event
     */
    public boolean wasCausedBy(DomainEvent other) {
        if (other == null) {
            return false;
        }
        return this.causationId != null && this.causationId.equals(other.getEventId());
    }

    @Override
    public String toString() {
        return String.format("%s[id=%s, type=%s, aggregate=%s/%s, source=%s]",
                getClass().getSimpleName(), eventId, eventType, aggregateType, aggregateId, sourceService);
    }
}
