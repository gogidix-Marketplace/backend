package com.gogidix.universal.tracking.infrastructure.persistence.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

/**
 * Redis cache service for tracking data.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TrackingCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String SESSION_CACHE_PREFIX = "tracking:session:";
    private static final String EVENT_CACHE_PREFIX = "tracking:event:";
    private static final Duration DEFAULT_TTL = Duration.ofMinutes(30);

    /**
     * Cache session data
     */
    public void cacheSession(String sessionId, Object sessionData) {
        String key = SESSION_CACHE_PREFIX + sessionId;
        redisTemplate.opsForValue().set(key, sessionData, DEFAULT_TTL);
        log.debug("Cached session: {}", sessionId);
    }

    /**
     * Get cached session data
     */
    public Optional<Object> getCachedSession(String sessionId) {
        String key = SESSION_CACHE_PREFIX + sessionId;
        Object data = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(data);
    }

    /**
     * Remove cached session data
     */
    public void evictSession(String sessionId) {
        String key = SESSION_CACHE_PREFIX + sessionId;
        redisTemplate.delete(key);
        log.debug("Evicted session from cache: {}", sessionId);
    }

    /**
     * Cache event data
     */
    public void cacheEvent(String eventId, Object eventData) {
        String key = EVENT_CACHE_PREFIX + eventId;
        redisTemplate.opsForValue().set(key, eventData, DEFAULT_TTL);
        log.debug("Cached event: {}", eventId);
    }

    /**
     * Get cached event data
     */
    public Optional<Object> getCachedEvent(String eventId) {
        String key = EVENT_CACHE_PREFIX + eventId;
        Object data = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(data);
    }

    /**
     * Remove cached event data
     */
    public void evictEvent(String eventId) {
        String key = EVENT_CACHE_PREFIX + eventId;
        redisTemplate.delete(key);
        log.debug("Evicted event from cache: {}", eventId);
    }

    /**
     * Clear all tracking cache
     */
    public void clearAllCache() {
        redisTemplate.delete(redisTemplate.keys("tracking:*"));
        log.debug("Cleared all tracking cache");
    }
}
