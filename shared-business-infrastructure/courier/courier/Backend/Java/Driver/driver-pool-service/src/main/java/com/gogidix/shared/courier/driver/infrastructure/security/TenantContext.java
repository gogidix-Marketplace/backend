package com.gogidix.shared.courier.driver.infrastructure.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Thread-local context holder for tenant ID
 */
@Slf4j
@Component
public class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public void setTenantId(String tenantId) {
        if (tenantId == null || tenantId.isBlank()) {
            log.warn("Attempted to set null or blank tenant ID");
            return;
        }
        CURRENT_TENANT.set(tenantId);
        log.debug("Tenant context set to: {}", tenantId);
    }

    public String getTenantId() {
        return CURRENT_TENANT.get();
    }

    public boolean hasTenantId() {
        return CURRENT_TENANT.get() != null;
    }

    public void clear() {
        CURRENT_TENANT.remove();
        log.debug("Tenant context cleared");
    }
}
