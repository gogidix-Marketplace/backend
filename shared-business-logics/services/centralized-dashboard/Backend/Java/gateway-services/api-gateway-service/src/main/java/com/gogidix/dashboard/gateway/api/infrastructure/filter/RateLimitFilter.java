package com.gogidix.dashboard.gateway.api.infrastructure.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * Rate limiting filter using Redis.
 */
@Slf4j
@Component
@Order(1)
public class RateLimitFilter extends OncePerRequestFilter {

    private static final String RATE_LIMIT_PREFIX = "rate_limit:";
    private static final String X_RATE_LIMIT_REMAINING = "X-RateLimit-Remaining";
    private static final String X_RATE_LIMIT_RESET = "X-RateLimit-Reset";

    @Value("${gateway.rate-limit.enabled:true}")
    private boolean rateLimitEnabled;

    @Value("${gateway.rate-limit.requests-per-minute:100}")
    private int requestsPerMinute;

    private final RedisTemplate<String, String> redisTemplate;

    public RateLimitFilter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (!rateLimitEnabled) {
            filterChain.doFilter(request, response);
            return;
        }

        String clientId = getClientId(request);
        String key = RATE_LIMIT_PREFIX + clientId;

        try {
            Long currentCount = redisTemplate.opsForValue().increment(key);

            if (currentCount == null) {
                currentCount = 1L;
            }

            if (currentCount == 1) {
                redisTemplate.expire(key, Duration.ofMinutes(1));
            }

            response.setHeader(X_RATE_LIMIT_REMAINING, String.valueOf(Math.max(0, requestsPerMinute - currentCount)));

            if (currentCount > requestsPerMinute) {
                log.warn("Rate limit exceeded for client: {}", clientId);
                response.setStatus(429);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Rate limit exceeded\",\"retryAfter\":60}");
                return;
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("Error in rate limiting", e);
            filterChain.doFilter(request, response);
        }
    }

    private String getClientId(HttpServletRequest request) {
        String tenantId = request.getHeader("X-Tenant-ID");
        String userAgent = request.getHeader("User-Agent");
        String remoteAddr = request.getRemoteAddr();

        return (tenantId != null ? tenantId : "default") + ":" +
               (userAgent != null ? userAgent.substring(0, Math.min(20, userAgent.length())) : "unknown") + ":" +
               remoteAddr;
    }
}
