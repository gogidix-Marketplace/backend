package com.gogidix.dashboard.aggregation.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Metric Identifier Value Object
 * 
 * Immutable identifier for aggregated metrics
 */
public class MetricId {
    private final String value;
    
    private MetricId(String value) {
        this.value = validateId(value);
    }
    
    public static MetricId of(String value) {
        return new MetricId(value);
    }
    
    public static MetricId generate() {
        return new MetricId(UUID.randomUUID().toString());
    }
    
    public static MetricId fromComponents(String metricType, String dimension, String timeWindow) {
        String composite = String.format("%s_%s_%s_%d", 
                                       metricType.toLowerCase().replace(" ", "_"),
                                       dimension.toLowerCase().replace(" ", "_"),
                                       timeWindow.toLowerCase().replace(" ", "_"),
                                       System.currentTimeMillis());
        return new MetricId(composite);
    }
    
    private String validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Metric ID cannot be null or empty");
        }
        if (id.length() > 255) {
            throw new IllegalArgumentException("Metric ID cannot exceed 255 characters");
        }
        return id.trim();
    }
    
    /**
     * Check if this ID follows UUID format
     */
    public boolean isUUIDFormat() {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Check if this ID is composite (contains underscores)
     */
    public boolean isComposite() {
        return value.contains("_");
    }
    
    /**
     * Extract metric type from composite ID
     */
    public String extractMetricType() {
        if (!isComposite()) {
            return null;
        }
        String[] parts = value.split("_");
        return parts.length > 0 ? parts[0] : null;
    }
    
    /**
     * Extract dimension from composite ID
     */
    public String extractDimension() {
        if (!isComposite()) {
            return null;
        }
        String[] parts = value.split("_");
        return parts.length > 1 ? parts[1] : null;
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