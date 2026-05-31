package com.gogidix.sales.countrydashboard.application.service;

import com.gogidix.sales.countrydashboard.application.dto.response.*;
import com.gogidix.sales.countrydashboard.application.mapper.CountryDashboardMapper;
import com.gogidix.sales.countrydashboard.domain.model.CountrySalesDashboard;
import com.gogidix.sales.countrydashboard.domain.repository.CountrySalesDashboardRepository;
import com.gogidix.sales.countrydashboard.domain.valueobject.Money;
import com.gogidix.sales.countrydashboard.shared.exception.NotFoundException;
import com.gogidix.sales.countrydashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Country Dashboard Query Service
 * Handles all read operations for country sales dashboards
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CountryDashboardQueryService {

    private final CountrySalesDashboardRepository dashboardRepository;
    private final CountryDashboardMapper mapper;

    @Cacheable(value = "countryDashboards", key = "#id")
    public CountrySalesDashboard getDashboardById(String id) {
        log.debug("Fetching dashboard by id: {}", id);
        return dashboardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("CountryDashboard", id));
    }

    @Cacheable(value = "countryDashboards", key = "#dashboardId + ':' + #tenantId")
    public CountrySalesDashboard getDashboard(String dashboardId, String tenantId) {
        log.debug("Fetching dashboard: {} for tenant: {}", dashboardId, tenantId);
        return dashboardRepository.findByDashboardIdAndTenantId(dashboardId, tenantId)
                .orElseThrow(() -> new NotFoundException("CountryDashboard", dashboardId));
    }

    @Cacheable(value = "dashboardByCountry", key = "#countryCode + ':' + #tenantId")
    public CountrySalesDashboard getDashboardByCountry(String countryCode, String tenantId) {
        log.debug("Fetching dashboard for country: {} and tenant: {}", countryCode, tenantId);
        return dashboardRepository.findByCountryCodeAndTenantId(countryCode, tenantId)
                .orElseThrow(() -> new NotFoundException("CountryDashboard for country", countryCode));
    }

    @Cacheable(value = "countryDashboards", key = "'all:' + #tenantId + ':' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public Page<CountryDashboardResponseDto> getAllDashboardsDto(String tenantId, Pageable pageable) {
        log.debug("Fetching all dashboards for tenant: {}", tenantId);
        Page<CountrySalesDashboard> dashboards = dashboardRepository.findByTenantId(tenantId, pageable);
        return new PageImpl<>(
                mapper.toResponseDtoList(dashboards.getContent()),
                pageable,
                dashboards.getTotalElements()
        );
    }

    @Cacheable(value = "countryDashboards", key = "'tenant:' + #tenantId")
    public List<CountrySalesDashboard> getAllDashboards(String tenantId) {
        log.debug("Fetching all dashboards for tenant: {}", tenantId);
        return dashboardRepository.findByTenantId(tenantId);
    }

    @Cacheable(value = "countryDashboards", key = "'status:' + #tenantId + ':' + #status")
    public List<CountryDashboardResponseDto> getDashboardsByStatus(String tenantId,
                                                                    CountrySalesDashboard.DashboardStatus status) {
        log.debug("Fetching dashboards for tenant: {} with status: {}", tenantId, status);
        return mapper.toResponseDtoList(dashboardRepository.findByTenantIdAndStatus(tenantId, status));
    }

    @Cacheable(value = "countryDashboards", key = "'region:' + #tenantId + ':' + #region")
    public List<CountryDashboardResponseDto> getDashboardsByRegion(String tenantId, String region) {
        log.debug("Fetching dashboards for tenant: {} in region: {}", tenantId, region);
        return mapper.toResponseDtoList(dashboardRepository.findByTenantIdAndRegion(tenantId, region));
    }

    @Cacheable(value = "countryDashboards", key = "'countries:' + #tenantId + ':' + #countryCodes.hashCode()")
    public List<CountryDashboardResponseDto> getDashboardsByCountries(String tenantId, List<String> countryCodes) {
        log.debug("Fetching dashboards for tenant: {} for countries: {}", tenantId, countryCodes);
        return mapper.toResponseDtoList(dashboardRepository.findByMultipleCountryCodes(tenantId, countryCodes));
    }

    @Cacheable(value = "dashboardMetrics", key = "#dashboardId")
    public CountrySalesDashboard.CountryMetrics getMetrics(String dashboardId) {
        log.debug("Fetching metrics for dashboard: {}", dashboardId);
        CountrySalesDashboard dashboard = getDashboardById(dashboardId);
        return dashboard.getMetrics();
    }

    @Cacheable(value = "dashboardTerritories", key = "#dashboardId")
    public List<TerritoryBreakdownResponseDto> getTerritories(String dashboardId) {
        log.debug("Fetching territories for dashboard: {}", dashboardId);
        CountrySalesDashboard dashboard = getDashboardById(dashboardId);
        return mapper.toTerritoryResponseDtoList(dashboard.getTerritories());
    }

    @Cacheable(value = "dashboardTerritories", key = "#dashboardId + ':top:' + #limit")
    public List<TerritoryBreakdownResponseDto> getTopTerritories(String dashboardId, int limit) {
        log.debug("Fetching top {} territories for dashboard: {}", limit, dashboardId);
        CountrySalesDashboard dashboard = getDashboardById(dashboardId);
        return mapper.toTerritoryResponseDtoList(dashboard.getTopPerformingTerritories(limit));
    }

    public TerritoryBreakdownResponseDto getTerritory(String dashboardId, String territoryId) {
        log.debug("Fetching territory: {} for dashboard: {}", territoryId, dashboardId);
        CountrySalesDashboard dashboard = getDashboardById(dashboardId);
        CountrySalesDashboard.TerritoryBreakdown territory = dashboard.getTerritory(territoryId);
        if (territory == null) {
            throw new NotFoundException("Territory", territoryId);
        }
        return mapper.toTerritoryResponseDto(territory);
    }

    @Cacheable(value = "dashboardComparison", key = "#dashboardId")
    public ComparisonDataResponseDto getComparisonData(String dashboardId) {
        log.debug("Fetching comparison data for dashboard: {}", dashboardId);
        CountrySalesDashboard dashboard = getDashboardById(dashboardId);
        return mapper.toComparisonResponseDto(dashboard.getComparisonData());
    }

    @Cacheable(value = "dashboardQuota", key = "#dashboardId")
    public CountrySalesDashboard.QuotaInfo getQuotaInfo(String dashboardId) {
        log.debug("Fetching quota info for dashboard: {}", dashboardId);
        CountrySalesDashboard dashboard = getDashboardById(dashboardId);
        return dashboard.getQuotaInfo();
    }

    @Cacheable(value = "dashboardTrends", key = "#dashboardId + ':' + #startDate + ':' + #endDate")
    public List<CountrySalesDashboard.TrendDataPoint> getTrendData(String dashboardId,
                                                                     LocalDate startDate,
                                                                     LocalDate endDate) {
        log.debug("Fetching trend data for dashboard: {} from {} to {}", dashboardId, startDate, endDate);
        CountrySalesDashboard dashboard = getDashboardById(dashboardId);

        if (dashboard.getTrendData() == null || dashboard.getTrendData().isEmpty()) {
            return new ArrayList<>();
        }

        return dashboard.getTrendData().stream()
                .filter(dp -> (startDate == null || !dp.getDate().isBefore(startDate)) &&
                        (endDate == null || !dp.getDate().isAfter(endDate)))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "dashboardTrends", key = "#dashboardId + ':territory:' + #territoryId")
    public List<CountrySalesDashboard.TrendDataPoint> getTrendDataForTerritory(String dashboardId, String territoryId) {
        log.debug("Fetching trend data for dashboard: {} and territory: {}", dashboardId, territoryId);
        CountrySalesDashboard dashboard = getDashboardById(dashboardId);

        if (dashboard.getTrendData() == null || dashboard.getTrendData().isEmpty()) {
            return new ArrayList<>();
        }

        return dashboard.getTrendData().stream()
                .filter(dp -> territoryId.equals(dp.getTerritory()))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "dashboardKPIs", key = "#dashboardId")
    public List<KpiResponseDto> getKPIs(String dashboardId) {
        log.debug("Fetching KPIs for dashboard: {}", dashboardId);
        CountrySalesDashboard dashboard = getDashboardById(dashboardId);
        return mapper.toKpiResponseDtoList(dashboard.getKpis());
    }

    @Cacheable(value = "dashboardKPIs", key = "#dashboardId + ':type:' + #metricType")
    public List<KpiResponseDto> getKPIsByType(String dashboardId, String metricType) {
        log.debug("Fetching KPIs of type {} for dashboard: {}", metricType, dashboardId);
        CountrySalesDashboard dashboard = getDashboardById(dashboardId);

        if (dashboard.getKpis() == null || dashboard.getKpis().isEmpty()) {
            return new ArrayList<>();
        }

        return mapper.toKpiResponseDtoList(dashboard.getKpis().stream()
                .filter(kpi -> kpi.getType() != null && kpi.getType().name().equals(metricType))
                .collect(Collectors.toList()));
    }

    @Cacheable(value = "dashboardMetrics", key = "#dashboardId + ':achievement'")
    public BigDecimal getAchievementPercentage(String dashboardId) {
        log.debug("Fetching achievement percentage for dashboard: {}", dashboardId);
        CountrySalesDashboard dashboard = getDashboardById(dashboardId);
        return dashboard.getMetrics() != null ?
                dashboard.getMetrics().getAchievementPercentage() : BigDecimal.ZERO;
    }

    @Cacheable(value = "dashboardMetrics", key = "#dashboardId + ':yoy'")
    public BigDecimal getYearOverYearGrowth(String dashboardId) {
        log.debug("Fetching YoY growth for dashboard: {}", dashboardId);
        CountrySalesDashboard dashboard = getDashboardById(dashboardId);
        return dashboard.getMetrics() != null ?
                dashboard.getMetrics().getYearOverYearGrowth() : BigDecimal.ZERO;
    }

    @Cacheable(value = "dashboardMetrics", key = "#dashboardId + ':mom'")
    public BigDecimal getMonthOverMonthGrowth(String dashboardId) {
        log.debug("Fetching MoM growth for dashboard: {}", dashboardId);
        CountrySalesDashboard dashboard = getDashboardById(dashboardId);
        return dashboard.getMetrics() != null ?
                dashboard.getMetrics().getMonthOverMonthGrowth() : BigDecimal.ZERO;
    }

    public List<CountrySalesDashboard.CountryMetric> getRegionalComparison(String dashboardId) {
        log.debug("Fetching regional comparison for dashboard: {}", dashboardId);
        CountrySalesDashboard dashboard = getDashboardById(dashboardId);

        if (dashboard.getComparisonData() == null ||
            dashboard.getComparisonData().getRegionalComparison() == null) {
            return new ArrayList<>();
        }

        return dashboard.getComparisonData().getRegionalComparison().getCountryMetrics();
    }

    public Money getConvertedRevenue(String dashboardId, String targetCurrency) {
        log.debug("Fetching converted revenue for dashboard: {} to currency: {}", dashboardId, targetCurrency);
        CountrySalesDashboard dashboard = getDashboardById(dashboardId);

        if (dashboard.getMetrics() == null || dashboard.getMetrics().getTotalRevenue() == null) {
            return Money.zero(targetCurrency);
        }

        return dashboard.convertToBaseCurrency(dashboard.getMetrics().getTotalRevenue());
    }

    public Boolean isQuotaOnTrack(String dashboardId) {
        log.debug("Checking if quota is on track for dashboard: {}", dashboardId);
        CountrySalesDashboard dashboard = getDashboardById(dashboardId);

        if (dashboard.getQuotaInfo() == null || dashboard.getQuotaInfo().getYearToDateAchievement() == null) {
            return false;
        }

        BigDecimal threshold = new BigDecimal("80");
        return dashboard.getQuotaInfo().getYearToDateAchievement().compareTo(threshold) >= 0;
    }

    public List<String> getUnderperformingTerritories(String dashboardId) {
        log.debug("Fetching underperforming territories for dashboard: {}", dashboardId);
        CountrySalesDashboard dashboard = getDashboardById(dashboardId);

        if (dashboard.getTerritories() == null || dashboard.getTerritories().isEmpty()) {
            return new ArrayList<>();
        }

        BigDecimal threshold = new BigDecimal("80");
        return dashboard.getTerritories().stream()
                .filter(t -> t.getAchievementPercentage() != null &&
                        t.getAchievementPercentage().compareTo(threshold) < 0)
                .map(CountrySalesDashboard.TerritoryBreakdown::getTerritoryName)
                .collect(Collectors.toList());
    }

    public List<String> getOverperformingTerritories(String dashboardId) {
        log.debug("Fetching overperforming territories for dashboard: {}", dashboardId);
        CountrySalesDashboard dashboard = getDashboardById(dashboardId);

        if (dashboard.getTerritories() == null || dashboard.getTerritories().isEmpty()) {
            return new ArrayList<>();
        }

        BigDecimal threshold = new BigDecimal("100");
        return dashboard.getTerritories().stream()
                .filter(t -> t.getAchievementPercentage() != null &&
                        t.getAchievementPercentage().compareTo(threshold) >= 0)
                .map(CountrySalesDashboard.TerritoryBreakdown::getTerritoryName)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "dashboardExecutiveSummary", key = "#dashboardId")
    public CountryDashboardResponseDto.ExecutiveSummaryDto getExecutiveSummary(String dashboardId) {
        log.debug("Fetching executive summary for dashboard: {}", dashboardId);
        CountrySalesDashboard dashboard = getDashboardById(dashboardId);
        return mapper.toResponseDto(dashboard).getExecutiveSummary();
    }

    @Cacheable(value = "dashboardSnapshot", key = "#dashboardId")
    public Map<String, Object> getDashboardSnapshot(String dashboardId) {
        log.debug("Fetching dashboard snapshot for: {}", dashboardId);
        CountrySalesDashboard dashboard = getDashboardById(dashboardId);

        Map<String, Object> snapshot = new LinkedHashMap<>();
        snapshot.put("dashboardId", dashboard.getDashboardId());
        snapshot.put("countryCode", dashboard.getCountryCode());
        snapshot.put("countryName", dashboard.getCountryName());
        snapshot.put("region", dashboard.getRegion());
        snapshot.put("status", dashboard.getStatus());
        snapshot.put("localCurrency", dashboard.getLocalCurrency());

        if (dashboard.getMetrics() != null) {
            Map<String, Object> metrics = new LinkedHashMap<>();
            metrics.put("totalRevenue", dashboard.getMetrics().getTotalRevenue());
            metrics.put("achievementPercentage", dashboard.getMetrics().getAchievementPercentage());
            metrics.put("totalDeals", dashboard.getMetrics().getTotalDeals());
            metrics.put("wonDeals", dashboard.getMetrics().getWonDeals());
            metrics.put("winRate", dashboard.getMetrics().getWinRate());
            snapshot.put("metrics", metrics);
        }

        if (dashboard.getQuotaInfo() != null) {
            Map<String, Object> quota = new LinkedHashMap<>();
            quota.put("annualQuota", dashboard.getQuotaInfo().getAnnualQuota());
            quota.put("yearToDateAchievement", dashboard.getQuotaInfo().getYearToDateAchievement());
            snapshot.put("quota", quota);
        }

        snapshot.put("territoriesCount", dashboard.getTerritories() != null ? dashboard.getTerritories().size() : 0);
        snapshot.put("lastDataUpdate", dashboard.getDataFreshness() != null ?
                dashboard.getDataFreshness().getLastDataUpdate() : null);

        return snapshot;
    }
}
