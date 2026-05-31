package com.gogidix.shared.infrastructure.services.observability.ratelimit.infrastructure.filter;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.services.observability.ratelimit.application.service.RateLimitingService;
import com.gogidix.shared.infrastructure.services.observability.ratelimit.domain.model.RateLimitConfig;
import com.gogidix.shared.infrastructure.services.observability.ratelimit.domain.port.in.RateLimitPort;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Optional;

/**
 * Rate limiting filter.
 * <p>
 * Applies rate limiting to incoming requests based on configured rules.
 */
@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {

    private final RateLimitPort rateLimitPort;
    private final TenantContextHolder tenantContextHolder;

    private static final String HEADER_RATE_LIMIT = "X-RateLimit-Limit";
    private static final String HEADER_RATE_LIMIT_REMAINING = "X-RateLimit-Remaining";
    private static final String HEADER_RATE_LIMIT_RESET = "X-RateLimit-Reset";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     jakarta.servlet.FilterChain filterChain) throws java.io.IOException, jakarta.servlet.ServletException {

        // Default rate limit configuration (can be made configurable)
        RateLimitConfig config = RateLimitConfig.builder()
                .capacity(100)
                .refillTokens(10)
                .refillPeriod(1) // 1 second
                .enabled(true)
                .build();

        String rateLimitKey = buildRateLimitKey(request);

        boolean allowed = rateLimitPort.tryConsume(rateLimitKey, config);

        if (!allowed) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("{\"error\":\"Rate limit exceeded\"}");
            response.setContentType("application/json");
            log.warn("Rate limit exceeded for key: {}", rateLimitKey);
            return;
        }

        // Add rate limit headers
        RateLimitingService.RateLimitResult status = rateLimitPort.getRateLimitStatus(rateLimitKey, config);
        response.setHeader(HEADER_RATE_LIMIT, String.valueOf(status.capacity()));
        response.setHeader(HEADER_RATE_LIMIT_REMAINING, String.valueOf(status.availableTokens()));
        response.setHeader(HEADER_RATE_LIMIT_RESET, String.valueOf(status.refillTokens()));

        filterChain.doFilter(request, response);
    }

    /**
     * Builds the rate limit key from the request.
     */
    private String buildRateLimitKey(HttpServletRequest request) {
        String tenantId = tenantContextHolder.getTenantId()
                .orElse("default");

        String userId = request.getHeader("X-User-Id");
        String apiKey = request.getHeader("X-API-Key");
        String ipAddress = request.getRemoteAddr();

        // Priority: API Key > User ID > IP Address
        if (apiKey != null && !apiKey.isEmpty()) {
            return "api-key:" + tenantId + ":" + apiKey;
        } else if (userId != null && !userId.isEmpty()) {
            return "user:" + tenantId + ":" + userId;
        } else {
            return "ip:" + tenantId + ":" + ipAddress;
        }
    }
}
