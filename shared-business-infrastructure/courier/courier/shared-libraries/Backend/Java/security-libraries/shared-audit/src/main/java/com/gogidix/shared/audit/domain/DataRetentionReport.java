package com.gogidix.shared.audit.domain;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Domain object representing a data retention report.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataRetentionReport {
    
    private String reportId;
    private LocalDateTime generatedAt;
    private Long totalRecords;
    private Long retainedRecords;
    private Long archivedRecords;
    private Long deletedRecords;
    private List<String> retentionPolicies;
    private String complianceStatus;
    
    /**
     * Calculates retention compliance percentage
     */
    public Double getRetentionCompliancePercentage() {
        if (totalRecords == null || totalRecords == 0) {
            return 100.0;
        }
        long compliantRecords = (retainedRecords != null ? retainedRecords : 0) + 
                               (archivedRecords != null ? archivedRecords : 0);
        return (compliantRecords * 100.0) / totalRecords;
    }
}