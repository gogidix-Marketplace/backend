package com.gogidix.aiservices.aiprediction.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Domain service for caching prediction results.
 */
@Slf4j
@Service
public class PredictionCacheService {

    private static final int DEFAULT_TTL_SECONDS = 3600; // 1 hour
    private final int ttlSeconds;
    private final Map<String, CacheEntry> cache;

    public PredictionCacheService() {
        this(DEFAULT_TTL_SECONDS);
    }

    public PredictionCacheService(int ttlSeconds) {
        this.ttlSeconds = ttlSeconds;
        this.cache = new ConcurrentHashMap<>();
    }

    public void put(String key, PredictionResult result) {
        if (key != null && result != null) {
            cache.put(key, new CacheEntry(result, LocalDateTime.now().plusSeconds(ttlSeconds)));
            log.debug("Cached prediction result with key: {}", key);
        }
    }

    public Optional<PredictionResult> get(String key) {
        if (key == null) {
            return Optional.empty();
        }

        CacheEntry entry = cache.get(key);
        if (entry == null) {
            return Optional.empty();
        }

        if (entry.expiry.isBefore(LocalDateTime.now())) {
            cache.remove(key);
            log.debug("Cache entry expired for key: {}", key);
            return Optional.empty();
        }

        return Optional.of(entry.result);
    }

    public void invalidate(String key) {
        cache.remove(key);
        log.debug("Invalidated cache entry for key: {}", key);
    }

    public void clear() {
        cache.clear();
        log.debug("Cleared all cache entries");
    }

    public int size() {
        return cache.size();
    }

    public String generateCacheKey(String modelId, Map<String, Object> inputData) {
        int hash = inputData.hashCode();
        return modelId + ":" + hash;
    }

    private record CacheEntry(PredictionResult result, LocalDateTime expiry) {
    }
}
