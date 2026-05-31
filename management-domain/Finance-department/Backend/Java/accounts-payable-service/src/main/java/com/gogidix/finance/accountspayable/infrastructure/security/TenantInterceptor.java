package com.gogidix.finance.accountspayable.infrastructure.security;

import com.gogidix.finance.accountspayable.shared.requestcontext.RequestContext;
import com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

/**
 * Tenant Interceptor
 * Intercepts incoming requests to extract and validate tenant context
 */
@Component
@Slf4j
public class TenantInterceptor implements HandlerInterceptor {

    private static final String TENANT_HEADER = "X-Tenant-ID";
    private static final String USER_HEADER = "X-User-ID";
    private static final String CORRELATION_HEADER = "X-Correlation-ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantId = request.getHeader(TENANT_HEADER);
        String userId = request.getHeader(USER_HEADER);
        String correlationId = request.getHeader(CORRELATION_HEADER);

        // Validate tenant ID
        if (tenantId == null || tenantId.isBlank()) {
            log.warn("Missing required header: {}", TENANT_HEADER);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }

        // Build request context
        RequestContext context = RequestContext.builder()
            .tenantId(tenantId)
            .userId(userId != null ? userId : "anonymous")
            .correlationId(correlationId != null ? correlationId : UUID.randomUUID().toString())
            .build();

        // Set context for this request
        RequestContextHolder.set(context);

        // Add correlation ID to response
        response.setHeader(CORRELATION_HEADER, context.getCorrelationId());

        log.debug("Request context set for tenant: {}, correlation: {}",
            tenantId, context.getCorrelationId());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        // Clean up request context
        RequestContextHolder.clear();
        log.debug("Request context cleared");
    }
}
