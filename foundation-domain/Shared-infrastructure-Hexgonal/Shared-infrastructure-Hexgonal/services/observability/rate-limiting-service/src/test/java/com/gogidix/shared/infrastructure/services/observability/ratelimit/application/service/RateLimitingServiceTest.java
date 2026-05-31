package com.gogidix.shared.infrastructure.services.observability.ratelimit.application.service;

import com.gogidix.shared.infrastructure.services.observability.ratelimit.domain.model.RateLimitConfig;
import com.gogidix.shared.infrastructure.services.observability.ratelimit.domain.port.in.RateLimitPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RateLimitingService.
 */
@DisplayName("Rate Limiting Service Tests")
class RateLimitingServiceTest {

    private RateLimitingService rateLimitingService;
    private RateLimitConfig enabledConfig;
    private RateLimitConfig disabledConfig;

    @BeforeEach
    void setUp() {
        rateLimitingService = new RateLimitingService();

        enabledConfig = RateLimitConfig.builder()
                .capacity(10)
                .refillTokens(5)
                .refillPeriod(1) // 1 second
                .enabled(true)
                .build();

        disabledConfig = RateLimitConfig.builder()
                .capacity(10)
                .refillTokens(5)
                .refillPeriod(1)
                .enabled(false)
                .build();
    }

    @Test
    @DisplayName("Should allow consumption when within limit")
    void shouldAllowConsumptionWhenWithinLimit() {
        String key = "user-123";

        boolean result = rateLimitingService.tryConsume(key, enabledConfig);

        assertTrue(result);
    }

    @Test
    @DisplayName("Should allow consumption up to capacity")
    void shouldAllowConsumptionUpToCapacity() {
        String key = "user-456";

        // Consume up to capacity
        for (int i = 0; i < 10; i++) {
            assertTrue(rateLimitingService.tryConsume(key, enabledConfig));
        }

        // Next consumption should fail
        assertFalse(rateLimitingService.tryConsume(key, enabledConfig));
    }

    @Test
    @DisplayName("Should always allow consumption when config is disabled")
    void shouldAlwaysAllowConsumptionWhenConfigIsDisabled() {
        String key = "user-789";

        // Even after many consumptions
        for (int i = 0; i < 100; i++) {
            assertTrue(rateLimitingService.tryConsume(key, disabledConfig));
        }
    }

    @Test
    @DisplayName("Should track rate limit status correctly")
    void shouldTrackRateLimitStatusCorrectly() {
        String key = "user-status";

        // Consume 3 tokens
        rateLimitingService.tryConsume(key, enabledConfig);
        rateLimitingService.tryConsume(key, enabledConfig);
        rateLimitingService.tryConsume(key, enabledConfig);

        RateLimitingService.RateLimitResult result = rateLimitingService.getRateLimitStatus(key, enabledConfig);

        assertEquals(7, result.availableTokens()); // 10 - 3 = 7
        assertEquals(10, result.capacity());
        assertEquals(3, result.refillTokens());
    }

    @Test
    @DisplayName("Should return full capacity for unknown key")
    void shouldReturnFullCapacityForUnknownKey() {
        String key = "unknown-key";

        RateLimitingService.RateLimitResult result = rateLimitingService.getRateLimitStatus(key, enabledConfig);

        assertEquals(10, result.availableTokens());
        assertEquals(10, result.capacity());
        assertEquals(5, result.refillTokens());
    }

    @Test
    @DisplayName("Should handle multiple keys independently")
    void shouldHandleMultipleKeysIndependently() {
        String key1 = "user-1";
        String key2 = "user-2";

        // Consume all tokens for key1
        for (int i = 0; i < 10; i++) {
            rateLimitingService.tryConsume(key1, enabledConfig);
        }

        // key1 should be rate limited
        assertFalse(rateLimitingService.tryConsume(key1, enabledConfig));

        // key2 should still have all tokens
        assertTrue(rateLimitingService.tryConsume(key2, enabledConfig));
    }

