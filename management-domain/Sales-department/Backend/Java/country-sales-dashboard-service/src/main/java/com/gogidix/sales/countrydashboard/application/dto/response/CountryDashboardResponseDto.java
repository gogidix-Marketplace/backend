package com.gogidix.sales.countrydashboard.application.dto.response;

import com.gogidix.sales.countrydashboard.domain.model.CountrySalesDashboard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Country Dashboard Response DTO
 * Used for API responses with country dashboard data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryDashboardResponseDto {

    private String dashboardId;
    private String tenantId;
    private String countryCode;
    private String countryName;
    private String region;
    private String status;
    private String type;
    private String localCurrency;
    private String baseCurrency;

    // Metrics
    private MetricsDto metrics;

    // Territories count
    private Integer territoriesCount;

    // KPIs count
    private Integer kpisCount;

    // Comparison summary
    private ComparisonSummaryDto comparisonSummary;

    // Quota summary
    private QuotaSummaryDto quotaSummary;

    // Executive summary
    private ExecutiveSummaryDto executiveSummary;

    // Data freshness
    private DataFreshnessDto dataFreshness;

    // Timestamps
    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastRefreshAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetricsDto {
        private BigDecimal totalRevenue;
        private String currency;
        private BigDecimal targetRevenue;
        private BigDecimal achievementPercentage;
        private Integer totalDeals;
        private Integer wonDeals;
        private Integer lostDeals;
        private BigDecimal winRate;
        private BigDecimal averageDealSize;
        private BigDecimal weightedPipeline;
        private Integer opportunitiesInPipeline;
        private Integer newCustomers;
        private Integer churnedCustomers;
        private BigDecimal retentionRate;
        private BigDecimal npsScore;
        private BigDecimal yearOverYearGrowth;
        private BigDecimal monthOverMonthGrowth;
        private BigDecimal quarterOverQuarterGrowth;
        private Integer activeSalesReps;
        private BigDecimal revenuePerRep;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComparisonSummaryDto {
        private BigDecimal yearOverYearGrowth;
        private BigDecimal monthOverMonthGrowth;
        private BigDecimal quarterOverQuarterGrowth;
        private String trend;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuotaSummaryDto {
        private BigDecimal annualQuota;
        private String currency;
        private BigDecimal yearToDateAchievement;
        private BigDecimal remainingPercentage;
        private BigDecimal remainingAmount;
        private Boolean onTrack;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExecutiveSummaryDto {
        private String headline;
        private String keyHighlight;
        private List<String> topPerformingTerritories;
        private List<String> underperformingTerritories;
        private String overallSentiment;
        private BigDecimal riskScore;
        private String primaryFocusArea;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataFreshnessDto {
        private Instant lastDataUpdate;
        private String dataQuality;
        private Integer completenessPercentage;
        private Integer lagMinutes;
    }
}
