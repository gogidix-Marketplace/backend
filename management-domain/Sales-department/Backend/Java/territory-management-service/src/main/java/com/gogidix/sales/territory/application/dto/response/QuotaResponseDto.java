package com.gogidix.sales.territory.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Quota Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotaResponseDto {

    private String id;
    private String quotaId;
    private String tenantId;
    private String territoryId;
    private String territoryName;
    private String salesRepresentativeId;

    // Quota details
    private QuotaTypeDto type;
    private BigDecimal amount;
    private String currency;
    private QuotaPeriodDto period;
    private Integer year;
    private Integer month;
    private LocalDate startDate;
    private LocalDate endDate;

    // Breakdown
    private List<QuotaBreakdownDto> breakdown;

    // Status
    private QuotaStatusDto status;

    // Performance tracking
    private BigDecimal currentAchievement;
    private BigDecimal attainmentPercentage;
    private Instant lastCalculatedAt;

    // Approval
    private String approvedBy;
    private Instant approvedAt;

    // Audit
    private Instant createdAt;
    private Instant updatedAt;

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class QuotaBreakdownDto {
        private String category;
        private BigDecimal amount;
        private String description;
        private String productId;
        private String productCategoryId;
    }

    public enum QuotaTypeDto {
        REVENUE,
        UNITS,
        MARGIN,
        ACTIVITY
    }

    public enum QuotaPeriodDto {
        DAILY,
        WEEKLY,
        MONTHLY,
        QUARTERLY,
        ANNUAL
    }

    public enum QuotaStatusDto {
        DRAFT,
        ACTIVE,
        PAUSED,
        COMPLETED,
        CANCELLED
    }
}
