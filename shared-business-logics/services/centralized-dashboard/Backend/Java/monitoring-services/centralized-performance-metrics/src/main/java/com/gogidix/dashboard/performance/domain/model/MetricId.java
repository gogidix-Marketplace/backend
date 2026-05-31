package com.gogidix.dashboard.performance.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Metric ID Value Object
 */
public class MetricId {
    private final String value;
    
    private MetricId(String value) {
        this.value = Objects.requireNonNull(value, "Metric ID cannot be null");
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException("Metric ID cannot be empty");
        }
    }
    
    public static MetricId generate() {
        return new MetricId(UUID.randomUUID().toString());
    }
    
    public static MetricId of(String value) {
        return new MetricId(value);
    }

    public static MetricId fromString(String value) {
        return of(value);
    }

    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetricId metricId = (MetricId) o;
        return Objects.equals(value, metricId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}