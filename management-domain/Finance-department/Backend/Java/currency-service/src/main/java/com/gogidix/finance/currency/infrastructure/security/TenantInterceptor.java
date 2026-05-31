package com.gogidix.finance.currency.infrastructure.security;

import com.gogidix.finance.currency.shared.requestcontext.RequestContext;
import com.gogidix.finance.currency.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

/**
 * Tenant Interceptor
 * Extracts tenant context from HTTP request and sets RequestContext
 * CRITICAL for multi-tenancy - must run before any service logic
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class TenantInterceptor implements HandlerInterceptor {

    private static final String TENANT_ID_HEADER = "X-Tenant-ID";
    private static final String USER_ID_HEADER = "X-User-ID";
    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantId = extractTenantId(request);
        String userId = request.getHeader(USER_ID_HEADER);
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        RequestContext context = RequestContext.builder()
            .tenantId(tenantId)
            .userId(userId)
            .correlationId(correlationId)
            .build();

        RequestContextHolder.set(context);

        response.setHeader(CORRELATION_ID_HEADER, correlationId);

        log.debug("Request context set - tenant: {}, user: {}, correlation: {}",
            tenantId, userId, correlationId);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                               Object handler, Exception ex) {
        RequestContextHolder.clear();
        log.debug("Request context cleared");
    }

    private String extractTenantId(HttpServletRequest request) {
        String tenantId = request.getHeader(TENANT_ID_HEADER);
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("Missing required header: " + TENANT_ID_HEADER);
        }
        return tenantId;
    }
}
