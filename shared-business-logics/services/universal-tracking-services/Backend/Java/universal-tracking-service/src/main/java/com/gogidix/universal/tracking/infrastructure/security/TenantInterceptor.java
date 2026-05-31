package com.gogidix.universal.tracking.infrastructure.security;

import com.gogidix.shared.security.context.RequestContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor to extract and validate tenant information from requests.
 */
@Slf4j
@Component
public class TenantInterceptor implements HandlerInterceptor {

    private static final String TENANT_ID_HEADER = "X-Tenant-ID";
    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantId = request.getHeader(TENANT_ID_HEADER);
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);

        if (tenantId != null && !tenantId.isEmpty()) {
            RequestContext.setTenantId(tenantId);
        }

        if (correlationId != null && !correlationId.isEmpty()) {
            RequestContext.setCorrelationId(correlationId);
        } else {
            // Generate correlation ID if not provided
            RequestContext.setCorrelationId(java.util.UUID.randomUUID().toString());
        }

        log.debug("Request: method={}, uri={}, tenantId={}, correlationId={}",
            request.getMethod(), request.getRequestURI(), tenantId, correlationId);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        // Clear request context after request completes
        RequestContext.clear();
    }
}
