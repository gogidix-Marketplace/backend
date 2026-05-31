package com.gogidix.dashboard.reporting.domain.model;

/**
 * Metric Display Value Object
 */
public class MetricDisplay {
    private final String metricName;
    private final Object value;
    private final String unit;
    private final String formattedValue;
    private final MetricStatus status;
    private final String trend;

    public MetricDisplay(String metricName, Object value, String unit) {
        this.metricName = metricName;
        this.value = value;
        this.unit = unit;
        this.formattedValue = null;
        this.status = null;
        this.trend = null;
    }

    private MetricDisplay(Builder builder) {
        this.metricName = builder.metricName;
        this.value = builder.value;
        this.unit = builder.unit;
        this.formattedValue = builder.formattedValue;
        this.status = builder.status;
        this.trend = builder.trend;
    }

    public String getMetricName() {
        return metricName;
    }

    public Object getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public String getFormattedValue() {
        return formattedValue;
    }

    public MetricStatus getStatus() {
        return status;
    }

    public String getTrend() {
        return trend;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String metricName;
        private Object value;
        private String unit;
        private String formattedValue;
        private MetricStatus status;
        private String trend;

        public Builder withMetricName(String metricName) {
            this.metricName = metricName;
            return this;
        }

        public Builder withName(String name) {
            this.metricName = name;
            return this;
        }

        public Builder withValue(Object value) {
            this.value = value;
            return this;
        }

        public Builder withUnit(String unit) {
            this.unit = unit;
            return this;
        }

        public Builder withFormattedValue(String formattedValue) {
            this.formattedValue = formattedValue;
            return this;
        }

        public Builder withStatus(MetricStatus status) {
            this.status = status;
            return this;
        }

        public Builder withTrend(String trend) {
            this.trend = trend;
            return this;
        }

        public MetricDisplay build() {
            return new MetricDisplay(this);
        }
    }
}
