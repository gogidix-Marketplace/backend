package com.gogidix.shared.audit.domain;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Domain object representing a compliance validation result.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceValidationResult {
    
    private String validationId;
    private LocalDateTime validatedAt;
    private ComplianceType complianceType;
    private Boolean isValid;
    private String validationStatus;
private List<String> violations;
    private List<String> warnings;
    private String remedialActions;
    private String validatedBy;
    
    /**
     * Checks if the validation passed
     */
    public boolean isPassed() {
        return isValid != null && isValid && 
               (violations == null || violations.isEmpty());
    }
}