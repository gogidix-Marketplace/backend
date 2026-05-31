package com.gogidix.dashboard.shared.util;

import com.gogidix.dashboard.shared.constants.DashboardConstants;
import com.gogidix.dashboard.shared.dto.TenantContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor to extract and set tenant context from HTTP headers.
 */
@Slf4j
@Component
public class TenantInterceptor implements HandlerInterceptor {

    private static final String TENANT_HEADER = DashboardConstants.TENANT_HEADER;
    private static final String CORRELATION_HEADER = DashboardConstants.CORRELATION_HEADER;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantId = request.getHeader(TENANT_HEADER);
        String correlationId = request.getHeader(CORRELATION_HEADER);

        if (tenantId == null || tenantId.isEmpty()) {
            tenantId = DashboardConstants.TENANT_DEFAULT;
            log.debug("No tenant ID provided, using default: {}", tenantId);
        }

        TenantContext.TenantContextBuilder contextBuilder = TenantContext.builder(tenantId)
                .correlationId(correlationId != null ? correlationId : java.util.UUID.randomUUID().toString())
                .requestTimestamp(java.time.LocalDateTime.now());

        TenantContext context = contextBuilder.build();

        TenantContext.setContext(context);

        log.debug("Tenant context set: tenant={}, correlation={}", tenantId, context.getCorrelationId());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        TenantContext.clearContext();
        log.debug("Tenant context cleared");
    }
}
