package com.gogidix.shared.audit.domain;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Domain object representing a suspicious activity report.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuspiciousActivityReport {
    
    private String reportId;
    private LocalDateTime generatedAt;
    private Long totalSuspiciousEvents;
    private Long highRiskEvents;
    private Long mediumRiskEvents;
    private Long lowRiskEvents;
    private List<String> suspiciousPatterns;
    private List<String> riskIndicators;
    private String threatAssessment;
    
    /**
     * Calculates high risk percentage
     */
    public Double getHighRiskPercentage() {
        if (totalSuspiciousEvents == null || totalSuspiciousEvents == 0) {
            return 0.0;
        }
        return highRiskEvents != null ? 
               (highRiskEvents * 100.0) / totalSuspiciousEvents : 0.0;
    }
}