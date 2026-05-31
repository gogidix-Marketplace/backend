package com.gogidix.cargo.aiplatform.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class RateLimitFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);
    private static final int MAX_REQUESTS_PER_MINUTE = 100;

    private final ReactiveRedisTemplate<String, Object> redisTemplate;

    private final ConcurrentHashMap<String, AtomicLong> localCounters = new ConcurrentHashMap<>();

    public RateLimitFilter(ReactiveRedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String clientId = exchange.getRequest().getHeaders().getFirst("X-User-Id");
        if (clientId == null) {
            clientId = exchange.getRequest().getRemoteAddress() != null
                    ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
                    : "anonymous";
        }

        String key = "rate_limit:" + clientId;
        AtomicLong counter = localCounters.computeIfAbsent(key, k -> new AtomicLong(0));

        long count = counter.incrementAndGet();
        if (count == 1) {
            Mono.delay(Duration.ofMinutes(1)).subscribe(d -> localCounters.remove(key));
        }

        if (count > MAX_REQUESTS_PER_MINUTE) {
            log.warn("Rate limit exceeded for client: {}", clientId);
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 3;
    }
}