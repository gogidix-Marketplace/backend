package com.gogidix.aiservices.anomalydetectionservice.infrastructure.governance;

import com.gogidix.aiservices.anomalydetectionservice.infrastructure.metrics.AnomalyDetectionMetrics;
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
    private final AnomalyDetectionMetrics metrics;
    private final ThresholdValidator thresholdValidator;

    public ComplianceReportGenerator(AnomalyDetectionMetrics metrics, ThresholdValidator thresholdValidator) {
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

        double detectionLatency = metrics.getDetectionLatencyP95();
        section.setP95DetectionLatencyMs(detectionLatency);
        section.setP95DetectionCompliant(detectionLatency <= 2000.0);

        double modelInferenceLatency = metrics.getModelInferenceLatencyP95();
        section.setModelInferenceLatencyMs(modelInferenceLatency);
        section.setModelInferenceCompliant(modelInferenceLatency <= 500.0);

        double errorRate = metrics.getErrorRate();
        section.setErrorRate(errorRate * 100);
        section.setErrorRateCompliant(errorRate <= 0.01);

        section.setOverallCompliant(
            section.isP95DetectionCompliant() &&
            section.isModelInferenceCompliant() &&
            section.isErrorRateCompliant()
        );

        return section;
    }

    private PerformanceMetricsSection generatePerformanceSection() {
        PerformanceMetricsSection section = new PerformanceMetricsSection();

        section.setTotalDetections((long) metrics.getMeterRegistry()
                .get("anomaly.detection.total").counter().count());

        section.setSuccessfulDetections((long) metrics.getMeterRegistry()
                .get("anomaly.detection.success").counter().count());

        section.setFailedDetections((long) metrics.getMeterRegistry()
                .get("anomaly.detection.failure").counter().count());

        section.setAnomaliesFound((long) metrics.getMeterRegistry()
                .get("anomaly.found").counter().count());

        section.setCriticalAnomalies((long) metrics.getMeterRegistry()
                .get("anomaly.critical").counter().count());

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
        private double p95DetectionLatencyMs;
        private boolean p95DetectionCompliant;
        private double modelInferenceLatencyMs;
        private boolean modelInferenceCompliant;
        private double errorRate;
        private boolean errorRateCompliant;
        private boolean overallCompliant;

        public double getP95DetectionLatencyMs() { return p95DetectionLatencyMs; }
        public void setP95DetectionLatencyMs(double p95DetectionLatencyMs) { this.p95DetectionLatencyMs = p95DetectionLatencyMs; }
        public boolean isP95DetectionCompliant() { return p95DetectionCompliant; }
        public void setP95DetectionCompliant(boolean p95DetectionCompliant) { this.p95DetectionCompliant = p95DetectionCompliant; }
        public double getModelInferenceLatencyMs() { return modelInferenceLatencyMs; }
        public void setModelInferenceLatencyMs(double modelInferenceLatencyMs) { this.modelInferenceLatencyMs = modelInferenceLatencyMs; }
        public boolean isModelInferenceCompliant() { return modelInferenceCompliant; }
        public void setModelInferenceCompliant(boolean modelInferenceCompliant) { this.modelInferenceCompliant = modelInferenceCompliant; }
        public double getErrorRate() { return errorRate; }
        public void setErrorRate(double errorRate) { this.errorRate = errorRate; }
        public boolean isErrorRateCompliant() { return errorRateCompliant; }
        public void setErrorRateCompliant(boolean errorRateCompliant) { this.errorRateCompliant = errorRateCompliant; }
        public boolean isOverallCompliant() { return overallCompliant; }
        public void setOverallCompliant(boolean overallCompliant) { this.overallCompliant = overallCompliant; }
    }

    public static class PerformanceMetricsSection {
        private long totalDetections;
        private long successfulDetections;
        private long failedDetections;
        private long anomaliesFound;
        private long criticalAnomalies;

        public long getTotalDetections() { return totalDetections; }
        public void setTotalDetections(long totalDetections) { this.totalDetections = totalDetections; }
        public long getSuccessfulDetections() { return successfulDetections; }
        public void setSuccessfulDetections(long successfulDetections) { this.successfulDetections = successfulDetections; }
        public long getFailedDetections() { return failedDetections; }
        public void setFailedDetections(long failedDetections) { this.failedDetections = failedDetections; }
        public long getAnomaliesFound() { return anomaliesFound; }
        public void setAnomaliesFound(long anomaliesFound) { this.anomaliesFound = anomaliesFound; }
        public long getCriticalAnomalies() { return criticalAnomalies; }
        public void setCriticalAnomalies(long criticalAnomalies) { this.criticalAnomalies = criticalAnomalies; }
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
