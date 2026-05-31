package com.gogidix.aiservices.aiproductrecommendationservice.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Value object representing a unique Product Rule identifier.
 */
public class ProductRuleId {

    private final UUID value;

    public ProductRuleId() {
        this.value = UUID.randomUUID();
    }

    public ProductRuleId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("ProductRuleId value cannot be null");
        }
        this.value = value;
    }

    public static ProductRuleId randomUUID() {
        return new ProductRuleId(UUID.randomUUID());
    }

    public static ProductRuleId fromString(String uuid) {
        try {
            return new ProductRuleId(UUID.fromString(uuid));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid UUID string: " + uuid, e);
        }
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductRuleId that = (ProductRuleId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
