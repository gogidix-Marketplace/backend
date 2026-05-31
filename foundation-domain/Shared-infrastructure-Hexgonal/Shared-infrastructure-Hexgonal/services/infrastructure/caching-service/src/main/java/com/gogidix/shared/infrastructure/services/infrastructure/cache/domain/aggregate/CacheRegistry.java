package com.gogidix.shared.infrastructure.services.infrastructure.cache.domain.aggregate;

import com.gogidix.shared.infrastructure.services.infrastructure.cache.domain.event.CacheEntryCreatedEvent;
import com.gogidix.shared.infrastructure.services.infrastructure.cache.domain.event.CacheEntryEvictedEvent;
import com.gogidix.shared.infrastructure.services.infrastructure.cache.domain.event.CacheInvalidatedEvent;
import com.gogidix.shared.infrastructure.services.infrastructure.cache.domain.model.CacheEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Cache Registry Aggregate Root.
 * Manages caching operations and publishes domain events.
 */
public class CacheRegistry {

    private static final Logger log = LoggerFactory.getLogger(CacheRegistry.class);

    // In-memory cache registry (backed by Redis in production)
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> regionIndex = new ConcurrentHashMap<>();

    // Statistics
    private volatile long hitCount = 0;
    private volatile long missCount = 0;
    private volatile long evictionCount = 0;

    // Event handlers
    private final Map<String, Consumer<CacheEntryCreatedEvent>> entryCreatedHandlers = new ConcurrentHashMap<>();
    private final Map<String, Consumer<CacheEntryEvictedEvent>> entryEvictedHandlers = new ConcurrentHashMap<>();
    private final Map<String, Consumer<CacheInvalidatedEvent>> cacheInvalidatedHandlers = new ConcurrentHashMap<>();

    public CacheRegistry() {
        log.info("CacheRegistry aggregate initialized");
    }

    // Event handler registration
    public void onCacheEntryCreated(Consumer<CacheEntryCreatedEvent> handler) {
        String handlerId = UUID.randomUUID().toString();
        entryCreatedHandlers.put(handlerId, handler);
    }

    public void onCacheEntryEvicted(Consumer<CacheEntryEvictedEvent> handler) {
        String handlerId = UUID.randomUUID().toString();
        entryEvictedHandlers.put(handlerId, handler);
    }

    public void onCacheInvalidated(Consumer<CacheInvalidatedEvent> handler) {
        String handlerId = UUID.randomUUID().toString();
        cacheInvalidatedHandlers.put(handlerId, handler);
    }

    // Cache operations
    public CacheEntry put(String key, Object value, Duration ttl, String region) {
        log.debug("Putting entry in cache: key={}, region={}, ttl={}", key, region, ttl);

        Instant now = Instant.now();
        CacheEntry entry = CacheEntry.builder()
            .key(key)
            .value(value)
            .createdAt(now)
            .ttl(ttl)
            .region(region)
            .build();

        cache.put(key, entry);
        regionIndex.computeIfAbsent(region, k -> ConcurrentHashMap.newKeySet()).add(key);

        CacheEntryCreatedEvent event = new CacheEntryCreatedEvent(
            key,
            region,
            ttl != null ? ttl.getSeconds() : -1,
            now
        );
        publishEvent(event);

        return entry;
    }

    public Optional<CacheEntry> get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null) {
            missCount++;
            log.debug("Cache miss: key={}", key);
            return Optional.empty();
        }

        if (entry.isExpired()) {
            evict(key);
            missCount++;
            log.debug("Cache entry expired: key={}", key);
            return Optional.empty();
        }

        hitCount++;
        log.debug("Cache hit: key={}", key);
        return Optional.of(entry);
    }

    public boolean evict(String key) {
        CacheEntry entry = cache.remove(key);
        if (entry != null) {
            regionIndex.getOrDefault(entry.getRegion(), Collections.emptySet()).remove(key);
            evictionCount++;

            CacheEntryEvictedEvent event = new CacheEntryEvictedEvent(
                key,
                entry.getRegion(),
                "MANUAL",
                Instant.now()
            );
            publishEvent(event);

            log.debug("Cache entry evicted: key={}", key);
            return true;
        }
        return false;
    }

    public void clearRegion(String region) {
        log.info("Clearing cache region: {}", region);
        Set<String> keys = regionIndex.getOrDefault(region, Collections.emptySet());
        keys.forEach(this::evict);
        regionIndex.remove(region);

        CacheInvalidatedEvent event = new CacheInvalidatedEvent(
            region,
            "REGION",
            keys.size(),
            Instant.now()
        );
        publishEvent(event);
    }

    public void clearAll() {
        log.info("Clearing all cache");
        int totalEntries = cache.size();
        cache.clear();
        regionIndex.clear();

        CacheInvalidatedEvent event = new CacheInvalidatedEvent(
            "ALL",
            "GLOBAL",
            totalEntries,
            Instant.now()
        );
        publishEvent(event);
    }

    public boolean exists(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null) {
            return false;
        }
        if (entry.isExpired()) {
            evict(key);
            return false;
        }
        return true;
    }

    public Set<String> keys(String region) {
        Set<String> regionKeys = regionIndex.get(region);
        if (regionKeys == null) {
            return Collections.emptySet();
        }
        // Filter expired entries
        Set<String> validKeys = new HashSet<>();
        for (String key : regionKeys) {
            CacheEntry entry = cache.get(key);
            if (entry != null && !entry.isExpired()) {
                validKeys.add(key);
            }
        }
        return validKeys;
    }

    public int size(String region) {
        return keys(region).size();
    }

    public int size() {
        return cache.size();
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("hitCount", hitCount);
        stats.put("missCount", missCount);
        stats.put("evictionCount", evictionCount);
        stats.put("size", cache.size());
        stats.put("regions", regionIndex.size());

        long total = hitCount + missCount;
        double hitRate = total > 0 ? (double) hitCount / total : 0.0;
        stats.put("hitRate", hitRate);

        return stats;
    }

    public void resetStatistics() {
        hitCount = 0;
        missCount = 0;
        evictionCount = 0;
        log.info("Cache statistics reset");
    }

    private void publishEvent(CacheEntryCreatedEvent event) {
        try {
            entryCreatedHandlers.values().forEach(handler -> {
                try {
                    handler.accept(event);
                } catch (Exception e) {
                    log.error("Error in cache entry created handler", e);
                }
            });
        } catch (Exception e) {
            log.error("Failed to publish CacheEntryCreatedEvent", e);
        }
    }

    private void publishEvent(CacheEntryEvictedEvent event) {
        try {
            entryEvictedHandlers.values().forEach(handler -> {
                try {
                    handler.accept(event);
                } catch (Exception e) {
                    log.error("Error in cache entry evicted handler", e);
                }
            });
        } catch (Exception e) {
            log.error("Failed to publish CacheEntryEvictedEvent", e);
        }
    }

    private void publishEvent(CacheInvalidatedEvent event) {
        try {
            cacheInvalidatedHandlers.values().forEach(handler -> {
                try {
                    handler.accept(event);
                } catch (Exception e) {
                    log.error("Error in cache invalidated handler", e);
                }
            });
        } catch (Exception e) {
            log.error("Failed to publish CacheInvalidatedEvent", e);
        }
    }
}
