package com.gogidix.hr.globalcompliance.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Category Compliance DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryComplianceDto {
    private String category;
    private Long totalRequirements;
    private Long activeRequirements;
    private Long totalChecks;
    private Long passedChecks;
    private Double complianceScore;
}
