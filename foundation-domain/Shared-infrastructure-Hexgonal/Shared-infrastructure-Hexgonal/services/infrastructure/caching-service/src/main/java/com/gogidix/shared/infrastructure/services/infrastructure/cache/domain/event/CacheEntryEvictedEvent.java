package com.gogidix.shared.infrastructure.services.infrastructure.cache.domain.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event published when a cache entry is evicted.
 */
public class CacheEntryEvictedEvent {

    private final String key;
    private final String region;
    private final String reason;
    private final Instant occurredAt;

    public CacheEntryEvictedEvent(String key, String region, String reason, Instant occurredAt) {
        this.key = key;
        this.region = region;
        this.reason = reason;
        this.occurredAt = occurredAt;
    }

    public String getKey() {
        return key;
    }

    public String getRegion() {
        return region;
    }

    public String getReason() {
        return reason;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheEntryEvictedEvent that = (CacheEntryEvictedEvent) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "CacheEntryEvictedEvent{" +
            "key='" + key + '\'' +
            ", region='" + region + '\'' +
            ", reason='" + reason + '\'' +
            ", occurredAt=" + occurredAt +
            '}';
    }
}
