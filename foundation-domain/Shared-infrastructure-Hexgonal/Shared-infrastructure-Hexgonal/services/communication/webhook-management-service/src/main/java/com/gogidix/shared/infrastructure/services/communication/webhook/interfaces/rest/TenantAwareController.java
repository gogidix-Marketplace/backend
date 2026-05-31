package com.gogidix.shared.infrastructure.services.communication.webhook.interfaces.rest;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

/**
 * Base controller with tenant validation.
 */
public abstract class TenantAwareController {
    private final TenantContextHolder tenantContextHolder;

    protected TenantAwareController(TenantContextHolder tenantContextHolder) {
        this.tenantContextHolder = tenantContextHolder;
    }

    /**
     * Validate and return tenant ID from request.
     */
    protected String validateTenantId(HttpServletRequest request) {
        String tenantId = request.getHeader("X-Tenant-ID");
        if (!StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException("Missing required header: X-Tenant-ID");
        }
        return tenantId.trim();
    }

    /**
     * Get tenant ID from context.
     */
    protected String getTenantId() {
        return tenantContextHolder.getRequiredTenantId();
    }
}
