package com.gogidix.dashboard.performance.domain.model;

import java.util.Objects;

/**
 * Performance Thresholds Value Object
 */
public class PerformanceThresholds {
    private final double minThreshold;
    private final double maxThreshold;
    private final double warningThreshold;
    private final double criticalThreshold;
    
    public PerformanceThresholds(double minThreshold, double maxThreshold, 
                                double warningThreshold, double criticalThreshold) {
        this.minThreshold = minThreshold;
        this.maxThreshold = maxThreshold;
        this.warningThreshold = warningThreshold;
        this.criticalThreshold = criticalThreshold;
    }
    
    public boolean isWarningThresholdBreached(double value) {
        return value >= warningThreshold;
    }
    
    public boolean isCriticalThresholdBreached(double value) {
        return value >= criticalThreshold;
    }
    
    public boolean isOptimal(double value) {
        return value >= minThreshold && value <= maxThreshold;
    }
    
    public double getMinThreshold() { return minThreshold; }
    public double getMaxThreshold() { return maxThreshold; }
    public double getWarningThreshold() { return warningThreshold; }
    public double getCriticalThreshold() { return criticalThreshold; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PerformanceThresholds that = (PerformanceThresholds) o;
        return Double.compare(that.minThreshold, minThreshold) == 0 &&
               Double.compare(that.maxThreshold, maxThreshold) == 0 &&
               Double.compare(that.warningThreshold, warningThreshold) == 0 &&
               Double.compare(that.criticalThreshold, criticalThreshold) == 0;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(minThreshold, maxThreshold, warningThreshold, criticalThreshold);
    }
}