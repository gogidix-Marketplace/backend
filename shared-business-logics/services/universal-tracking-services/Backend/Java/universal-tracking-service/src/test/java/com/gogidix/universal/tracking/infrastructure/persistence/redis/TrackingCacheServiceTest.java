package com.gogidix.universal.tracking.infrastructure.persistence.redis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TrackingCacheService.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrackingCacheServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private TrackingCacheService cacheService;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testCacheSession_Success() {
        // Given
        String sessionId = "session-123";
        Object sessionData = new Object();

        // When
        cacheService.cacheSession(sessionId, sessionData);

        // Then
        verify(valueOperations).set(eq("tracking:session:" + sessionId), eq(sessionData), any());
    }

    @Test
    void testGetCachedSession_Found() {
        // Given
        String sessionId = "session-123";
        Object sessionData = new Object();
        when(valueOperations.get("tracking:session:" + sessionId)).thenReturn(sessionData);

        // When
        Optional<Object> result = cacheService.getCachedSession(sessionId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(sessionData, result.get());
    }

    @Test
    void testGetCachedSession_NotFound() {
        // Given
        String sessionId = "non-existent";
        when(valueOperations.get("tracking:session:" + sessionId)).thenReturn(null);

        // When
        Optional<Object> result = cacheService.getCachedSession(sessionId);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void testEvictSession_Success() {
        // Given
        String sessionId = "session-123";
        when(redisTemplate.delete(anyString())).thenReturn(true);

        // When
        cacheService.evictSession(sessionId);

        // Then
        verify(redisTemplate).delete("tracking:session:" + sessionId);
    }

    @Test
    void testCacheEvent_Success() {
        // Given
        String eventId = "event-123";
        Object eventData = new Object();

        // When
        cacheService.cacheEvent(eventId, eventData);

        // Then
        verify(valueOperations).set(eq("tracking:event:" + eventId), eq(eventData), any());
    }

    @Test
    void testGetCachedEvent_Found() {
        // Given
        String eventId = "event-123";
        Object eventData = new Object();
        when(valueOperations.get("tracking:event:" + eventId)).thenReturn(eventData);

        // When
        Optional<Object> result = cacheService.getCachedEvent(eventId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(eventData, result.get());
    }

    @Test
    void testGetCachedEvent_NotFound() {
        // Given
        String eventId = "non-existent";
        when(valueOperations.get("tracking:event:" + eventId)).thenReturn(null);

        // When
        Optional<Object> result = cacheService.getCachedEvent(eventId);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void testEvictEvent_Success() {
        // Given
        String eventId = "event-123";
        when(redisTemplate.delete(anyString())).thenReturn(true);

        // When
        cacheService.evictEvent(eventId);

        // Then
        verify(redisTemplate).delete("tracking:event:" + eventId);
    }

    @Test
    void testClearAllCache_Success() {
        // Given
        when(redisTemplate.keys("tracking:*")).thenReturn(Set.of("tracking:session:1", "tracking:event:1"));
        when(redisTemplate.delete(anySet())).thenReturn(2L);

        // When
        cacheService.clearAllCache();

        // Then
        verify(redisTemplate).keys("tracking:*");
        verify(redisTemplate).delete(anySet());
    }

    @Test
    void testClearAllCache_NoKeys() {
        // Given
        when(redisTemplate.keys("tracking:*")).thenReturn(null);

        // When
        cacheService.clearAllCache();

        // Then
        verify(redisTemplate).keys("tracking:*");
        verify(redisTemplate, never()).delete(anySet());
    }
}
