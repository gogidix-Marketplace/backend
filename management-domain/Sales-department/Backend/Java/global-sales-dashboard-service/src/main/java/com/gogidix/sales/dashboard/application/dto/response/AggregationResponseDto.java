package com.gogidix.sales.dashboard.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Aggregation Response DTO
 * Used for sending aggregation data to clients
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AggregationResponseDto {

    private String id;
    private String aggregationId;
    private String tenantId;
    private String aggregationType;
    private String dimension;
    private String dimensionValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private String periodType;
    private Integer periodValue;
    private Integer year;

    // Metrics
    private MoneyDto totalRevenue;
    private MoneyDto targetRevenue;
    private Double achievementPercentage;
    private Integer totalDeals;
    private Integer wonDeals;
    private Double winRate;
    private MoneyDto averageDealSize;
    private MoneyDto averageDiscount;
    private Integer newOpportunities;
    private MoneyDto pipelineValue;
    private MoneyDto weightedPipeline;

    // Breakdown
    private List<BreakdownItemDto> breakdown;

    // Comparison
    private ComparisonDto comparison;

    // Metadata
    private String dataSource;
    private Instant aggregationTime;
    private Integer dataVersion;
    private Boolean isComplete;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MoneyDto {
        private Double amount;
        private String currency;
        private String formatted;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BreakdownItemDto {
        private String key;
        private String label;
        private MoneyDto value;
        private Integer count;
        private Double percentage;
        private String color;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComparisonDto {
        private MoneyDto previousPeriodRevenue;
        private Double growthRate;
        private MoneyDto difference;
        private String trend;
    }
}
