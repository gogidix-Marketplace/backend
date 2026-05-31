package com.gogidix.aiservices.aianalyticsdashboard.infrastructure.governance;

import com.gogidix.aiservices.aianalyticsdashboard.infrastructure.metrics.DashboardMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Governance validator for enforcing operational thresholds.
 * Validates that the service operates within defined SLO boundaries.
 */
@Component
public class ThresholdValidator {

    private static final Logger log = LoggerFactory.getLogger(ThresholdValidator.class);

    private final DashboardMetrics metrics;

    // Threshold definitions for Analytics Dashboard Service
    private static final double MAX_P95_LATENCY_MS = 500.0;
    private static final double MAX_P99_LATENCY_MS = 1000.0;
    private static final double MAX_QUERY_LATENCY_MS = 300.0;
    private static final double MAX_AGGREGATION_LATENCY_MS = 400.0;
    private static final double MAX_ERROR_RATE = 0.01;  // 1%

    public ThresholdValidator(DashboardMetrics metrics) {
        this.metrics = metrics;
    }

    /**
     * Validate all SLO thresholds
     */
    public ComplianceReport validateAllThresholds() {
        ComplianceReport report = new ComplianceReport();

        // Validate P95 latency for dashboard creation
        double p95CreationLatency = metrics.getDashboardCreationLatencyP95();
        boolean p95CreationCompliant = p95CreationLatency <= MAX_P95_LATENCY_MS;
        report.addCheck("Dashboard Creation P95 Latency", p95CreationLatency, MAX_P95_LATENCY_MS, p95CreationCompliant);

        // Validate P99 latency for dashboard creation
        double p99CreationLatency = metrics.getDashboardCreationLatencyP99();
        boolean p99CreationCompliant = p99CreationLatency <= MAX_P99_LATENCY_MS;
        report.addCheck("Dashboard Creation P99 Latency", p99CreationLatency, MAX_P99_LATENCY_MS, p99CreationCompliant);

        // Validate query latency
        double queryLatency = metrics.getDashboardQueryLatencyP95();
        boolean queryCompliant = queryLatency <= MAX_QUERY_LATENCY_MS;
        report.addCheck("Dashboard Query P95 Latency", queryLatency, MAX_QUERY_LATENCY_MS, queryCompliant);

        // Validate aggregation latency
        double aggregationLatency = metrics.getMetricAggregationLatencyP95();
        boolean aggregationCompliant = aggregationLatency <= MAX_AGGREGATION_LATENCY_MS;
        report.addCheck("Metric Aggregation P95 Latency", aggregationLatency, MAX_AGGREGATION_LATENCY_MS, aggregationCompliant);

        // Validate error rate
        double errorRate = metrics.getErrorRate();
        boolean errorCompliant = errorRate <= MAX_ERROR_RATE;
        report.addCheck("Error Rate", errorRate * 100, MAX_ERROR_RATE * 100, errorCompliant, "%");

        // Overall compliance
        report.setCompliant(report.getCheckCount() == report.getPassedCheckCount());

        log.info("Threshold validation completed: {} - {} checks passed, {} checks failed",
                report.isCompliant() ? "COMPLIANT" : "NON-COMPLIANT",
                report.getPassedCheckCount(),
                report.getFailedCheckCount());

        return report;
    }

    /**
     * Validate a specific threshold
     */
    public ThresholdValidation validateThreshold(ThresholdType type) {
        return switch (type) {
            case P95_CREATION_LATENCY -> {
                double value = metrics.getDashboardCreationLatencyP95();
                boolean passed = value <= MAX_P95_LATENCY_MS;
                yield new ThresholdValidation(type, value, MAX_P95_LATENCY_MS, "ms", passed);
            }
            case P99_CREATION_LATENCY -> {
                double value = metrics.getDashboardCreationLatencyP99();
                boolean passed = value <= MAX_P99_LATENCY_MS;
                yield new ThresholdValidation(type, value, MAX_P99_LATENCY_MS, "ms", passed);
            }
            case P95_QUERY_LATENCY -> {
                double value = metrics.getDashboardQueryLatencyP95();
                boolean passed = value <= MAX_QUERY_LATENCY_MS;
                yield new ThresholdValidation(type, value, MAX_QUERY_LATENCY_MS, "ms", passed);
            }
            case P95_AGGREGATION_LATENCY -> {
                double value = metrics.getMetricAggregationLatencyP95();
                boolean passed = value <= MAX_AGGREGATION_LATENCY_MS;
                yield new ThresholdValidation(type, value, MAX_AGGREGATION_LATENCY_MS, "ms", passed);
            }
            case ERROR_RATE -> {
                double value = metrics.getErrorRate() * 100;
                boolean passed = value <= MAX_ERROR_RATE * 100;
                yield new ThresholdValidation(type, value, MAX_ERROR_RATE * 100, "%", passed);
            }
        };
    }

    /**
     * Check if service is in a degraded state
     */
    public boolean isDegraded() {
        ComplianceReport report = validateAllThresholds();
        return !report.isCompliant();
    }

    /**
     * Get health status based on thresholds
     */
    public HealthStatus getHealthStatus() {
        ComplianceReport report = validateAllThresholds();

        if (report.isCompliant()) {
            return HealthStatus.HEALTHY;
        } else if (report.getPassedCheckCount() >= report.getCheckCount() * 0.75) {
            return HealthStatus.DEGRADED;
        } else {
            return HealthStatus.UNHEALTHY;
        }
    }

    public enum ThresholdType {
        P95_CREATION_LATENCY,
        P99_CREATION_LATENCY,
        P95_QUERY_LATENCY,
        P95_AGGREGATION_LATENCY,
        ERROR_RATE
    }

    public enum HealthStatus {
        HEALTHY,
        DEGRADED,
        UNHEALTHY
    }

    public static class ThresholdValidation {
        private final ThresholdType type;
        private final double actualValue;
        private final double threshold;
        private final String unit;
        private final boolean passed;

        public ThresholdValidation(ThresholdType type, double actualValue, double threshold, String unit, boolean passed) {
            this.type = type;
            this.actualValue = actualValue;
            this.threshold = threshold;
            this.unit = unit;
            this.passed = passed;
        }

        public ThresholdType getType() { return type; }
        public double getActualValue() { return actualValue; }
        public double getThreshold() { return threshold; }
        public String getUnit() { return unit; }
        public boolean isPassed() { return passed; }
    }

    public static class ComplianceReport {
        private final java.util.List<CheckResult> checks = new java.util.ArrayList<>();
        private boolean compliant;

        public void addCheck(String name, double actual, double threshold, boolean passed) {
            checks.add(new CheckResult(name, actual, threshold, passed, ""));
        }

        public void addCheck(String name, double actual, double threshold, boolean passed, String unit) {
            checks.add(new CheckResult(name, actual, threshold, passed, unit));
        }

        public boolean isCompliant() { return compliant; }
        public void setCompliant(boolean compliant) { this.compliant = compliant; }
        public java.util.List<CheckResult> getChecks() { return checks; }
        public int getCheckCount() { return checks.size(); }
        public long getPassedCheckCount() { return checks.stream().filter(CheckResult::passed).count(); }
        public long getFailedCheckCount() { return checks.stream().filter(c -> !c.passed()).count(); }

        public record CheckResult(
            String name,
            double actualValue,
            double threshold,
            boolean passed,
            String unit
        ) {
            public double getVariance() { return actualValue - threshold; }
            public double getVariancePercent() { return threshold > 0 ? ((actualValue - threshold) / threshold) * 100 : 0; }
        }
    }
}
