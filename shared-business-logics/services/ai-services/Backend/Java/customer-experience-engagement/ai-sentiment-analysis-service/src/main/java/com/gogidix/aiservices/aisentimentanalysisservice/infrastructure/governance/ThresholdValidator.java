package com.gogidix.aiservices.aisentimentanalysisservice.infrastructure.governance;

import com.gogidix.aiservices.aisentimentanalysisservice.infrastructure.metrics.SentimentAnalysisMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ThresholdValidator {

    private static final Logger log = LoggerFactory.getLogger(ThresholdValidator.class);

    private final SentimentAnalysisMetrics metrics;

    private static final double MAX_P95_LATENCY_MS = 500.0;
    private static final double MAX_P99_LATENCY_MS = 1000.0;
    private static final double MAX_ERROR_RATE = 0.01;
    private static final double MAX_AI_LATENCY_MS = 400.0;

    public ThresholdValidator(SentimentAnalysisMetrics metrics) {
        this.metrics = metrics;
    }

    public ComplianceReport validateAllThresholds() {
        ComplianceReport report = new ComplianceReport();

        double p95Latency = metrics.getAnalysisLatencyP95();
        boolean p95Compliant = p95Latency <= MAX_P95_LATENCY_MS;
        report.addCheck("P95 Latency", p95Latency, MAX_P95_LATENCY_MS, p95Compliant);

        double p99Latency = metrics.getAnalysisLatencyP99();
        boolean p99Compliant = p99Latency <= MAX_P99_LATENCY_MS;
        report.addCheck("P99 Latency", p99Latency, MAX_P99_LATENCY_MS, p99Compliant);

        double aiLatency = metrics.getAiInferenceLatencyP95();
        boolean aiCompliant = aiLatency <= MAX_AI_LATENCY_MS;
        report.addCheck("AI Inference Latency", aiLatency, MAX_AI_LATENCY_MS, aiCompliant);

        double errorRate = metrics.getErrorRate();
        boolean errorCompliant = errorRate <= MAX_ERROR_RATE;
        report.addCheck("Error Rate", errorRate * 100, MAX_ERROR_RATE * 100, errorCompliant, "%");

        report.setCompliant(report.getCheckCount() == report.getPassedCheckCount());

        log.info("Threshold validation completed: {} - {} checks passed, {} checks failed",
                report.isCompliant() ? "COMPLIANT" : "NON-COMPLIANT",
                report.getPassedCheckCount(),
                report.getFailedCheckCount());

        return report;
    }

    public ThresholdValidation validateThreshold(ThresholdType type) {
        return switch (type) {
            case P95_LATENCY -> {
                double value = metrics.getAnalysisLatencyP95();
                boolean passed = value <= MAX_P95_LATENCY_MS;
                yield new ThresholdValidation(type, value, MAX_P95_LATENCY_MS, "ms", passed);
            }
            case P99_LATENCY -> {
                double value = metrics.getAnalysisLatencyP99();
                boolean passed = value <= MAX_P99_LATENCY_MS;
                yield new ThresholdValidation(type, value, MAX_P99_LATENCY_MS, "ms", passed);
            }
            case AI_INFERENCE_LATENCY -> {
                double value = metrics.getAiInferenceLatencyP95();
                boolean passed = value <= MAX_AI_LATENCY_MS;
                yield new ThresholdValidation(type, value, MAX_AI_LATENCY_MS, "ms", passed);
            }
            case ERROR_RATE -> {
                double value = metrics.getErrorRate() * 100;
                boolean passed = value <= MAX_ERROR_RATE * 100;
                yield new ThresholdValidation(type, value, MAX_ERROR_RATE * 100, "%", passed);
            }
        };
    }

    public boolean isDegraded() {
        ComplianceReport report = validateAllThresholds();
        return !report.isCompliant();
    }

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
        P95_LATENCY,
        P99_LATENCY,
        AI_INFERENCE_LATENCY,
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
