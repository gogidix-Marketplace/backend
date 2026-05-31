package com.gogidix.shared.infrastructure.services.infrastructure.caching.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CacheEntry domain model.
 */
@DisplayName("CacheEntry Domain Model Tests")
class CacheEntryTest {

    @Test
    @DisplayName("Should create cache entry with constructor")
    void shouldCreateCacheEntryWithConstructor() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusSeconds(3600);

        CacheEntry entry = new CacheEntry("tenant123", "key1", "serialized-value", 3600L, now, expiresAt);

        assertEquals("tenant123", entry.getTenantId());
        assertEquals("key1", entry.getKey());
        assertEquals("serialized-value", entry.getSerializedValue());
        assertEquals(3600L, entry.getTtl());
        assertEquals(now, entry.getCreatedAt());
        assertEquals(expiresAt, entry.getExpiresAt());
        assertEquals(0L, entry.getAccessCount());
    }

    @Test
    @DisplayName("Should create cache entry with no-args constructor")
    void shouldCreateCacheEntryWithNoArgsConstructor() {
        CacheEntry entry = new CacheEntry();

        assertNotNull(entry);
        assertNull(entry.getTenantId());
        assertNull(entry.getKey());
        assertNull(entry.getSerializedValue());
        assertNull(entry.getTtl());
        assertNull(entry.getAccessCount());
    }

    @Test
    @DisplayName("Should set and get all fields")
    void shouldSetAndGetAllFields() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusSeconds(7200);

        CacheEntry entry = new CacheEntry();
        entry.setId("entry123");
        entry.setTenantId("tenant123");
        entry.setKey("test-key");
        entry.setValue("raw-value");
        entry.setSerializedValue("{\"data\":\"value\"}");
        entry.setTtl(7200L);
        entry.setCreatedAt(now);
        entry.setExpiresAt(expiresAt);
        entry.setLastAccessedAt(now);
        entry.setAccessCount(5L);

        assertEquals("entry123", entry.getId());
        assertEquals("tenant123", entry.getTenantId());
        assertEquals("test-key", entry.getKey());
        assertEquals("raw-value", entry.getValue());
        assertEquals("{\"data\":\"value\"}", entry.getSerializedValue());
        assertEquals(7200L, entry.getTtl());
        assertEquals(now, entry.getCreatedAt());
        assertEquals(expiresAt, entry.getExpiresAt());
        assertEquals(now, entry.getLastAccessedAt());
        assertEquals(5L, entry.getAccessCount());
    }

    @Test
    @DisplayName("Should check if cache entry is expired")
    void shouldCheckIfCacheEntryIsExpired() {
        CacheEntry entry = new CacheEntry();
        entry.setExpiresAt(LocalDateTime.now().minusMinutes(1));

        assertTrue(entry.isExpired());

        entry.setExpiresAt(LocalDateTime.now().plusMinutes(1));
        assertFalse(entry.isExpired());

        entry.setExpiresAt(null);
        assertFalse(entry.isExpired());
    }

    @Test
    @DisplayName("Should update access on cache hit")
    void shouldUpdateAccessOnCacheHit() {
        CacheEntry entry = new CacheEntry();
        entry.setAccessCount(0L);
        entry.setLastAccessedAt(null);

        entry.updateAccess();

        assertEquals(1L, entry.getAccessCount());
        assertNotNull(entry.getLastAccessedAt());

        entry.updateAccess();

        assertEquals(2L, entry.getAccessCount());
    }

    @Test
    @DisplayName("Should handle access count increment")
    void shouldHandleAccessCountIncrement() {
        CacheEntry entry = new CacheEntry();
        entry.setAccessCount(10L);

        assertEquals(10L, entry.getAccessCount());

        entry.updateAccess();

        assertEquals(11L, entry.getAccessCount());
    }

    @Test
    @DisplayName("Should handle last accessed at timestamp")
    void shouldHandleLastAccessedAtTimestamp() {
        CacheEntry entry = new CacheEntry();
        LocalDateTime timestamp = LocalDateTime.now();

        entry.setLastAccessedAt(timestamp);

        assertEquals(timestamp, entry.getLastAccessedAt());
    }

    @Test
    @DisplayName("Should handle TTL")
    void shouldHandleTtl() {
        CacheEntry entry = new CacheEntry();
        entry.setTtl(3600L);

        assertEquals(3600L, entry.getTtl());

        entry.setTtl(7200L);
        assertEquals(7200L, entry.getTtl());
    }

    @Test
    @DisplayName("Should handle expiration time")
    void shouldHandleExpirationTime() {
        CacheEntry entry = new CacheEntry();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(2);

        entry.setExpiresAt(expiresAt);

        assertEquals(expiresAt, entry.getExpiresAt());
    }

    @Test
    @DisplayName("Should handle created at timestamp")
    void shouldHandleCreatedAtTimestamp() {
        CacheEntry entry = new CacheEntry();
        LocalDateTime createdAt = LocalDateTime.now();

        entry.setCreatedAt(createdAt);

        assertEquals(createdAt, entry.getCreatedAt());
    }

    @Test
    @DisplayName("Should handle raw value")
    void shouldHandleRawValue() {
        CacheEntry entry = new CacheEntry();
        entry.setValue("raw cache value");

        assertEquals("raw cache value", entry.getValue());
    }

    @Test
    @DisplayName("Should handle serialized value")
    void shouldHandleSerializedValue() {
        CacheEntry entry = new CacheEntry();
        entry.setSerializedValue("{\"key\":\"value\"}");

        assertEquals("{\"key\":\"value\"}", entry.getSerializedValue());
    }

    @Test
    @DisplayName("Should check expiration with current time")
    void shouldCheckExpirationWithCurrentTime() {
        CacheEntry entry = new CacheEntry();
        entry.setExpiresAt(LocalDateTime.now().plusSeconds(1));

        // Just before expiration
        assertFalse(entry.isExpired());

        // This test may have timing issues but demonstrates the concept
        // In real tests, you would use a clock abstraction
    }

    @Test
    @DisplayName("Should handle null access count initialization")
    void shouldHandleNullAccessCountInitialization() {
        CacheEntry entry = new CacheEntry();
        entry.setAccessCount(null);

        entry.updateAccess();

        assertEquals(1L, entry.getAccessCount());
    }

    @Test
    @DisplayName("Should handle ID field")
    void shouldHandleIdField() {
        CacheEntry entry = new CacheEntry();
        entry.setId("cache-entry-123");

        assertEquals("cache-entry-123", entry.getId());
    }

    @Test
    @DisplayName("Should handle tenant ID")
    void shouldHandleTenantId() {
        CacheEntry entry = new CacheEntry();
        entry.setTenantId("tenant-abc");

        assertEquals("tenant-abc", entry.getTenantId());
    }

    @Test
    @DisplayName("Should handle cache key")
    void shouldHandleCacheKey() {
        CacheEntry entry = new CacheEntry();
        entry.setKey("user:123:profile");

        assertEquals("user:123:profile", entry.getKey());
    }
}
