package com.gogidix.shared.audit.api.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for audit trail completeness validation responses.
 * Contains analysis of audit trail integrity and completeness.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditTrailCompletenessDTO {
    
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private LocalDateTime validatedAt;
    private String validatedBy;
    
    // Overall completeness status
    private Boolean isComplete;
    private Double completenessPercentage;
    private String completenessStatus; // COMPLETE, INCOMPLETE, GAPS_DETECTED
    
    // Event statistics
    private Long totalEventsExpected;
    private Long totalEventsFound;
    private Long missingEventCount;
    private Long duplicateEventCount;
    private Long corruptedEventCount;
    
    // Gap analysis
    private List<AuditGapDTO> detectedGaps;
    private List<String> missingEventTypes;
    private List<String> affectedDomains;
    private List<String> affectedUsers;
    
    // Integrity verification
    private Boolean integrityVerified;
    private Long integrityCheckCount;
    private Long integrityFailureCount;
    private List<String> integrityIssues;
    
    // Sequence validation
    private Boolean sequenceValidated;
    private Long sequenceGapCount;
    private List<SequenceGapDTO> sequenceGaps;
    
    // Timestamp validation
    private Boolean timestampValidated;
    private Long timestampAnomalyCount;
    private List<TimestampAnomalyDTO> timestampAnomalies;
    
    // Compliance impact
    private Map<String, String> complianceImpact; // compliance type -> impact level
    private Boolean requiresRemediation;
    private String remediationPriority; // LOW, MEDIUM, HIGH, CRITICAL
    private List<String> remediationActions;
    
    /**
     * Nested DTO for audit gaps
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuditGapDTO {
        private String gapId;
        private LocalDateTime gapStart;
        private LocalDateTime gapEnd;
        private Long durationMinutes;
        private String affectedDomain;
        private String gapType; // MISSING_EVENTS, SEQUENCE_GAP, TIMESTAMP_GAP
        private String severity; // LOW, MEDIUM, HIGH, CRITICAL
        private String description;
        private String possibleCause;
        private Boolean canBeRecovered;
        private String recoveryMethod;
    }
    
    /**
     * Nested DTO for sequence gaps
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SequenceGapDTO {
        private Long expectedSequence;
        private Long actualSequence;
        private Long gapSize;
        private LocalDateTime detectedAt;
        private String context;
        private String impact;
    }
    
    /**
     * Nested DTO for timestamp anomalies
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TimestampAnomalyDTO {
        private String eventId;
        private LocalDateTime recordedTimestamp;
        private LocalDateTime expectedTimestamp;
        private Long deviationMinutes;
        private String anomalyType; // FUTURE_TIMESTAMP, PAST_TIMESTAMP, OUT_OF_ORDER
        private String impact;
        private String possibleCause;
    }
    
    /**
     * Calculates the gap percentage
     */
    public Double getGapPercentage() {
        if (totalEventsExpected == null || totalEventsExpected == 0) {
            return 0.0;
        }
        return (missingEventCount != null) ? 
            (missingEventCount.doubleValue() / totalEventsExpected.doubleValue() * 100.0) : 0.0;
    }
    
    /**
     * Determines if the audit trail meets compliance standards
     */
    public boolean meetsComplianceStandards() {
        return isComplete != null && isComplete &&
               getGapPercentage() <= 1.0 && // Less than 1% gaps
               integrityVerified != null && integrityVerified &&
               (integrityFailureCount == null || integrityFailureCount == 0);
    }
    
    /**
     * Gets the highest severity gap level
     */
    public String getHighestGapSeverity() {
        if (detectedGaps == null || detectedGaps.isEmpty()) {
            return "NONE";
        }
        
        boolean hasCritical = detectedGaps.stream().anyMatch(g -> "CRITICAL".equals(g.getSeverity()));
        if (hasCritical) return "CRITICAL";
        
        boolean hasHigh = detectedGaps.stream().anyMatch(g -> "HIGH".equals(g.getSeverity()));
        if (hasHigh) return "HIGH";
        
        boolean hasMedium = detectedGaps.stream().anyMatch(g -> "MEDIUM".equals(g.getSeverity()));
        if (hasMedium) return "MEDIUM";
        
        return "LOW";
    }
    
    /**
     * Calculates the overall trail health score (0-100)
     */
    public Double getTrailHealthScore() {
        double score = 100.0;
        
        // Deduct for gaps
        if (completenessPercentage != null) {
            score = completenessPercentage;
        }
        
        // Deduct for integrity issues
        if (integrityVerified != null && !integrityVerified) {
            score -= 20.0;
        }
        
        // Deduct for sequence issues
        if (sequenceValidated != null && !sequenceValidated) {
            score -= 10.0;
        }
        
        // Deduct for timestamp issues
        if (timestampValidated != null && !timestampValidated) {
            score -= 5.0;
        }
        
        return Math.max(0.0, score);
    }
}