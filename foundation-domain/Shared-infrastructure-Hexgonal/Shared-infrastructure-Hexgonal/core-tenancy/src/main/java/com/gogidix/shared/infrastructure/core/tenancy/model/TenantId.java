package com.gogidix.shared.infrastructure.core.tenancy.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

/**
 * Value object representing tenant identifier.
 * <p>
 * Embedded in MongoDB documents and used throughout the application
 * for multi-tenant data isolation.
 * <p>
 * This value object ensures tenant ID validation and provides
 * type-safe tenant identification throughout the domain.
 */
@Getter
@EqualsAndHashCode
public class TenantId {

    private String value;

    /**
     * Default constructor for MongoDB deserialization.
     */
    public TenantId() {
    }

    /**
     * Creates a new TenantId with the specified value.
     *
     * @param value the tenant identifier, must not be null or empty
     * @throws IllegalArgumentException if value is null or empty
     */
    public TenantId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Tenant ID cannot be null or empty");
        }
        this.value = value.trim();
    }

    /**
     * Sets the tenant identifier value.
     * Used by MongoDB deserialization.
     *
     * @param value the tenant ID to set
     */
    public void setValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Tenant ID cannot be null or empty");
        }
        this.value = value.trim();
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * Static factory method to create a TenantId.
     *
     * @param value the tenant identifier
     * @return a new TenantId instance
     */
    public static TenantId of(String value) {
        return new TenantId(value);
    }
}
