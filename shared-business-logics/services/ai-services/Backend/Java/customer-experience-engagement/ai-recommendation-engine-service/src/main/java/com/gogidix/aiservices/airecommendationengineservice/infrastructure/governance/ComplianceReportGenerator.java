package com.gogidix.aiservices.airecommendationengineservice.infrastructure.governance;

import com.gogidix.aiservices.airecommendationengineservice.infrastructure.metrics.RecommendationEngineMetrics;
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

    private final RecommendationEngineMetrics metrics;
    private final ThresholdValidator thresholdValidator;

    private static final double TARGET_P95_LATENCY_MS = 500.0;
    private static final double TARGET_ERROR_RATE = 0.01;
    private static final double TARGET_AVAILABILITY = 0.999;

    public ComplianceReportGenerator(
            RecommendationEngineMetrics metrics,
            ThresholdValidator thresholdValidator) {
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
        double p95Latency = metrics.getRecommendationLatencyP95();
        section.setP95LatencyMs(p95Latency);
        section.setP95LatencyCompliant(p95Latency <= TARGET_P95_LATENCY_MS);
        section.setP95LatencyVariance(p95Latency - TARGET_P95_LATENCY_MS);

        double p99Latency = metrics.getRecommendationLatencyP99();
        section.setP99LatencyMs(p99Latency);
        section.setP99LatencyCompliant(p99Latency <= 1000.0);

        double errorRate = metrics.getErrorRate();
        section.setErrorRate(errorRate * 100);
        section.setErrorRateCompliant(errorRate <= TARGET_ERROR_RATE);

        double availability = 1.0 - errorRate;
        section.setAvailability(availability * 100);
        section.setAvailabilityCompliant(availability >= TARGET_AVAILABILITY);

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
                .get("ai.recommendation.engine.total").counter().count());
        section.setSuccessfulRequests((long) metrics.getMeterRegistry()
                .get("ai.recommendation.engine.success").counter().count());
        section.setFailedRequests((long) metrics.getMeterRegistry()
                .get("ai.recommendation.engine.failure").counter().count());
        section.setRecommendationsGenerated((long) metrics.getMeterRegistry()
                .get("ai.recommendation.engine.generated").counter().count());
        section.setThroughputRps(0.0);
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
        private long recommendationsGenerated;
        private double throughputRps;

        public long getTotalRequests() { return totalRequests; }
        public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }
        public long getSuccessfulRequests() { return successfulRequests; }
        public void setSuccessfulRequests(long successfulRequests) { this.successfulRequests = successfulRequests; }
        public long getFailedRequests() { return failedRequests; }
        public void setFailedRequests(long failedRequests) { this.failedRequests = failedRequests; }
        public long getRecommendationsGenerated() { return recommendationsGenerated; }
        public void setRecommendationsGenerated(long recommendationsGenerated) { this.recommendationsGenerated = recommendationsGenerated; }
        public double getThroughputRps() { return throughputRps; }
        public void setThroughputRps(double throughputRps) { this.throughputRps = throughputRps; }
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
