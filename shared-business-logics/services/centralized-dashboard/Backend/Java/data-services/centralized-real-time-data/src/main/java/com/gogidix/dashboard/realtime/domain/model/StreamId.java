package com.gogidix.dashboard.realtime.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Stream ID Value Object
 */
public class StreamId {
    private final String value;
    
    private StreamId(String value) {
        this.value = Objects.requireNonNull(value, "Stream ID cannot be null");
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException("Stream ID cannot be empty");
        }
    }
    
    public static StreamId generate() {
        return new StreamId(UUID.randomUUID().toString());
    }
    
    public static StreamId of(String value) {
        return new StreamId(value);
    }

    public static StreamId fromString(String value) {
        return of(value);
    }

    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreamId streamId = (StreamId) o;
        return Objects.equals(value, streamId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}