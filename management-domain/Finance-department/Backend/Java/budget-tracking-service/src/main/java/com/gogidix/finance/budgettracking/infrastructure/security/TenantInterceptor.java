package com.gogidix.finance.budgettracking.infrastructure.security;

import com.gogidix.finance.budgettracking.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Tenant Interceptor
 * Validates and enriches requests with tenant information
 */
@Component
@Slf4j
public class TenantInterceptor implements HandlerInterceptor {

    private static final String TENANT_ID_HEADER = "X-Tenant-ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantId = request.getHeader(TENANT_ID_HEADER);

        if (tenantId == null || tenantId.isBlank()) {
            log.warn("Missing tenant header in request to: {}", request.getRequestURI());
            return false;
        }

        try {
            String currentTenantId = RequestContextHolder.getTenantId();
            if (!currentTenantId.equals(tenantId)) {
                log.warn("Tenant mismatch: header={}, context={}", tenantId, currentTenantId);
            }
        } catch (IllegalStateException e) {
            log.debug("RequestContext not yet set for tenant: {}", tenantId);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        // Cleanup if needed
    }
}
