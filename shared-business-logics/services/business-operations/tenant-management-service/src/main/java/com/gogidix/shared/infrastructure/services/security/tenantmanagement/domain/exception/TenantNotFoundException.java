package com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.exception;

/**
 * Exception thrown when a tenant is not found.
 */
public class TenantNotFoundException extends RuntimeException {

    public TenantNotFoundException(String tenantId) {
        super("Tenant not found: " + tenantId);
    }
}
