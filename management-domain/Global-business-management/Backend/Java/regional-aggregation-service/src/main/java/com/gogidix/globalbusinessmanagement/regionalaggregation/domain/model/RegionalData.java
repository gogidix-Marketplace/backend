package com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Domain model for regional data aggregation.
 * Represents aggregated business data for a specific region.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "regional_data")
public class RegionalData {

    @Id
    private String id;

    @NotBlank(message = "Region code is required")
    @Indexed
    private String regionCode;

    @NotBlank(message = "Region name is required")
    private String regionName;

    @NotBlank(message = "Period identifier is required")
    @Indexed
    private String periodId;

    @NotNull(message = "Aggregation date is required")
    @Indexed
    private LocalDateTime aggregationDate;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    @NotNull(message = "Aggregation type is required")
    private AggregationType aggregationType;

    @NotNull(message = "Aggregation status is required")
    @Builder.Default
    private AggregationStatus status = AggregationStatus.PENDING;

    @Valid
    @NotEmpty(message = "Country contributions are required")
    private List<CountryContribution> countryContributions;

    @Valid
    private AggregatedMetrics aggregatedMetrics;

    @Valid
    private RegionalTrends trends;

    @Valid
    private RegionalBenchmarking benchmarking;

    @Valid
    private RegionalRankings rankings;

    private Map<String, Object> metadata;

    private String dataSource;

    @Indexed
    private Instant lastUpdated;

    private Instant createdAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AggregatedMetrics {
        private BigDecimal totalRevenue;
        private BigDecimal totalExpenses;
        private BigDecimal totalProfit;
        private BigDecimal profitMargin;
        private Long totalOrders;
        private Long totalCustomers;
        private Long newCustomers;
        private BigDecimal customerRetentionRate;
        private BigDecimal averageOrderValue;
        private BigDecimal marketPenetration;
        private BigDecimal customerAcquisitionCost;
        private BigDecimal customerLifetimeValue;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionalTrends {
        private BigDecimal revenueTrend;
        private String revenueTrendDirection;
        private BigDecimal orderTrend;
        private String orderTrendDirection;
        private BigDecimal customerTrend;
        private String customerTrendDirection;
        private BigDecimal profitMarginTrend;
        private String profitMarginTrendDirection;
        private String topGrowthDriver;
        private String topRiskFactor;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionalBenchmarking {
        private BigDecimal vsGlobalAverage;
        private String vsGlobalAverageDirection;
        private BigDecimal vsTopPerformer;
        private BigDecimal vsPreviousPeriod;
        private String vsPreviousPeriodDirection;
        private Integer globalRank;
        private Integer regionalRank;
        private BigDecimal performanceScore;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionalRankings {
        private Integer revenueRank;
        private Integer growthRank;
        private Integer profitabilityRank;
        private Integer customerSatisfactionRank;
        private Integer marketShareRank;
        private Integer overallRank;
    }

    public enum AggregationType {
        DAILY,
        WEEKLY,
        MONTHLY,
        QUARTERLY,
        YEARLY,
        CUSTOM
    }

    public enum AggregationStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        CANCELLED
    }

    public boolean isCompleted() {
        return AggregationStatus.COMPLETED.equals(status);
    }

    public boolean isFailed() {
        return AggregationStatus.FAILED.equals(status);
    }

    public boolean isInProgress() {
        return AggregationStatus.IN_PROGRESS.equals(status);
    }

    public BigDecimal calculateTotalRevenue() {
        return countryContributions.stream()
            .map(CountryContribution::getRevenueContribution)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalOrders() {
        return countryContributions.stream()
            .map(CountryContribution::getOrderCount)
            .map(BigDecimal::valueOf)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
