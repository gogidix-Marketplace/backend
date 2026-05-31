package com.gogidix.aiservices.nlpprocessingservice.infrastructure.governance;

import com.gogidix.aiservices.nlpprocessingservice.infrastructure.metrics.NlpProcessingMetrics;
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

    private final NlpProcessingMetrics metrics;
    private final ThresholdValidator thresholdValidator;

    // SLO Targets for Content Processing - NLP
    private static final double TARGET_P95_LATENCY_MS = 300.0;
    private static final double TARGET_P99_LATENCY_MS = 500.0;
    private static final double TARGET_ERROR_RATE = 0.02;  // 2%
    private static final double TARGET_AVAILABILITY = 0.998;  // 99.8%

    public ComplianceReportGenerator(
            NlpProcessingMetrics metrics,
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
        double p95Latency = metrics.getProcessingLatencyP95();
        section.setP95LatencyMs(p95Latency);
        section.setP95LatencyCompliant(p95Latency <= TARGET_P95_LATENCY_MS);
        section.setP95LatencyVariance(p95Latency - TARGET_P95_LATENCY_MS);

        // P99 Latency
        double p99Latency = metrics.getProcessingLatencyP99();
        section.setP99LatencyMs(p99Latency);
        section.setP99LatencyCompliant(p99Latency <= TARGET_P99_LATENCY_MS);

        // Error Rate
        double errorRate = metrics.getErrorRate();
        section.setErrorRate(errorRate * 100);
        section.setErrorRateCompliant(errorRate <= TARGET_ERROR_RATE);

        // Availability (assumed 100% - error rate for now)
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

        section.setTotalRequests((long) metrics.getMeterRegistry()
                .get("nlp.processing.total").counter().count());

        section.setSuccessfulRequests((long) metrics.getMeterRegistry()
                .get("nlp.processing.success").counter().count());

        section.setFailedRequests((long) metrics.getMeterRegistry()
                .get("nlp.processing.failure").counter().count());

        section.setTextsProcessed((long) metrics.getMeterRegistry()
                .get("nlp.processing.texts").counter().count());

        section.setTokensProcessed((long) metrics.getMeterRegistry()
                .get("nlp.processing.tokens").counter().count());

        section.setValidationFailures((long) metrics.getMeterRegistry()
                .get("nlp.processing.validation.failed").counter().count());

        // Throughput (requests per second - approximate from total)
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
        // Approximate throughput based on total requests
        // In production, this would calculate actual RPS over a time window
        return 0.0; // Placeholder
    }

    private String generateReportId() {
        return "GOV-NLP-" + DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")
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
            map.put("p99LatencyMs", sloCompliance.getP99LatencyMs());
            map.put("p99LatencyCompliant", sloCompliance.isP99LatencyCompliant());
            map.put("errorRate", sloCompliance.getErrorRate());
            map.put("errorRateCompliant", sloCompliance.isErrorRateCompliant());
            map.put("availability", sloCompliance.getAvailability());
            map.put("overallCompliant", sloCompliance.isOverallCompliant());
            return map;
        }

        private Map<String, Object> performanceMetricsToMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("totalRequests", performanceMetrics.getTotalRequests());
            map.put("successfulRequests", performanceMetrics.getSuccessfulRequests());
            map.put("failedRequests", performanceMetrics.getFailedRequests());
            map.put("textsProcessed", performanceMetrics.getTextsProcessed());
            map.put("tokensProcessed", performanceMetrics.getTokensProcessed());
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
        private long totalRequests;
        private long successfulRequests;
        private long failedRequests;
        private long textsProcessed;
        private long tokensProcessed;
        private long validationFailures;
        private double throughputRps;

        // Getters and Setters
        public long getTotalRequests() { return totalRequests; }
        public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }
        public long getSuccessfulRequests() { return successfulRequests; }
        public void setSuccessfulRequests(long successfulRequests) { this.successfulRequests = successfulRequests; }
        public long getFailedRequests() { return failedRequests; }
        public void setFailedRequests(long failedRequests) { this.failedRequests = failedRequests; }
        public long getTextsProcessed() { return textsProcessed; }
        public void setTextsProcessed(long textsProcessed) { this.textsProcessed = textsProcessed; }
        public long getTokensProcessed() { return tokensProcessed; }
        public void setTokensProcessed(long tokensProcessed) { this.tokensProcessed = tokensProcessed; }
        public long getValidationFailures() { return validationFailures; }
        public void setValidationFailures(long validationFailures) { this.validationFailures = validationFailures; }
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
