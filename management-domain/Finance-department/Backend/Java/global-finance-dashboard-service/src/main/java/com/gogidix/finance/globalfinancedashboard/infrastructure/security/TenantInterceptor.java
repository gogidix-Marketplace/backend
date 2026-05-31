package com.gogidix.finance.globalfinancedashboard.infrastructure.security;

import com.gogidix.finance.globalfinancedashboard.shared.requestcontext.RequestContext;
import com.gogidix.finance.globalfinancedashboard.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

/**
 * INTERCEPTS ALL REQUESTS and extracts tenant context from JWT
 * This is the ENTRY POINT for multi-tenancy
 */
@Component
public class TenantInterceptor implements HandlerInterceptor, Ordered {

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                            HttpServletResponse response,
                            Object handler) {

        // 1. Extract tenantId from headers (simplified for demo)
        String tenantId = request.getHeader("X-Tenant-ID");
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("Missing X-Tenant-ID header");
        }

        // 2. Extract userId from headers (simplified for demo)
        String userId = request.getHeader("X-User-ID");

        // 3. Extract correlationId or generate new one
        String correlationId = request.getHeader("X-Correlation-ID");
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        // 4. Build and set RequestContext
        RequestContext context = RequestContext.builder()
            .tenantId(tenantId)
            .userId(userId)
            .correlationId(correlationId)
            .build();

        RequestContextHolder.set(context);

        // 5. Add correlationId to response for tracing
        response.setHeader("X-Correlation-ID", correlationId);

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
}
