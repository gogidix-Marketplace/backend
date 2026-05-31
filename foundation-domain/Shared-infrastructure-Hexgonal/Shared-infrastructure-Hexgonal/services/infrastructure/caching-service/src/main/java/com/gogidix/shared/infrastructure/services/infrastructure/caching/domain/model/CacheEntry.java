package com.gogidix.shared.infrastructure.services.infrastructure.caching.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Domain entity representing a cache entry.
 */
@Document(collection = "cache_entries")
public class CacheEntry {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String key;

    private String value;

    private String serializedValue;

    private Long ttl;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private LocalDateTime lastAccessedAt;

    private Long accessCount;

    public CacheEntry() {}

    public CacheEntry(String tenantId, String key, String serializedValue, Long ttl,
                     LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.tenantId = tenantId;
        this.key = key;
        this.serializedValue = serializedValue;
        this.ttl = ttl;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.accessCount = 0L;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getSerializedValue() { return serializedValue; }
    public void setSerializedValue(String serializedValue) { this.serializedValue = serializedValue; }

    public Long getTtl() { return ttl; }
    public void setTtl(Long ttl) { this.ttl = ttl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public LocalDateTime getLastAccessedAt() { return lastAccessedAt; }
    public void setLastAccessedAt(LocalDateTime lastAccessedAt) { this.lastAccessedAt = lastAccessedAt; }

    public Long getAccessCount() { return accessCount; }
    public void setAccessCount(Long accessCount) { this.accessCount = accessCount; }

    /**
     * Check if the cache entry has expired.
     *
     * @return true if expired
     */
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    /**
     * Update the last accessed time and increment access count.
     */
    public void updateAccess() {
        this.lastAccessedAt = LocalDateTime.now();
        this.accessCount = this.accessCount == null ? 1L : this.accessCount + 1;
    }
}
