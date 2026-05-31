package com.gogidix.hr.globalcompliance.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Country Dashboard DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryDashboardDto {
    private String countryCode;
    private Long totalRequirements;
    private Long activeRequirements;
    private Long totalChecks;
    private Long pendingChecks;
    private Long overdueChecks;
    private Long totalIssues;
    private Long openIssues;
    private Long criticalIssues;
    private Double averageComplianceScore;
}
