package com.gogidix.shared.audit.domain.port;

import com.gogidix.shared.audit.domain.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Domain port for compliance reporting and regulatory requirements.
 * This interface defines the contract for generating compliance reports.
 */
public interface ComplianceReporter {

    /**
     * Generates a compliance report for a specific compliance type and time period
     */
    CompletableFuture<ComplianceReport> generateComplianceReport(
        ComplianceType complianceType,
        LocalDateTime startTime,
        LocalDateTime endTime
    );

    /**
     * Generates an audit trail report for a specific user
     */
    CompletableFuture<List<AuditTrailEntry>> generateUserAuditTrail(
        String userId,
        LocalDateTime startTime,
        LocalDateTime endTime
    );

    /**
     * Generates a security incident report
     */
    CompletableFuture<SecurityIncidentReport> generateSecurityIncidentReport(
        LocalDateTime startTime,
        LocalDateTime endTime
    );

    /**
     * Generates a financial transaction audit report
     */
    CompletableFuture<FinancialAuditReport> generateFinancialAuditReport(
        LocalDateTime startTime,
        LocalDateTime endTime
    );

    /**
     * Validates compliance with specific regulatory requirements
     */
    CompletableFuture<ComplianceValidationResult> validateCompliance(
        ComplianceType complianceType,
        LocalDateTime startTime,
        LocalDateTime endTime
    );

    /**
     * Generates data retention compliance report
     */
    CompletableFuture<DataRetentionReport> generateDataRetentionReport();

    /**
     * Generates access control audit report
     */
    CompletableFuture<AccessControlReport> generateAccessControlReport(
        LocalDateTime startTime,
        LocalDateTime endTime
    );

    /**
     * Checks for audit log completeness and integrity
     */
    CompletableFuture<AuditIntegrityReport> checkAuditIntegrity(
        LocalDateTime startTime,
        LocalDateTime endTime
    );

    /**
     * Generates suspicious activity report
     */
    CompletableFuture<SuspiciousActivityReport> generateSuspiciousActivityReport(
        LocalDateTime startTime,
        LocalDateTime endTime
    );

    /**
     * Exports audit data for regulatory submission
     */
    CompletableFuture<String> exportAuditDataForRegulator(
        ComplianceType complianceType,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String exportFormat
    );

    /**
     * Schedules automatic compliance reporting
     */
    void scheduleComplianceReporting(ComplianceType complianceType, String cronExpression);

    /**
     * Gets compliance requirements for a specific business domain
     */
    List<ComplianceRequirement> getComplianceRequirements(BusinessDomain domain);
}
