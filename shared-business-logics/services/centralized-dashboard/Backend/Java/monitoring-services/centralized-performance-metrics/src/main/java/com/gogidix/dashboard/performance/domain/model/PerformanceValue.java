package com.gogidix.dashboard.performance.domain.model;

import java.util.Objects;

/**
 * Performance Value Object
 */
public class PerformanceValue {
    private final double value;
    private final String unit;
    
    public PerformanceValue(double value, String unit) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("Performance value must be finite");
        }
        this.value = value;
        this.unit = Objects.requireNonNull(unit, "Unit cannot be null");
    }

    public static PerformanceValue of(double value) {
        return new PerformanceValue(value, "");
    }

    public static PerformanceValue of(double value, String unit) {
        return new PerformanceValue(value, unit);
    }

    public double getValue() {
        return value;
    }
    
    public String getUnit() {
        return unit;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PerformanceValue that = (PerformanceValue) o;
        return Double.compare(that.value, value) == 0 && Objects.equals(unit, unit);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value, unit);
    }
    
    @Override
    public String toString() {
        return value + " " + unit;
    }
}