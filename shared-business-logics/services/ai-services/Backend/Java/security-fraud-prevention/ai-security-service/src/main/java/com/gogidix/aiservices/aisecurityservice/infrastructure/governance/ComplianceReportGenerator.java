package com.gogidix.aiservices.aisecurityservice.infrastructure.governance;

import com.gogidix.aiservices.aisecurityservice.infrastructure.metrics.SecurityServiceMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Generates compliance reports for financial-grade governance.
 */
@Component
public class ComplianceReportGenerator {

    private static final Logger log = LoggerFactory.getLogger(ComplianceReportGenerator.class);
    private final SecurityServiceMetrics metrics;
    private final ThresholdValidator thresholdValidator;

    public ComplianceReportGenerator(SecurityServiceMetrics metrics, ThresholdValidator thresholdValidator) {
        this.metrics = metrics;
        this.thresholdValidator = thresholdValidator;
    }

    public GovernanceReport generateReport() {
        GovernanceReport report = new GovernanceReport();
        report.setGeneratedAt(Instant.now());
        report.setReportId(generateReportId());
        report.setSloCompliance(generateSloComplianceSection());
        report.setPerformanceMetrics(generatePerformanceSection());
        report.setGovernanceStatus(calculateGovernanceStatus());

        log.info("Generated governance report: {}", report.getReportId());
        return report;
    }

    private SloComplianceSection generateSloComplianceSection() {
        SloComplianceSection section = new SloComplianceSection();

        double encryptionLatency = metrics.getEncryptionLatencyP95();
        section.setP95EncryptionLatencyMs(encryptionLatency);
        section.setP95EncryptionCompliant(encryptionLatency <= 1000.0);

        double decryptionLatency = metrics.getDecryptionLatencyP95();
        section.setP95DecryptionLatencyMs(decryptionLatency);
        section.setP95DecryptionCompliant(decryptionLatency <= 500.0);

        double errorRate = metrics.getErrorRate();
        section.setErrorRate(errorRate * 100);
        section.setErrorRateCompliant(errorRate <= 0.001);

        section.setOverallCompliant(
            section.isP95EncryptionCompliant() &&
            section.isP95DecryptionCompliant() &&
            section.isErrorRateCompliant()
        );

        return section;
    }

    private PerformanceMetricsSection generatePerformanceSection() {
        PerformanceMetricsSection section = new PerformanceMetricsSection();

        section.setTotalEncryptions((long) metrics.getMeterRegistry()
                .get("security.encryption.total").counter().count());

        section.setSuccessfulEncryptions((long) metrics.getMeterRegistry()
                .get("security.encryption.success").counter().count());

        section.setFailedEncryptions((long) metrics.getMeterRegistry()
                .get("security.encryption.failure").counter().count());

        section.setKeysGenerated((long) metrics.getMeterRegistry()
                .get("security.key.generation").counter().count());

        return section;
    }

    private GovernanceStatus calculateGovernanceStatus() {
        ThresholdValidator.HealthStatus health = thresholdValidator.getHealthStatus();

        GovernanceStatus status = new GovernanceStatus();
        status.setHealthStatus(health.toString());

        if (health == ThresholdValidator.HealthStatus.HEALTHY) {
            status.setStatus("COMPLIANT");
            status.setSeverity("INFO");
            status.setMessage("All service level objectives are being met.");
        } else if (health == ThresholdValidator.HealthStatus.DEGRADED) {
            status.setStatus("WARNING");
            status.setSeverity("WARN");
            status.setMessage("Some service level objectives are not being met.");
        } else {
            status.setStatus("NON-COMPLIANT");
            status.setSeverity("CRITICAL");
            status.setMessage("Critical service level objectives are not being met.");
        }

        return status;
    }

    private String generateReportId() {
        return "GOV-" + DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")
                .format(LocalDateTime.now(ZoneId.of("UTC")));
    }

    // Nested classes
    public static class GovernanceReport {
        private String reportId;
        private Instant generatedAt;
        private SloComplianceSection sloCompliance;
        private PerformanceMetricsSection performanceMetrics;
        private GovernanceStatus governanceStatus;

