package com.gogidix.shared.infrastructure.services.infrastructure.caching.domain.port.in;

import java.util.Map;
import java.util.Set;

/**
 * Input port for Cache use cases.
 * Defines the contract for application services to interact with the domain.
 */
public interface CachePort {

    /**
     * Put a value in the cache.
     *
     * @param key the cache key
     * @param value the value to cache
     * @param ttl time to live in seconds
     */
    void put(String key, Object value, long ttl);

    /**
     * Put a value in the cache with default TTL.
     *
     * @param key the cache key
     * @param value the value to cache
     */
    void put(String key, Object value);

    /**
     * Get a value from the cache.
     *
     * @param key the cache key
     * @return the cached value, or null if not found or expired
     */
    Object get(String key);

    /**
     * Get a value from the cache with type casting.
     *
     * @param key the cache key
     * @param type the expected type
     * @param <T> the type parameter
     * @return the cached value, or null if not found or expired
     */
    <T> T get(String key, Class<T> type);

    /**
     * Evict a specific entry from the cache.
     *
     * @param key the cache key
     */
    void evict(String key);

    /**
     * Clear all cache entries for the current tenant.
     */
    void clear();

    /**
     * Put multiple values in the cache.
     *
     * @param entries map of keys to values
     * @param ttl time to live in seconds
     */
    void putAll(Map<String, Object> entries, long ttl);

    /**
     * Get multiple values from the cache.
     *
     * @param keys set of cache keys
     * @return map of keys to values
     */
    Map<String, Object> getAll(Set<String> keys);

    /**
     * Evict multiple entries from the cache.
     *
     * @param keys set of cache keys
     */
    void evictAll(Set<String> keys);

    /**
     * Check if a key exists in the cache.
     *
     * @param key the cache key
     * @return true if the key exists and is not expired
     */
    boolean exists(String key);

    /**
     * Get the TTL for a cache key.
     *
     * @param key the cache key
     * @return remaining TTL in seconds, or -1 if not found
     */
    long getTtl(String key);

    /**
     * Get cache statistics for the current tenant.
     *
     * @return map containing cache stats
     */
    Map<String, Object> getStats();
}
