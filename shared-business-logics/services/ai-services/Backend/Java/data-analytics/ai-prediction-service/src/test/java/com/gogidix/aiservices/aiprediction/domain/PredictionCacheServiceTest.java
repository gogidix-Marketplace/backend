package com.gogidix.aiservices.aiprediction.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("PredictionCacheService Domain Service Tests")
class PredictionCacheServiceTest {

    @InjectMocks
    private PredictionCacheService cacheService;

    @Test
    @DisplayName("Should cache prediction result")
    void shouldCachePredictionResult() {
        // Given
        String cacheKey = "model-001:input-hash";
        PredictionResult result = new PredictionResult(
            "pred-1", "model-001", "1.0.0", Map.of("value", 42), 0.9, LocalDateTime.now()
        );

        // When
        cacheService.put(cacheKey, result);

        // Then
        Optional<PredictionResult> cached = cacheService.get(cacheKey);
        assertThat(cached).isPresent();
        assertThat(cached.get().getPredictionId()).isEqualTo("pred-1");
    }

    @Test
    @DisplayName("Should return empty for non-existent cache key")
    void shouldReturnEmptyForNonExistentKey() {
        // When
        Optional<PredictionResult> result = cacheService.get("non-existent-key");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should expire cache entries after TTL")
    void shouldExpireCacheAfterTTL() throws InterruptedException {
        // Given
        PredictionCacheService shortCache = new PredictionCacheService(1); // 1 second TTL
        String key = "test-key";
        PredictionResult result = new PredictionResult(
            "pred-1", "m1", "v1", Map.of(), 0.8, LocalDateTime.now()
        );

        // When
        shortCache.put(key, result);
        Thread.sleep(1100);

        // Then
        Optional<PredictionResult> expired = shortCache.get(key);
        assertThat(expired).isEmpty();
    }

    @Test
    @DisplayName("Should invalidate specific cache entry")
    void shouldInvalidateCacheEntry() {
        // Given
        String key = "to-invalidate";
        PredictionResult result = new PredictionResult(
            "pred-1", "m1", "v1", Map.of(), 0.8, LocalDateTime.now()
        );
        cacheService.put(key, result);

        // When
        cacheService.invalidate(key);

        // Then
        assertThat(cacheService.get(key)).isEmpty();
    }

    @Test
    @DisplayName("Should clear all cache entries")
    void shouldClearAllCache() {
        // Given
        cacheService.put("key1", new PredictionResult("p1", "m1", "v1", Map.of(), 0.8, LocalDateTime.now()));
        cacheService.put("key2", new PredictionResult("p2", "m1", "v1", Map.of(), 0.7, LocalDateTime.now()));

        // When
        cacheService.clear();

        // Then
        assertThat(cacheService.get("key1")).isEmpty();
        assertThat(cacheService.get("key2")).isEmpty();
    }

    @Test
    @DisplayName("Should generate cache key from model and input")
    void shouldGenerateCacheKey() {
        // When
        String key = cacheService.generateCacheKey("model-001", Map.of("feature1", 10, "feature2", 20));

        // Then
        assertThat(key).startsWith("model-001:");
        assertThat(key).doesNotContain(" ");
    }
}
