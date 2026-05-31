package com.gogidix.sales.dashboard.infrastructure.security;

import com.gogidix.sales.dashboard.shared.requestcontext.RequestContext;
import com.gogidix.sales.dashboard.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

/**
 * Tenant Interceptor
 * Extracts tenant information from HTTP headers and sets up request context
 */
@Component
@Slf4j
public class TenantInterceptor implements HandlerInterceptor {

    private static final String TENANT_ID_HEADER = "X-Tenant-Id";
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantId = request.getHeader(TENANT_ID_HEADER);
        String userId = request.getHeader(USER_ID_HEADER);
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        RequestContext context = RequestContext.builder()
                .tenantId(tenantId != null ? tenantId : "default")
                .userId(userId != null ? userId : "system")
                .correlationId(correlationId)
                .requestId(UUID.randomUUID().toString())
                .userAgent(request.getHeader("User-Agent"))
                .ipAddress(request.getRemoteAddr())
                .build();

        RequestContextHolder.set(context);

        log.debug("Request context set for tenant: {}, correlationId: {}",
                context.getTenantId(), context.getCorrelationId());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                 Object handler, Exception ex) {
        RequestContextHolder.clear();
    }
}
