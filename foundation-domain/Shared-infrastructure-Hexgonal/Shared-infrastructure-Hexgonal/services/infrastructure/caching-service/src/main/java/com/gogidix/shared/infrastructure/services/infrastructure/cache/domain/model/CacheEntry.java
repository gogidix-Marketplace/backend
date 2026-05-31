package com.gogidix.shared.infrastructure.services.infrastructure.cache.domain.model;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * Cache Entry domain model.
 * Represents an entry in the cache.
 */
public class CacheEntry {

    private final String key;
    private final Object value;
    private final Instant createdAt;
    private final Instant expiresAt;
    private final Duration ttl;
    private final CacheType type;
    private final String region;

    private CacheEntry(Builder builder) {
        this.key = builder.key;
        this.value = builder.value;
        this.createdAt = builder.createdAt;
        this.expiresAt = builder.expiresAt;
        this.ttl = builder.ttl;
        this.type = builder.type;
        this.region = builder.region;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public String getKey() { return key; }
    public Object getValue() { return value; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getExpiresAt() { return expiresAt; }
    public Duration getTtl() { return ttl; }
    public CacheType getType() { return type; }
    public String getRegion() { return region; }

    public boolean isExpired() {
        return expiresAt != null && Instant.now().isAfter(expiresAt);
    }

    public long getTimeToLiveSeconds() {
        if (expiresAt == null) {
            return -1;
        }
        return Math.max(0, Duration.between(Instant.now(), expiresAt).getSeconds());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheEntry that = (CacheEntry) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "CacheEntry{" +
            "key='" + key + '\'' +
            ", type=" + type +
            ", region='" + region + '\'' +
            ", ttl=" + ttl +
            '}';
    }

    public enum CacheType {
        SIMPLE,
        LIST,
        HASH,
        SET,
        SORTED_SET,
        STREAM
    }

    public static class Builder {
        private String key;
        private Object value;
        private Instant createdAt;
        private Instant expiresAt;
        private Duration ttl;
        private CacheType type = CacheType.SIMPLE;
        private String region = "default";

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder value(Object value) {
            this.value = value;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder expiresAt(Instant expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public Builder ttl(Duration ttl) {
            this.ttl = ttl;
            this.expiresAt = ttl != null ? Instant.now().plus(ttl) : null;
            return this;
        }

        public Builder type(CacheType type) {
            this.type = type;
            return this;
        }

        public Builder region(String region) {
            this.region = region;
            return this;
        }

        public CacheEntry build() {
            return new CacheEntry(this);
        }
    }
}
