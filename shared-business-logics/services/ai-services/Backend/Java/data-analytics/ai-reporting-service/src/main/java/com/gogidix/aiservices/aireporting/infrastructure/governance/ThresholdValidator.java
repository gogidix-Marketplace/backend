package com.gogidix.aiservices.aireporting.infrastructure.governance;

import com.gogidix.aiservices.aireporting.infrastructure.metrics.ReportingMetrics;
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

    private final ReportingMetrics metrics;

    // Threshold definitions for Reporting Service
    private static final double MAX_P95_LATENCY_MS = 1000.0;
    private static final double MAX_P99_LATENCY_MS = 2000.0;
    private static final double MAX_DATA_EXTRACTION_LATENCY_MS = 800.0;
    private static final double MAX_ERROR_RATE = 0.01;  // 1%

    public ThresholdValidator(ReportingMetrics metrics) {
        this.metrics = metrics;
    }

    /**
     * Validate all SLO thresholds
     */
    public ComplianceReport validateAllThresholds() {
        ComplianceReport report = new ComplianceReport();

        // Validate P95 latency for report generation
        double p95GenerationLatency = metrics.getReportGenerationLatencyP95();
        boolean p95GenerationCompliant = p95GenerationLatency <= MAX_P95_LATENCY_MS;
        report.addCheck("Report Generation P95 Latency", p95GenerationLatency, MAX_P95_LATENCY_MS, p95GenerationCompliant);

        // Validate P99 latency for report generation
        double p99GenerationLatency = metrics.getReportGenerationLatencyP99();
        boolean p99GenerationCompliant = p99GenerationLatency <= MAX_P99_LATENCY_MS;
        report.addCheck("Report Generation P99 Latency", p99GenerationLatency, MAX_P99_LATENCY_MS, p99GenerationCompliant);

        // Validate data extraction latency
        double extractionLatency = metrics.getDataExtractionLatencyP95();
        boolean extractionCompliant = extractionLatency <= MAX_DATA_EXTRACTION_LATENCY_MS;
        report.addCheck("Data Extraction P95 Latency", extractionLatency, MAX_DATA_EXTRACTION_LATENCY_MS, extractionCompliant);

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
            case P95_GENERATION_LATENCY -> {
                double value = metrics.getReportGenerationLatencyP95();
                boolean passed = value <= MAX_P95_LATENCY_MS;
                yield new ThresholdValidation(type, value, MAX_P95_LATENCY_MS, "ms", passed);
            }
            case P99_GENERATION_LATENCY -> {
                double value = metrics.getReportGenerationLatencyP99();
                boolean passed = value <= MAX_P99_LATENCY_MS;
                yield new ThresholdValidation(type, value, MAX_P99_LATENCY_MS, "ms", passed);
            }
            case P95_DATA_EXTRACTION_LATENCY -> {
                double value = metrics.getDataExtractionLatencyP95();
                boolean passed = value <= MAX_DATA_EXTRACTION_LATENCY_MS;
                yield new ThresholdValidation(type, value, MAX_DATA_EXTRACTION_LATENCY_MS, "ms", passed);
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
        P95_GENERATION_LATENCY,
        P99_GENERATION_LATENCY,
        P95_DATA_EXTRACTION_LATENCY,
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
