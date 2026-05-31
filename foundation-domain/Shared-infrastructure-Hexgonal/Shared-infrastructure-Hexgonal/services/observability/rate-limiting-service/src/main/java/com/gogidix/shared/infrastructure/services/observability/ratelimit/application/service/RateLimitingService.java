package com.gogidix.shared.infrastructure.services.observability.ratelimit.application.service;

import com.gogidix.shared.infrastructure.services.observability.ratelimit.domain.model.RateLimitConfig;
import com.gogidix.shared.infrastructure.services.observability.ratelimit.domain.port.in.RateLimitPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Rate Limiting Service implementation.
 * <p>
 * Uses in-memory rate limiting with sliding window counter.
 */
@Slf4j
@Service
public class RateLimitingService implements RateLimitPort {

    private final ConcurrentHashMap<String, RateLimitBucket> buckets = new ConcurrentHashMap<>();

    @Override
    public boolean tryConsume(String key, RateLimitConfig config) {
        if (!config.isEnabled()) {
            return true;
        }

        RateLimitBucket bucket = buckets.computeIfAbsent(key, k -> new RateLimitBucket(config));
        boolean consumed = bucket.tryConsume(1);

        if (!consumed) {
            log.warn("Rate limit exceeded for key: {}", key);
        }

        return consumed;
    }

    @Override
    public RateLimitResult getRateLimitStatus(String key, RateLimitConfig config) {
        RateLimitBucket bucket = buckets.get(key);
        if (bucket == null) {
            return new RateLimitResult(config.getCapacity(), config.getCapacity(), config.getRefillTokens());
        }

        long availableTokens = bucket.getAvailableTokens();
        return new RateLimitResult(
                availableTokens,
                config.getCapacity(),
                Math.min(config.getRefillTokens(), config.getCapacity() - availableTokens)
        );
    }

    /**
     * In-memory rate limit bucket using token bucket algorithm.
     */
    private static class RateLimitBucket {
        private final long capacity;
        private final long refillTokens;
        private final long refillPeriodMs;
        private final AtomicLong availableTokens;
        private volatile long lastRefillTime;

        public RateLimitBucket(RateLimitConfig config) {
            this.capacity = config.getCapacity();
            this.refillTokens = config.getRefillTokens();
            this.refillPeriodMs = Duration.ofSeconds(config.getRefillPeriod()).toMillis();
            this.availableTokens = new AtomicLong(config.getCapacity());
            this.lastRefillTime = System.currentTimeMillis();
        }

        public synchronized boolean tryConsume(int tokens) {
            refill();
            long current = availableTokens.get();
            if (current >= tokens) {
                availableTokens.addAndGet(-tokens);
                return true;
            }
            return false;
        }

        private void refill() {
            long now = System.currentTimeMillis();
            long elapsed = now - lastRefillTime;

            if (elapsed >= refillPeriodMs) {
                long refillPeriods = elapsed / refillPeriodMs;
                long tokensToAdd = Math.min(refillTokens * refillPeriods, capacity - availableTokens.get());
                if (tokensToAdd > 0) {
                    availableTokens.addAndGet(tokensToAdd);
                }
                lastRefillTime = now;
            }
        }

        public long getAvailableTokens() {
            refill();
            return availableTokens.get();
        }
    }

    /**
     * Rate limit result record.
     */
    public record RateLimitResult(
            long availableTokens,
            long capacity,
            long refillTokens
    ) {
    }
}
