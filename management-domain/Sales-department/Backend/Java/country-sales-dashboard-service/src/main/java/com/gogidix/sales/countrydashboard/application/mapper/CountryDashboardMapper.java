package com.gogidix.sales.countrydashboard.application.mapper;

import com.gogidix.sales.countrydashboard.application.dto.response.*;
import com.gogidix.sales.countrydashboard.domain.model.CountrySalesDashboard;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Country Dashboard Mapper
 * Maps between domain entities and DTOs
 */
@Component
public class CountryDashboardMapper {

    public CountryDashboardResponseDto toResponseDto(CountrySalesDashboard dashboard) {
        if (dashboard == null) {
            return null;
        }

        return CountryDashboardResponseDto.builder()
                .dashboardId(dashboard.getDashboardId())
                .tenantId(dashboard.getTenantId())
                .countryCode(dashboard.getCountryCode())
                .countryName(dashboard.getCountryName())
                .region(dashboard.getRegion())
                .status(dashboard.getStatus() != null ? dashboard.getStatus().name() : null)
                .type(dashboard.getType() != null ? dashboard.getType().name() : null)
                .localCurrency(dashboard.getLocalCurrency())
                .baseCurrency(dashboard.getBaseCurrency())
                .metrics(toMetricsDto(dashboard))
                .territoriesCount(dashboard.getTerritories() != null ? dashboard.getTerritories().size() : 0)
                .kpisCount(dashboard.getKpis() != null ? dashboard.getKpis().size() : 0)
                .comparisonSummary(toComparisonSummaryDto(dashboard))
                .quotaSummary(toQuotaSummaryDto(dashboard))
                .executiveSummary(toExecutiveSummaryDto(dashboard))
                .dataFreshness(toDataFreshnessDto(dashboard))
                .createdAt(dashboard.getCreatedAt())
                .updatedAt(dashboard.getUpdatedAt())
                .lastRefreshAt(dashboard.getDataFreshness() != null ?
                        dashboard.getDataFreshness().getLastDataUpdate() : null)
                .build();
    }

