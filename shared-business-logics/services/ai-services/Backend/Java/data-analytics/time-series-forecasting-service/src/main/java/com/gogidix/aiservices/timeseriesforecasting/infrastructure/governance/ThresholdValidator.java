package com.gogidix.aiservices.timeseriesforecasting.infrastructure.governance;

import com.gogidix.aiservices.timeseriesforecasting.infrastructure.metrics.TimeSeriesForecastingMetrics;
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

    private final TimeSeriesForecastingMetrics metrics;

    // Threshold definitions for Time Series Forecasting Service
    private static final double MAX_P95_LATENCY_MS = 2000.0;
    private static final double MAX_P99_LATENCY_MS = 5000.0;
    private static final double MAX_MODEL_TRAINING_LATENCY_MS = 3000.0;
    private static final double MAX_ERROR_RATE = 0.01;  // 1%

    public ThresholdValidator(TimeSeriesForecastingMetrics metrics) {
        this.metrics = metrics;
    }

    /**
     * Validate all SLO thresholds
     */
    public ComplianceReport validateAllThresholds() {
        ComplianceReport report = new ComplianceReport();

        // Validate P95 latency for forecasting
        double p95ForecastingLatency = metrics.getForecastingLatencyP95();
        boolean p95ForecastingCompliant = p95ForecastingLatency <= MAX_P95_LATENCY_MS;
        report.addCheck("Time Series Forecasting P95 Latency", p95ForecastingLatency, MAX_P95_LATENCY_MS, p95ForecastingCompliant);

        // Validate P99 latency for forecasting
        double p99ForecastingLatency = metrics.getForecastingLatencyP99();
        boolean p99ForecastingCompliant = p99ForecastingLatency <= MAX_P99_LATENCY_MS;
        report.addCheck("Time Series Forecasting P99 Latency", p99ForecastingLatency, MAX_P99_LATENCY_MS, p99ForecastingCompliant);

        // Validate model training latency
        double modelTrainingLatency = metrics.getModelTrainingLatencyP95();
        boolean modelTrainingCompliant = modelTrainingLatency <= MAX_MODEL_TRAINING_LATENCY_MS;
        report.addCheck("Model Training P95 Latency", modelTrainingLatency, MAX_MODEL_TRAINING_LATENCY_MS, modelTrainingCompliant);

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
            case P95_FORECASTING_LATENCY -> {
                double value = metrics.getForecastingLatencyP95();
                boolean passed = value <= MAX_P95_LATENCY_MS;
                yield new ThresholdValidation(type, value, MAX_P95_LATENCY_MS, "ms", passed);
            }
            case P99_FORECASTING_LATENCY -> {
                double value = metrics.getForecastingLatencyP99();
                boolean passed = value <= MAX_P99_LATENCY_MS;
                yield new ThresholdValidation(type, value, MAX_P99_LATENCY_MS, "ms", passed);
            }
            case P95_MODEL_TRAINING_LATENCY -> {
                double value = metrics.getModelTrainingLatencyP95();
                boolean passed = value <= MAX_MODEL_TRAINING_LATENCY_MS;
                yield new ThresholdValidation(type, value, MAX_MODEL_TRAINING_LATENCY_MS, "ms", passed);
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
        P95_FORECASTING_LATENCY,
        P99_FORECASTING_LATENCY,
        P95_MODEL_TRAINING_LATENCY,
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
