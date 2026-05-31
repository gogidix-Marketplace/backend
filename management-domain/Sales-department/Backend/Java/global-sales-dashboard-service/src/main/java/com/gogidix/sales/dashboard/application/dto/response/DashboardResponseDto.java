package com.gogidix.sales.dashboard.application.dto.response;

import com.gogidix.sales.dashboard.domain.model.GlobalSalesDashboard;
import com.gogidix.sales.dashboard.domain.model.KPIWidget;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Dashboard Response DTO
 * Used for sending dashboard data to clients
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponseDto {

    private String id;
    private String dashboardId;
    private String tenantId;
    private String name;
    private String description;
    private DashboardStatusDto status;
    private DashboardTypeDto type;
    private GlobalMetricsDto globalMetrics;
    private List<RegionalMetricDto> regionalMetrics;
    private List<KPIWidgetDto> widgets;
    private List<TimeSeriesDataDto> trendData;
    private ExecutiveSummaryDto executiveSummary;
    private DashboardConfigurationDto configuration;
    private String baseCurrency;
    private Instant lastRefreshAt;
    private String lastRefreshedBy;
    private DataFreshnessDto dataFreshness;
    private List<DrillDownConfigDto> drillDownConfigs;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardStatusDto {
        private String name;
        private String value;

        public static DashboardStatusDto from(GlobalSalesDashboard.DashboardStatus status) {
            return DashboardStatusDto.builder()
                    .name(status.name())
                    .value(status.toString())
                    .build();
        }

        public GlobalSalesDashboard.DashboardStatus toDomain() {
            return GlobalSalesDashboard.DashboardStatus.valueOf(this.name);
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardTypeDto {
        private String name;
        private String value;

        public static DashboardTypeDto from(GlobalSalesDashboard.DashboardType type) {
            return DashboardTypeDto.builder()
                    .name(type.name())
                    .value(type.toString())
                    .build();
        }

        public GlobalSalesDashboard.DashboardType toDomain() {
            return GlobalSalesDashboard.DashboardType.valueOf(this.name);
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GlobalMetricsDto {
        private MoneyDto totalRevenue;
        private MoneyDto targetRevenue;
        private Double achievementPercentage;
        private Integer totalDeals;
        private Integer wonDeals;
        private Double winRate;
        private MoneyDto averageDealSize;
        private Integer activeSalesReps;
        private MoneyDto weightedPipeline;
        private Integer opportunitiesInPipeline;
        private Double yearOverYearGrowth;
        private Double monthOverMonthGrowth;
        private Double quarterOverQuarterGrowth;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionalMetricDto {
        private String regionCode;
        private String regionName;
        private MoneyDto revenue;
        private MoneyDto target;
        private Double achievementPercentage;
        private Integer deals;
        private Double growthRate;
        private Integer rank;
        private String currency;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KPIWidgetDto {
        private String widgetId;
        private String title;
        private String description;
        private WidgetTypeDto type;
        private WidgetCategoryDto category;
        private Object value;
        private String displayValue;
        private TrendInfoDto trendInfo;
        private TargetInfoDto targetInfo;
        private Integer row;
        private Integer column;
        private Boolean isActive;
        private Instant lastUpdated;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WidgetTypeDto {
        private String name;

        public static WidgetTypeDto from(KPIWidget.WidgetType type) {
            return WidgetTypeDto.builder().name(type.name()).build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WidgetCategoryDto {
        private String name;

        public static WidgetCategoryDto from(KPIWidget.WidgetCategory category) {
            return WidgetCategoryDto.builder().name(category.name()).build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrendInfoDto {
        private String direction;
        private Double value;
        private String percentage;
        private Boolean isPositive;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TargetInfoDto {
        private Double target;
        private Double actual;
        private Double achievement;
        private Double remaining;
        private Boolean isOnTrack;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeSeriesDataDto {
        private String period;
        private LocalDate date;
        private MoneyDto revenue;
        private Integer deals;
        private Double conversionRate;
        private MoneyDto averageDealSize;
        private String region;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExecutiveSummaryDto {
        private String headline;
        private String keyHighlight;
        private List<String> topPerformers;
        private List<String> areasForImprovement;
        private String overallSentiment;
        private Double riskScore;
        private List<String> recommendations;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardConfigurationDto {
        private Integer refreshIntervalMinutes;
        private Boolean autoRefresh;
        private List<String> enabledRegions;
        private List<String> enabledMetrics;
        private String dateRange;
        private String comparisonMode;
        private Boolean showTargets;
        private Boolean showForecasts;
        private String defaultView;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataFreshnessDto {
        private Instant lastDataUpdate;
        private String dataQuality;
        private Integer completenessPercentage;
        private List<String> missingDataRegions;
        private Integer lagMinutes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DrillDownConfigDto {
        private String id;
        private String name;
        private String targetDashboardId;
        private List<String> filters;
        private String type;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MoneyDto {
        private Double amount;
        private String currency;
        private String formatted;
    }
}
