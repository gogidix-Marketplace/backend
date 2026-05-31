package com.gogidix.aiservices.aifeatureextractionservice.domain.model;

import java.util.Objects;

/**
 * Value object representing a single feature value.
 */
public record FeatureValue(String name, Object value) {
    public FeatureValue {
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(value, "value cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
    }

    public static FeatureValue of(String name, Object value) {
        return new FeatureValue(name, value);
    }

    public static FeatureValue numeric(String name, Number value) {
        return new FeatureValue(name, value.doubleValue());
    }

    public static FeatureValue categorical(String name, String value) {
        return new FeatureValue(name, value);
    }

    public static FeatureValue text(String name, String value) {
        return new FeatureValue(name, value);
    }
}
