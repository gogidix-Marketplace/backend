package com.gogidix.shared.audit.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Value object representing a security incident report.
 */
@Data
@Builder
public class SecurityIncidentReport {
    
    private final String reportId;
    private final LocalDateTime generatedAt;
    private final LocalDateTime periodStart;
    private final LocalDateTime periodEnd;
    
    private final int totalSecurityEvents;
    private final int criticalIncidents;
    private final int highSeverityIncidents;
    private final int mediumSeverityIncidents;
    private final int lowSeverityIncidents;
    
    private final List<AuditEvent> criticalSecurityEvents;
    private final List<AuditEvent> suspiciousPatterns;
    private final List<AuditEvent> accessDeniedEvents;
    private final List<AuditEvent> privilegeEscalationAttempts;
    
    private final int uniqueThreatsDetected;
    private final List<String> topThreatSources;
    private final List<String> mostTargetedResources;
    
    private final String riskAssessment;
    private final List<String> recommendedActions;
    private final SecurityThreatLevel overallThreatLevel;
    
    /**
     * Calculates the security risk score (0-100)
     */
    public int getSecurityRiskScore() {
        int score = 0;
        
        if (criticalIncidents > 0) score += 40;
        if (highSeverityIncidents > 5) score += 25;
        if (suspiciousPatterns.size() > 10) score += 20;
        if (privilegeEscalationAttempts.size() > 0) score += 15;
        
        return Math.min(score, 100);
    }
    
    /**
     * Determines if immediate action is required
     */
    public boolean requiresImmediateAction() {
        return criticalIncidents > 0 || 
               privilegeEscalationAttempts.size() > 0 ||
               overallThreatLevel == SecurityThreatLevel.CRITICAL;
    }
}