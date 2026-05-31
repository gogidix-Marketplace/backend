package com.gogidix.aiservices.aiauthenticationservice.infrastructure.governance;

import com.gogidix.aiservices.aiauthenticationservice.infrastructure.metrics.AuthenticationMetrics;
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

    private final AuthenticationMetrics metrics;
    private final ThresholdValidator thresholdValidator;

    // SLO Targets for authentication
    private static final double TARGET_P95_LATENCY_MS = 200.0;
    private static final double TARGET_P99_LATENCY_MS = 500.0;
    private static final double TARGET_ERROR_RATE = 0.005;  // 0.5%
    private static final double TARGET_SUCCESS_RATE = 0.99;  // 99%
    private static final double TARGET_AVAILABILITY = 0.999;  // 99.9%

    public ComplianceReportGenerator(
            AuthenticationMetrics metrics,
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

        // Security Metrics Section
        report.setSecurityMetrics(generateSecuritySection());

        // Governance Status
        report.setGovernanceStatus(calculateGovernanceStatus());

        log.info("Generated governance report: {}", report.getReportId());

        return report;
    }

    private SloComplianceSection generateSloComplianceSection() {
        SloComplianceSection section = new SloComplianceSection();

        // P95 Latency
        double p95Latency = metrics.getAuthenticationLatencyP95();
        section.setP95LatencyMs(p95Latency);
        section.setP95LatencyCompliant(p95Latency <= TARGET_P95_LATENCY_MS);
        section.setP95LatencyVariance(p95Latency - TARGET_P95_LATENCY_MS);

        // P99 Latency
        double p99Latency = metrics.getAuthenticationLatencyP99();
        section.setP99LatencyMs(p99Latency);
        section.setP99LatencyCompliant(p99Latency <= TARGET_P99_LATENCY_MS);

        // Error Rate
        double errorRate = metrics.getErrorRate();
        section.setErrorRate(errorRate * 100);
        section.setErrorRateCompliant(errorRate <= TARGET_ERROR_RATE);

        // Success Rate
        double successRate = metrics.getSuccessRate();
        section.setSuccessRate(successRate * 100);
        section.setSuccessRateCompliant(successRate >= TARGET_SUCCESS_RATE);

        // Availability (assumed 100% - error rate for now)
        double availability = 1.0 - errorRate;
        section.setAvailability(availability * 100);
        section.setAvailabilityCompliant(availability >= TARGET_AVAILABILITY);

        // Overall SLO Status
        section.setOverallCompliant(
            section.isP95LatencyCompliant() &&
            section.isP99LatencyCompliant() &&
            section.isErrorRateCompliant() &&
            section.isSuccessRateCompliant() &&
            section.isAvailabilityCompliant()
        );

        return section;
    }

    private PerformanceMetricsSection generatePerformanceSection() {
        PerformanceMetricsSection section = new PerformanceMetricsSection();

        section.setTotalRequests((long) metrics.getMeterRegistry()
                .get("authentication.total").counter().count());

        section.setSuccessfulRequests((long) metrics.getMeterRegistry()
                .get("authentication.success").counter().count());

        section.setFailedRequests((long) metrics.getMeterRegistry()
                .get("authentication.failure").counter().count());

        section.setMfaRequired((long) metrics.getMeterRegistry()
                .get("authentication.mfa.required").counter().count());

        section.setAccountLocks((long) metrics.getMeterRegistry()
                .get("authentication.account.locked").counter().count());

        section.setHighRiskBlocked((long) metrics.getMeterRegistry()
                .get("authentication.highrisk.blocked").counter().count());

        section.setBiometricAuth((long) metrics.getMeterRegistry()
                .get("authentication.biometric.total").counter().count());

        section.setPasswordAuth((long) metrics.getMeterRegistry()
                .get("authentication.password.total").counter().count());

        // Throughput (requests per second - approximate from total)
        section.setThroughputRps(calculateThroughput());

        return section;
    }

    private SecurityMetricsSection generateSecuritySection() {
        SecurityMetricsSection section = new SecurityMetricsSection();

        long total = (long) metrics.getMeterRegistry().get("authentication.total").counter().count();
        long blocked = (long) metrics.getMeterRegistry().get("authentication.highrisk.blocked").counter().count();
        long locked = (long) metrics.getMeterRegistry().get("authentication.account.locked").counter().count();

        section.setTotalAttempts(total);
        section.setBlockedAttempts(blocked);
        section.setLockedAccounts(locked);
        section.setBlockRate(total > 0 ? (double) blocked / total * 100 : 0.0);

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
        private SecurityMetricsSection securityMetrics;
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
        public SecurityMetricsSection getSecurityMetrics() { return securityMetrics; }
        public void setSecurityMetrics(SecurityMetricsSection securityMetrics) { this.securityMetrics = securityMetrics; }
        public GovernanceStatus getGovernanceStatus() { return governanceStatus; }
        public void setGovernanceStatus(GovernanceStatus governanceStatus) { this.governanceStatus = governanceStatus; }

        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("reportId", reportId);
            map.put("generatedAt", generatedAt.toString());
            map.put("sloCompliance", sloComplianceToMap());
            map.put("performanceMetrics", performanceMetricsToMap());
            map.put("securityMetrics", securityMetricsToMap());
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
            map.put("successRate", sloCompliance.getSuccessRate());
            map.put("successRateCompliant", sloCompliance.isSuccessRateCompliant());
            map.put("availability", sloCompliance.getAvailability());
            map.put("overallCompliant", sloCompliance.isOverallCompliant());
            return map;
        }

        private Map<String, Object> performanceMetricsToMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("totalRequests", performanceMetrics.getTotalRequests());
            map.put("successfulRequests", performanceMetrics.getSuccessfulRequests());
            map.put("failedRequests", performanceMetrics.getFailedRequests());
            map.put("mfaRequired", performanceMetrics.getMfaRequired());
            map.put("accountLocks", performanceMetrics.getAccountLocks());
            map.put("biometricAuth", performanceMetrics.getBiometricAuth());
            map.put("passwordAuth", performanceMetrics.getPasswordAuth());
            return map;
        }

        private Map<String, Object> securityMetricsToMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("totalAttempts", securityMetrics.getTotalAttempts());
            map.put("blockedAttempts", securityMetrics.getBlockedAttempts());
            map.put("lockedAccounts", securityMetrics.getLockedAccounts());
            map.put("blockRate", securityMetrics.getBlockRate());
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
        private double successRate;
        private boolean successRateCompliant;
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
        public double getSuccessRate() { return successRate; }
        public void setSuccessRate(double successRate) { this.successRate = successRate; }
        public boolean isSuccessRateCompliant() { return successRateCompliant; }
        public void setSuccessRateCompliant(boolean successRateCompliant) { this.successRateCompliant = successRateCompliant; }
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
        private long mfaRequired;
        private long accountLocks;
        private long highRiskBlocked;
        private long biometricAuth;
        private long passwordAuth;
        private double throughputRps;

        // Getters and Setters
        public long getTotalRequests() { return totalRequests; }
        public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }
        public long getSuccessfulRequests() { return successfulRequests; }
        public void setSuccessfulRequests(long successfulRequests) { this.successfulRequests = successfulRequests; }
        public long getFailedRequests() { return failedRequests; }
        public void setFailedRequests(long failedRequests) { this.failedRequests = failedRequests; }
        public long getMfaRequired() { return mfaRequired; }
        public void setMfaRequired(long mfaRequired) { this.mfaRequired = mfaRequired; }
        public long getAccountLocks() { return accountLocks; }
        public void setAccountLocks(long accountLocks) { this.accountLocks = accountLocks; }
        public long getHighRiskBlocked() { return highRiskBlocked; }
        public void setHighRiskBlocked(long highRiskBlocked) { this.highRiskBlocked = highRiskBlocked; }
        public long getBiometricAuth() { return biometricAuth; }
        public void setBiometricAuth(long biometricAuth) { this.biometricAuth = biometricAuth; }
        public long getPasswordAuth() { return passwordAuth; }
        public void setPasswordAuth(long passwordAuth) { this.passwordAuth = passwordAuth; }
        public double getThroughputRps() { return throughputRps; }
        public void setThroughputRps(double throughputRps) { this.throughputRps = throughputRps; }
    }

    public static class SecurityMetricsSection {
        private long totalAttempts;
        private long blockedAttempts;
        private long lockedAccounts;
        private double blockRate;

        // Getters and Setters
        public long getTotalAttempts() { return totalAttempts; }
        public void setTotalAttempts(long totalAttempts) { this.totalAttempts = totalAttempts; }
        public long getBlockedAttempts() { return blockedAttempts; }
        public void setBlockedAttempts(long blockedAttempts) { this.blockedAttempts = blockedAttempts; }
        public long getLockedAccounts() { return lockedAccounts; }
        public void setLockedAccounts(long lockedAccounts) { this.lockedAccounts = lockedAccounts; }
        public double getBlockRate() { return blockRate; }
        public void setBlockRate(double blockRate) { this.blockRate = blockRate; }
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
