package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.interfaces.rest;
import com.gogidix.shared.multitenancy.context.TenantContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
/**
 * Base controller with tenant validation.
 */
public abstract class TenantAwareController {
    private static final String HEADER_TENANT_ID = "X-Tenant-ID";
    /**
     * Validate and return tenant ID from request.
     */
    protected String validateTenantId(HttpServletRequest request) {
        String tenantId = request.getHeader(HEADER_TENANT_ID);
        if (!StringUtils.hasText(tenantId)) {
            throw new IllegalArgumentException(
                "Missing required header: " + HEADER_TENANT_ID
            );
        }
        return tenantId.trim();
    }
    /**
     * Get tenant ID from context.
     */
    protected String getTenantId() {
        String tenantId = TenantContextHolder.getTenantIdAsString();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant ID not set in context");
        }
        return tenantId;
    }
}
