package com.gogidix.shared.infrastructure.services.infrastructure.cache.application.service;

import com.gogidix.shared.infrastructure.services.infrastructure.cache.application.port.in.CacheManagementPort;
import com.gogidix.shared.infrastructure.services.infrastructure.cache.domain.aggregate.CacheRegistry;
import com.gogidix.shared.infrastructure.services.infrastructure.cache.domain.model.CacheEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Application service for cache management.
 * Implements the hexagonal architecture pattern.
 */
@Service
public class CacheManagementService implements CacheManagementPort {

    private static final Logger log = LoggerFactory.getLogger(CacheManagementService.class);

    private final CacheRegistry cacheRegistry;

    public CacheManagementService(CacheRegistry cacheRegistry) {
        this.cacheRegistry = cacheRegistry;
    }

    @Override
    public CacheEntry put(String key, Object value, Duration ttl, String region) {
        log.debug("Putting cache entry: key={}, region={}, ttl={}", key, region, ttl);
        return cacheRegistry.put(key, value, ttl, region);
    }

    @Override
    public Optional<CacheEntry> get(String key) {
        return cacheRegistry.get(key);
    }

    @Override
    public boolean evict(String key) {
        log.debug("Evicting cache entry: key={}", key);
        return cacheRegistry.evict(key);
    }

    @Override
    public void clearRegion(String region) {
        log.info("Clearing cache region: {}", region);
        cacheRegistry.clearRegion(region);
    }

    @Override
    public void clearAll() {
        log.info("Clearing all cache");
        cacheRegistry.clearAll();
    }

    @Override
    public boolean exists(String key) {
        return cacheRegistry.exists(key);
    }

    @Override
    public Set<String> keys(String region) {
        return cacheRegistry.keys(region);
    }

    @Override
    public int size(String region) {
        return cacheRegistry.size(region);
    }

    @Override
    public Map<String, Object> getStatistics() {
        return cacheRegistry.getStatistics();
    }

    @Override
    public void resetStatistics() {
        log.info("Resetting cache statistics");
        cacheRegistry.resetStatistics();
    }
}
