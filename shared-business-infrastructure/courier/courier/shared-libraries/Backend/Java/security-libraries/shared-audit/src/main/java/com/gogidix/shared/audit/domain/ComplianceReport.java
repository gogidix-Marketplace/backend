package com.gogidix.shared.audit.domain;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * Value object representing a compliance report generated from audit events.
 */
@Data
@Builder
@AllArgsConstructor
public class ComplianceReport {
    
    private final String eventId;
    private final LocalDateTime timestamp;
    private final ComplianceType complianceType;
    private final String auditTrail;
    private final String riskAssessment;
    private final String regulatoryContext;
    private final String reportId;
    private final LocalDateTime generatedAt;
    
    /**
     * Validates that this compliance report meets regulatory requirements
     */
    public boolean isValid() {
        return eventId != null && !eventId.isEmpty() &&
               timestamp != null &&
               complianceType != null &&
               auditTrail != null && !auditTrail.isEmpty() &&
               regulatoryContext != null && !regulatoryContext.isEmpty();
    }
    
    /**
     * Gets the compliance score for this report (0-100)
     */
    public int getComplianceScore() {
        int score = 0;
        
        if (eventId != null && !eventId.isEmpty()) score += 20;
        if (timestamp != null) score += 20;
        if (auditTrail != null && !auditTrail.isEmpty()) score += 30;
        if (riskAssessment != null && !riskAssessment.isEmpty()) score += 15;
        if (regulatoryContext != null && !regulatoryContext.isEmpty()) score += 15;
        
        return score;
    }
    
    /**
     * Determines if this report requires regulatory filing
     */
    public boolean requiresRegulatoryFiling() {
        return complianceType == ComplianceType.SOX ||
               complianceType == ComplianceType.PCI_DSS ||
               (complianceType == ComplianceType.GDPR && 
                auditTrail.contains("data breach"));
    }
}