package com.gogidix.aiservices.aisecurityanalysisservice.infrastructure.governance;

import com.gogidix.aiservices.aisecurityanalysisservice.infrastructure.metrics.SecurityAnalysisMetrics;
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
 * Produces structured reports for audit and operational review.
 */
@Component
public class ComplianceReportGenerator {

    private static final Logger log = LoggerFactory.getLogger(ComplianceReportGenerator.class);

    private final SecurityAnalysisMetrics metrics;
    private final ThresholdValidator thresholdValidator;

    // SLO Targets for security analysis
    private static final double TARGET_P95_LATENCY_MS = 5000.0;
    private static final double TARGET_P99_LATENCY_MS = 10000.0;
    private static final double TARGET_ERROR_RATE = 0.02;
    private static final double TARGET_SUCCESS_RATE = 0.98;

    public ComplianceReportGenerator(
            SecurityAnalysisMetrics metrics,
            ThresholdValidator thresholdValidator) {
        this.metrics = metrics;
        this.thresholdValidator = thresholdValidator;
    }

    /**
     * Generate a comprehensive compliance report
     */
    public GovernanceReport generateReport() {
        GovernanceReport report = new GovernanceReport();
        report.setGeneratedAt(Instant.now());
        report.setReportId(generateReportId());

        report.setSloCompliance(generateSloComplianceSection());
        report.setPerformanceMetrics(generatePerformanceSection());
        report.setSecurityMetrics(generateSecuritySection());
        report.setGovernanceStatus(calculateGovernanceStatus());

        log.info("Generated governance report: {}", report.getReportId());
        return report;
    }

    private SloComplianceSection generateSloComplianceSection() {
        SloComplianceSection section = new SloComplianceSection();

        double p95Latency = metrics.getScanLatencyP95();
        section.setP95LatencyMs(p95Latency);
        section.setP95LatencyCompliant(p95Latency <= TARGET_P95_LATENCY_MS);

        double p99Latency = metrics.getScanLatencyP99();
        section.setP99LatencyMs(p99Latency);
        section.setP99LatencyCompliant(p99Latency <= TARGET_P99_LATENCY_MS);

        double errorRate = metrics.getErrorRate();
        section.setErrorRate(errorRate * 100);
        section.setErrorRateCompliant(errorRate <= TARGET_ERROR_RATE);

        double successRate = metrics.getSuccessRate();
        section.setSuccessRate(successRate * 100);
        section.setSuccessRateCompliant(successRate >= TARGET_SUCCESS_RATE);

        section.setOverallCompliant(
            section.isP95LatencyCompliant() &&
            section.isP99LatencyCompliant() &&
            section.isErrorRateCompliant() &&
            section.isSuccessRateCompliant()
        );

        return section;
    }

    private PerformanceMetricsSection generatePerformanceSection() {
        PerformanceMetricsSection section = new PerformanceMetricsSection();

        section.setTotalScans((long) metrics.getMeterRegistry()
                .get("security.scan.total").counter().count());

        section.setCompletedScans((long) metrics.getMeterRegistry()
                .get("security.scan.completed").counter().count());

        section.setFailedScans((long) metrics.getMeterRegistry()
                .get("security.scan.failed").counter().count());

        section.setVulnerabilitiesFound((long) metrics.getMeterRegistry()
                .get("security.vulnerability.found").counter().count());

        section.setReportsGenerated((long) metrics.getMeterRegistry()
                .get("security.report.generated").counter().count());

        return section;
    }

