package com.gogidix.shared.infrastructure.core.tenancy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TenantId - Value object representing a tenant identifier.
 * Local implementation for sla-management-service independence.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantId {
    private String value;

    public static TenantId of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Tenant ID cannot be null or empty");
        }
        return new TenantId(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
