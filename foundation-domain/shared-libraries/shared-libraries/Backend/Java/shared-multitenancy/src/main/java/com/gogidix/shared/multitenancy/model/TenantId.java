package com.gogidix.shared.multitenancy.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

/**
 * Tenant ID Value Object
 * <p>
 * Type-safe wrapper for tenant identifier to prevent mixing with other string IDs.
 * This is a value object - equality is based on the ID value, not reference.
 * </p>
 *
 * @author Foundation Team
 * @version 1.0.0
 */
@Getter
@EqualsAndHashCode
public final class TenantId {

    private final String value;

    private TenantId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Tenant ID cannot be null or empty");
        }
        this.value = value.trim();
    }

    /**
     * Create TenantId from string
     */
    public static TenantId of(String value) {
        return new TenantId(value);
    }

    /**
     * Create new random TenantId (UUID-based)
     */
    public static TenantId generate() {
        return new TenantId(UUID.randomUUID().toString());
    }

    /**
     * Get the raw string value
     */
    public String stringValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