    private SecurityMetricsSection generateSecuritySection() {
        SecurityMetricsSection section = new SecurityMetricsSection();

        section.setCriticalVulnerabilities((long) metrics.getMeterRegistry()
                .get("security.vulnerability.critical").counter().count());

        section.setHighVulnerabilities((long) metrics.getMeterRegistry()
                .get("security.vulnerability.high").counter().count());

        section.setMediumVulnerabilities((long) metrics.getMeterRegistry()
                .get("security.vulnerability.medium").counter().count());

        section.setLowVulnerabilities((long) metrics.getMeterRegistry()
                .get("security.vulnerability.low").counter().count());

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

    // Nested classes for report structure
    public static class GovernanceReport {
        private String reportId;
        private Instant generatedAt;
        private SloComplianceSection sloCompliance;
        private PerformanceMetricsSection performanceMetrics;
        private SecurityMetricsSection securityMetrics;
        private GovernanceStatus governanceStatus;

        public String getReportId() { return reportId; }
        public void setReportId(String reportId) { this.reportId = reportId; }
        public Instant getGeneratedAt() { return generatedAt; }
        public void setGeneratedAt(Instant generatedAt) { this.generatedAt = generatedAt; }
        public SloComplianceSection getSloCompliance() { return sloCompliance; }
        public void setSloCompliance(SloComplianceSection sloCompliance) { this.sloCompliance = sloCompliance; }
        public PerformanceMetricsSection getPerformanceMetrics() { return performanceMetrics; }
        public void setPerformanceMetrics(PerformanceMetricsSection performanceMetrics) { this.performanceMetrics = performanceMetrics; }
        public SecurityMetricsSection getSecurityMetrics() { return securityMetrics; }
        public void setSecurityMetrics(SecurityMetricsSection securityMetrics) { this.securityMetrics = securityMetrics; }
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
        private double p95LatencyMs;
        private boolean p95LatencyCompliant;
        private double p99LatencyMs;
        private boolean p99LatencyCompliant;
        private double errorRate;
        private boolean errorRateCompliant;
        private double successRate;
        private boolean successRateCompliant;
        private boolean overallCompliant;

        public double getP95LatencyMs() { return p95LatencyMs; }
        public void setP95LatencyMs(double p95LatencyMs) { this.p95LatencyMs = p95LatencyMs; }
        public boolean isP95LatencyCompliant() { return p95LatencyCompliant; }
        public void setP95LatencyCompliant(boolean p95LatencyCompliant) { this.p95LatencyCompliant = p95LatencyCompliant; }
        public double getP99LatencyMs() { return p99LatencyMs; }
        public void setP99LatencyMs(double p99LatencyMs) { this.p99LatencyMs = p99LatencyMs; }
        public boolean isP99LatencyCompliant() { return p99LatencyCompliant; }
        public void setP99LatencyCompliant(boolean p99LatencyCompliant) { this.p99LatencyCompliant = p99LatencyCompliant; }
        public double getErrorRate() { return errorRate; }
        public void setErrorRate(double errorRate) { this.errorRate = errorRate; }
        public boolean isErrorRateCompliant() { return errorRateCompliant; }
        public void setErrorRateCompliant(boolean errorRateCompliant) { this.errorRateCompliant = errorRateCompliant; }
        public double getSuccessRate() { return successRate; }
        public void setSuccessRate(double successRate) { this.successRate = successRate; }
        public boolean isSuccessRateCompliant() { return successRateCompliant; }
        public void setSuccessRateCompliant(boolean successRateCompliant) { this.successRateCompliant = successRateCompliant; }
        public boolean isOverallCompliant() { return overallCompliant; }
        public void setOverallCompliant(boolean overallCompliant) { this.overallCompliant = overallCompliant; }
    }

    public static class PerformanceMetricsSection {
        private long totalScans;
        private long completedScans;
        private long failedScans;
        private long vulnerabilitiesFound;
        private long reportsGenerated;

        public long getTotalScans() { return totalScans; }
        public void setTotalScans(long totalScans) { this.totalScans = totalScans; }
        public long getCompletedScans() { return completedScans; }
        public void setCompletedScans(long completedScans) { this.completedScans = completedScans; }
        public long getFailedScans() { return failedScans; }
        public void setFailedScans(long failedScans) { this.failedScans = failedScans; }
        public long getVulnerabilitiesFound() { return vulnerabilitiesFound; }
        public void setVulnerabilitiesFound(long vulnerabilitiesFound) { this.vulnerabilitiesFound = vulnerabilitiesFound; }
        public long getReportsGenerated() { return reportsGenerated; }
        public void setReportsGenerated(long reportsGenerated) { this.reportsGenerated = reportsGenerated; }
    }

    public static class SecurityMetricsSection {
        private long criticalVulnerabilities;
        private long highVulnerabilities;
        private long mediumVulnerabilities;
        private long lowVulnerabilities;

        public long getCriticalVulnerabilities() { return criticalVulnerabilities; }
        public void setCriticalVulnerabilities(long criticalVulnerabilities) { this.criticalVulnerabilities = criticalVulnerabilities; }
        public long getHighVulnerabilities() { return highVulnerabilities; }
        public void setHighVulnerabilities(long highVulnerabilities) { this.highVulnerabilities = highVulnerabilities; }
        public long getMediumVulnerabilities() { return mediumVulnerabilities; }
        public void setMediumVulnerabilities(long mediumVulnerabilities) { this.mediumVulnerabilities = mediumVulnerabilities; }
        public long getLowVulnerabilities() { return lowVulnerabilities; }
        public void setLowVulnerabilities(long lowVulnerabilities) { this.lowVulnerabilities = lowVulnerabilities; }
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
