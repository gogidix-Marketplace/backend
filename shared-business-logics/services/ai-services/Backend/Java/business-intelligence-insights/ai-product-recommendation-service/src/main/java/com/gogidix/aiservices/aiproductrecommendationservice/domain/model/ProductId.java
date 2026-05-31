package com.gogidix.aiservices.aiproductrecommendationservice.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Value object representing a unique Product identifier.
 */
public class ProductId {

    private final UUID value;

    public ProductId() {
        this.value = UUID.randomUUID();
    }

    public ProductId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("ProductId value cannot be null");
        }
        this.value = value;
    }

    public static ProductId randomProductId() {
        return new ProductId(UUID.randomUUID());
    }

    public static ProductId of(String uuid) {
        return new ProductId(UUID.fromString(uuid));
    }

    public UUID getValue() {
        return value;
    }

    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductId productId = (ProductId) o;
        return Objects.equals(value, productId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
