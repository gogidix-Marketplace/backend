package com.gogidix.sales.dashboard.application.service;

import com.gogidix.sales.dashboard.application.dto.response.DashboardResponseDto;
import com.gogidix.sales.dashboard.domain.model.GlobalSalesDashboard;
import com.gogidix.sales.dashboard.domain.port.in.DashboardQuery;
import com.gogidix.sales.dashboard.domain.repository.GlobalSalesDashboardRepository;
import com.gogidix.sales.dashboard.domain.valueobject.CurrencyConversion;
import com.gogidix.sales.dashboard.shared.exception.NotFoundException;
import com.gogidix.sales.dashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dashboard Query Service
 * Handles all read operations for dashboards
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardQueryService {

    private final GlobalSalesDashboardRepository dashboardRepository;

    public GlobalSalesDashboard getById(String dashboardId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching dashboard: {} for tenant: {}", dashboardId, tenantId);

        return dashboardRepository.findByDashboardIdAndTenantId(dashboardId, tenantId)
                .orElseThrow(() -> new NotFoundException("Dashboard", dashboardId));
    }

    public DashboardResponseDto getDashboardDto(String dashboardId) {
        GlobalSalesDashboard dashboard = getById(dashboardId);
        return toDto(dashboard);
    }

    @Cacheable(value = "dashboards", key = "#dashboardId + '_' + #tenantId")
    public GlobalSalesDashboard getByIdWithCache(String dashboardId, String tenantId) {
        log.debug("Fetching dashboard with cache: {} for tenant: {}", dashboardId, tenantId);

        return dashboardRepository.findByDashboardIdAndTenantId(dashboardId, tenantId)
                .orElseThrow(() -> new NotFoundException("Dashboard", dashboardId));
    }

    public Page<GlobalSalesDashboard> getByType(GlobalSalesDashboard.DashboardType type,
                                                  int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching dashboards by type: {} for tenant: {}", type, tenantId);

        List<GlobalSalesDashboard> dashboards = dashboardRepository.findByTenantIdAndType(tenantId, type);
        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(dashboards, pageRequest, dashboards.size());
    }

    public Page<GlobalSalesDashboard> getByStatus(GlobalSalesDashboard.DashboardStatus status,
                                                    int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching dashboards by status: {} for tenant: {}", status, tenantId);

        List<GlobalSalesDashboard> dashboards = dashboardRepository.findByTenantIdAndStatus(tenantId, status);
        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(dashboards, pageRequest, dashboards.size());
    }

    public List<GlobalSalesDashboard> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching all dashboards for tenant: {}", tenantId);

        return dashboardRepository.findByTenantId(tenantId);
    }

    public Page<DashboardResponseDto> getAllDashboardsDto(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        List<GlobalSalesDashboard> dashboards = dashboardRepository.findByTenantId(tenantId);
        PageRequest pageRequest = PageRequest.of(page, size);

        int start = (int) pageRequest.getOffset();
        int end = Math.min(start + pageRequest.getPageSize(), dashboards.size());

        List<DashboardResponseDto> dtoList = dashboards.subList(start, end).stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageRequest, dashboards.size());
    }

    public GlobalSalesDashboard.GlobalMetrics getGlobalMetrics(String dashboardId, String currency) {
        GlobalSalesDashboard dashboard = getById(dashboardId);

        if (currency != null && !currency.equals(dashboard.getBaseCurrency())) {
            return convertMetricsCurrency(dashboard.getGlobalMetrics(), currency, dashboard);
        }

        return dashboard.getGlobalMetrics();
    }

    public List<GlobalSalesDashboard.RegionalMetric> getRegionalMetrics(String dashboardId,
                                                                          String regionCode,
                                                                          String baseCurrency) {
        GlobalSalesDashboard dashboard = getById(dashboardId);

        if (regionCode != null) {
            GlobalSalesDashboard.RegionalMetric metric = dashboard.getRegionalMetric(regionCode);
            return metric != null ? List.of(metric) : List.of();
        }

        List<GlobalSalesDashboard.RegionalMetric> metrics = dashboard.getRegionalMetrics();

        if (baseCurrency != null && !baseCurrency.equals(dashboard.getBaseCurrency())) {
            CurrencyConversion converter = CurrencyConversion.create(dashboard.getBaseCurrency(), baseCurrency);
            return metrics.stream()
                    .map(m -> convertRegionalMetricCurrency(m, baseCurrency, converter))
                    .collect(Collectors.toList());
        }

        return metrics;
    }

    public List<GlobalSalesDashboard.RegionalMetric> getTopPerformingRegions(String dashboardId,
                                                                                Integer limit,
                                                                                String sortBy) {
        GlobalSalesDashboard dashboard = getById(dashboardId);

        List<GlobalSalesDashboard.RegionalMetric> regions = dashboard.getRegionalMetrics();

        if ("REVENUE".equals(sortBy)) {
            regions = regions.stream()
                    .sorted((r1, r2) -> r2.getRevenue().getAmount().compareTo(r1.getRevenue().getAmount()))
                    .collect(Collectors.toList());
        } else if ("GROWTH_RATE".equals(sortBy)) {
            regions = regions.stream()
                    .sorted((r1, r2) -> r2.getGrowthRate().compareTo(r1.getGrowthRate()))
                    .collect(Collectors.toList());
        } else if ("ACHIEVEMENT".equals(sortBy)) {
            regions = regions.stream()
                    .sorted((r1, r2) -> r2.getAchievementPercentage().compareTo(r1.getAchievementPercentage()))
                    .collect(Collectors.toList());
        }

        int actualLimit = limit != null ? limit : 5;
        return regions.stream()
                .limit(actualLimit)
                .collect(Collectors.toList());
    }

    public List<GlobalSalesDashboard.TimeSeriesData> getTrendData(String dashboardId,
                                                                    LocalDate startDate,
                                                                    LocalDate endDate,
                                                                    String region) {
        GlobalSalesDashboard dashboard = getById(dashboardId);

        return dashboard.getTrendData().stream()
                .filter(d -> startDate == null || !d.getDate().isBefore(startDate))
                .filter(d -> endDate == null || !d.getDate().isAfter(endDate))
                .filter(d -> region == null || region.equals(d.getRegion()))
                .collect(Collectors.toList());
    }

    public GlobalSalesDashboard.ExecutiveSummary getExecutiveSummary(String dashboardId) {
        GlobalSalesDashboard dashboard = getById(dashboardId);
        return dashboard.getExecutiveSummary();
    }

    public List<GlobalSalesDashboard.KPIWidget> getWidgets(String dashboardId) {
        GlobalSalesDashboard dashboard = getById(dashboardId);
        return dashboard.getWidgets();
    }

    public GlobalSalesDashboard getDashboardSummary(String dashboardId) {
        GlobalSalesDashboard dashboard = getById(dashboardId);

        // Create a summary version with minimal data
        GlobalSalesDashboard summary = GlobalSalesDashboard.builder()
                .dashboardId(dashboard.getDashboardId())
                .tenantId(dashboard.getTenantId())
                .name(dashboard.getName())
                .description(dashboard.getDescription())
                .status(dashboard.getStatus())
                .type(dashboard.getType())
                .baseCurrency(dashboard.getBaseCurrency())
                .globalMetrics(dashboard.getGlobalMetrics())
                .lastRefreshAt(dashboard.getLastRefreshAt())
                .build();

        return summary;
    }

    public long countByTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return dashboardRepository.countByTenantId(tenantId);
    }

    public long countByStatus(GlobalSalesDashboard.DashboardStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        return dashboardRepository.countByTenantIdAndStatus(tenantId, status);
    }

    private DashboardResponseDto toDto(GlobalSalesDashboard dashboard) {
        return DashboardResponseDto.builder()
                .id(dashboard.getId())
                .dashboardId(dashboard.getDashboardId())
                .tenantId(dashboard.getTenantId())
                .name(dashboard.getName())
                .description(dashboard.getDescription())
                .status(DashboardResponseDto.DashboardStatusDto.from(dashboard.getStatus()))
                .type(DashboardResponseDto.DashboardTypeDto.from(dashboard.getType()))
                .globalMetrics(toMetricsDto(dashboard.getGlobalMetrics()))
                .regionalMetrics(toRegionalMetricsDto(dashboard.getRegionalMetrics()))
                .widgets(toWidgetsDto(dashboard.getWidgets()))
                .trendData(toTrendDataDto(dashboard.getTrendData()))
                .executiveSummary(toExecutiveSummaryDto(dashboard.getExecutiveSummary()))
                .configuration(toConfigurationDto(dashboard.getConfiguration()))
                .baseCurrency(dashboard.getBaseCurrency())
                .lastRefreshAt(dashboard.getLastRefreshAt())
                .lastRefreshedBy(dashboard.getLastRefreshedBy())
                .dataFreshness(toDataFreshnessDto(dashboard.getDataFreshness()))
                .createdAt(dashboard.getCreatedAt())
                .updatedAt(dashboard.getUpdatedAt())
                .build();
    }

    private DashboardResponseDto.GlobalMetricsDto toMetricsDto(GlobalSalesDashboard.GlobalMetrics metrics) {
        if (metrics == null) {
            return null;
        }
        return DashboardResponseDto.GlobalMetricsDto.builder()
                .totalRevenue(toMoneyDto(metrics.getTotalRevenue()))
                .targetRevenue(toMoneyDto(metrics.getTargetRevenue()))
                .achievementPercentage(toDouble(metrics.getAchievementPercentage()))
                .totalDeals(metrics.getTotalDeals())
                .wonDeals(metrics.getWonDeals())
                .winRate(toDouble(metrics.getWinRate()))
                .averageDealSize(toMoneyDto(metrics.getAverageDealSize()))
                .activeSalesReps(metrics.getActiveSalesReps())
                .weightedPipeline(toMoneyDto(metrics.getWeightedPipeline()))
                .opportunitiesInPipeline(metrics.getOpportunitiesInPipeline())
                .yearOverYearGrowth(toDouble(metrics.getYearOverYearGrowth()))
                .monthOverMonthGrowth(toDouble(metrics.getMonthOverMonthGrowth()))
                .quarterOverQuarterGrowth(toDouble(metrics.getQuarterOverQuarterGrowth()))
                .build();
    }

    private DashboardResponseDto.RegionalMetricDto toRegionalMetricDto(GlobalSalesDashboard.RegionalMetric metric) {
        if (metric == null) {
            return null;
        }
        return DashboardResponseDto.RegionalMetricDto.builder()
                .regionCode(metric.getRegionCode())
                .regionName(metric.getRegionName())
                .revenue(toMoneyDto(metric.getRevenue()))
                .target(toMoneyDto(metric.getTarget()))
                .achievementPercentage(toDouble(metric.getAchievementPercentage()))
                .deals(metric.getDeals())
                .growthRate(toDouble(metric.getGrowthRate()))
                .rank(metric.getRank())
                .currency(metric.getCurrency())
                .build();
    }

    private DashboardResponseDto.KPIWidgetDto toKPIWidgetDto(GlobalSalesDashboard.KPIWidget widget) {
        if (widget == null) {
            return null;
        }
        return DashboardResponseDto.KPIWidgetDto.builder()
                .widgetId(widget.getWidgetId())
                .title(widget.getTitle())
                .description(widget.getDescription())
                .type(DashboardResponseDto.WidgetTypeDto.from(widget.getType()))
                .category(DashboardResponseDto.WidgetCategoryDto.from(widget.getCategory()))
                .value(widget.getValue())
                .row(widget.getPosition())
                .column(widget.getPosition())
                .isActive(widget.getIsVisible())
                .lastUpdated(widget.getLastUpdated())
                .build();
    }

    private DashboardResponseDto.TimeSeriesDataDto toTimeSeriesDataDto(GlobalSalesDashboard.TimeSeriesData data) {
        if (data == null) {
            return null;
        }
        return DashboardResponseDto.TimeSeriesDataDto.builder()
                .period(data.getPeriod())
                .date(data.getDate())
                .revenue(toMoneyDto(data.getRevenue()))
                .deals(data.getDeals())
                .conversionRate(toDouble(data.getConversionRate()))
                .averageDealSize(toMoneyDto(data.getAverageDealSize()))
                .region(data.getRegion())
                .build();
    }

    private DashboardResponseDto.ExecutiveSummaryDto toExecutiveSummaryDto(
            GlobalSalesDashboard.ExecutiveSummary summary) {
        if (summary == null) {
            return null;
        }
        return DashboardResponseDto.ExecutiveSummaryDto.builder()
                .headline(summary.getHeadline())
                .keyHighlight(summary.getKeyHighlight())
                .topPerformers(summary.getTopPerformers())
                .areasForImprovement(summary.getAreasForImprovement())
                .overallSentiment(summary.getOverallSentiment())
                .riskScore(toDouble(summary.getRiskScore()))
                .recommendations(summary.getRecommendations())
                .build();
    }

    private DashboardResponseDto.DashboardConfigurationDto toConfigurationDto(
            GlobalSalesDashboard.DashboardConfiguration config) {
        if (config == null) {
            return null;
        }
        return DashboardResponseDto.DashboardConfigurationDto.builder()
                .refreshIntervalMinutes(config.getRefreshIntervalMinutes())
                .autoRefresh(config.getAutoRefresh())
                .enabledRegions(config.getEnabledRegions())
                .enabledMetrics(config.getEnabledMetrics())
                .dateRange(config.getDateRange())
                .comparisonMode(config.getComparisonMode() != null ?
                        config.getComparisonMode().name() : null)
                .showTargets(config.getShowTargets())
                .showForecasts(config.getShowForecasts())
                .defaultView(config.getDefaultView())
                .build();
    }

    private DashboardResponseDto.DataFreshnessDto toDataFreshnessDto(
            GlobalSalesDashboard.DataFreshness freshness) {
        if (freshness == null) {
            return null;
        }
        return DashboardResponseDto.DataFreshnessDto.builder()
                .lastDataUpdate(freshness.getLastDataUpdate())
                .dataQuality(freshness.getDataQuality())
                .completenessPercentage(freshness.getCompletenessPercentage())
                .missingDataRegions(freshness.getMissingDataRegions())
                .lagMinutes(freshness.getLagMinutes())
                .build();
    }

    private DashboardResponseDto.MoneyDto toMoneyDto(GlobalSalesDashboard.Money money) {
        if (money == null) {
            return null;
        }
        return DashboardResponseDto.MoneyDto.builder()
                .amount(toDouble(money.getAmount()))
                .currency(money.getCurrency())
                .formatted(money.getCurrency() + " " + money.getAmount())
                .build();
    }

    private List<DashboardResponseDto.RegionalMetricDto> toRegionalMetricsDto(
            List<GlobalSalesDashboard.RegionalMetric> metrics) {
        if (metrics == null) {
            return List.of();
        }
        return metrics.stream()
                .map(this::toRegionalMetricDto)
                .collect(Collectors.toList());
    }

    private List<DashboardResponseDto.KPIWidgetDto> toWidgetsDto(List<GlobalSalesDashboard.KPIWidget> widgets) {
        if (widgets == null) {
            return List.of();
        }
        return widgets.stream()
                .map(this::toKPIWidgetDto)
                .collect(Collectors.toList());
    }

    private List<DashboardResponseDto.TimeSeriesDataDto> toTrendDataDto(
            List<GlobalSalesDashboard.TimeSeriesData> trendData) {
        if (trendData == null) {
            return List.of();
        }
        return trendData.stream()
                .map(this::toTimeSeriesDataDto)
                .collect(Collectors.toList());
    }

    private GlobalSalesDashboard.GlobalMetrics convertMetricsCurrency(
            GlobalSalesDashboard.GlobalMetrics metrics, String targetCurrency,
            GlobalSalesDashboard dashboard) {
        CurrencyConversion converter = CurrencyConversion.create(dashboard.getBaseCurrency(), targetCurrency);

        return GlobalSalesDashboard.GlobalMetrics.builder()
                .totalRevenue(convertMoney(metrics.getTotalRevenue(), targetCurrency, converter))
                .targetRevenue(convertMoney(metrics.getTargetRevenue(), targetCurrency, converter))
                .achievementPercentage(metrics.getAchievementPercentage())
                .totalDeals(metrics.getTotalDeals())
                .wonDeals(metrics.getWonDeals())
                .winRate(metrics.getWinRate())
                .averageDealSize(convertMoney(metrics.getAverageDealSize(), targetCurrency, converter))
                .activeSalesReps(metrics.getActiveSalesReps())
                .weightedPipeline(convertMoney(metrics.getWeightedPipeline(), targetCurrency, converter))
                .opportunitiesInPipeline(metrics.getOpportunitiesInPipeline())
                .yearOverYearGrowth(metrics.getYearOverYearGrowth())
                .monthOverMonthGrowth(metrics.getMonthOverMonthGrowth())
                .quarterOverQuarterGrowth(metrics.getQuarterOverQuarterGrowth())
                .build();
    }

    private GlobalSalesDashboard.RegionalMetric convertRegionalMetricCurrency(
            GlobalSalesDashboard.RegionalMetric metric, String targetCurrency,
            CurrencyConversion converter) {
        return GlobalSalesDashboard.RegionalMetric.builder()
                .regionCode(metric.getRegionCode())
                .regionName(metric.getRegionName())
                .revenue(convertMoney(metric.getRevenue(), targetCurrency, converter))
                .target(convertMoney(metric.getTarget(), targetCurrency, converter))
                .achievementPercentage(metric.getAchievementPercentage())
                .deals(metric.getDeals())
                .growthRate(metric.getGrowthRate())
                .rank(metric.getRank())
                .currency(targetCurrency)
                .build();
    }

    private GlobalSalesDashboard.Money convertMoney(GlobalSalesDashboard.Money money,
                                                     String targetCurrency,
                                                     CurrencyConversion converter) {
        if (money == null) {
            return GlobalSalesDashboard.Money.builder()
                    .amount(BigDecimal.ZERO)
                    .currency(targetCurrency)
                    .build();
        }

        CurrencyConversion.Money moneyVo = CurrencyConversion.Money.builder()
                .amount(money.getAmount())
                .currency(money.getCurrency())
                .build();

        CurrencyConversion.Money converted = converter.convert(moneyVo, targetCurrency);

        return GlobalSalesDashboard.Money.builder()
                .amount(converted.getAmount())
                .currency(converted.getCurrency())
                .build();
    }

    private Double toDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : null;
    }
}
