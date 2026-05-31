package com.gogidix.shared.infrastructure.services.observability.ratelimit.domain.port.in;

import com.gogidix.shared.infrastructure.services.observability.ratelimit.application.service.RateLimitingService;
import com.gogidix.shared.infrastructure.services.observability.ratelimit.domain.model.RateLimitConfig;

/**
 * Rate Limiting use case port.
 */
public interface RateLimitPort {

    /**
     * Attempts to consume a token from the rate limit bucket.
     *
     * @param key the rate limit key (e.g., user ID, API key, IP address)
     * @param config the rate limit configuration
     * @return true if the request is allowed, false if rate limited
     */
    boolean tryConsume(String key, RateLimitConfig config);

    /**
     * Gets the current rate limit status.
     *
     * @param key the rate limit key
     * @param config the rate limit configuration
     * @return the rate limit status
     */
    RateLimitingService.RateLimitResult getRateLimitStatus(String key, RateLimitConfig config);
}
