package com.gogidix.aiservices.aianalyticsdashboard.infrastructure.governance;

import com.gogidix.aiservices.aianalyticsdashboard.infrastructure.metrics.DashboardMetrics;
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

    private final DashboardMetrics metrics;
    private final ThresholdValidator thresholdValidator;

    // SLO Targets for Analytics Dashboard Service
    private static final double TARGET_P95_LATENCY_MS = 500.0;
    private static final double TARGET_P99_LATENCY_MS = 1000.0;
    private static final double TARGET_QUERY_LATENCY_MS = 300.0;
    private static final double TARGET_ERROR_RATE = 0.01;  // 1%
    private static final double TARGET_AVAILABILITY = 0.999;  // 99.9%

    public ComplianceReportGenerator(
            DashboardMetrics metrics,
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

        // P95 Creation Latency
        double p95CreationLatency = metrics.getDashboardCreationLatencyP95();
        section.setP95CreationLatencyMs(p95CreationLatency);
        section.setP95CreationLatencyCompliant(p95CreationLatency <= TARGET_P95_LATENCY_MS);
        section.setP95CreationLatencyVariance(p95CreationLatency - TARGET_P95_LATENCY_MS);

        // P99 Creation Latency
        double p99CreationLatency = metrics.getDashboardCreationLatencyP99();
        section.setP99CreationLatencyMs(p99CreationLatency);
        section.setP99CreationLatencyCompliant(p99CreationLatency <= TARGET_P99_LATENCY_MS);

        // P95 Query Latency
        double queryLatency = metrics.getDashboardQueryLatencyP95();
        section.setP95QueryLatencyMs(queryLatency);
        section.setP95QueryLatencyCompliant(queryLatency <= TARGET_QUERY_LATENCY_MS);
        section.setP95QueryLatencyVariance(queryLatency - TARGET_QUERY_LATENCY_MS);

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
            section.isP95CreationLatencyCompliant() &&
            section.isP99CreationLatencyCompliant() &&
            section.isP95QueryLatencyCompliant() &&
            section.isErrorRateCompliant() &&
            section.isAvailabilityCompliant()
        );

        return section;
    }

    private PerformanceMetricsSection generatePerformanceSection() {
        PerformanceMetricsSection section = new PerformanceMetricsSection();

        section.setDashboardsCreated((long) metrics.getMeterRegistry()
                .get("dashboard.created").counter().count());

        section.setDashboardsUpdated((long) metrics.getMeterRegistry()
                .get("dashboard.updated").counter().count());

        section.setDashboardsViewed((long) metrics.getMeterRegistry()
                .get("dashboard.view").counter().count());

        section.setWidgetsAdded((long) metrics.getMeterRegistry()
                .get("widget.added").counter().count());

        section.setWidgetsRemoved((long) metrics.getMeterRegistry()
                .get("widget.removed").counter().count());

        section.setMetricQueries((long) metrics.getMeterRegistry()
                .get("metric.query.total").counter().count());

        section.setSuccessfulQueries((long) metrics.getMeterRegistry()
                .get("metric.query.success").counter().count());

        section.setFailedQueries((long) metrics.getMeterRegistry()
                .get("metric.query.failure").counter().count());

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
            map.put("p95CreationLatencyMs", sloCompliance.getP95CreationLatencyMs());
            map.put("p95CreationLatencyCompliant", sloCompliance.isP95CreationLatencyCompliant());
            map.put("p95QueryLatencyMs", sloCompliance.getP95QueryLatencyMs());
            map.put("p95QueryLatencyCompliant", sloCompliance.isP95QueryLatencyCompliant());
            map.put("errorRate", sloCompliance.getErrorRate());
            map.put("errorRateCompliant", sloCompliance.isErrorRateCompliant());
            map.put("availability", sloCompliance.getAvailability());
            map.put("overallCompliant", sloCompliance.isOverallCompliant());
            return map;
        }

        private Map<String, Object> performanceMetricsToMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("dashboardsCreated", performanceMetrics.getDashboardsCreated());
            map.put("dashboardsUpdated", performanceMetrics.getDashboardsUpdated());
            map.put("dashboardsViewed", performanceMetrics.getDashboardsViewed());
            map.put("widgetsAdded", performanceMetrics.getWidgetsAdded());
            map.put("widgetsRemoved", performanceMetrics.getWidgetsRemoved());
            map.put("metricQueries", performanceMetrics.getMetricQueries());
            map.put("successfulQueries", performanceMetrics.getSuccessfulQueries());
            map.put("failedQueries", performanceMetrics.getFailedQueries());
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
        private double p95CreationLatencyMs;
        private boolean p95CreationLatencyCompliant;
        private double p95CreationLatencyVariance;
        private double p99CreationLatencyMs;
        private boolean p99CreationLatencyCompliant;
        private double p95QueryLatencyMs;
        private boolean p95QueryLatencyCompliant;
        private double p95QueryLatencyVariance;
        private double errorRate;
        private boolean errorRateCompliant;
        private double availability;
        private boolean availabilityCompliant;
        private boolean overallCompliant;

        // Getters and Setters
        public double getP95CreationLatencyMs() { return p95CreationLatencyMs; }
        public void setP95CreationLatencyMs(double p95CreationLatencyMs) { this.p95CreationLatencyMs = p95CreationLatencyMs; }
        public boolean isP95CreationLatencyCompliant() { return p95CreationLatencyCompliant; }
        public void setP95CreationLatencyCompliant(boolean p95CreationLatencyCompliant) { this.p95CreationLatencyCompliant = p95CreationLatencyCompliant; }
        public double getP95CreationLatencyVariance() { return p95CreationLatencyVariance; }
        public void setP95CreationLatencyVariance(double p95CreationLatencyVariance) { this.p95CreationLatencyVariance = p95CreationLatencyVariance; }
        public double getP99CreationLatencyMs() { return p99CreationLatencyMs; }
        public void setP99CreationLatencyMs(double p99CreationLatencyMs) { this.p99CreationLatencyMs = p99CreationLatencyMs; }
        public boolean isP99CreationLatencyCompliant() { return p99CreationLatencyCompliant; }
        public void setP99CreationLatencyCompliant(boolean p99CreationLatencyCompliant) { this.p99CreationLatencyCompliant = p99CreationLatencyCompliant; }
        public double getP95QueryLatencyMs() { return p95QueryLatencyMs; }
        public void setP95QueryLatencyMs(double p95QueryLatencyMs) { this.p95QueryLatencyMs = p95QueryLatencyMs; }
        public boolean isP95QueryLatencyCompliant() { return p95QueryLatencyCompliant; }
        public void setP95QueryLatencyCompliant(boolean p95QueryLatencyCompliant) { this.p95QueryLatencyCompliant = p95QueryLatencyCompliant; }
        public double getP95QueryLatencyVariance() { return p95QueryLatencyVariance; }
        public void setP95QueryLatencyVariance(double p95QueryLatencyVariance) { this.p95QueryLatencyVariance = p95QueryLatencyVariance; }
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
        private long dashboardsCreated;
        private long dashboardsUpdated;
        private long dashboardsViewed;
        private long widgetsAdded;
        private long widgetsRemoved;
        private long metricQueries;
        private long successfulQueries;
        private long failedQueries;
        private double throughputRps;

        // Getters and Setters
        public long getDashboardsCreated() { return dashboardsCreated; }
        public void setDashboardsCreated(long dashboardsCreated) { this.dashboardsCreated = dashboardsCreated; }
        public long getDashboardsUpdated() { return dashboardsUpdated; }
        public void setDashboardsUpdated(long dashboardsUpdated) { this.dashboardsUpdated = dashboardsUpdated; }
        public long getDashboardsViewed() { return dashboardsViewed; }
        public void setDashboardsViewed(long dashboardsViewed) { this.dashboardsViewed = dashboardsViewed; }
        public long getWidgetsAdded() { return widgetsAdded; }
        public void setWidgetsAdded(long widgetsAdded) { this.widgetsAdded = widgetsAdded; }
        public long getWidgetsRemoved() { return widgetsRemoved; }
        public void setWidgetsRemoved(long widgetsRemoved) { this.widgetsRemoved = widgetsRemoved; }
        public long getMetricQueries() { return metricQueries; }
        public void setMetricQueries(long metricQueries) { this.metricQueries = metricQueries; }
        public long getSuccessfulQueries() { return successfulQueries; }
        public void setSuccessfulQueries(long successfulQueries) { this.successfulQueries = successfulQueries; }
        public long getFailedQueries() { return failedQueries; }
        public void setFailedQueries(long failedQueries) { this.failedQueries = failedQueries; }
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
