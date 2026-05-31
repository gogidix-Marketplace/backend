package com.gogidix.shared.utilities.domain.port.out;

import java.time.Duration;
import java.util.Optional;

/**
 * Cache service port for utility operations
 * Defines the contract for caching utility operation results
 */
public interface CacheService {
    
    /**
     * Stores a value in cache with TTL
     */
    void put(String key, Object value, Duration ttl);
    
    /**
     * Stores a value in cache with default TTL
     */
    void put(String key, Object value);
    
    /**
     * Retrieves a value from cache
     */
    <T> Optional<T> get(String key, Class<T> type);
    
    /**
     * Checks if a key exists in cache
     */
    boolean exists(String key);
    
    /**
     * Removes a value from cache
     */
    void evict(String key);
    
    /**
     * Removes all values from cache region
     */
    void evictRegion(String region);
    
    /**
     * Gets the remaining TTL for a key
     */
    Optional<Duration> getTtl(String key);
    
    /**
     * Extends the TTL for a key
     */
    boolean extend(String key, Duration additionalTime);
    
    /**
     * Gets cache statistics for a region
     */
    CacheStatistics getStatistics(String region);
    
    /**
     * Checks if cache is available
     */
    boolean isAvailable();
    
    /**
     * Cache statistics
     */
    class CacheStatistics {
        private final String region;
        private final long hitCount;
        private final long missCount;
        private final long evictionCount;
        private final double hitRate;
        private final long size;
        
        public CacheStatistics(String region, long hitCount, long missCount, 
                             long evictionCount, double hitRate, long size) {
            this.region = region;
            this.hitCount = hitCount;
            this.missCount = missCount;
            this.evictionCount = evictionCount;
            this.hitRate = hitRate;
            this.size = size;
        }
        
        public String getRegion() { return region; }
        public long getHitCount() { return hitCount; }
        public long getMissCount() { return missCount; }
        public long getEvictionCount() { return evictionCount; }
        public double getHitRate() { return hitRate; }
        public long getSize() { return size; }
        public long getTotalRequests() { return hitCount + missCount; }
    }
}