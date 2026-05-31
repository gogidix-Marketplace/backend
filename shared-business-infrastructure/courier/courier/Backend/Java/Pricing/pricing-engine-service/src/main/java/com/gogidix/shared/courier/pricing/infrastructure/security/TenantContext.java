package com.gogidix.shared.courier.pricing.infrastructure.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Thread-local context holder for tenant ID
 * Provides tenant isolation throughout the request lifecycle
 */
@Slf4j
@Component
public class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    /**
     * Set the current tenant ID for this request
     */
    public void setTenantId(String tenantId) {
        if (tenantId == null || tenantId.isBlank()) {
            log.warn("Attempted to set null or blank tenant ID");
            return;
        }
        CURRENT_TENANT.set(tenantId);
        log.debug("Tenant context set to: {}", tenantId);
    }

    /**
     * Get the current tenant ID for this request
     */
    public String getTenantId() {
        String tenantId = CURRENT_TENANT.get();
        if (tenantId == null) {
            log.warn("Tenant context not set - returning null");
        }
        return tenantId;
    }

    /**
     * Check if tenant context is set
     */
    public boolean hasTenant() {
        return CURRENT_TENANT.get() != null;
    }

    /**
     * Clear the current tenant context
     */
    public void clear() {
        String previousTenant = CURRENT_TENANT.get();
        CURRENT_TENANT.remove();
        log.debug("Tenant context cleared for: {}", previousTenant);
    }

    /**
     * Get tenant ID or throw exception if not set
     */
    public String getRequiredTenantId() {
        String tenantId = getTenantId();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant context is required but not set");
        }
        return tenantId;
    }

    /**
     * Execute operation with tenant context
     */
    public <T> T executeWithTenant(String tenantId, TenantOperation<T> operation) {
        try {
            setTenantId(tenantId);
            return operation.execute();
        } finally {
            clear();
        }
    }

    /**
     * Execute operation with tenant context (void return)
     */
    public void executeWithTenant(String tenantId, TenantOperationVoid operation) {
        try {
            setTenantId(tenantId);
            operation.execute();
        } finally {
            clear();
        }
    }

    @FunctionalInterface
    public interface TenantOperation<T> {
        T execute();
    }

    @FunctionalInterface
    public interface TenantOperationVoid {
        void execute();
    }
}
