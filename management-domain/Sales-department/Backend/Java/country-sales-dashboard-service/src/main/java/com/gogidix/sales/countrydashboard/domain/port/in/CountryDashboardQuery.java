package com.gogidix.sales.countrydashboard.domain.port.in;

import com.gogidix.sales.countrydashboard.domain.model.CountrySalesDashboard;
import com.gogidix.sales.countrydashboard.domain.valueobject.TimePeriod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Country Dashboard Query (Input Port)
 * Defines query operations for country dashboards
 */
public interface CountryDashboardQuery {

    Optional<CountrySalesDashboard> findById(String id);

    Optional<CountrySalesDashboard> findByDashboardIdAndTenantId(String dashboardId, String tenantId);

    Optional<CountrySalesDashboard> findByCountryCodeAndTenantId(String countryCode, String tenantId);

    List<CountrySalesDashboard> findByTenantId(String tenantId);

    Page<CountrySalesDashboard> findByTenantId(String tenantId, Pageable pageable);

    List<CountrySalesDashboard> findByTenantIdAndStatus(String tenantId, CountrySalesDashboard.DashboardStatus status);

    List<CountrySalesDashboard> findByTenantIdAndRegion(String tenantId, String region);

    List<CountrySalesDashboard> findByMultipleCountryCodes(String tenantId, List<String> countryCodes);

    CountrySalesDashboard.CountryMetrics getMetrics(String dashboardId);

    List<CountrySalesDashboard.TerritoryBreakdown> getTerritories(String dashboardId);

    List<CountrySalesDashboard.TerritoryBreakdown> getTopTerritories(String dashboardId, int limit);

    CountrySalesDashboard.TerritoryBreakdown getTerritory(String dashboardId, String territoryId);

    CountrySalesDashboard.ComparisonData getComparisonData(String dashboardId);

    CountrySalesDashboard.QuotaInfo getQuotaInfo(String dashboardId);

    List<CountrySalesDashboard.TrendDataPoint> getTrendData(String dashboardId, LocalDate startDate, LocalDate endDate);

    List<CountrySalesDashboard.TrendDataPoint> getTrendDataForTerritory(String dashboardId, String territoryId);

    List<CountrySalesDashboard.CountryKPI> getKPIs(String dashboardId);

    List<CountrySalesDashboard.CountryKPI> getKPIsByType(String dashboardId, String metricType);

    BigDecimal getAchievementPercentage(String dashboardId);

    BigDecimal getYearOverYearGrowth(String dashboardId);

    BigDecimal getMonthOverMonthGrowth(String dashboardId);

    List<CountrySalesDashboard.CountryMetric> getRegionalComparison(String dashboardId);

    Money getConvertedRevenue(String dashboardId, String targetCurrency);

    Boolean isQuotaOnTrack(String dashboardId);

    List<String> getUnderperformingTerritories(String dashboardId);

    List<String> getOverperformingTerritories(String dashboardId);

    CountrySalesDashboard.ExecutiveSummary getExecutiveSummary(String dashboardId);

    java.util.Map<String, Object> getDashboardSnapshot(String dashboardId);

    /**
     * Money value object helper
     */
    class Money {
        private final BigDecimal amount;
        private final String currency;

        public Money(BigDecimal amount, String currency) {
            this.amount = amount;
            this.currency = currency;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public String getCurrency() {
            return currency;
        }
    }
}
