package com.gogidix.shared.audit.domain;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Domain object representing a compliance requirement.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceRequirement {
    
    private String requirementId;
    private String requirementName;
    private ComplianceType complianceType;
    private String description;
    private String category;
    private String priority;
    private LocalDateTime effectiveDate;
    private LocalDateTime expiryDate;
    private List<String> controls;
    private Boolean isMandatory;
    private String validationCriteria;
    
    /**
     * Checks if the requirement is currently active
     */
    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return (effectiveDate == null || effectiveDate.isBefore(now)) &&
               (expiryDate == null || expiryDate.isAfter(now));
    }
}