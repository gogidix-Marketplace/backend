package com.gogidix.management.executive.workflow.domain.model;

import lombok.Value;

import java.util.Objects;

/**
 * Value object representing a Tenant ID
 * Provides type safety for tenant identification
 */
@Value
public class TenantId {
    String value;

    public TenantId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Tenant ID cannot be null or blank");
        }
        this.value = value;
    }

    public static TenantId of(String value) {
        return new TenantId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantId tenantId = (TenantId) o;
        return Objects.equals(value, tenantId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "TenantId{" + value + "}";
    }
}
