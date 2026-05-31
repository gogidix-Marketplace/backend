package com.gogidix.aiservices.aifrauddetectionservice.application.query;

import com.gogidix.aiservices.aifrauddetectionservice.application.dto.ComplianceReportDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * CQRS Query for generating compliance reports.
 * Used to retrieve compliance and audit reports for specified date ranges.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetComplianceReportQuery implements Query<ComplianceReportDTO> {

    @NotNull(message = "Start date is required")
    private Instant startDate;

    @NotNull(message = "End date is required")
    private Instant endDate;

    @NotNull(message = "Report type is required")
    private ReportType reportType;

    private String tenantId;

    private String format;

    @Builder.Default
    private boolean includeDetails = true;

    /**
     * Types of compliance reports available.
     */
    public enum ReportType {
        FRAUD_DETECTION,
        PATTERN_ANALYSIS,
        AUDIT_TRAIL,
        RISK_ASSESSMENT,
        REGULATORY_COMPLIANCE
    }
}
