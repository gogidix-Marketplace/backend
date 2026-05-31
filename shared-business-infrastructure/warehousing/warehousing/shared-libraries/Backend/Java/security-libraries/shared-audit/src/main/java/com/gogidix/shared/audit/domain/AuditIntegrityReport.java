package com.gogidix.shared.audit.domain;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Domain object representing an audit integrity report.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditIntegrityReport {
    
    private String reportId;
    private LocalDateTime generatedAt;
    private Long totalAuditEvents;
    private Long integrityChecksPerformed;
    private Long integrityFailures;
    private List<String> integrityIssues;
    private List<String> tamperedEvents;
    private String integrityScore;
    
    /**
     * Calculates integrity success rate
     */
    public Double getIntegritySuccessRate() {
        if (integrityChecksPerformed == null || integrityChecksPerformed == 0) {
            return 100.0;
        }
        long successfulChecks = integrityChecksPerformed - (integrityFailures != null ? integrityFailures : 0);
        return (successfulChecks * 100.0) / integrityChecksPerformed;
    }
}