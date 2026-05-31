package com.gogidix.aiservices.aifrauddetectionservice.domain.event;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Domain event emitted when a compliance report is generated.
 * This event tracks compliance monitoring and SLO adherence.
 */
@Value
@Builder
public class ComplianceReportGeneratedEvent implements DomainEvent {

    /**
     * Unique identifier for this event instance.
     */
    String eventId;

    /**
     * ID of the generated report.
     */
    String reportId;

    /**
     * Type of report (e.g., "DAILY", "WEEKLY", "MONTHLY", "SLO", "AUDIT").
     */
    String reportType;

    /**
     * Reporting period start time.
     */
    Instant periodStart;

    /**
     * Reporting period end time.
     */
    Instant periodEnd;

    /**
     * Tenant ID for multi-tenancy support.
     */
    String tenantId;

    /**
     * When the report was generated.
     */
    Instant occurredAt;

    /**
     * ID of the aggregate that generated this event.
     */
    String aggregateId;

    /**
     * Overall compliance score (0-100).
     */
    Double overallComplianceScore;

    /**
     * Whether the system is compliant with SLOs.
     */
    Boolean isCompliant;

    /**
     * List of metrics included in the report.
     */
    List<String> includedMetrics;

    /**
     * List of failing metrics.
     */
    List<String> failingMetrics;

    /**
     * User or system that requested the report.
     */
    String generatedBy;

    /**
     * Additional report metadata.
     */
    String metadata;

    /**
     * Number of transactions analyzed in the report.
     */
    Long transactionCount;

    /**
     * Number of fraud cases detected in the period.
     */
    Long fraudDetectionCount;

    @Override
    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public String getAggregateId() {
        return aggregateId;
    }

    @Override
    public String getEventType() {
        return "ComplianceReportGenerated";
    }

    @Override
    public String getAggregateType() {
        return "ComplianceMonitoring";
    }

    /**
     * Creates a new ComplianceReportGeneratedEvent with generated ID and timestamp.
     */
    public static ComplianceReportGeneratedEvent create(String reportId, String reportType,
                                                         Instant periodStart, Instant periodEnd) {
        return ComplianceReportGeneratedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .reportId(reportId)
                .reportType(reportType)
                .periodStart(periodStart)
                .periodEnd(periodEnd)
                .occurredAt(Instant.now())
                .aggregateId(reportId)
                .build();
    }

    /**
     * Checks if the overall compliance is acceptable (score >= 80%).
     */
    public boolean hasGoodCompliance() {
        return overallComplianceScore != null && overallComplianceScore >= 80.0;
    }

    /**
     * Checks if there are critical compliance issues.
     */
    public boolean hasCriticalIssues() {
        return overallComplianceScore != null && overallComplianceScore < 50.0;
    }

    /**
     * Checks if the report indicates SLO compliance.
     */
    public boolean meetsSLO() {
        return isCompliant != null && isCompliant;
    }

    /**
     * Gets the number of failing metrics.
     */
    public int getFailingMetricCount() {
        return failingMetrics != null ? failingMetrics.size() : 0;
    }

    /**
     * Gets the fraud detection rate as a percentage.
     */
    public double getFraudDetectionRate() {
        if (transactionCount != null && transactionCount > 0 && fraudDetectionCount != null) {
            return (fraudDetectionCount.doubleValue() / transactionCount.doubleValue()) * 100.0;
        }
        return 0.0;
    }
}
