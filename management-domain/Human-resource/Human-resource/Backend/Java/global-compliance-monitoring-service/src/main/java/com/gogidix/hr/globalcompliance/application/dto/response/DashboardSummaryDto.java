package com.gogidix.hr.globalcompliance.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dashboard Summary DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDto {
    private Long totalRequirements;
    private Long activeRequirements;
    private Long totalChecks;
    private Long pendingChecks;
    private Long overdueChecks;
    private Long totalIssues;
    private Long openIssues;
    private Long criticalIssues;
    private Long overdueIssues;
    private Double averageComplianceScore;
}
