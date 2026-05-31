package com.gogidix.aiservices.aisecurityservice.infrastructure.governance;

import com.gogidix.aiservices.aisecurityservice.infrastructure.metrics.SecurityServiceMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Governance validator for enforcing operational thresholds.
 */
@Component
public class ThresholdValidator {

    private static final Logger log = LoggerFactory.getLogger(ThresholdValidator.class);
    private final SecurityServiceMetrics metrics;

    private static final double MAX_P95_ENCRYPTION_MS = 1000.0;
    private static final double MAX_P95_DECRYPTION_MS = 500.0;
    private static final double MAX_ERROR_RATE = 0.001;  // 0.1% for crypto

    public ThresholdValidator(SecurityServiceMetrics metrics) {
        this.metrics = metrics;
    }

    public ComplianceReport validateAllThresholds() {
        ComplianceReport report = new ComplianceReport();

        double encryptionLatency = metrics.getEncryptionLatencyP95();
        boolean encryptionCompliant = encryptionLatency <= MAX_P95_ENCRYPTION_MS;
        report.addCheck("P95 Encryption Latency", encryptionLatency, MAX_P95_ENCRYPTION_MS, encryptionCompliant);

        double decryptionLatency = metrics.getDecryptionLatencyP95();
        boolean decryptionCompliant = decryptionLatency <= MAX_P95_DECRYPTION_MS;
        report.addCheck("P95 Decryption Latency", decryptionLatency, MAX_P95_DECRYPTION_MS, decryptionCompliant);

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
