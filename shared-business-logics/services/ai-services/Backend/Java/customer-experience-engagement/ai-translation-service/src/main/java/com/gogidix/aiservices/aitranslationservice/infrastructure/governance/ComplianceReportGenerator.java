package com.gogidix.aiservices.aitranslationservice.infrastructure.governance;

import com.gogidix.aiservices.aitranslationservice.infrastructure.metrics.TranslationMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class ComplianceReportGenerator {

    private static final Logger log = LoggerFactory.getLogger(ComplianceReportGenerator.class);
    private final TranslationMetrics metrics;
    private final ThresholdValidator thresholdValidator;

    private static final double TARGET_P95_LATENCY_MS = 500.0;
    private static final double TARGET_ERROR_RATE = 0.01;
    private static final double TARGET_AVAILABILITY = 0.999;

    public ComplianceReportGenerator(TranslationMetrics metrics, ThresholdValidator thresholdValidator) {
        this.metrics = metrics;
        this.thresholdValidator = thresholdValidator;
    }

    public GovernanceReport generateReport() {
        GovernanceReport report = new GovernanceReport();
        report.setGeneratedAt(Instant.now());
        report.setReportId("GOV-" + DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")
                .format(LocalDateTime.now(ZoneId.of("UTC"))));
        report.setSloCompliance(generateSloComplianceSection());
        report.setPerformanceMetrics(generatePerformanceSection());
        report.setGovernanceStatus(calculateGovernanceStatus());
        return report;
    }

    private SloComplianceSection generateSloComplianceSection() {
        SloComplianceSection section = new SloComplianceSection();
        double p95 = metrics.getTranslationLatencyP95();
        section.setP95LatencyMs(p95);
        section.setP95LatencyCompliant(p95 <= TARGET_P95_LATENCY_MS);
        section.setP99LatencyMs(metrics.getTranslationLatencyP99());
        section.setP99LatencyCompliant(metrics.getTranslationLatencyP99() <= 1000.0);
        double errorRate = metrics.getErrorRate();
        section.setErrorRate(errorRate * 100);
        section.setErrorRateCompliant(errorRate <= TARGET_ERROR_RATE);
        double availability = 1.0 - errorRate;
        section.setAvailability(availability * 100);
        section.setAvailabilityCompliant(availability >= TARGET_AVAILABILITY);
        section.setOverallCompliant(section.isP95LatencyCompliant() && section.isP99LatencyCompliant()
                && section.isErrorRateCompliant() && section.isAvailabilityCompliant());
        return section;
    }

    private PerformanceMetricsSection generatePerformanceSection() {
        PerformanceMetricsSection section = new PerformanceMetricsSection();
        section.setTotalRequests((long) metrics.getMeterRegistry().get("ai.translation.total").counter().count());
        section.setSuccessfulRequests((long) metrics.getMeterRegistry().get("ai.translation.success").counter().count());
        section.setFailedRequests((long) metrics.getMeterRegistry().get("ai.translation.failure").counter().count());
        section.setTranslationsPerformed(section.getTotalRequests());
        return section;
    }

    private GovernanceStatus calculateGovernanceStatus() {
        ThresholdValidator.HealthStatus health = thresholdValidator.getHealthStatus();
        GovernanceStatus status = new GovernanceStatus();
        status.setHealthStatus(health.toString());
        if (health == ThresholdValidator.HealthStatus.HEALTHY) {
            status.setStatus("COMPLIANT"); status.setSeverity("INFO"); status.setMessage("All SLOs met.");
        } else if (health == ThresholdValidator.HealthStatus.DEGRADED) {
            status.setStatus("WARNING"); status.setSeverity("WARN"); status.setMessage("Some SLOs not met.");
        } else {
            status.setStatus("NON-COMPLIANT"); status.setSeverity("CRITICAL"); status.setMessage("Critical SLOs not met.");
        }
        return status;
    }

    public static class GovernanceReport {
        private String reportId; private Instant generatedAt;
        private SloComplianceSection sloCompliance; private PerformanceMetricsSection performanceMetrics; private GovernanceStatus governanceStatus;
        public String getReportId() { return reportId; } public void setReportId(String r) { this.reportId = r; }
        public Instant getGeneratedAt() { return generatedAt; } public void setGeneratedAt(Instant g) { this.generatedAt = g; }
        public SloComplianceSection getSloCompliance() { return sloCompliance; } public void setSloCompliance(SloComplianceSection s) { this.sloCompliance = s; }
        public PerformanceMetricsSection getPerformanceMetrics() { return performanceMetrics; } public void setPerformanceMetrics(PerformanceMetricsSection p) { this.performanceMetrics = p; }
        public GovernanceStatus getGovernanceStatus() { return governanceStatus; } public void setGovernanceStatus(GovernanceStatus g) { this.governanceStatus = g; }
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("reportId", reportId); map.put("generatedAt", generatedAt.toString());
            return map;
        }
    }

    public static class SloComplianceSection {
        private double p95LatencyMs; private boolean p95LatencyCompliant;
        private double p99LatencyMs; private boolean p99LatencyCompliant;
        private double errorRate; private boolean errorRateCompliant;
        private double availability; private boolean availabilityCompliant; private boolean overallCompliant;
        public double getP95LatencyMs() { return p95LatencyMs; } public void setP95LatencyMs(double v) { this.p95LatencyMs = v; }
        public boolean isP95LatencyCompliant() { return p95LatencyCompliant; } public void setP95LatencyCompliant(boolean v) { this.p95LatencyCompliant = v; }
        public double getP99LatencyMs() { return p99LatencyMs; } public void setP99LatencyMs(double v) { this.p99LatencyMs = v; }
        public boolean isP99LatencyCompliant() { return p99LatencyCompliant; } public void setP99LatencyCompliant(boolean v) { this.p99LatencyCompliant = v; }
        public double getErrorRate() { return errorRate; } public void setErrorRate(double v) { this.errorRate = v; }
        public boolean isErrorRateCompliant() { return errorRateCompliant; } public void setErrorRateCompliant(boolean v) { this.errorRateCompliant = v; }
        public double getAvailability() { return availability; } public void setAvailability(double v) { this.availability = v; }
        public boolean isAvailabilityCompliant() { return availabilityCompliant; } public void setAvailabilityCompliant(boolean v) { this.availabilityCompliant = v; }
        public boolean isOverallCompliant() { return overallCompliant; } public void setOverallCompliant(boolean v) { this.overallCompliant = v; }
    }

    public static class PerformanceMetricsSection {
        private long totalRequests; private long successfulRequests; private long failedRequests; private long translationsPerformed;
        public long getTotalRequests() { return totalRequests; } public void setTotalRequests(long v) { this.totalRequests = v; }
        public long getSuccessfulRequests() { return successfulRequests; } public void setSuccessfulRequests(long v) { this.successfulRequests = v; }
        public long getFailedRequests() { return failedRequests; } public void setFailedRequests(long v) { this.failedRequests = v; }
        public long getTranslationsPerformed() { return translationsPerformed; } public void setTranslationsPerformed(long v) { this.translationsPerformed = v; }
    }

    public static class GovernanceStatus {
        private String status; private String healthStatus; private String severity; private String message;
        public String getStatus() { return status; } public void setStatus(String s) { this.status = s; }
        public String getHealthStatus() { return healthStatus; } public void setHealthStatus(String h) { this.healthStatus = h; }
        public String getSeverity() { return severity; } public void setSeverity(String s) { this.severity = s; }
        public String getMessage() { return message; } public void setMessage(String m) { this.message = m; }
    }
}
