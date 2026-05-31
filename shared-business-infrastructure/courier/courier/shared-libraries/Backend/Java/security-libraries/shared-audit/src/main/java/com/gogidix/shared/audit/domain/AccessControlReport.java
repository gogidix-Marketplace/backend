package com.gogidix.shared.audit.domain;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Domain object representing an access control report.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessControlReport {

    private String reportId;
    private LocalDateTime generatedAt;
    private Long totalAccessAttempts;
    private Long successfulAccess;
    private Long failedAccess;
    private Long blockedAccess;
    private List<String> unauthorizedAttempts;
    private List<String> securityViolations;
    private String riskAssessment;

    /**
     * Calculates access success rate
     */
    public Double getAccessSuccessRate() {
        if (totalAccessAttempts == null || totalAccessAttempts == 0) {
            return 0.0;
        }
        return successfulAccess != null ?
               (successfulAccess * 100.0) / totalAccessAttempts : 0.0;
    }
}
