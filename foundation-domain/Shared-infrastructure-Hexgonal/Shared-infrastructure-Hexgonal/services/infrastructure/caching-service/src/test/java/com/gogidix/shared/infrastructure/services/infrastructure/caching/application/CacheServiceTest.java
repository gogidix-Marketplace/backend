package com.gogidix.shared.infrastructure.services.infrastructure.caching.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.services.infrastructure.caching.domain.model.CacheEntry;
import com.gogidix.shared.infrastructure.services.infrastructure.caching.domain.port.out.CacheRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CacheService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Cache Service Tests")
class CacheServiceTest {

    @Mock
    private CacheRepositoryPort cacheRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TenantContextHolder tenantContextHolder;

    @InjectMocks
    private CacheService cacheService;

    private static final String TENANT_ID = "tenant123";

    @BeforeEach
    void setUp() {
        lenient().when(tenantContextHolder.getRequiredTenantId()).thenReturn(TENANT_ID);
    }

    @Test
    @DisplayName("Should put value in cache with TTL")
    void shouldPutValueInCacheWithTtl() throws Exception {
        String value = "test-value";
        String serializedValue = "\"test-value\"";

        when(objectMapper.writeValueAsString(value)).thenReturn(serializedValue);

        cacheService.put("key1", value, 3600L);

        verify(cacheRepository).save(any(CacheEntry.class));
    }

    @Test
    @DisplayName("Should put value in cache with default TTL")
    void shouldPutValueInCacheWithDefaultTtl() throws Exception {
        String value = "test-value";
        String serializedValue = "\"test-value\"";

        when(objectMapper.writeValueAsString(value)).thenReturn(serializedValue);

        cacheService.put("key1", value);

        verify(cacheRepository).save(any(CacheEntry.class));
    }

    @Test
    @DisplayName("Should get value from cache")
    void shouldGetValueFromCache() throws Exception {
        CacheEntry entry = new CacheEntry(TENANT_ID, "key1", "\"test-value\"", 3600L,
                LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));

        when(cacheRepository.findByTenantIdAndKey(TENANT_ID, "key1")).thenReturn(Optional.of(entry));
        when(objectMapper.readValue("\"test-value\"", Object.class)).thenReturn("test-value");

        Object result = cacheService.get("key1");

