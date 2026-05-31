package com.gogidix.sales.analytics.infrastructure.security;

import com.gogidix.sales.analytics.shared.requestcontext.RequestContext;
import com.gogidix.sales.analytics.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

/**
 * Tenant Interceptor for Multi-Tenancy
 * INTERCEPTS ALL REQUESTS and extracts tenant context from headers
 * This is the ENTRY POINT for multi-tenancy
 */
@Component
public class TenantInterceptor implements HandlerInterceptor, Ordered {

    private static final String TENANT_HEADER = "X-Tenant-ID";
    private static final String USER_HEADER = "X-User-ID";
    private static final String CORRELATION_HEADER = "X-Correlation-ID";

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                            HttpServletResponse response,
                            Object handler) {

        // 1. Extract tenantId from headers (required)
        String tenantId = request.getHeader(TENANT_HEADER);
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("Missing required header: " + TENANT_HEADER);
        }

        // 2. Extract userId from headers (optional)
        String userId = request.getHeader(USER_HEADER);

        // 3. Extract correlationId or generate new one
        String correlationId = request.getHeader(CORRELATION_HEADER);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        // 4. Extract additional request metadata
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = getClientIpAddress(request);

        // 5. Build and set RequestContext
        RequestContext context = RequestContext.builder()
                .tenantId(tenantId)
                .userId(userId)
                .correlationId(correlationId)
                .requestId(UUID.randomUUID().toString())
                .userAgent(userAgent)
                .ipAddress(ipAddress)
                .build();

        RequestContextHolder.set(context);

        // 6. Add correlationId to response for tracing
        response.setHeader(CORRELATION_HEADER, correlationId);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                               HttpServletResponse response,
                               Object handler,
                               Exception ex) {
        // ALWAYS clear context to prevent memory leaks
        RequestContextHolder.clear();
    }

    /**
     * Extracts the client IP address from the request
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
