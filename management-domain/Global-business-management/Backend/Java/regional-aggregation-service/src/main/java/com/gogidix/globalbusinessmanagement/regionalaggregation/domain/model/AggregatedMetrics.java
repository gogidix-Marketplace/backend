package com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Domain model for aggregated metrics across regions.
 * Stores comprehensive aggregated business metrics.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "aggregated_metrics")
@CompoundIndex(name = "region_period_idx", def = "{'regionCode': 1, 'periodId': 1}", unique = true)
public class AggregatedMetrics {

    @Id
    private String id;

    @NotNull(message = "Region code is required")
    @Indexed
    private String regionCode;

    @NotNull(message = "Period identifier is required")
    @Indexed
    private String periodId;

    @NotNull(message = "Metrics period start is required")
    private LocalDateTime periodStart;

    @NotNull(message = "Metrics period end is required")
    private LocalDateTime periodEnd;

    @NotNull(message = "Aggregation timestamp is required")
    @Indexed
    private Instant aggregatedAt;

    @NotNull(message = "Financial metrics are required")
    @Valid
    private FinancialMetrics financial;

    @NotNull(message = "Customer metrics are required")
    @Valid
    private CustomerMetrics customer;

    @NotNull(message = "Operational metrics are required")
    @Valid
    private OperationalMetrics operational;

    @Valid
    private MarketMetrics market;

    @Valid
    private PerformanceMetrics performance;

    @Valid
    private List<MetricVariance> variances;

    @Valid
    private List<RegionalComparison> regionalComparisons;

    @Valid
    private MetricQualityScore qualityScore;

    @Indexed
    private String dataVersion;

    private Map<String, Object> additionalMetrics;

    @Indexed
    private MetricStatus status;

    private Instant createdAt;

    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FinancialMetrics {
        private BigDecimal totalRevenue;
        private BigDecimal revenuePerCustomer;
        private BigDecimal revenuePerOrder;
        private BigDecimal totalExpenses;
        private BigDecimal grossProfit;
        private BigDecimal netProfit;
        private BigDecimal profitMargin;
        private BigDecimal operatingMargin;
        private BigDecimal ebitda;
        private BigDecimal taxAmount;
        private BigDecimal currencyAdjustment;
        private BigDecimal revenueGrowthRate;
        private BigDecimal profitGrowthRate;
        private BigDecimal expenseGrowthRate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerMetrics {
        private Long totalCustomers;
        private Long activeCustomers;
        private Long newCustomers;
        private Long churnedCustomers;
        private Long returningCustomers;
        private BigDecimal customerRetentionRate;
        private BigDecimal customerAcquisitionRate;
        private BigDecimal customerChurnRate;
        private BigDecimal customerLifetimeValue;
        private BigDecimal customerAcquisitionCost;
        private BigDecimal averageRevenuePerUser;
        private BigDecimal customerSatisfactionScore;
        private BigDecimal netPromoterScore;
        private Integer supportTickets;
        private BigDecimal avgResolutionTime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OperationalMetrics {
        private Long totalOrders;
        private Long ordersPerCustomer;
        private BigDecimal averageOrderValue;
        private BigDecimal orderFulfillmentRate;
        private BigDecimal onTimeDeliveryRate;
        private BigDecimal returnRate;
        private BigDecimal refundRate;
        private Long inventoryTurnover;
        private BigDecimal fulfillmentCostPerOrder;
        private Long totalTransactions;
        private BigDecimal transactionSuccessRate;
        private BigDecimal systemUptime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MarketMetrics {
        private BigDecimal marketShare;
        private BigDecimal marketSize;
        private BigDecimal marketPenetration;
        private BigDecimal marketGrowthRate;
        private Integer competitorCount;
        private BigDecimal avgCompetitorMarketShare;
        private BigDecimal relativeMarketPosition;
        private Integer marketRank;
        private BigDecimal brandAwareness;
        private BigDecimal customerReach;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceMetrics {
        private BigDecimal efficiencyScore;
        private BigDecimal productivityScore;
        private BigDecimal qualityScore;
        private BigDecimal innovationScore;
        private BigDecimal agilityScore;
        private BigDecimal overallPerformanceScore;
        private String performanceGrade;
        private Integer percentileRank;
        private BigDecimal yearOverYearImprovement;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetricVariance {
        private String metricName;
        private BigDecimal actualValue;
        private BigDecimal targetValue;
        private BigDecimal variance;
        private BigDecimal variancePercentage;
        private String varianceDirection;
        private Boolean isWithinTolerance;
        private String severity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionalComparison {
        private String compareRegionCode;
        private String compareRegionName;
        private String metricName;
        private BigDecimal thisRegionValue;
        private BigDecimal compareRegionValue;
        private BigDecimal difference;
        private BigDecimal differencePercentage;
        private String comparisonResult;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetricQualityScore {
        private BigDecimal completeness;
        private BigDecimal accuracy;
        private BigDecimal timeliness;
        private BigDecimal consistency;
        private BigDecimal validity;
        private BigDecimal overallScore;
        private String qualityGrade;
    }

    public enum MetricStatus {
        DRAFT,
        PENDING_REVIEW,
        APPROVED,
        PUBLISHED,
        ARCHIVED
    }

    public boolean isPublished() {
        return MetricStatus.PUBLISHED.equals(status);
    }

    public boolean hasHighQualityScore() {
        return qualityScore != null &&
            qualityScore.getOverallScore() != null &&
            qualityScore.getOverallScore().compareTo(new BigDecimal("0.8")) >= 0;
    }

    public BigDecimal calculateGrowthRate(BigDecimal currentValue, BigDecimal previousValue) {
        if (previousValue == null || previousValue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return currentValue.subtract(previousValue)
            .divide(previousValue, 4, BigDecimal.ROUND_HALF_UP)
            .multiply(BigDecimal.valueOf(100));
    }
}
