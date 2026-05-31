package com.gogidix.shared.infrastructure.services.gateway.apigateway.infrastructure.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Rate Limit Gateway Filter
 *
 * <p>Implements per-tenant rate limiting using token bucket algorithm.
 * Limits are based on tenant tier configuration.</p>
 *
 * @author Foundation Team
 * @version 1.0.0
 */
@Slf4j
public class RateLimitGatewayFilter implements GatewayFilter, Ordered {

    private static final String TENANT_HEADER = "X-Tenant-ID";
    private static final String RATE_LIMIT_REMAINING = "X-RateLimit-Remaining";
    private static final String RATE_LIMIT_RESET = "X-RateLimit-Reset";

    private final ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();
    private final int defaultLimit;
    private final int refillIntervalSeconds;

    public RateLimitGatewayFilter() {
        this.defaultLimit = 1000; // Default requests per minute
        this.refillIntervalSeconds = 60;
    }

    public RateLimitGatewayFilter(Config config) {
        this.defaultLimit = config.defaultLimit;
        this.refillIntervalSeconds = config.refillIntervalSeconds;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String tenantId = exchange.getRequest().getHeaders().getFirst(TENANT_HEADER);

        if (tenantId == null || tenantId.isEmpty()) {
            // No rate limiting for requests without tenant (public endpoints)
            return chain.filter(exchange);
        }

        TokenBucket bucket = buckets.computeIfAbsent(tenantId, t -> createBucket(t, defaultLimit));

        synchronized (bucket) {
            refillTokens(bucket);

            if (bucket.tokens > 0) {
                bucket.tokens--;
                exchange.getResponse().getHeaders().add(RATE_LIMIT_REMAINING, String.valueOf(bucket.tokens));
                return chain.filter(exchange);
            } else {
                log.warn("Rate limit exceeded for tenant: {}", tenantId);
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                exchange.getResponse().getHeaders().add("Retry-After", String.valueOf(refillIntervalSeconds));
                return exchange.getResponse().setComplete();
            }
        }
    }

    @Override
    public int getOrder() {
        return -90;
    }

    private TokenBucket createBucket(String tenantId, int limit) {
        log.info("Created rate limit bucket for tenant: {} with limit: {}", tenantId, limit);
        return new TokenBucket(limit);
    }

    private void refillTokens(TokenBucket bucket) {
        long now = System.currentTimeMillis();
        long elapsed = (now - bucket.lastRefillTime) / 1000;

        if (elapsed >= refillIntervalSeconds) {
            int refillPeriods = (int) (elapsed / refillIntervalSeconds);
            bucket.tokens = Math.min(bucket.capacity, bucket.tokens + (refillPeriods * bucket.capacity));
            bucket.lastRefillTime = now;
        }
    }

    private static class TokenBucket {
        final int capacity;
        int tokens;
        long lastRefillTime;

        TokenBucket(int capacity) {
            this.capacity = capacity;
            this.tokens = capacity;
            this.lastRefillTime = System.currentTimeMillis();
        }
    }

    /**
     * Configuration for rate limiting
     */
    public static class Config {
        private int defaultLimit = 1000;
        private int refillIntervalSeconds = 60;

        public Config setDefaultLimit(int limit) {
            this.defaultLimit = limit;
            return this;
        }

        public Config setRefillIntervalSeconds(int seconds) {
            this.refillIntervalSeconds = seconds;
            return this;
        }
    }
}
