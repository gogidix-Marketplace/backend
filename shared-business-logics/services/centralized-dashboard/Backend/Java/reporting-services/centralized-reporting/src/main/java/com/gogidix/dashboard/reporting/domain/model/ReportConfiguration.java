package com.gogidix.dashboard.reporting.domain.model;

/**
 * Report Configuration Value Object
 */
public class ReportConfiguration {
    private final OutputFormat format;
    private final boolean includeCharts;
    private final boolean includeSummary;

    public ReportConfiguration(OutputFormat format, boolean includeCharts, boolean includeSummary) {
        this.format = format;
        this.includeCharts = includeCharts;
        this.includeSummary = includeSummary;
    }

    public OutputFormat getFormat() {
        return format;
    }

    public boolean isIncludeCharts() {
        return includeCharts;
    }

    public boolean isIncludeSummary() {
        return includeSummary;
    }

    /**
     * Get number of sections per page
     */
    public int getSectionsPerPage() {
        return 10; // Default 10 sections per page
    }

    /**
     * Get output format
     */
    public OutputFormat getOutputFormat() {
        return format;
    }

    /**
     * Get metric status based on value
     */
    public MetricStatus getMetricStatus(String metricName, Double value) {
        // Simple threshold logic
        if (value >= 90.0) return MetricStatus.EXCELLENT;
        if (value >= 70.0) return MetricStatus.GOOD;
        if (value >= 50.0) return MetricStatus.FAIR;
        return MetricStatus.POOR;
    }

    /**
     * Calculate trend for metric
     */
    public String calculateTrend(String metricName, Double value) {
        return "↑"; // Default upward trend
    }
}
