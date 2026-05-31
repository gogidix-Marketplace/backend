package com.gogidix.hr.globalcompliance.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Compliance Score DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceScoreDto {
    private Double overallScore;
    private String rating;
    private Long passedChecks;
    private Long failedChecks;
    private Long partialChecks;
    private Long totalChecks;
    private Double compliancePercentage;
}
