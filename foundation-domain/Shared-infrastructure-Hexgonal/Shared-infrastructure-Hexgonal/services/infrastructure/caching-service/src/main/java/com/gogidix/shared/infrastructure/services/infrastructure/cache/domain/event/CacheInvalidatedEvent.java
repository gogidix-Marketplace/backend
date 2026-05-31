package com.gogidix.shared.infrastructure.services.infrastructure.cache.domain.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event published when cache is invalidated.
 */
public class CacheInvalidatedEvent {

    private final String scope;
    private final String scopeType;
    private final int entriesAffected;
    private final Instant occurredAt;

    public CacheInvalidatedEvent(String scope, String scopeType, int entriesAffected, Instant occurredAt) {
        this.scope = scope;
        this.scopeType = scopeType;
        this.entriesAffected = entriesAffected;
        this.occurredAt = occurredAt;
    }

    public String getScope() {
        return scope;
    }

    public String getScopeType() {
        return scopeType;
    }

    public int getEntriesAffected() {
        return entriesAffected;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheInvalidatedEvent that = (CacheInvalidatedEvent) o;
        return Objects.equals(scope, that.scope) && Objects.equals(scopeType, that.scopeType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scope, scopeType);
    }

    @Override
    public String toString() {
        return "CacheInvalidatedEvent{" +
            "scope='" + scope + '\'' +
            ", scopeType='" + scopeType + '\'' +
            ", entriesAffected=" + entriesAffected +
            ", occurredAt=" + occurredAt +
            '}';
    }
}
