package com.gogidix.finance.bankreconciliation.infrastructure.security;

import com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContext;
import com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
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

    private static final String TENANT_ID_HEADER = "X-Tenant-ID";
    private static final String USER_ID_HEADER = "X-User-ID";
    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                            @NonNull HttpServletResponse response,
                            @NonNull Object handler) throws Exception {

        String tenantId = request.getHeader(TENANT_ID_HEADER);
        String userId = request.getHeader(USER_ID_HEADER);
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);

        // Validate required tenant ID
        if (tenantId == null || tenantId.isBlank()) {
            log.warn("Missing required header: {}", TENANT_ID_HEADER);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                "X-Tenant-ID header is required");
            return false;
        }

        // Build request context
        RequestContext context = RequestContext.builder()
                .tenantId(tenantId)
                .userId(userId != null && !userId.isBlank() ? userId : "anonymous")
                .correlationId(correlationId != null && !correlationId.isBlank()
                    ? correlationId : UUID.randomUUID().toString())
                .requestId(UUID.randomUUID().toString())
                .userAgent(request.getHeader("User-Agent"))
                .ipAddress(request.getRemoteAddr())
                .requestPath(request.getRequestURI())
                .requestMethod(request.getMethod())
                .build();

        // Set context for this request
        RequestContextHolder.set(context);

        // Add correlation ID to response for tracing
        response.setHeader(CORRELATION_ID_HEADER, context.getCorrelationId());

        log.debug("Request context set for tenant: {}, correlationId: {}",
            tenantId, context.getCorrelationId());

        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                               @NonNull HttpServletResponse response,
                               @NonNull Object handler,
                               Exception ex) throws Exception {

        // Clear context after request completes
        RequestContextHolder.clear();
        log.debug("Request context cleared");
    }
}
