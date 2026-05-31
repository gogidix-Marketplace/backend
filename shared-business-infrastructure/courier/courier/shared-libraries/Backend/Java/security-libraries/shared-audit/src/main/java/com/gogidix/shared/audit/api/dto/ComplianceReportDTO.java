package com.gogidix.shared.audit.api.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for compliance report responses.
 * Contains comprehensive compliance analysis and findings.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComplianceReportDTO {
    
    private String reportId;
    private LocalDateTime generatedAt;
    private String status;
    private String complianceStatus;
    private Long totalEvents;
    private Long compliantEvents;
    private java.util.List<ComplianceViolationDTO> violations;
    private java.util.List<ComplianceRecommendationDTO> recommendations;
    
    /**
     * Nested DTO for compliance findings
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ComplianceFindingDTO {
        private String findingId;
        private String category;
        private String severity; // INFO, LOW, MEDIUM, HIGH, CRITICAL
        private String description;
        private Long eventCount;
        private String evidence;
        private String recommendation;
        private String status; // OPEN, IN_PROGRESS, RESOLVED
    }
    
    /**
     * Nested DTO for compliance violations
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ComplianceViolationDTO {
        private String violationId;
        private String rule;
        private String description;
        private String severity; // LOW, MEDIUM, HIGH, CRITICAL
        private Long occurrenceCount;
        private LocalDateTime firstOccurrence;
        private LocalDateTime lastOccurrence;
        private String remediation;
        private String status; // OPEN, ACKNOWLEDGED, RESOLVED
    }
    
    /**
     * Nested DTO for compliance recommendations
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ComplianceRecommendationDTO {
        private String recommendationId;
        private String category;
        private String priority; // LOW, MEDIUM, HIGH, URGENT
        private String description;
        private String rationale;
        private String implementation;
        private Integer estimatedEffort;
        private LocalDateTime targetDate;
    }
    
    /**
     * Nested DTO for compliance requirement status
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ComplianceRequirementStatusDTO {
        private String requirementId;
        private String requirementName;
        private String status; // MET, PARTIALLY_MET, NOT_MET
        private Double compliancePercentage;
        private String evidence;
        private String gap;
        private String remediation;
    }
    
    /**
     * Calculates the compliance percentage
     */
    public Double getCompliancePercentage() {
        if (totalEvents == null || totalEvents == 0) {
            return 100.0;
        }
        return (compliantEvents != null) ? 
            (compliantEvents.doubleValue() / totalEvents.doubleValue() * 100.0) : 0.0;
    }
    
    /**
     * Determines if the report indicates compliance
     */
    public boolean isCompliant() {
        return getCompliancePercentage() >= 95.0 && 
               "COMPLIANT".equals(complianceStatus) &&
               (violations == null || violations.isEmpty());
    }
    
    /**
     * Gets the number of critical violations
     */
    public long getCriticalViolationCount() {
        if (violations == null) {
            return 0;
        }
        return violations.stream()
            .filter(v -> "CRITICAL".equals(v.getSeverity()))
            .count();
    }
    
    /**
     * Gets the number of high-priority recommendations
     */
    public long getHighPriorityRecommendationCount() {
        if (recommendations == null) {
            return 0;
        }
        return recommendations.stream()
            .filter(r -> "HIGH".equals(r.getPriority()) || "URGENT".equals(r.getPriority()))
            .count();
    }
}