        public String getReportId() { return reportId; }
        public void setReportId(String reportId) { this.reportId = reportId; }
        public Instant getGeneratedAt() { return generatedAt; }
        public void setGeneratedAt(Instant generatedAt) { this.generatedAt = generatedAt; }
        public SloComplianceSection getSloCompliance() { return sloCompliance; }
        public void setSloCompliance(SloComplianceSection sloCompliance) { this.sloCompliance = sloCompliance; }
        public PerformanceMetricsSection getPerformanceMetrics() { return performanceMetrics; }
        public void setPerformanceMetrics(PerformanceMetricsSection performanceMetrics) { this.performanceMetrics = performanceMetrics; }
        public GovernanceStatus getGovernanceStatus() { return governanceStatus; }
        public void setGovernanceStatus(GovernanceStatus governanceStatus) { this.governanceStatus = governanceStatus; }

        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("reportId", reportId);
            map.put("generatedAt", generatedAt.toString());
            return map;
        }
    }

    public static class SloComplianceSection {
        private double p95EncryptionLatencyMs;
        private boolean p95EncryptionCompliant;
        private double p95DecryptionLatencyMs;
        private boolean p95DecryptionCompliant;
        private double errorRate;
        private boolean errorRateCompliant;
        private boolean overallCompliant;

        public double getP95EncryptionLatencyMs() { return p95EncryptionLatencyMs; }
        public void setP95EncryptionLatencyMs(double p95EncryptionLatencyMs) { this.p95EncryptionLatencyMs = p95EncryptionLatencyMs; }
        public boolean isP95EncryptionCompliant() { return p95EncryptionCompliant; }
        public void setP95EncryptionCompliant(boolean p95EncryptionCompliant) { this.p95EncryptionCompliant = p95EncryptionCompliant; }
        public double getP95DecryptionLatencyMs() { return p95DecryptionLatencyMs; }
        public void setP95DecryptionLatencyMs(double p95DecryptionLatencyMs) { this.p95DecryptionLatencyMs = p95DecryptionLatencyMs; }
        public boolean isP95DecryptionCompliant() { return p95DecryptionCompliant; }
        public void setP95DecryptionCompliant(boolean p95DecryptionCompliant) { this.p95DecryptionCompliant = p95DecryptionCompliant; }
        public double getErrorRate() { return errorRate; }
        public void setErrorRate(double errorRate) { this.errorRate = errorRate; }
        public boolean isErrorRateCompliant() { return errorRateCompliant; }
        public void setErrorRateCompliant(boolean errorRateCompliant) { this.errorRateCompliant = errorRateCompliant; }
        public boolean isOverallCompliant() { return overallCompliant; }
        public void setOverallCompliant(boolean overallCompliant) { this.overallCompliant = overallCompliant; }
    }

    public static class PerformanceMetricsSection {
        private long totalEncryptions;
        private long successfulEncryptions;
        private long failedEncryptions;
        private long keysGenerated;

        public long getTotalEncryptions() { return totalEncryptions; }
        public void setTotalEncryptions(long totalEncryptions) { this.totalEncryptions = totalEncryptions; }
        public long getSuccessfulEncryptions() { return successfulEncryptions; }
        public void setSuccessfulEncryptions(long successfulEncryptions) { this.successfulEncryptions = successfulEncryptions; }
        public long getFailedEncryptions() { return failedEncryptions; }
        public void setFailedEncryptions(long failedEncryptions) { this.failedEncryptions = failedEncryptions; }
        public long getKeysGenerated() { return keysGenerated; }
        public void setKeysGenerated(long keysGenerated) { this.keysGenerated = keysGenerated; }
    }

    public static class GovernanceStatus {
        private String status;
        private String healthStatus;
        private String severity;
        private String message;

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getHealthStatus() { return healthStatus; }
        public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
        public String getSeverity() { return severity; }
        public void setSeverity(String severity) { this.severity = severity; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