    public List<CountryDashboardResponseDto> toResponseDtoList(List<CountrySalesDashboard> dashboards) {
        if (dashboards == null) {
            return List.of();
        }
        return dashboards.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public TerritoryBreakdownResponseDto toTerritoryResponseDto(CountrySalesDashboard.TerritoryBreakdown territory) {
        if (territory == null) {
            return null;
        }

        return TerritoryBreakdownResponseDto.builder()
                .territoryId(territory.getTerritoryId())
                .territoryName(territory.getTerritoryName())
                .territoryCode(territory.getTerritoryCode())
                .revenue(territory.getRevenue() != null ? territory.getRevenue().getAmount() : null)
                .currency(territory.getRevenue() != null ? territory.getRevenue().getCurrency() : null)
                .quota(territory.getQuota() != null ? territory.getQuota().getAmount() : null)
                .achievementPercentage(territory.getAchievementPercentage())
                .deals(territory.getDeals())
                .wonDeals(territory.getWonDeals())
                .winRate(territory.getWinRate())
                .averageDealSize(territory.getAverageDealSize() != null ?
                        territory.getAverageDealSize().getAmount() : null)
                .rank(territory.getRank())
                .previousRank(territory.getPreviousRank())
                .growthRate(territory.getGrowthRate())
                .topPerformer(territory.getTopPerformer())
                .performanceIndicator(determinePerformanceIndicator(territory))
                .lastUpdated(territory.getLastUpdated())
                .attributes(territory.getAttributes())
                .build();
    }

    public List<TerritoryBreakdownResponseDto> toTerritoryResponseDtoList(
            List<CountrySalesDashboard.TerritoryBreakdown> territories) {
        if (territories == null) {
            return List.of();
        }
        return territories.stream()
                .map(this::toTerritoryResponseDto)
                .collect(Collectors.toList());
    }

    public ComparisonDataResponseDto toComparisonResponseDto(CountrySalesDashboard.ComparisonData comparison) {
        if (comparison == null) {
            return null;
        }

        return ComparisonDataResponseDto.builder()
                .yearOverYear(toYearOverYearDto(comparison.getYearOverYear()))
                .monthOverMonth(toMonthOverMonthDto(comparison.getMonthOverMonth()))
                .quarterOverQuarter(toQuarterOverQuarterDto(comparison.getQuarterOverQuarter()))
                .regionalComparison(toRegionalComparisonDto(comparison.getRegionalComparison()))
                .build();
    }

    public KpiResponseDto toKpiResponseDto(CountrySalesDashboard.CountryKPI kpi) {
        if (kpi == null) {
            return null;
        }

        return KpiResponseDto.builder()
                .kpiId(kpi.getKpiId())
                .name(kpi.getName())
                .type(kpi.getType())
                .value(kpi.getValue())
                .monetaryValue(kpi.getMonetaryValue() != null ? kpi.getMonetaryValue().getAmount() : null)
                .currency(kpi.getMonetaryValue() != null ? kpi.getMonetaryValue().getCurrency() : null)
                .percentageValue(kpi.getPercentageValue())
                .target(kpi.getTarget())
                .achievement(kpi.getAchievement())
                .trend(kpi.getTrend())
                .trendValue(kpi.getTrendValue())
                .weight(kpi.getWeight())
                .isCritical(kpi.getIsCritical())
                .lastUpdated(kpi.getLastUpdated())
                .build();
    }

    public List<KpiResponseDto> toKpiResponseDtoList(List<CountrySalesDashboard.CountryKPI> kpis) {
        if (kpis == null) {
            return List.of();
        }
        return kpis.stream()
                .map(this::toKpiResponseDto)
                .collect(Collectors.toList());
    }

    // Private helper methods

    private CountryDashboardResponseDto.MetricsDto toMetricsDto(CountrySalesDashboard dashboard) {
        if (dashboard.getMetrics() == null) {
            return null;
        }

        var metrics = dashboard.getMetrics();
        return CountryDashboardResponseDto.MetricsDto.builder()
                .totalRevenue(metrics.getTotalRevenue() != null ? metrics.getTotalRevenue().getAmount() : null)
                .currency(metrics.getTotalRevenue() != null ? metrics.getTotalRevenue().getCurrency() : null)
                .targetRevenue(metrics.getTargetRevenue() != null ? metrics.getTargetRevenue().getAmount() : null)
                .achievementPercentage(metrics.getAchievementPercentage())
                .totalDeals(metrics.getTotalDeals())
                .wonDeals(metrics.getWonDeals())
                .lostDeals(metrics.getLostDeals())
                .winRate(metrics.getWinRate())
                .averageDealSize(metrics.getAverageDealSize() != null ? metrics.getAverageDealSize().getAmount() : null)
                .weightedPipeline(metrics.getWeightedPipeline() != null ? metrics.getWeightedPipeline().getAmount() : null)
                .opportunitiesInPipeline(metrics.getOpportunitiesInPipeline())
                .newCustomers(metrics.getNewCustomers())
                .churnedCustomers(metrics.getChurnedCustomers())
                .retentionRate(metrics.getRetentionRate())
                .npsScore(metrics.getNpsScore())
                .yearOverYearGrowth(metrics.getYearOverYearGrowth())
                .monthOverMonthGrowth(metrics.getMonthOverMonthGrowth())
                .quarterOverQuarterGrowth(metrics.getQuarterOverQuarterGrowth())
                .activeSalesReps(metrics.getActiveSalesReps())
                .revenuePerRep(metrics.getRevenuePerRep() != null ? metrics.getRevenuePerRep().getAmount() : null)
                .build();
    }

    private CountryDashboardResponseDto.ComparisonSummaryDto toComparisonSummaryDto(CountrySalesDashboard dashboard) {
        if (dashboard.getMetrics() == null) {
            return null;
        }

        var metrics = dashboard.getMetrics();
        String trend = determineTrend(metrics.getMonthOverMonthGrowth());

        return CountryDashboardResponseDto.ComparisonSummaryDto.builder()
                .yearOverYearGrowth(metrics.getYearOverYearGrowth())
                .monthOverMonthGrowth(metrics.getMonthOverMonthGrowth())
                .quarterOverQuarterGrowth(metrics.getQuarterOverQuarterGrowth())
                .trend(trend)
                .build();
    }

    private CountryDashboardResponseDto.QuotaSummaryDto toQuotaSummaryDto(CountrySalesDashboard dashboard) {
        if (dashboard.getQuotaInfo() == null) {
            return null;
        }

        var quota = dashboard.getQuotaInfo();
        boolean onTrack = quota.getYearToDateAchievement() != null &&
                quota.getYearToDateAchievement().compareTo(new java.math.BigDecimal("80")) >= 0;

        return CountryDashboardResponseDto.QuotaSummaryDto.builder()
                .annualQuota(quota.getAnnualQuota() != null ? quota.getAnnualQuota().getAmount() : null)
                .currency(quota.getAnnualQuota() != null ? quota.getAnnualQuota().getCurrency() : null)
                .yearToDateAchievement(quota.getYearToDateAchievement())
                .remainingPercentage(quota.getRemainingPercentage())
                .remainingAmount(quota.getRemainingAmount() != null ? quota.getRemainingAmount().getAmount() : null)
                .onTrack(onTrack)
                .build();
    }

    private CountryDashboardResponseDto.ExecutiveSummaryDto toExecutiveSummaryDto(CountrySalesDashboard dashboard) {
        if (dashboard.getExecutiveSummary() == null) {
            return null;
        }

        var summary = dashboard.getExecutiveSummary();
        return CountryDashboardResponseDto.ExecutiveSummaryDto.builder()
                .headline(summary.getHeadline())
                .keyHighlight(summary.getKeyHighlight())
                .topPerformingTerritories(summary.getTopPerformingTerritories())
                .underperformingTerritories(summary.getUnderperformingTerritories())
                .overallSentiment(summary.getOverallSentiment())
                .riskScore(summary.getRiskScore())
                .primaryFocusArea(summary.getPrimaryFocusArea())
                .build();
    }

    private CountryDashboardResponseDto.DataFreshnessDto toDataFreshnessDto(CountrySalesDashboard dashboard) {
        if (dashboard.getDataFreshness() == null) {
            return null;
        }

        var freshness = dashboard.getDataFreshness();
        return CountryDashboardResponseDto.DataFreshnessDto.builder()
                .lastDataUpdate(freshness.getLastDataUpdate())
                .dataQuality(freshness.getDataQuality())
                .completenessPercentage(freshness.getCompletenessPercentage())
                .lagMinutes(freshness.getLagMinutes())
                .build();
    }

    private ComparisonDataResponseDto.YearOverYearDto toYearOverYearDto(
            CountrySalesDashboard.YearOverYearComparison yoy) {
        if (yoy == null) {
            return null;
        }

        return ComparisonDataResponseDto.YearOverYearDto.builder()
                .currentYearRevenue(yoy.getCurrentYearRevenue() != null ? yoy.getCurrentYearRevenue().getAmount() : null)
                .previousYearRevenue(yoy.getPreviousYearRevenue() != null ? yoy.getPreviousYearRevenue().getAmount() : null)
                .growthPercentage(yoy.getGrowthPercentage())
                .variance(yoy.getVariance() != null ? yoy.getVariance().getAmount() : null)
                .currentYearDeals(yoy.getCurrentYearDeals())
                .previousYearDeals(yoy.getPreviousYearDeals())
                .dealsGrowthPercentage(yoy.getDealsGrowthPercentage())
                .comparisonDate(yoy.getComparisonDate())
                .build();
    }

    private ComparisonDataResponseDto.MonthOverMonthDto toMonthOverMonthDto(
            CountrySalesDashboard.MonthOverMonthComparison mom) {
        if (mom == null) {
            return null;
        }

        return ComparisonDataResponseDto.MonthOverMonthDto.builder()
                .currentMonthRevenue(mom.getCurrentMonthRevenue() != null ? mom.getCurrentMonthRevenue().getAmount() : null)
                .previousMonthRevenue(mom.getPreviousMonthRevenue() != null ? mom.getPreviousMonthRevenue().getAmount() : null)
                .growthPercentage(mom.getGrowthPercentage())
                .variance(mom.getVariance() != null ? mom.getVariance().getAmount() : null)
                .currentMonthDeals(mom.getCurrentMonthDeals())
                .previousMonthDeals(mom.getPreviousMonthDeals())
                .dealsGrowthPercentage(mom.getDealsGrowthPercentage())
                .comparisonDate(mom.getComparisonDate())
                .build();
    }

    private ComparisonDataResponseDto.QuarterOverQuarterDto toQuarterOverQuarterDto(
            CountrySalesDashboard.QuarterOverQuarterComparison qoq) {
        if (qoq == null) {
            return null;
        }

        return ComparisonDataResponseDto.QuarterOverQuarterDto.builder()
                .currentQuarterRevenue(qoq.getCurrentQuarterRevenue() != null ? qoq.getCurrentQuarterRevenue().getAmount() : null)
                .previousQuarterRevenue(qoq.getPreviousQuarterRevenue() != null ? qoq.getPreviousQuarterRevenue().getAmount() : null)
                .growthPercentage(qoq.getGrowthPercentage())
                .variance(qoq.getVariance() != null ? qoq.getVariance().getAmount() : null)
                .currentQuarterDeals(qoq.getCurrentQuarterDeals())
                .previousQuarterDeals(qoq.getPreviousQuarterDeals())
                .dealsGrowthPercentage(qoq.getDealsGrowthPercentage())
                .comparisonDate(qoq.getComparisonDate())
                .build();
    }

    private ComparisonDataResponseDto.RegionalComparisonDto toRegionalComparisonDto(
            CountrySalesDashboard.RegionalComparison regional) {
        if (regional == null) {
            return null;
        }

        List<ComparisonDataResponseDto.CountryMetricDto> countryMetrics = null;
        if (regional.getCountryMetrics() != null) {
            countryMetrics = regional.getCountryMetrics().stream()
                    .map(cm -> ComparisonDataResponseDto.CountryMetricDto.builder()
                            .countryCode(cm.getCountryCode())
                            .countryName(cm.getCountryName())
                            .revenue(cm.getRevenue() != null ? cm.getRevenue().getAmount() : null)
                            .achievement(cm.getAchievement())
                            .growth(cm.getGrowth())
                            .rank(cm.getRank())
                            .build())
                    .collect(Collectors.toList());
        }

        return ComparisonDataResponseDto.RegionalComparisonDto.builder()
                .region(regional.getRegion())
                .countryMetrics(countryMetrics)
                .regionalAverage(regional.getRegionalAverage() != null ? regional.getRegionalAverage().getAmount() : null)
                .rank(regional.getRank())
                .percentile(regional.getPercentile())
                .build();
    }

    private String determineTrend(java.math.BigDecimal growth) {
        if (growth == null) {
            return "NEUTRAL";
        }
        if (growth.compareTo(java.math.BigDecimal.ZERO) > 0) {
            return "UP";
        } else if (growth.compareTo(java.math.BigDecimal.ZERO) < 0) {
            return "DOWN";
        }
        return "NEUTRAL";
    }

    private String determinePerformanceIndicator(CountrySalesDashboard.TerritoryBreakdown territory) {
        if (territory.getAchievementPercentage() == null) {
            return "UNKNOWN";
        }

        var achievement = territory.getAchievementPercentage();
        if (achievement.compareTo(new java.math.BigDecimal("100")) >= 0) {
            return "EXCEEDING";
        } else if (achievement.compareTo(new java.math.BigDecimal("80")) >= 0) {
            return "ON_TRACK";
        } else if (achievement.compareTo(new java.math.BigDecimal("50")) >= 0) {
            return "BELOW_TARGET";
        } else {
            return "CRITICAL";
        }
    }
}
