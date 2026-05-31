package com.gogidix.aiservices.aipersonalizationservice.domain.event;

import com.gogidix.aiservices.aipersonalizationservice.domain.model.EventType;
import lombok.Builder;

import java.time.Instant;
import java.util.*;

@Builder
public class BehaviorEvent {
    private final UUID eventId;
    private final UUID userId;
    private final EventType eventType;
    private final String itemId;
    private final Instant timestamp;
    @Builder.Default
    private Map<String, Object> properties = new HashMap<>();

    public BehaviorEvent(UUID eventId, UUID userId, EventType eventType, String itemId, Instant timestamp, Map<String, Object> properties) {
        if (eventType == null) {
            throw new IllegalArgumentException("Event type cannot be null");
        }
        if (itemId == null || itemId.trim().isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be null or empty");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null");
        }
        this.eventId = eventId != null ? eventId : UUID.randomUUID();
        this.userId = userId;
        this.eventType = eventType;
        this.itemId = itemId;
        this.timestamp = timestamp;
        this.properties = properties != null ? new HashMap<>(properties) : new HashMap<>();
    }

    public UUID getEventId() {
        return eventId;
    }

    public UUID getUserId() {
        return userId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getItemId() {
        return itemId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    public void addProperties(String key, Object value) {
        if (properties == null) {
            this.properties = new HashMap<>();
        }
        this.properties.put(key, value);
    }

    public Object getProperty(String key) {
        return properties != null ? properties.get(key) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BehaviorEvent that = (BehaviorEvent) o;
        return Objects.equals(eventId, that.eventId) ||
                (Objects.equals(userId, that.userId) &&
                        eventType == that.eventType &&
                        Objects.equals(itemId, that.itemId) &&
                        Objects.equals(timestamp, that.timestamp));
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, userId, eventType, itemId, timestamp);
    }
}
