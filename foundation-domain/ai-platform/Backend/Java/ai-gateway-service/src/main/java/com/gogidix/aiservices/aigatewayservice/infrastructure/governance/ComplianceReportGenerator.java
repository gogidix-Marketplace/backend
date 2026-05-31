package com.gogidix.aiservices.aigatewayservice.infrastructure.governance;

import com.gogidix.aiservices.aigatewayservice.infrastructure.metrics.GatewayMetrics;
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

    private final GatewayMetrics metrics;
    private final ThresholdValidator thresholdValidator;

    // SLO Targets for AI Gateway Service
    private static final double TARGET_P95_LATENCY_MS = 500.0;
    private static final double TARGET_P99_LATENCY_MS = 1000.0;
    private static final double TARGET_ROUTING_LATENCY_MS = 100.0;
    private static final double TARGET_FILTER_LATENCY_MS = 50.0;
    private static final double TARGET_ERROR_RATE = 0.01;  // 1%
    private static final double TARGET_AVAILABILITY = 0.999;  // 99.9%

    public ComplianceReportGenerator(
            GatewayMetrics metrics,
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

        // P95 Gateway Latency
        double p95Latency = metrics.getGatewayLatencyP95();
        section.setP95GatewayLatencyMs(p95Latency);
        section.setP95GatewayLatencyCompliant(p95Latency <= TARGET_P95_LATENCY_MS);
        section.setP95GatewayLatencyVariance(p95Latency - TARGET_P95_LATENCY_MS);

        // P99 Gateway Latency
        double p99Latency = metrics.getGatewayLatencyP99();
        section.setP99GatewayLatencyMs(p99Latency);
        section.setP99GatewayLatencyCompliant(p99Latency <= TARGET_P99_LATENCY_MS);

        // P95 Routing Latency
        double routingLatency = metrics.getRoutingLatencyP95();
        section.setP95RoutingLatencyMs(routingLatency);
        section.setP95RoutingLatencyCompliant(routingLatency <= TARGET_ROUTING_LATENCY_MS);

        // P95 Filter Latency
        double filterLatency = metrics.getFilterLatencyP95();
        section.setP95FilterLatencyMs(filterLatency);
        section.setP95FilterLatencyCompliant(filterLatency <= TARGET_FILTER_LATENCY_MS);

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
            section.isP95GatewayLatencyCompliant() &&
            section.isP99GatewayLatencyCompliant() &&
            section.isP95RoutingLatencyCompliant() &&
            section.isP95FilterLatencyCompliant() &&
            section.isErrorRateCompliant() &&
            section.isAvailabilityCompliant()
        );

        return section;
    }

    private PerformanceMetricsSection generatePerformanceSection() {
        PerformanceMetricsSection section = new PerformanceMetricsSection();

        section.setTotalRequests((long) metrics.getMeterRegistry()
                .get("ai.gateway.requests.total").counter().count());

        section.setSuccessfulRequests((long) metrics.getMeterRegistry()
                .get("ai.gateway.requests.success").counter().count());

        section.setFailedRequests((long) metrics.getMeterRegistry()
                .get("ai.gateway.requests.failure").counter().count());

        section.setRoutesCreated((long) metrics.getMeterRegistry()
                .get("ai.gateway.routes.created").counter().count());

        section.setRoutesDeleted((long) metrics.getMeterRegistry()
                .get("ai.gateway.routes.deleted").counter().count());

        section.setCircuitBreakerTrips((long) metrics.getMeterRegistry()
                .get("ai.gateway.circuit.tripped").counter().count());

        section.setRateLimitExceeded((long) metrics.getMeterRegistry()
                .get("ai.gateway.ratelimit.exceeded").counter().count());

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
            map.put("p95GatewayLatencyMs", sloCompliance.getP95GatewayLatencyMs());
            map.put("p95GatewayLatencyCompliant", sloCompliance.isP95GatewayLatencyCompliant());
            map.put("p95RoutingLatencyMs", sloCompliance.getP95RoutingLatencyMs());
            map.put("p95RoutingLatencyCompliant", sloCompliance.isP95RoutingLatencyCompliant());
            map.put("p95FilterLatencyMs", sloCompliance.getP95FilterLatencyMs());
            map.put("p95FilterLatencyCompliant", sloCompliance.isP95FilterLatencyCompliant());
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
            map.put("routesCreated", performanceMetrics.getRoutesCreated());
            map.put("routesDeleted", performanceMetrics.getRoutesDeleted());
            map.put("circuitBreakerTrips", performanceMetrics.getCircuitBreakerTrips());
            map.put("rateLimitExceeded", performanceMetrics.getRateLimitExceeded());
            map.put("throughputRps", performanceMetrics.getThroughputRps());
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
        private double p95GatewayLatencyMs;
        private boolean p95GatewayLatencyCompliant;
        private double p95GatewayLatencyVariance;
        private double p99GatewayLatencyMs;
        private boolean p99GatewayLatencyCompliant;
        private double p95RoutingLatencyMs;
        private boolean p95RoutingLatencyCompliant;
        private double p95FilterLatencyMs;
        private boolean p95FilterLatencyCompliant;
        private double errorRate;
        private boolean errorRateCompliant;
        private double availability;
        private boolean availabilityCompliant;
        private boolean overallCompliant;

        // Getters and Setters
        public double getP95GatewayLatencyMs() { return p95GatewayLatencyMs; }
        public void setP95GatewayLatencyMs(double p95GatewayLatencyMs) { this.p95GatewayLatencyMs = p95GatewayLatencyMs; }
        public boolean isP95GatewayLatencyCompliant() { return p95GatewayLatencyCompliant; }
        public void setP95GatewayLatencyCompliant(boolean p95GatewayLatencyCompliant) { this.p95GatewayLatencyCompliant = p95GatewayLatencyCompliant; }
        public double getP95GatewayLatencyVariance() { return p95GatewayLatencyVariance; }
        public void setP95GatewayLatencyVariance(double p95GatewayLatencyVariance) { this.p95GatewayLatencyVariance = p95GatewayLatencyVariance; }
        public double getP99GatewayLatencyMs() { return p99GatewayLatencyMs; }
        public void setP99GatewayLatencyMs(double p99GatewayLatencyMs) { this.p99GatewayLatencyMs = p99GatewayLatencyMs; }
        public boolean isP99GatewayLatencyCompliant() { return p99GatewayLatencyCompliant; }
        public void setP99GatewayLatencyCompliant(boolean p99GatewayLatencyCompliant) { this.p99GatewayLatencyCompliant = p99GatewayLatencyCompliant; }
        public double getP95RoutingLatencyMs() { return p95RoutingLatencyMs; }
        public void setP95RoutingLatencyMs(double p95RoutingLatencyMs) { this.p95RoutingLatencyMs = p95RoutingLatencyMs; }
        public boolean isP95RoutingLatencyCompliant() { return p95RoutingLatencyCompliant; }
        public void setP95RoutingLatencyCompliant(boolean p95RoutingLatencyCompliant) { this.p95RoutingLatencyCompliant = p95RoutingLatencyCompliant; }
        public double getP95FilterLatencyMs() { return p95FilterLatencyMs; }
        public void setP95FilterLatencyMs(double p95FilterLatencyMs) { this.p95FilterLatencyMs = p95FilterLatencyMs; }
        public boolean isP95FilterLatencyCompliant() { return p95FilterLatencyCompliant; }
        public void setP95FilterLatencyCompliant(boolean p95FilterLatencyCompliant) { this.p95FilterLatencyCompliant = p95FilterLatencyCompliant; }
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
        private long routesCreated;
        private long routesDeleted;
        private long circuitBreakerTrips;
        private long rateLimitExceeded;
        private double throughputRps;

        // Getters and Setters
        public long getTotalRequests() { return totalRequests; }
        public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }
        public long getSuccessfulRequests() { return successfulRequests; }
        public void setSuccessfulRequests(long successfulRequests) { this.successfulRequests = successfulRequests; }
        public long getFailedRequests() { return failedRequests; }
        public void setFailedRequests(long failedRequests) { this.failedRequests = failedRequests; }
        public long getRoutesCreated() { return routesCreated; }
        public void setRoutesCreated(long routesCreated) { this.routesCreated = routesCreated; }
        public long getRoutesDeleted() { return routesDeleted; }
        public void setRoutesDeleted(long routesDeleted) { this.routesDeleted = routesDeleted; }
        public long getCircuitBreakerTrips() { return circuitBreakerTrips; }
        public void setCircuitBreakerTrips(long circuitBreakerTrips) { this.circuitBreakerTrips = circuitBreakerTrips; }
        public long getRateLimitExceeded() { return rateLimitExceeded; }
        public void setRateLimitExceeded(long rateLimitExceeded) { this.rateLimitExceeded = rateLimitExceeded; }
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
