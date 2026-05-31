package com.gogidix.aiservices.aifeaturestoreservice.domain.model;

import java.util.Map;
import java.util.Objects;

/**
 * Value object representing feature metadata.
 */
public record FeatureMetadata(String key, Object value) {
    public FeatureMetadata {
        Objects.requireNonNull(key, "key cannot be null");
        Objects.requireNonNull(value, "value cannot be null");
    }

    public static FeatureMetadata of(String key, Object value) {
        return new FeatureMetadata(key, value);
    }
}
