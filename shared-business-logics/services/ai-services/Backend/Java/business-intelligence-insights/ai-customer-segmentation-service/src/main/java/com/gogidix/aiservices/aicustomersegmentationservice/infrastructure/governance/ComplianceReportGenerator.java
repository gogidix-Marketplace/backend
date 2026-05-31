package com.gogidix.aiservices.aicustomersegmentationservice.infrastructure.governance;

import com.gogidix.aiservices.aicustomersegmentationservice.infrastructure.metrics.CustomerSegmentationMetrics;
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

    private final CustomerSegmentationMetrics metrics;
    private final ThresholdValidator thresholdValidator;

    // SLO Targets
    private static final double TARGET_P95_LATENCY_MS = 500.0;
    private static final double TARGET_P99_LATENCY_MS = 1000.0;
    private static final double TARGET_ERROR_RATE = 0.01;  // 1%
    private static final double TARGET_AVAILABILITY = 0.999;  // 99.9%

    public ComplianceReportGenerator(
            CustomerSegmentationMetrics metrics,
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

        // SLO Compliance Section
        report.setSloCompliance(generateSloComplianceSection());

        // Performance Metrics Section
        report.setPerformanceMetrics(generatePerformanceSection());

        // Governance Status
        report.setGovernanceStatus(calculateGovernanceStatus());

        log.info("Generated governance report: {}", report.getReportId());

        return report;
    }

    private SloComplianceSection generateSloComplianceSection() {
        SloComplianceSection section = new SloComplianceSection();

        // P95 Latency
        double p95Latency = metrics.getSegmentationLatencyP95();
        section.setP95LatencyMs(p95Latency);
        section.setP95LatencyCompliant(p95Latency <= TARGET_P95_LATENCY_MS);
        section.setP95LatencyVariance(p95Latency - TARGET_P95_LATENCY_MS);

        // P99 Latency
        double p99Latency = metrics.getSegmentationLatencyP99();
        section.setP99LatencyMs(p99Latency);
        section.setP99LatencyCompliant(p99Latency <= TARGET_P99_LATENCY_MS);

        // Error Rate
        double errorRate = metrics.getErrorRate();
        section.setErrorRate(errorRate * 100);
        section.setErrorRateCompliant(errorRate <= TARGET_ERROR_RATE);

        // Availability
        double availability = 1.0 - errorRate;
        section.setAvailability(availability * 100);
        section.setAvailabilityCompliant(availability >= TARGET_AVAILABILITY);

        // Overall SLO Status
        section.setOverallCompliant(
            section.isP95LatencyCompliant() &&
            section.isP99LatencyCompliant() &&
            section.isErrorRateCompliant() &&
            section.isAvailabilityCompliant()
        );

        return section;
    }

    private PerformanceMetricsSection generatePerformanceSection() {
        PerformanceMetricsSection section = new PerformanceMetricsSection();

        section.setTotalSegmentations((long) metrics.getMeterRegistry()
                .get("segmentation.total").counter().count());

        section.setSuccessfulSegmentations((long) metrics.getMeterRegistry()
                .get("segmentation.success").counter().count());

        section.setFailedSegmentations((long) metrics.getMeterRegistry()
                .get("segmentation.failure").counter().count());

        section.setSegmentsCreated((long) metrics.getMeterRegistry()
                .get("segment.created").counter().count());

        section.setSegmentsUpdated((long) metrics.getMeterRegistry()
                .get("segment.updated").counter().count());

        section.setCustomersAnalyzed((long) metrics.getMeterRegistry()
                .get("customer.analyzed").counter().count());

        section.setThroughputRps(calculateThroughput());

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

    private double calculateThroughput() {
        // Approximate throughput based on total segmentations
        return 0.0; // Placeholder
    }

    private String generateReportId() {
        return "GOV-" + DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")
                .format(LocalDateTime.now(ZoneId.of("UTC")));
    }

    /**
     * Governance report data structure
     */
    public static class GovernanceReport {
        private String reportId;
        private Instant generatedAt;
        private SloComplianceSection sloCompliance;
        private PerformanceMetricsSection performanceMetrics;
        private GovernanceStatus governanceStatus;

        // Getters and Setters
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
            map.put("sloCompliance", sloComplianceToMap());
            map.put("performanceMetrics", performanceMetricsToMap());
            map.put("governanceStatus", governanceStatusToMap());
            return map;
        }

        private Map<String, Object> sloComplianceToMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("p95LatencyMs", sloCompliance.getP95LatencyMs());
            map.put("p95LatencyCompliant", sloCompliance.isP95LatencyCompliant());
            map.put("errorRate", sloCompliance.getErrorRate());
            map.put("errorRateCompliant", sloCompliance.isErrorRateCompliant());
            map.put("availability", sloCompliance.getAvailability());
            map.put("overallCompliant", sloCompliance.isOverallCompliant());
            return map;
        }

        private Map<String, Object> performanceMetricsToMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("totalSegmentations", performanceMetrics.getTotalSegmentations());
            map.put("successfulSegmentations", performanceMetrics.getSuccessfulSegmentations());
            map.put("failedSegmentations", performanceMetrics.getFailedSegmentations());
            map.put("segmentsCreated", performanceMetrics.getSegmentsCreated());
            map.put("customersAnalyzed", performanceMetrics.getCustomersAnalyzed());
            return map;
        }

        private Map<String, Object> governanceStatusToMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("status", governanceStatus.getStatus());
            map.put("healthStatus", governanceStatus.getHealthStatus());
            map.put("severity", governanceStatus.getSeverity());
            map.put("message", governanceStatus.getMessage());
            return map;
        }
    }

    public static class SloComplianceSection {
        private double p95LatencyMs;
        private boolean p95LatencyCompliant;
        private double p95LatencyVariance;
        private double p99LatencyMs;
        private boolean p99LatencyCompliant;
        private double errorRate;
        private boolean errorRateCompliant;
        private double availability;
        private boolean availabilityCompliant;
        private boolean overallCompliant;

        // Getters and Setters
        public double getP95LatencyMs() { return p95LatencyMs; }
        public void setP95LatencyMs(double p95LatencyMs) { this.p95LatencyMs = p95LatencyMs; }
        public boolean isP95LatencyCompliant() { return p95LatencyCompliant; }
        public void setP95LatencyCompliant(boolean p95LatencyCompliant) { this.p95LatencyCompliant = p95LatencyCompliant; }
        public double getP95LatencyVariance() { return p95LatencyVariance; }
        public void setP95LatencyVariance(double p95LatencyVariance) { this.p95LatencyVariance = p95LatencyVariance; }
        public double getP99LatencyMs() { return p99LatencyMs; }
        public void setP99LatencyMs(double p99LatencyMs) { this.p99LatencyMs = p99LatencyMs; }
        public boolean isP99LatencyCompliant() { return p99LatencyCompliant; }
        public void setP99LatencyCompliant(boolean p99LatencyCompliant) { this.p99LatencyCompliant = p99LatencyCompliant; }
        public double getErrorRate() { return errorRate; }
        public void setErrorRate(double errorRate) { this.errorRate = errorRate; }
        public boolean isErrorRateCompliant() { return errorRateCompliant; }
        public void setErrorRateCompliant(boolean errorRateCompliant) { this.errorRateCompliant = errorRateCompliant; }
        public double getAvailability() { return availability; }
        public void setAvailability(double availability) { this.availability = availability; }
        public boolean isAvailabilityCompliant() { return availabilityCompliant; }
        public void setAvailabilityCompliant(boolean availabilityCompliant) { this.availabilityCompliant = availabilityCompliant; }
        public boolean isOverallCompliant() { return overallCompliant; }
        public void setOverallCompliant(boolean overallCompliant) { this.overallCompliant = overallCompliant; }
    }

    public static class PerformanceMetricsSection {
        private long totalSegmentations;
        private long successfulSegmentations;
        private long failedSegmentations;
        private long segmentsCreated;
        private long segmentsUpdated;
        private long customersAnalyzed;
        private double throughputRps;

        // Getters and Setters
        public long getTotalSegmentations() { return totalSegmentations; }
        public void setTotalSegmentations(long totalSegmentations) { this.totalSegmentations = totalSegmentations; }
        public long getSuccessfulSegmentations() { return successfulSegmentations; }
        public void setSuccessfulSegmentations(long successfulSegmentations) { this.successfulSegmentations = successfulSegmentations; }
        public long getFailedSegmentations() { return failedSegmentations; }
        public void setFailedSegmentations(long failedSegmentations) { this.failedSegmentations = failedSegmentations; }
        public long getSegmentsCreated() { return segmentsCreated; }
        public void setSegmentsCreated(long segmentsCreated) { this.segmentsCreated = segmentsCreated; }
        public long getSegmentsUpdated() { return segmentsUpdated; }
        public void setSegmentsUpdated(long segmentsUpdated) { this.segmentsUpdated = segmentsUpdated; }
        public long getCustomersAnalyzed() { return customersAnalyzed; }
        public void setCustomersAnalyzed(long customersAnalyzed) { this.customersAnalyzed = customersAnalyzed; }
        public double getThroughputRps() { return throughputRps; }
        public void setThroughputRps(double throughputRps) { this.throughputRps = throughputRps; }
    }

    public static class GovernanceStatus {
        private String status;
        private String healthStatus;
        private String severity;
        private String message;

        // Getters and Setters
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
