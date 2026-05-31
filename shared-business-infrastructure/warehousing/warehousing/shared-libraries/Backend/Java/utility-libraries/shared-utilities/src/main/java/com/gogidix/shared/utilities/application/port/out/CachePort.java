package com.gogidix.shared.utilities.application.port.out;

import com.gogidix.shared.utilities.domain.model.UtilityResult;

/**
 * Port for cache operations
 * Defines the contract for caching utility results
 */
public interface CachePort {

    /**
     * Get cached result by key
     * 
     * @param key Cache key
     * @return Cached UtilityResult or null if not found
     */
    UtilityResult<?> get(String key);

    /**
     * Put result in cache with TTL
     * 
     * @param key Cache key
     * @param result Result to cache
     * @param ttlSeconds Time to live in seconds
     */
    void put(String key, UtilityResult<?> result, long ttlSeconds);

    /**
     * Remove result from cache
     * 
     * @param key Cache key
     */
    void remove(String key);

    /**
     * Clear all cache entries for utility service
     */
    void clear();

    /**
     * Check if key exists in cache
     * 
     * @param key Cache key
     * @return true if key exists, false otherwise
     */
    boolean exists(String key);

    /**
     * Get cache statistics
     * 
     * @return Cache statistics as a string
     */
    String getStatistics();
}