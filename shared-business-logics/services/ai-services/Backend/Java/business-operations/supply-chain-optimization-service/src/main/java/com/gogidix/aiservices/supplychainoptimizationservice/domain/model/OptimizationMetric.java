package com.gogidix.aiservices.supplychainoptimizationservice.domain.model;

import java.util.Map;

public class OptimizationMetric {
    private final String metricName;
    private final double currentValue;
    private final double optimizedValue;
    private final double improvement;
    private final String unit;
    private final Map<String, Object> metadata;

    private OptimizationMetric(Builder builder) {
        this.metricName = builder.metricName;
        this.currentValue = builder.currentValue;
        this.optimizedValue = builder.optimizedValue;
        this.improvement = calculateImprovement(builder.currentValue, builder.optimizedValue, builder.direction);
        this.unit = builder.unit;
        this.metadata = builder.metadata;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getMetricName() {
        return metricName;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public double getOptimizedValue() {
        return optimizedValue;
    }

    public double getImprovement() {
        return improvement;
    }

    public String getUnit() {
        return unit;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public boolean isPositiveImprovement() {
        return improvement > 0;
    }

    public double getImprovementPercentage() {
        if (currentValue == 0) {
            return optimizedValue > 0 ? 100 : 0;
        }
        return (improvement / currentValue) * 100;
    }

    private static double calculateImprovement(double current, double optimized, ImprovementDirection direction) {
        if (direction == ImprovementDirection.HIGHER_IS_BETTER) {
            return optimized - current;
        } else {
            return current - optimized;
        }
    }

    public enum ImprovementDirection {
        HIGHER_IS_BETTER,
        LOWER_IS_BETTER
    }

    public static class Builder {
        private String metricName;
        private double currentValue;
        private double optimizedValue;
        private String unit;
        private Map<String, Object> metadata = Map.of();
        private ImprovementDirection direction = ImprovementDirection.LOWER_IS_BETTER;

        public Builder metricName(String metricName) {
            this.metricName = metricName;
            return this;
        }

        public Builder currentValue(double currentValue) {
            this.currentValue = currentValue;
            return this;
        }

        public Builder optimizedValue(double optimizedValue) {
            this.optimizedValue = optimizedValue;
            return this;
        }

        public Builder unit(String unit) {
            this.unit = unit;
            return this;
        }

        public Builder metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder direction(ImprovementDirection direction) {
            this.direction = direction;
            return this;
        }

        public OptimizationMetric build() {
            return new OptimizationMetric(this);
        }
    }
}
