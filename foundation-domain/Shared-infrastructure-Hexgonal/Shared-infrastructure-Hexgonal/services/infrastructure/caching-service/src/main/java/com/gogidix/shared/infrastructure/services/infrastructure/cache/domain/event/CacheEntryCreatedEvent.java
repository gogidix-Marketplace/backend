package com.gogidix.shared.infrastructure.services.infrastructure.cache.domain.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event published when a cache entry is created.
 */
public class CacheEntryCreatedEvent {

    private final String key;
    private final String region;
    private final long ttlSeconds;
    private final Instant occurredAt;

    public CacheEntryCreatedEvent(String key, String region, long ttlSeconds, Instant occurredAt) {
        this.key = key;
        this.region = region;
        this.ttlSeconds = ttlSeconds;
        this.occurredAt = occurredAt;
    }

    public String getKey() {
        return key;
    }

    public String getRegion() {
        return region;
    }

    public long getTtlSeconds() {
        return ttlSeconds;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheEntryCreatedEvent that = (CacheEntryCreatedEvent) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "CacheEntryCreatedEvent{" +
            "key='" + key + '\'' +
            ", region='" + region + '\'' +
            ", ttlSeconds=" + ttlSeconds +
            ", occurredAt=" + occurredAt +
            '}';
    }
}
