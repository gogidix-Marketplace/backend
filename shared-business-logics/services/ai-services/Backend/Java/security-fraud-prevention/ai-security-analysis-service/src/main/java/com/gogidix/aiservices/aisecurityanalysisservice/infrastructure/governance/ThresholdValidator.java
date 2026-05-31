package com.gogidix.aiservices.aisecurityanalysisservice.infrastructure.governance;

import com.gogidix.aiservices.aisecurityanalysisservice.infrastructure.metrics.SecurityAnalysisMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Governance validator for enforcing operational thresholds.
 * Validates that the security analysis service operates within defined SLO boundaries.
 */
@Component
public class ThresholdValidator {

    private static final Logger log = LoggerFactory.getLogger(ThresholdValidator.class);

    private final SecurityAnalysisMetrics metrics;

    // Threshold definitions for security analysis (longer timeouts for scans)
    private static final double MAX_P95_SCAN_LATENCY_MS = 5000.0;  // 5 seconds for full scan
    private static final double MAX_P99_SCAN_LATENCY_MS = 10000.0; // 10 seconds
    private static final double MAX_ERROR_RATE = 0.02;  // 2% - scans can fail
    private static final double MAX_VULNERABILITY_DETECTION_MS = 1000.0;
    private static final double MIN_SUCCESS_RATE = 0.98;  // 98% success rate

    public ThresholdValidator(SecurityAnalysisMetrics metrics) {
        this.metrics = metrics;
    }

    /**
     * Validate all SLO thresholds
     */
    public ComplianceReport validateAllThresholds() {
        ComplianceReport report = new ComplianceReport();

        // Validate P95 scan latency
        double p95Latency = metrics.getScanLatencyP95();
        boolean p95Compliant = p95Latency <= MAX_P95_SCAN_LATENCY_MS;
        report.addCheck("P95 Scan Latency", p95Latency, MAX_P95_SCAN_LATENCY_MS, p95Compliant);

        // Validate P99 scan latency
        double p99Latency = metrics.getScanLatencyP99();
        boolean p99Compliant = p99Latency <= MAX_P99_SCAN_LATENCY_MS;
        report.addCheck("P99 Scan Latency", p99Latency, MAX_P99_SCAN_LATENCY_MS, p99Compliant);

        // Validate vulnerability detection latency
        double vulnDetectionLatency = metrics.getVulnerabilityDetectionLatencyP95();
        boolean vulnCompliant = vulnDetectionLatency <= MAX_VULNERABILITY_DETECTION_MS;
        report.addCheck("Vulnerability Detection Latency", vulnDetectionLatency, MAX_VULNERABILITY_DETECTION_MS, vulnCompliant);

        // Validate error rate
        double errorRate = metrics.getErrorRate();
        boolean errorCompliant = errorRate <= MAX_ERROR_RATE;
        report.addCheck("Error Rate", errorRate * 100, MAX_ERROR_RATE * 100, errorCompliant, "%");

        // Validate success rate
        double successRate = metrics.getSuccessRate();
        boolean successCompliant = successRate >= MIN_SUCCESS_RATE;
        report.addCheck("Success Rate", successRate * 100, MIN_SUCCESS_RATE * 100, successCompliant, "%");

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
            case P95_SCAN_LATENCY -> {
                double value = metrics.getScanLatencyP95();
                boolean passed = value <= MAX_P95_SCAN_LATENCY_MS;
                yield new ThresholdValidation(type, value, MAX_P95_SCAN_LATENCY_MS, "ms", passed);
            }
            case P99_SCAN_LATENCY -> {
                double value = metrics.getScanLatencyP99();
                boolean passed = value <= MAX_P99_SCAN_LATENCY_MS;
                yield new ThresholdValidation(type, value, MAX_P99_SCAN_LATENCY_MS, "ms", passed);
            }
            case VULNERABILITY_DETECTION_LATENCY -> {
                double value = metrics.getVulnerabilityDetectionLatencyP95();
                boolean passed = value <= MAX_VULNERABILITY_DETECTION_MS;
                yield new ThresholdValidation(type, value, MAX_VULNERABILITY_DETECTION_MS, "ms", passed);
            }
            case ERROR_RATE -> {
                double value = metrics.getErrorRate() * 100;
                boolean passed = value <= MAX_ERROR_RATE * 100;
                yield new ThresholdValidation(type, value, MAX_ERROR_RATE * 100, "%", passed);
            }
            case SUCCESS_RATE -> {
                double value = metrics.getSuccessRate() * 100;
                boolean passed = value >= MIN_SUCCESS_RATE * 100;
                yield new ThresholdValidation(type, value, MIN_SUCCESS_RATE * 100, "%", passed);
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
        P95_SCAN_LATENCY,
        P99_SCAN_LATENCY,
        VULNERABILITY_DETECTION_LATENCY,
        ERROR_RATE,
        SUCCESS_RATE
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
