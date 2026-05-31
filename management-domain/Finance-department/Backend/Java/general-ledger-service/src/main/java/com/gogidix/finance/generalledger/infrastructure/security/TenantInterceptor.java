package com.gogidix.finance.generalledger.infrastructure.security;

import com.gogidix.finance.generalledger.shared.exception.ValidationException;
import com.gogidix.finance.generalledger.shared.requestcontext.RequestContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Tenant Interceptor
 * Validates tenant context for incoming requests
 */
@Component
@Slf4j
public class TenantInterceptor implements HandlerInterceptor {

    private static final String TENANT_ID_HEADER = "X-Tenant-ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantId = request.getHeader(TENANT_ID_HEADER);

        if (tenantId == null || tenantId.isBlank()) {
            log.warn("Missing tenant ID in request to: {}", request.getRequestURI());
            throw new ValidationException(TENANT_ID_HEADER, "Tenant ID is required");
        }

        // Validate tenant ID format
        if (!isValidTenantId(tenantId)) {
            log.warn("Invalid tenant ID format: {}", tenantId);
            throw new ValidationException(TENANT_ID_HEADER, "Invalid tenant ID format");
        }

        // Tenant is validated in the RequestContextFilter, this is additional validation
        log.debug("Tenant validated: {} for request: {}", tenantId, request.getRequestURI());

        return true;
    }

    private boolean isValidTenantId(String tenantId) {
        // Basic validation - tenant ID should be alphanumeric with hyphens/underscores allowed
        return tenantId != null && tenantId.matches("^[a-zA-Z0-9_-]+$");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        // Clean up any tenant-specific resources if needed
        String tenantId = RequestContextHolder.getTenantId();
        if (tenantId != null) {
            log.debug("Request completed for tenant: {}", tenantId);
        }
    }
}
