package com.gogidix.sales.territory.application.dto.response;

import com.gogidix.sales.territory.domain.valueobject.GeographicBoundary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Territory Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TerritoryResponseDto {

    private String id;
    private String territoryId;
    private String tenantId;
    private String name;
    private String code;
    private String description;
    private TerritoryTypeDto type;
    private TerritoryStatusDto status;

    // Geographic boundaries
    private GeographicBoundary geographicBoundary;

    // Product-based territories
    private List<String> productCategories;
    private List<String> productIds;

    // Customer segment boundaries
    private List<String> customerSegments;
    private List<String> customerTierIds;

    // Hierarchy
    private String parentTerritoryId;
    private List<String> childTerritoryIds;

    // Metadata
    private String regionId;
    private String managerId;
    private Integer priority;

    // Performance tracking
    private TerritoryPerformanceDto performance;

    // Realignment tracking
    private Boolean pendingRealignment;
    private Instant realignmentRequestedAt;
    private String realignmentRequestedBy;

    // Audit
    private Instant createdAt;
    private Instant updatedAt;

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class TerritoryPerformanceDto {
        private BigDecimal currentSales;
        private BigDecimal quota;
        private BigDecimal quotaAttainment;
        private Integer accountsCount;
        private Integer dealsCount;
        private Integer activeLeads;
        private BigDecimal winRate;
        private Instant lastUpdated;
        private String periodStart;
        private String periodEnd;
    }

    public enum TerritoryTypeDto {
        GEOGRAPHIC,
        PRODUCT_BASED,
        CUSTOMER_SEGMENT,
        HYBRID
    }

    public enum TerritoryStatusDto {
        ACTIVE,
        INACTIVE,
        PENDING_REALIGNMENT,
        ARCHIVED
    }
}
