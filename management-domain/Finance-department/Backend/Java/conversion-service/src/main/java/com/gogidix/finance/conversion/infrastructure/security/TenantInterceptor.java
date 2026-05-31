package com.gogidix.finance.conversion.infrastructure.security;

import com.gogidix.finance.conversion.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Tenant Interceptor
 * Validates tenant context and enforces multi-tenancy isolation
 */
@Component
@Slf4j
public class TenantInterceptor implements HandlerInterceptor {

    private static final String TENANT_HEADER = "X-Tenant-ID";

    @Override
    public boolean preHandle(HttpServletRequest request,
                            HttpServletResponse response,
                            Object handler) throws Exception {

        String tenantId = request.getHeader(TENANT_HEADER);

        if (tenantId == null || tenantId.isBlank()) {
            log.warn("Missing tenant header in request: {}", request.getRequestURI());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "X-Tenant-ID header is required");
            return false;
        }

        // Validate tenant ID format
        if (!isValidTenantId(tenantId)) {
            log.warn("Invalid tenant ID format: {}", tenantId);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid X-Tenant-ID format");
            return false;
        }

        // Verify tenant context is set
        try {
            String contextTenantId = RequestContextHolder.getTenantId();
            if (!contextTenantId.equals(tenantId)) {
                log.error("Tenant mismatch: header={}, context={}", tenantId, contextTenantId);
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Tenant context mismatch");
                return false;
            }
        } catch (Exception e) {
            log.error("Error validating tenant context", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error validating tenant context");
            return false;
        }

        log.debug("Tenant validation passed for: {}", tenantId);
        return true;
    }

    /**
     * Validates the tenant ID format
     */
    private boolean isValidTenantId(String tenantId) {
        if (tenantId == null || tenantId.isBlank()) {
            return false;
        }

        // Tenant ID should be alphanumeric with possible hyphens/underscores
        // Length between 3 and 50 characters
        return tenantId.matches("^[a-zA-Z0-9_-]{3,50}$");
    }

    /**
     * Checks if a tenant ID is allowed to access a resource
     */
    public static boolean isTenantAllowed(String resourceTenantId, String requestTenantId) {
        return resourceTenantId != null && resourceTenantId.equals(requestTenantId);
    }
}
