package com.gogidix.shared.utilities.infrastructure.adapter.out.cache;

import com.gogidix.shared.utilities.application.port.out.CachePort;
import com.gogidix.shared.utilities.domain.model.UtilityResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Cache adapter implementation using Spring Cache abstraction
 * Provides caching capabilities for utility operations
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CacheAdapter implements CachePort {

    private final CacheManager cacheManager;
    private static final String CACHE_NAME = "utility-cache";

    @Override
    public UtilityResult<?> get(String key) {
        try {
            Cache cache = cacheManager.getCache(CACHE_NAME);
            if (cache != null) {
                Cache.ValueWrapper valueWrapper = cache.get(key);
                if (valueWrapper != null) {
                    @SuppressWarnings("unchecked")
                    UtilityResult<?> result = (UtilityResult<?>) valueWrapper.get();
                    log.debug("Cache hit for key: {}", key);
                    return result;
                }
            }
            log.debug("Cache miss for key: {}", key);
            return null;
        } catch (Exception e) {
            log.warn("Error retrieving from cache for key: {}", key, e);
            return null;
        }
    }

    @Override
    public void put(String key, UtilityResult<?> result, long ttlSeconds) {
        try {
            Cache cache = cacheManager.getCache(CACHE_NAME);
            if (cache != null) {
                // Note: TTL handling depends on the cache implementation
                // This is a simplified version - actual TTL would be configured in cache manager
                cache.put(key, result);
                log.debug("Cached result for key: {} with TTL: {}s", key, ttlSeconds);
            }
        } catch (Exception e) {
            log.warn("Error caching result for key: {}", key, e);
        }
    }

    @Override
    public void remove(String key) {
        try {
            Cache cache = cacheManager.getCache(CACHE_NAME);
            if (cache != null) {
                cache.evict(key);
                log.debug("Evicted cache entry for key: {}", key);
            }
        } catch (Exception e) {
            log.warn("Error removing from cache for key: {}", key, e);
        }
    }

    @Override
    public void clear() {
        try {
            Cache cache = cacheManager.getCache(CACHE_NAME);
            if (cache != null) {
                cache.clear();
                log.info("Cleared all cache entries for utility service");
            }
        } catch (Exception e) {
            log.warn("Error clearing cache", e);
        }
    }

    @Override
    public boolean exists(String key) {
        try {
            Cache cache = cacheManager.getCache(CACHE_NAME);
            if (cache != null) {
                Cache.ValueWrapper valueWrapper = cache.get(key);
                return valueWrapper != null;
            }
            return false;
        } catch (Exception e) {
            log.warn("Error checking cache existence for key: {}", key, e);
            return false;
        }
    }

    @Override
    public String getStatistics() {
        try {
            // This is a simplified implementation
            // In a real scenario, you'd gather actual cache statistics
            return String.format("Cache: %s, Status: Active, Implementation: Spring Cache", CACHE_NAME);
        } catch (Exception e) {
            log.warn("Error retrieving cache statistics", e);
            return "Cache statistics unavailable";
        }
    }
}