package com.gogidix.aiservices.aifrauddetectionservice.domain.port.in;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Input port for compliance monitoring operations.
 * This use case handles Service Level Objective (SLO) monitoring, threshold validation,
 * and compliance reporting for the fraud detection system.
 *
 * Ensures the fraud detection service meets operational and regulatory requirements.
 */
@UseCase(
    value = "Monitors compliance with SLOs and generates compliance reports",
    category = "monitoring"
)
public interface ComplianceMonitoringUseCase {

    /**
     * Validates whether the current system metrics are within defined thresholds.
     *
     * @param thresholdName Name of the threshold to validate
     * @param actualValue Current actual value to compare against threshold
     * @return Validation result indicating if threshold is breached
     */
    ThresholdValidationResult validateThreshold(String thresholdName, Double actualValue);

    /**
     * Generates a compliance report for the specified time period.
     *
     * @param request Report generation request with time range and type
     * @return Generated compliance report
     */
    ComplianceReport generateComplianceReport(ReportRequest request);

    /**
     * Checks current SLO compliance status across all monitored metrics.
     *
     * @param tenantId Optional tenant identifier for tenant-specific SLOs
     * @return Overall SLO compliance status with individual metric breakdowns
     */
    SLOComplianceStatus checkSLOCompliance(String tenantId);

    /**
     * Result of threshold validation.
     */
    record ThresholdValidationResult(
        String thresholdName,
        Double thresholdValue,
        Double actualValue,
        boolean breached,
        String severity, // "LOW", "MEDIUM", "HIGH", "CRITICAL"
        Instant validatedAt
    ) {}

    /**
     * Request for generating a compliance report.
     */
    record ReportRequest(
        String reportType, // "DAILY", "WEEKLY", "MONTHLY", "CUSTOM"
        Instant startDate,
        Instant endDate,
        String tenantId,
        List<String> includeMetrics
    ) {}

    /**
     * Generated compliance report.
     */
    record ComplianceReport(
        String reportId,
        String reportType,
        Instant generatedAt,
        Instant periodStart,
        Instant periodEnd,
        String tenantId,
        double overallComplianceScore,
        List<MetricCompliance> metricCompliances,
        List<String> recommendations
    ) {}

    /**
     * Individual metric compliance data.
     */
    record MetricCompliance(
        String metricName,
        double thresholdValue,
        double actualValue,
        boolean compliant,
        double compliancePercentage
    ) {}

    /**
     * Overall SLO compliance status.
     */
    record SLOComplianceStatus(
        boolean overallCompliant,
        double overallScore,
        Instant lastChecked,
        List<MetricStatus> metricStatuses,
        List<String> failingMetrics
    ) {}

    /**
     * Status of an individual metric.
     */
    record MetricStatus(
        String metricName,
        boolean compliant,
        double currentValue,
        double threshold,
        String trend, // "IMPROVING", "STABLE", "DECLINING"
        Instant lastUpdated
    ) {}
}
