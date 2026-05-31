package com.gogidix.sales.territory.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Territory Assignment Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TerritoryAssignmentResponseDto {

    private String id;
    private String assignmentId;
    private String tenantId;
    private String territoryId;
    private String territoryName;
    private String salesRepresentativeId;
    private String salesRepresentativeName;
    private AssignmentStatusDto status;
    private AssignmentTypeDto type;

    // Assignment period
    private Instant assignedAt;
    private String assignedBy;
    private LocalDate effectiveDate;
    private LocalDate endDate;

    // Assignment details
    private Boolean primaryAssignment;
    private Integer priority;
    private String notes;

    // Performance tracking
    private AssignmentPerformanceDto performance;

    // Audit
    private Instant createdAt;
    private Instant updatedAt;

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class AssignmentPerformanceDto {
        private BigDecimal salesGenerated;
        private Integer accountsManaged;
        private Integer dealsClosed;
        private BigDecimal quotaAttainment;
        private Instant lastUpdated;
    }

    public enum AssignmentStatusDto {
        ACTIVE,
        INACTIVE,
        PENDING,
        REVOKED,
        EXPIRED
    }

    public enum AssignmentTypeDto {
        FULL_TIME,
        PART_TIME,
        SHARED,
        TEMPORARY,
        COVER
    }
}
