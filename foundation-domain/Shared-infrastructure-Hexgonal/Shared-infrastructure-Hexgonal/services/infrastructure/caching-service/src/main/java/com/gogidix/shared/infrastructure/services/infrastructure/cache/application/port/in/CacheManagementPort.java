package com.gogidix.shared.infrastructure.services.infrastructure.cache.application.port.in;

import com.gogidix.shared.infrastructure.services.infrastructure.cache.domain.model.CacheEntry;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Input port for cache management operations.
 * Defines the contract for caching use cases.
 */
public interface CacheManagementPort {

    /**
     * Put an entry in the cache.
     */
    CacheEntry put(String key, Object value, Duration ttl, String region);

    /**
     * Get an entry from the cache.
     */
    Optional<CacheEntry> get(String key);

    /**
     * Evict an entry from the cache.
     */
    boolean evict(String key);

    /**
     * Clear all entries in a region.
     */
    void clearRegion(String region);

    /**
     * Clear all cache entries.
     */
    void clearAll();

    /**
     * Check if a key exists in the cache.
     */
    boolean exists(String key);

    /**
     * Get all keys in a region.
     */
    Set<String> keys(String region);

    /**
     * Get the size of a region.
     */
    int size(String region);

    /**
     * Get cache statistics.
     */
    Map<String, Object> getStatistics();

    /**
     * Reset cache statistics.
     */
    void resetStatistics();
}
