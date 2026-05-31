package com.gogidix.aiservices.anomalydetectionservice.infrastructure.governance;

import com.gogidix.aiservices.anomalydetectionservice.infrastructure.metrics.AnomalyDetectionMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Governance validator for enforcing operational thresholds.
 */
@Component
public class ThresholdValidator {

    private static final Logger log = LoggerFactory.getLogger(ThresholdValidator.class);
    private final AnomalyDetectionMetrics metrics;

    private static final double MAX_P95_DETECTION_MS = 2000.0;
    private static final double MAX_P99_DETECTION_MS = 5000.0;
    private static final double MAX_MODEL_INFERENCE_MS = 500.0;
    private static final double MAX_ERROR_RATE = 0.01;  // 1%

    public ThresholdValidator(AnomalyDetectionMetrics metrics) {
        this.metrics = metrics;
    }

    public ComplianceReport validateAllThresholds() {
        ComplianceReport report = new ComplianceReport();

        double detectionLatency = metrics.getDetectionLatencyP95();
        boolean detectionCompliant = detectionLatency <= MAX_P95_DETECTION_MS;
        report.addCheck("P95 Detection Latency", detectionLatency, MAX_P95_DETECTION_MS, detectionCompliant);

        double modelInferenceLatency = metrics.getModelInferenceLatencyP95();
        boolean modelCompliant = modelInferenceLatency <= MAX_MODEL_INFERENCE_MS;
        report.addCheck("Model Inference Latency", modelInferenceLatency, MAX_MODEL_INFERENCE_MS, modelCompliant);

        double errorRate = metrics.getErrorRate();
        boolean errorCompliant = errorRate <= MAX_ERROR_RATE;
        report.addCheck("Error Rate", errorRate * 100, MAX_ERROR_RATE * 100, errorCompliant, "%");

        report.setCompliant(report.getCheckCount() == report.getPassedCheckCount());

        log.info("Threshold validation: {}", report.isCompliant() ? "COMPLIANT" : "NON-COMPLIANT");

        return report;
    }

    public HealthStatus getHealthStatus() {
        ComplianceReport report = validateAllThresholds();
        if (report.isCompliant()) return HealthStatus.HEALTHY;
        if (report.getPassedCheckCount() >= report.getCheckCount() * 0.75) return HealthStatus.DEGRADED;
        return HealthStatus.UNHEALTHY;
    }

    public enum HealthStatus { HEALTHY, DEGRADED, UNHEALTHY }

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

        public record CheckResult(String name, double actualValue, double threshold, boolean passed, String unit) {
            public double getVariance() { return actualValue - threshold; }
        }
    }
}
