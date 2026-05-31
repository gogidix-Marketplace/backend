package com.gogidix.management.shared.exception;

public class TenantNotActiveException extends DomainException {
    private final String tenantId;

    public TenantNotActiveException(String tenantId) {
        super("Tenant is not active: " + tenantId, "TENANT_NOT_ACTIVE");
        this.tenantId = tenantId;
    }

    public String getTenantId() {
        return tenantId;
    }
}