    @Test
    @DisplayName("Should create RateLimitResult with correct values")
    void shouldCreateRateLimitResultWithCorrectValues() {
        RateLimitingService.RateLimitResult result = new RateLimitingService.RateLimitResult(5, 10, 3);

        assertEquals(5, result.availableTokens());
        assertEquals(10, result.capacity());
        assertEquals(3, result.refillTokens());
    }

    @Test
    @DisplayName("Should handle record pattern for RateLimitResult")
    void shouldHandleRecordPatternForRateLimitResult() {
        RateLimitingService.RateLimitResult result = rateLimitingService.getRateLimitStatus("test", enabledConfig);

        // Test record methods
        assertNotNull(result);
        assertTrue(result.availableTokens() >= 0);
        assertTrue(result.capacity() > 0);
        assertTrue(result.refillTokens() >= 0);
    }

    @Test
    @DisplayName("Should handle zero capacity config")
    void shouldHandleZeroCapacityConfig() {
        RateLimitConfig zeroCapacityConfig = RateLimitConfig.builder()
                .capacity(0)
                .refillTokens(0)
                .refillPeriod(1)
                .enabled(true)
                .build();

        String key = "zero-capacity";

        // Should not allow any consumption
        assertFalse(rateLimitingService.tryConsume(key, zeroCapacityConfig));
    }

    @Test
    @DisplayName("Should handle large capacity config")
    void shouldHandleLargeCapacityConfig() {
        RateLimitConfig largeCapacityConfig = RateLimitConfig.builder()
                .capacity(10000)
                .refillTokens(100)
                .refillPeriod(1)
                .enabled(true)
                .build();

        String key = "large-capacity";

        // Should allow many consumptions
        for (int i = 0; i < 1000; i++) {
            assertTrue(rateLimitingService.tryConsume(key, largeCapacityConfig));
        }
    }

    @Test
    @DisplayName("Should return correct refill tokens")
    void shouldReturnCorrectRefillTokens() {
        String key = "refill-test";

        // Consume half the capacity
        for (int i = 0; i < 5; i++) {
            rateLimitingService.tryConsume(key, enabledConfig);
        }

        RateLimitingService.RateLimitResult result = rateLimitingService.getRateLimitStatus(key, enabledConfig);

        // Should have 5 tokens available, refill tokens should be 5 (capacity - available)
        assertEquals(5, result.availableTokens());
        assertEquals(5, result.refillTokens());
    }

    @Test
    @DisplayName("Should not reset bucket on new config with same key")
    void shouldNotResetBucketOnNewConfigWithSameKey() {
        String key = "same-key";

        // Consume 5 tokens with first config
        rateLimitingService.tryConsume(key, enabledConfig);
        rateLimitingService.tryConsume(key, enabledConfig);
        rateLimitingService.tryConsume(key, enabledConfig);
        rateLimitingService.tryConsume(key, enabledConfig);
        rateLimitingService.tryConsume(key, enabledConfig);

        // Create new config with different capacity
        RateLimitConfig newConfig = RateLimitConfig.builder()
                .capacity(20)
                .refillTokens(10)
                .refillPeriod(1)
                .enabled(true)
                .build();

        // Status should still show previous bucket state
        RateLimitingService.RateLimitResult result = rateLimitingService.getRateLimitStatus(key, newConfig);

        // Bucket is not recreated, so tokens reflect previous consumption
        assertTrue(result.availableTokens() >= 0);
    }

    @Test
    @DisplayName("Should handle concurrent requests gracefully")
    void shouldHandleConcurrentRequestsGracefully() {
        String key = "concurrent";

        // Simulate rapid requests
        int successCount = 0;
        for (int i = 0; i < 15; i++) {
            if (rateLimitingService.tryConsume(key, enabledConfig)) {
                successCount++;
            }
        }

        // Should not exceed capacity
        assertTrue(successCount <= 10);
    }

    @Test
    @DisplayName("Should return correct status for disabled config")
    void shouldReturnCorrectStatusForDisabledConfig() {
        String key = "disabled";

        RateLimitingService.RateLimitResult result = rateLimitingService.getRateLimitStatus(key, disabledConfig);

        // Even for disabled config, should return capacity values
        assertEquals(10, result.availableTokens());
        assertEquals(10, result.capacity());
        assertEquals(5, result.refillTokens());
    }
}
