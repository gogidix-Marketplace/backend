package com.gogidix.shared.audit.api.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;

/**
 * DTO for audit event search requests.
 * Provides comprehensive search criteria for audit event queries.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditSearchDTO {
    
    private String userId;
    private String action;
    private String resourceType;
    private String resourceId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    
    @Min(value = 1, message = "Limit must be at least 1")
    @Max(value = 1000, message = "Limit cannot exceed 1000")
    private Integer limit;
    
    @Min(value = 0, message = "Offset cannot be negative")
    private Integer offset;
    
    private String sortBy;
    private String sortDirection;
    
    /**
     * Returns the effective max results with a default value
     */
    public int getEffectiveMaxResults() {
        return limit != null ? limit : 100; // Default to 100
    }
    
    /**
     * Returns the effective sort field with a default value
     */
    public String getEffectiveSortBy() {
        return sortBy != null ? sortBy : "timestamp"; // Default to timestamp
    }
    
    /**
     * Returns the effective sort direction with a default value
     */
    public String getEffectiveSortDirection() {
        return sortDirection != null ? sortDirection : "DESC"; // Default to descending
    }
    
}