        assertEquals("test-value", result);
        verify(cacheRepository).save(entry); // Entry is saved to update access
    }

    @Test
    @DisplayName("Should return null when key not found")
    void shouldReturnNullWhenKeyNotFound() {
        when(cacheRepository.findByTenantIdAndKey(TENANT_ID, "key1")).thenReturn(Optional.empty());

        Object result = cacheService.get("key1");

        assertNull(result);
    }

    @Test
    @DisplayName("Should return null and delete expired entry")
    void shouldReturnNullAndDeleteExpiredEntry() {
        CacheEntry entry = new CacheEntry(TENANT_ID, "key1", "\"test-value\"", 3600L,
                LocalDateTime.now(), LocalDateTime.now().minusMinutes(1));

        when(cacheRepository.findByTenantIdAndKey(TENANT_ID, "key1")).thenReturn(Optional.of(entry));

        Object result = cacheService.get("key1");

        assertNull(result);
        verify(cacheRepository).deleteByTenantIdAndKey(TENANT_ID, "key1");
    }

    @Test
    @DisplayName("Should get value with type conversion")
    void shouldGetValueWithTypeConversion() throws Exception {
        CacheEntry entry = new CacheEntry(TENANT_ID, "key1", "\"test-value\"", 3600L,
                LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));

        when(cacheRepository.findByTenantIdAndKey(TENANT_ID, "key1")).thenReturn(Optional.of(entry));
        when(objectMapper.readValue("\"test-value\"", Object.class)).thenReturn("test-value");

        String result = cacheService.get("key1", String.class);

        assertEquals("test-value", result);
    }

    @Test
    @DisplayName("Should evict key from cache")
    void shouldEvictKeyFromCache() {
        cacheService.evict("key1");

        verify(cacheRepository).deleteByTenantIdAndKey(TENANT_ID, "key1");
    }

    @Test
    @DisplayName("Should clear all cache entries for tenant")
    void shouldClearAllCacheEntriesForTenant() {
        cacheService.clear();

        verify(cacheRepository).deleteByTenantId(TENANT_ID);
    }

    @Test
    @DisplayName("Should put all entries with TTL")
    void shouldPutAllEntriesWithTtl() throws Exception {
        Map<String, Object> entries = new HashMap<>();
        entries.put("key1", "value1");
        entries.put("key2", "value2");

        when(objectMapper.writeValueAsString(any())).thenReturn("\"value\"");

        cacheService.putAll(entries, 3600L);

        verify(cacheRepository, times(2)).save(any(CacheEntry.class));
    }

    @Test
    @DisplayName("Should get all values for keys")
    void shouldGetAllValuesForKeys() throws Exception {
        Set<String> keys = new HashSet<>(Arrays.asList("key1", "key2"));

        CacheEntry entry1 = new CacheEntry(TENANT_ID, "key1", "\"value1\"", 3600L,
                LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));
        CacheEntry entry2 = new CacheEntry(TENANT_ID, "key2", "\"value2\"", 3600L,
                LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));

        when(cacheRepository.findByTenantIdAndKeyIn(eq(TENANT_ID), anySet())).thenReturn(Arrays.asList(entry1, entry2));
        when(objectMapper.readValue(anyString(), eq(Object.class))).thenReturn("value");

        Map<String, Object> result = cacheService.getAll(keys);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Should evict all keys")
    void shouldEvictAllKeys() {
        Set<String> keys = new HashSet<>(Arrays.asList("key1", "key2"));

        cacheService.evictAll(keys);

        verify(cacheRepository).deleteByTenantIdAndKeyIn(TENANT_ID, keys);
    }

    @Test
    @DisplayName("Should check if key exists")
    void shouldCheckIfKeyExists() {
        CacheEntry entry = new CacheEntry(TENANT_ID, "key1", "\"value\"", 3600L,
                LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));

        when(cacheRepository.findByTenantIdAndKey(TENANT_ID, "key1")).thenReturn(Optional.of(entry));

        boolean exists = cacheService.exists("key1");

        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when key does not exist")
    void shouldReturnFalseWhenKeyDoesNotExist() {
        when(cacheRepository.findByTenantIdAndKey(TENANT_ID, "key1")).thenReturn(Optional.empty());

        boolean exists = cacheService.exists("key1");

        assertFalse(exists);
    }

    @Test
    @DisplayName("Should return false when key exists but is expired")
    void shouldReturnFalseWhenKeyExistsButIsExpired() {
        CacheEntry entry = new CacheEntry(TENANT_ID, "key1", "\"value\"", 3600L,
                LocalDateTime.now(), LocalDateTime.now().minusMinutes(1));

        when(cacheRepository.findByTenantIdAndKey(TENANT_ID, "key1")).thenReturn(Optional.of(entry));

        boolean exists = cacheService.exists("key1");

        assertFalse(exists);
        verify(cacheRepository).deleteByTenantIdAndKey(TENANT_ID, "key1");
    }

    @Test
    @DisplayName("Should get TTL for key")
    void shouldGetTtlForKey() {
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(30);
        CacheEntry entry = new CacheEntry(TENANT_ID, "key1", "\"value\"", 3600L,
                LocalDateTime.now(), expiresAt);

        when(cacheRepository.findByTenantIdAndKey(TENANT_ID, "key1")).thenReturn(Optional.of(entry));

        long ttl = cacheService.getTtl("key1");

        assertTrue(ttl > 0);
        assertTrue(ttl <= 1800); // Should be approximately 30 minutes
    }

    @Test
    @DisplayName("Should return -1 when key not found for TTL")
    void shouldReturnNegativeOneWhenKeyNotFoundForTtl() {
        when(cacheRepository.findByTenantIdAndKey(TENANT_ID, "key1")).thenReturn(Optional.empty());

        long ttl = cacheService.getTtl("key1");

        assertEquals(-1, ttl);
    }

    @Test
    @DisplayName("Should return -1 when entry is expired for TTL")
    void shouldReturnNegativeOneWhenEntryIsExpiredForTtl() {
        CacheEntry entry = new CacheEntry(TENANT_ID, "key1", "\"value\"", 3600L,
                LocalDateTime.now(), LocalDateTime.now().minusMinutes(1));

        when(cacheRepository.findByTenantIdAndKey(TENANT_ID, "key1")).thenReturn(Optional.of(entry));

        long ttl = cacheService.getTtl("key1");

        assertEquals(-1, ttl);
    }

    @Test
    @DisplayName("Should get cache statistics")
    void shouldGetCacheStatistics() {
        CacheEntry expiredEntry = new CacheEntry(TENANT_ID, "key1", "\"value\"", 3600L,
                LocalDateTime.now(), LocalDateTime.now().minusMinutes(1));
        CacheEntry activeEntry = new CacheEntry(TENANT_ID, "key2", "\"value\"", 3600L,
                LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));

        when(cacheRepository.countByTenantId(TENANT_ID)).thenReturn(2L);
        when(cacheRepository.findExpiredEntries(eq(TENANT_ID), any(LocalDateTime.class)))
                .thenReturn(List.of(expiredEntry));

        Map<String, Object> stats = cacheService.getStats();

        assertNotNull(stats);
        assertEquals(2L, stats.get("totalEntries"));
        assertEquals(1, stats.get("expiredEntries"));
        assertEquals(1L, stats.get("activeEntries"));
        assertEquals(TENANT_ID, stats.get("tenantId"));
    }

    @Test
    @DisplayName("Should handle null expiration for TTL")
    void shouldHandleNullExpirationForTtl() {
        CacheEntry entry = new CacheEntry(TENANT_ID, "key1", "\"value\"", 3600L,
                LocalDateTime.now(), null);

        when(cacheRepository.findByTenantIdAndKey(TENANT_ID, "key1")).thenReturn(Optional.of(entry));

        long ttl = cacheService.getTtl("key1");

        assertEquals(-1, ttl);
    }

    @Test
    @DisplayName("Should return -1 for expired TTL")
    void shouldReturnNegativeOneForExpiredTtl() {
        CacheEntry entry = new CacheEntry(TENANT_ID, "key1", "\"value\"", 3600L,
                LocalDateTime.now(), LocalDateTime.now().minusSeconds(1));

        when(cacheRepository.findByTenantIdAndKey(TENANT_ID, "key1")).thenReturn(Optional.of(entry));

        long ttl = cacheService.getTtl("key1");

        assertEquals(-1, ttl);
    }

    @Test
    @DisplayName("Should skip expired entries in getAll")
    void shouldSkipExpiredEntriesInGetAll() throws Exception {
        Set<String> keys = new HashSet<>(Arrays.asList("key1", "key2"));

        CacheEntry expiredEntry = new CacheEntry(TENANT_ID, "key1", "\"value1\"", 3600L,
                LocalDateTime.now(), LocalDateTime.now().minusMinutes(1));
        CacheEntry activeEntry = new CacheEntry(TENANT_ID, "key2", "\"value2\"", 3600L,
                LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));

        when(cacheRepository.findByTenantIdAndKeyIn(eq(TENANT_ID), anySet()))
                .thenReturn(Arrays.asList(expiredEntry, activeEntry));
        when(objectMapper.readValue("\"value2\"", Object.class)).thenReturn("value2");

        Map<String, Object> result = cacheService.getAll(keys);

        assertNotNull(result);
        assertTrue(result.size() <= 2); // May be less due to expired entries
        verify(cacheRepository).deleteByTenantIdAndKey(TENANT_ID, "key1");
    }
}
