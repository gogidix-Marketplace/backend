package com.gogidix.aiservices.voicerecognitionservice.infrastructure.governance;

import com.gogidix.aiservices.voicerecognitionservice.infrastructure.metrics.VoiceRecognitionMetrics;
import org.springframework.stereotype.Component;

@Component
public class ThresholdValidator {
    private final VoiceRecognitionMetrics metrics;
    private static final double MAX_P95_MS = 500.0;
    private static final double MAX_P99_MS = 1000.0;
    private static final double MAX_RECOGNITION_MS = 300.0;
    private static final double MAX_ERROR_RATE = 0.01;

    public ThresholdValidator(VoiceRecognitionMetrics metrics) { this.metrics = metrics; }

    public ComplianceReport validateAllThresholds() {
        ComplianceReport report = new ComplianceReport();
        report.addCheck("P95 Latency", metrics.getRecognitionLatencyP95(), MAX_P95_MS, metrics.getRecognitionLatencyP95() <= MAX_P95_MS);
        report.addCheck("P99 Latency", metrics.getRecognitionLatencyP99(), MAX_P99_MS, metrics.getRecognitionLatencyP99() <= MAX_P99_MS);
        report.addCheck("Recognition Latency", metrics.getRecognitionLatencyP95(), MAX_RECOGNITION_MS, metrics.getRecognitionLatencyP95() <= MAX_RECOGNITION_MS);
        double er = metrics.getErrorRate();
        report.addCheck("Error Rate", er * 100, MAX_ERROR_RATE * 100, er <= MAX_ERROR_RATE, "%");
        report.setCompliant(report.getCheckCount() == report.getPassedCheckCount());
        return report;
    }

    public ThresholdValidation validateThreshold(ThresholdType type) {
        return switch (type) {
            case P95_LATENCY -> { double v = metrics.getRecognitionLatencyP95(); yield new ThresholdValidation(type, v, MAX_P95_MS, "ms", v <= MAX_P95_MS); }
            case P99_LATENCY -> { double v = metrics.getRecognitionLatencyP99(); yield new ThresholdValidation(type, v, MAX_P99_MS, "ms", v <= MAX_P99_MS); }
            case RECOGNITION_LATENCY -> { double v = metrics.getRecognitionLatencyP95(); yield new ThresholdValidation(type, v, MAX_RECOGNITION_MS, "ms", v <= MAX_RECOGNITION_MS); }
            case ERROR_RATE -> { double v = metrics.getErrorRate() * 100; yield new ThresholdValidation(type, v, MAX_ERROR_RATE * 100, "%", v <= MAX_ERROR_RATE * 100); }
        };
    }

    public boolean isDegraded() { return !validateAllThresholds().isCompliant(); }
    public HealthStatus getHealthStatus() {
        var r = validateAllThresholds();
        if (r.isCompliant()) return HealthStatus.HEALTHY;
        if (r.getPassedCheckCount() >= r.getCheckCount() * 0.75) return HealthStatus.DEGRADED;
        return HealthStatus.UNHEALTHY;
    }

    public enum ThresholdType { P95_LATENCY, P99_LATENCY, RECOGNITION_LATENCY, ERROR_RATE }
    public enum HealthStatus { HEALTHY, DEGRADED, UNHEALTHY }

    public static class ThresholdValidation {
        private final ThresholdType type; private final double actualValue; private final double threshold; private final String unit; private final boolean passed;
        public ThresholdValidation(ThresholdType t, double a, double th, String u, boolean p) { type=t; actualValue=a; threshold=th; unit=u; passed=p; }
        public ThresholdType getType() { return type; } public double getActualValue() { return actualValue; } public double getThreshold() { return threshold; } public String getUnit() { return unit; } public boolean isPassed() { return passed; }
    }

    public static class ComplianceReport {
        private final java.util.List<CheckResult> checks = new java.util.ArrayList<>(); private boolean compliant;
        public void addCheck(String n, double a, double t, boolean p) { checks.add(new CheckResult(n, a, t, p, "")); }
        public void addCheck(String n, double a, double t, boolean p, String u) { checks.add(new CheckResult(n, a, t, p, u)); }
        public boolean isCompliant() { return compliant; } public void setCompliant(boolean c) { compliant = c; }
        public java.util.List<CheckResult> getChecks() { return checks; } public int getCheckCount() { return checks.size(); }
        public long getPassedCheckCount() { return checks.stream().filter(CheckResult::passed).count(); }
        public long getFailedCheckCount() { return checks.stream().filter(c -> !c.passed()).count(); }
        public record CheckResult(String name, double actualValue, double threshold, boolean passed, String unit) {
            public double getVariance() { return actualValue - threshold; }
            public double getVariancePercent() { return threshold > 0 ? ((actualValue - threshold) / threshold) * 100 : 0; }
        }
    }
}
