package com.gogidix.shared.warehousing.fulfillment.packing.infrastructure.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Thread-local context holder for tenant isolation
 */
@Slf4j
@Component
public class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public String getTenantId() {
        String tenantId = CURRENT_TENANT.get();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant context not initialized");
        }
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        if (tenantId == null || tenantId.isBlank()) {
            log.warn("Attempted to set null or blank tenant ID");
            return;
        }
        CURRENT_TENANT.set(tenantId);
        log.debug("Tenant context set to: {}", tenantId);
    }

    public void clear() {
        CURRENT_TENANT.remove();
        log.debug("Tenant context cleared");
    }

    public boolean isInitialized() {
        return CURRENT_TENANT.get() != null;
    }
}
