package com.gogidix.hr.globalhrdashboard.domain.model;

/**
 * Enum representing the trend direction of a metric
 */
public enum MetricTrend {
    UP("Increasing", "Metric value is increasing"),
    DOWN("Decreasing", "Metric value is decreasing"),
    STABLE("Stable", "Metric value is relatively stable"),
    IMPROVING("Improving", "Metric value is improving in a positive direction"),
    DECLINING("Declining", "Metric value is declining in a negative direction"),
    VOLATILE("Volatile", "Metric value is fluctuating significantly");

    private final String label;
    private final String description;

    MetricTrend(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPositive() {
        return this == IMPROVING || this == UP;
    }

    public boolean isNegative() {
        return this == DECLINING || this == DOWN;
    }

    public boolean isNeutral() {
        return this == STABLE || this == VOLATILE;
    }

    public static MetricTrend fromValueComparison(Double current, Double previous) {
        if (current == null || previous == null) {
            return STABLE;
        }

        double changePercent = ((current - previous) / previous) * 100;

        if (Math.abs(changePercent) < 1.0) {
            return STABLE;
        } else if (Math.abs(changePercent) > 10.0) {
            return changePercent > 0 ? IMPROVING : DECLINING;
        } else {
            return changePercent > 0 ? UP : DOWN;
        }
    }
}
