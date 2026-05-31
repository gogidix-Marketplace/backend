package com.gogidix.shared.infrastructure.core.tenancy.interceptor;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * TenantInterceptor - Interceptor to extract tenant ID from HTTP headers.
 * Local implementation for payment-processing-service independence.
 */
@Component
public class TenantInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TenantInterceptor.class);
    private static final String TENANT_HEADER = "X-Tenant-ID";
    private static final String DEFAULT_TENANT = "default";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tenantId = request.getHeader(TENANT_HEADER);

        if (tenantId == null || tenantId.trim().isEmpty()) {
            log.debug("No tenant ID found in header '{}', using default tenant", TENANT_HEADER);
            tenantId = DEFAULT_TENANT;
        }

        log.debug("Setting tenant context to: {}", tenantId);
        TenantContextHolder.setTenantId(tenantId);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        TenantContextHolder.clear();
        log.debug("Cleared tenant context");
    }
}
