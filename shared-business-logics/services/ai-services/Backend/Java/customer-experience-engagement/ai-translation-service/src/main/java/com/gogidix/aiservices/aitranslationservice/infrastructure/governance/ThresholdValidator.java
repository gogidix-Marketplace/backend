package com.gogidix.aiservices.aitranslationservice.infrastructure.governance;

import com.gogidix.aiservices.aitranslationservice.infrastructure.metrics.TranslationMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ThresholdValidator {

    private static final Logger log = LoggerFactory.getLogger(ThresholdValidator.class);
    private final TranslationMetrics metrics;

    private static final double MAX_P95_LATENCY_MS = 500.0;
    private static final double MAX_P99_LATENCY_MS = 1000.0;
    private static final double MAX_DETECTION_LATENCY_MS = 200.0;
    private static final double MAX_ERROR_RATE = 0.01;

    public ThresholdValidator(TranslationMetrics metrics) {
        this.metrics = metrics;
    }

    public ComplianceReport validateAllThresholds() {
        ComplianceReport report = new ComplianceReport();

        double p95 = metrics.getTranslationLatencyP95();
        report.addCheck("P95 Latency", p95, MAX_P95_LATENCY_MS, p95 <= MAX_P95_LATENCY_MS);

        double p99 = metrics.getTranslationLatencyP99();
        report.addCheck("P99 Latency", p99, MAX_P99_LATENCY_MS, p99 <= MAX_P99_LATENCY_MS);

        double detection = metrics.getLanguageDetectionLatencyP95();
        report.addCheck("Detection Latency", detection, MAX_DETECTION_LATENCY_MS, detection <= MAX_DETECTION_LATENCY_MS);

        double errorRate = metrics.getErrorRate();
        report.addCheck("Error Rate", errorRate * 100, MAX_ERROR_RATE * 100, errorRate <= MAX_ERROR_RATE, "%");

        report.setCompliant(report.getCheckCount() == report.getPassedCheckCount());
        return report;
    }

    public ThresholdValidation validateThreshold(ThresholdType type) {
        return switch (type) {
            case P95_LATENCY -> {
                double v = metrics.getTranslationLatencyP95();
                yield new ThresholdValidation(type, v, MAX_P95_LATENCY_MS, "ms", v <= MAX_P95_LATENCY_MS);
            }
            case P99_LATENCY -> {
                double v = metrics.getTranslationLatencyP99();
                yield new ThresholdValidation(type, v, MAX_P99_LATENCY_MS, "ms", v <= MAX_P99_LATENCY_MS);
            }
            case DETECTION_LATENCY -> {
                double v = metrics.getLanguageDetectionLatencyP95();
                yield new ThresholdValidation(type, v, MAX_DETECTION_LATENCY_MS, "ms", v <= MAX_DETECTION_LATENCY_MS);
            }
            case ERROR_RATE -> {
                double v = metrics.getErrorRate() * 100;
                yield new ThresholdValidation(type, v, MAX_ERROR_RATE * 100, "%", v <= MAX_ERROR_RATE * 100);
            }
        };
    }

    public boolean isDegraded() { return !validateAllThresholds().isCompliant(); }

    public HealthStatus getHealthStatus() {
        var report = validateAllThresholds();
        if (report.isCompliant()) return HealthStatus.HEALTHY;
        if (report.getPassedCheckCount() >= report.getCheckCount() * 0.75) return HealthStatus.DEGRADED;
        return HealthStatus.UNHEALTHY;
    }

    public enum ThresholdType { P95_LATENCY, P99_LATENCY, DETECTION_LATENCY, ERROR_RATE }
    public enum HealthStatus { HEALTHY, DEGRADED, UNHEALTHY }

    public static class ThresholdValidation {
        private final ThresholdType type;
        private final double actualValue;
        private final double threshold;
        private final String unit;
        private final boolean passed;

        public ThresholdValidation(ThresholdType type, double actualValue, double threshold, String unit, boolean passed) {
            this.type = type; this.actualValue = actualValue; this.threshold = threshold; this.unit = unit; this.passed = passed;
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

        public void addCheck(String name, double actual, double threshold, boolean passed) { checks.add(new CheckResult(name, actual, threshold, passed, "")); }
        public void addCheck(String name, double actual, double threshold, boolean passed, String unit) { checks.add(new CheckResult(name, actual, threshold, passed, unit)); }
        public boolean isCompliant() { return compliant; }
        public void setCompliant(boolean compliant) { this.compliant = compliant; }
        public java.util.List<CheckResult> getChecks() { return checks; }
        public int getCheckCount() { return checks.size(); }
        public long getPassedCheckCount() { return checks.stream().filter(CheckResult::passed).count(); }
        public long getFailedCheckCount() { return checks.stream().filter(c -> !c.passed()).count(); }

        public record CheckResult(String name, double actualValue, double threshold, boolean passed, String unit) {
            public double getVariance() { return actualValue - threshold; }
            public double getVariancePercent() { return threshold > 0 ? ((actualValue - threshold) / threshold) * 100 : 0; }
        }
    }
}
