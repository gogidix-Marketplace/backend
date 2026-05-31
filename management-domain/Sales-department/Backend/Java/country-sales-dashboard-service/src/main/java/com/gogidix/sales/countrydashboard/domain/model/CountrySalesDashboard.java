package com.gogidix.sales.countrydashboard.domain.model;

import com.gogidix.sales.countrydashboard.domain.event.*;
import com.gogidix.sales.countrydashboard.domain.valueobject.CountryCode;
import com.gogidix.sales.countrydashboard.domain.valueobject.MetricType;
import com.gogidix.sales.countrydashboard.domain.valueobject.Money;
import com.gogidix.sales.countrydashboard.domain.valueobject.TimePeriod;
import com.gogidix.sales.countrydashboard.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

/**
 * Country Sales Dashboard Domain Entity
 * Multi-tenant country-specific sales dashboard with territory breakdown
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "country_sales_dashboards")
public class CountrySalesDashboard extends BaseEntity {

    private String dashboardId;
    private String tenantId;
    private String countryCode;
    private String countryName;
    private String region;
    private DashboardStatus status;
    private DashboardType type;

    // Country Metrics
    private CountryMetrics metrics;

    // Territory Breakdown
    private List<TerritoryBreakdown> territories;

    // KPIs and Targets
    private List<CountryKPI> kpis;

    // Comparison Data (YoY, MoM, QoQ)
    private ComparisonData comparisonData;

    // Currency Settings
    private String localCurrency;
    private String baseCurrency;
    private Map<String, BigDecimal> exchangeRates;

    // Quota Management
    private QuotaInfo quotaInfo;

    // Time Series Trend Data
    private List<TrendDataPoint> trendData;

    // Dashboard Configuration
    private DashboardConfiguration configuration;

    // Executive Summary
    private ExecutiveSummary executiveSummary;

    // Data Freshness
    private DataFreshness dataFreshness;

    // Domain Events
    @Builder.Default
    private List<Object> domainEvents = new ArrayList<>();

    public enum DashboardStatus {
        ACTIVE, ARCHIVED, DRAFT, PUBLISHED
    }

    public enum DashboardType {
        EXECUTIVE, OPERATIONAL, TERRITORIAL, COMPARATIVE
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CountryMetrics {
        private Money totalRevenue;
        private Money targetRevenue;
        private BigDecimal achievementPercentage;
        private Integer totalDeals;
        private Integer wonDeals;
        private Integer lostDeals;
        private BigDecimal winRate;
        private Money averageDealSize;
        private Money weightedPipeline;
        private Integer opportunitiesInPipeline;
        private Integer newCustomers;
        private Integer churnedCustomers;
        private BigDecimal retentionRate;
        private BigDecimal npsScore;
        private BigDecimal yearOverYearGrowth;
        private BigDecimal monthOverMonthGrowth;
        private BigDecimal quarterOverQuarterGrowth;
        private Integer activeSalesReps;
        private Money revenuePerRep;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TerritoryBreakdown {
        private String territoryId;
        private String territoryName;
        private String territoryCode;
        private Money revenue;
        private Money quota;
        private BigDecimal achievementPercentage;
        private Integer deals;
        private Integer wonDeals;
        private BigDecimal winRate;
        private Money averageDealSize;
        private Integer rank;
        private Integer previousRank;
        private BigDecimal growthRate;
        private String topPerformer;
        private Instant lastUpdated;
        private Map<String, Object> attributes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CountryKPI {
        private String kpiId;
        private String name;
        private MetricType type;
        private Object value;
        private Money monetaryValue;
        private BigDecimal percentageValue;
        private String target;
        private BigDecimal achievement;
        private String trend;
        private BigDecimal trendValue;
        private Integer weight;
        private Boolean isCritical;
        private Instant lastUpdated;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComparisonData {
        private YearOverYearComparison yearOverYear;
        private MonthOverMonthComparison monthOverMonth;
        private QuarterOverQuarterComparison quarterOverQuarter;
        private RegionalComparison regionalComparison;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class YearOverYearComparison {
        private Money currentYearRevenue;
        private Money previousYearRevenue;
        private BigDecimal growthPercentage;
        private Money variance;
        private Integer currentYearDeals;
        private Integer previousYearDeals;
        private BigDecimal dealsGrowthPercentage;
        private Instant comparisonDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthOverMonthComparison {
        private Money currentMonthRevenue;
        private Money previousMonthRevenue;
        private BigDecimal growthPercentage;
        private Money variance;
        private Integer currentMonthDeals;
        private Integer previousMonthDeals;
        private BigDecimal dealsGrowthPercentage;
        private Instant comparisonDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuarterOverQuarterComparison {
        private Money currentQuarterRevenue;
        private Money previousQuarterRevenue;
        private BigDecimal growthPercentage;
        private Money variance;
        private Integer currentQuarterDeals;
        private Integer previousQuarterDeals;
        private BigDecimal dealsGrowthPercentage;
        private Instant comparisonDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionalComparison {
        private String region;
        private List<CountryMetric> countryMetrics;
        private Money regionalAverage;
        private String rank;
        private BigDecimal percentile;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CountryMetric {
        private String countryCode;
        private String countryName;
        private Money revenue;
        private BigDecimal achievement;
        private BigDecimal growth;
        private Integer rank;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuotaInfo {
        private Money annualQuota;
        private Money quarterlyQuota;
        private Money monthlyQuota;
        private Money yearToDateRevenue;
        private BigDecimal yearToDateAchievement;
        private BigDecimal remainingPercentage;
        private Money remainingAmount;
        private Money projectedRevenue;
        private BigDecimal forecastAccuracy;
        private Instant lastUpdated;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrendDataPoint {
        private String period;
        private LocalDate date;
        private Money revenue;
        private Integer deals;
        private BigDecimal winRate;
        private Money averageDealSize;
        private Integer newCustomers;
        private BigDecimal growthRate;
        private String territory;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardConfiguration {
        private Integer refreshIntervalMinutes;
        private Boolean autoRefresh;
        private List<String> enabledTerritories;
        private List<MetricType> enabledMetrics;
        private TimePeriod.PeriodType defaultPeriod;
        private ComparisonMode comparisonMode;
        private Boolean showTargets;
        private Boolean showForecasts;
        private String defaultView;
        private Integer maxTerritoryRankings;
    }

    public enum ComparisonMode {
        YEAR_OVER_YEAR, MONTH_OVER_MONTH, QUARTER_OVER_QUARTER, NONE
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExecutiveSummary {
        private String headline;
        private String keyHighlight;
        private List<String> topPerformingTerritories;
        private List<String> underperformingTerritories;
        private String overallSentiment;
        private BigDecimal riskScore;
        private List<String> recommendations;
        private String primaryFocusArea;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataFreshness {
        private Instant lastDataUpdate;
        private String dataQuality;
        private Integer completenessPercentage;
        private List<String> missingDataTerritories;
        private Integer lagMinutes;
        private Instant lastCurrencyUpdate;
    }

    // Factory Methods
    public static CountrySalesDashboard create(String tenantId, String countryCode, String countryName,
                                               DashboardType type, String localCurrency) {
        CountryCode.validate(countryCode);
        String baseCurrency = "USD";

        CountrySalesDashboard dashboard = CountrySalesDashboard.builder()
                .dashboardId(UUID.randomUUID().toString())
                .tenantId(tenantId)
                .countryCode(countryCode.toUpperCase())
                .countryName(countryName)
                .region(CountryCode.getRegionForCountry(countryCode))
                .status(DashboardStatus.DRAFT)
                .type(type)
                .localCurrency(localCurrency)
                .baseCurrency(baseCurrency)
                .metrics(createEmptyMetrics(localCurrency))
                .territories(new ArrayList<>())
                .kpis(new ArrayList<>())
                .comparisonData(createEmptyComparisonData())
                .exchangeRates(new HashMap<>())
                .quotaInfo(createEmptyQuotaInfo(localCurrency))
                .trendData(new ArrayList<>())
                .configuration(createDefaultConfiguration())
                .executiveSummary(createEmptyExecutiveSummary())
                .build();

        dashboard.addDomainEvent(CountryDashboardCreatedEvent.builder()
                .dashboardId(dashboard.getDashboardId())
                .tenantId(tenantId)
                .countryCode(dashboard.getCountryCode())
                .countryName(countryName)
                .baseCurrency(baseCurrency)
                .createdAt(Instant.now())
                .build());

        return dashboard;
    }

    // Domain Logic
    public void updateMetrics(CountryMetrics metrics) {
        CountryMetricUpdatedEvent.Builder eventBuilder = CountryMetricUpdatedEvent.builder()
                .dashboardId(this.dashboardId)
                .tenantId(this.tenantId)
                .countryCode(this.countryCode)
                .updatedAt(Instant.now());

        if (this.metrics != null && this.metrics.totalRevenue != null && metrics.totalRevenue != null) {
            eventBuilder.previousValue(this.metrics.totalRevenue.getAmount());
            eventBuilder.currentValue(metrics.totalRevenue.getAmount());
            eventBuilder.metricType("TOTAL_REVENUE");
        }

        this.metrics = metrics;
        this.calculateAchievementPercentage();

        CountryMetricUpdatedEvent event = eventBuilder.build();
        if (event.getCurrentValue() != null && event.getPreviousValue() != null) {
            BigDecimal change = event.getCurrentValue().subtract(event.getPreviousValue());
            if (event.getPreviousValue().compareTo(BigDecimal.ZERO) > 0) {
                event.setChangePercentage(change.divide(event.getPreviousValue(), 4, BigDecimal.ROUND_HALF_UP)
                        .multiply(new BigDecimal("100")));
            }
        }
        addDomainEvent(event);
    }

    public void updateTerritoryMetric(TerritoryBreakdown territory) {
        if (this.territories == null) {
            this.territories = new ArrayList<>();
        }

        this.territories.removeIf(t -> t.getTerritoryId().equals(territory.getTerritoryId()));
        territory.setLastUpdated(Instant.now());
        this.territories.add(territory);
        this.rankTerritories();
        this.recalculateMetricsFromTerritories();

        addDomainEvent(TerritoryPerformanceUpdatedEvent.builder()
                .dashboardId(this.dashboardId)
                .tenantId(this.tenantId)
                .countryCode(this.countryCode)
                .territoryId(territory.getTerritoryId())
                .territoryName(territory.getTerritoryName())
                .revenue(territory.getRevenue() != null ? territory.getRevenue().getAmount() : BigDecimal.ZERO)
                .quota(territory.getQuota() != null ? territory.getQuota().getAmount() : BigDecimal.ZERO)
                .achievementPercentage(territory.getAchievementPercentage())
                .rank(territory.getRank())
                .updatedAt(Instant.now())
                .build());
    }

    public void updateQuota(Money newQuota, String reason) {
        Money previousQuota = this.quotaInfo != null ? this.quotaInfo.getAnnualQuota() : Money.zero(this.localCurrency);

        if (this.quotaInfo == null) {
            this.quotaInfo = createEmptyQuotaInfo(this.localCurrency);
        }

        this.quotaInfo.setAnnualQuota(newQuota);
        this.quotaInfo.setQuarterlyQuota(newQuota.divide(new BigDecimal("4")));
        this.quotaInfo.setMonthlyQuota(newQuota.divide(new BigDecimal("12")));
        this.quotaInfo.setLastUpdated(Instant.now());
        this.calculateAchievementPercentage();

        addDomainEvent(CountryQuotaAdjustedEvent.builder()
                .dashboardId(this.dashboardId)
                .tenantId(this.tenantId)
                .countryCode(this.countryCode)
                .previousQuota(previousQuota.getAmount())
                .newQuota(newQuota.getAmount())
                .reason(reason)
                .adjustedAt(Instant.now())
                .build());
    }

    public void addKPI(CountryKPI kpi) {
        if (this.kpis == null) {
            this.kpis = new ArrayList<>();
        }
        kpi.setKpiId(UUID.randomUUID().toString());
        kpi.setLastUpdated(Instant.now());
        this.kpis.add(kpi);
    }

    public void updateKPI(String kpiId, Object value) {
        if (this.kpis != null) {
            this.kpis.stream()
                    .filter(k -> k.getKpiId().equals(kpiId))
                    .findFirst()
                    .ifPresent(k -> {
                        k.setValue(value);
                        k.setLastUpdated(Instant.now());
                    });
        }
    }

    public void addTrendDataPoint(TrendDataPoint dataPoint) {
        if (this.trendData == null) {
            this.trendData = new ArrayList<>();
        }
        this.trendData.add(dataPoint);
    }

    public void calculateYoYComparison() {
        if (this.comparisonData == null) {
            this.comparisonData = createEmptyComparisonData();
        }

        TimePeriod currentYear = TimePeriod.currentYear();
        TimePeriod previousYear = TimePeriod.lastYear();

        Money currentRevenue = calculateRevenueForPeriod(currentYear);
        Money previousRevenue = calculateRevenueForPeriod(previousYear);

        BigDecimal growthPercentage = BigDecimal.ZERO;
        Money variance = Money.zero(this.baseCurrency);

        if (previousRevenue.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            variance = currentRevenue.subtract(previousRevenue);
            growthPercentage = variance.getAmount()
                    .divide(previousRevenue.getAmount(), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
        }

        this.comparisonData.setYearOverYear(YearOverYearComparison.builder()
                .currentYearRevenue(currentRevenue)
                .previousYearRevenue(previousRevenue)
                .growthPercentage(growthPercentage)
                .variance(variance)
                .comparisonDate(Instant.now())
                .build());

        if (this.metrics != null) {
            this.metrics.setYearOverYearGrowth(growthPercentage);
        }
    }

    public void calculateMoMComparison() {
        if (this.comparisonData == null) {
            this.comparisonData = createEmptyComparisonData();
        }

        TimePeriod currentMonth = TimePeriod.currentMonth();
        TimePeriod previousMonth = TimePeriod.lastMonth();

        Money currentRevenue = calculateRevenueForPeriod(currentMonth);
        Money previousRevenue = calculateRevenueForPeriod(previousMonth);

        BigDecimal growthPercentage = BigDecimal.ZERO;
        Money variance = Money.zero(this.baseCurrency);

        if (previousRevenue.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            variance = currentRevenue.subtract(previousRevenue);
            growthPercentage = variance.getAmount()
                    .divide(previousRevenue.getAmount(), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
        }

        this.comparisonData.setMonthOverMonth(MonthOverMonthComparison.builder()
                .currentMonthRevenue(currentRevenue)
                .previousMonthRevenue(previousRevenue)
                .growthPercentage(growthPercentage)
                .variance(variance)
                .comparisonDate(Instant.now())
                .build());

        if (this.metrics != null) {
            this.metrics.setMonthOverMonthGrowth(growthPercentage);
        }
    }

    public void calculateQoQComparison() {
        if (this.comparisonData == null) {
            this.comparisonData = createEmptyComparisonData();
        }

        TimePeriod currentQuarter = TimePeriod.currentQuarter();
        TimePeriod lastQuarter = TimePeriod.lastQuarter();

        Money currentRevenue = calculateRevenueForPeriod(currentQuarter);
        Money previousRevenue = calculateRevenueForPeriod(lastQuarter);

        BigDecimal growthPercentage = BigDecimal.ZERO;
        Money variance = Money.zero(this.baseCurrency);

        if (previousRevenue.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            variance = currentRevenue.subtract(previousRevenue);
            growthPercentage = variance.getAmount()
                    .divide(previousRevenue.getAmount(), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
        }

        this.comparisonData.setQuarterOverQuarter(QuarterOverQuarterComparison.builder()
                .currentQuarterRevenue(currentRevenue)
                .previousQuarterRevenue(previousRevenue)
                .growthPercentage(growthPercentage)
                .variance(variance)
                .comparisonDate(Instant.now())
                .build());

        if (this.metrics != null) {
            this.metrics.setQuarterOverQuarterGrowth(growthPercentage);
        }
    }

    public void updateExchangeRates(Map<String, BigDecimal> rates) {
        if (this.exchangeRates == null) {
            this.exchangeRates = new HashMap<>();
        }
        this.exchangeRates.putAll(rates);

        if (this.dataFreshness == null) {
            this.dataFreshness = DataFreshness.builder().build();
        }
        this.dataFreshness.setLastCurrencyUpdate(Instant.now());
    }

    public Money convertToBaseCurrency(Money amount) {
        if (amount.getCurrency().equals(this.baseCurrency)) {
            return amount;
        }

        if (this.exchangeRates != null) {
            BigDecimal rate = this.exchangeRates.get(amount.getCurrency());
            if (rate != null) {
                Money converted = amount.convert(rate, this.baseCurrency);

                addDomainEvent(CurrencyConversionAppliedEvent.builder()
                        .dashboardId(this.dashboardId)
                        .tenantId(this.tenantId)
                        .fromCurrency(amount.getCurrency())
                        .toCurrency(this.baseCurrency)
                        .exchangeRate(rate)
                        .originalAmount(amount.getAmount())
                        .convertedAmount(converted.getAmount())
                        .appliedAt(Instant.now())
                        .build());

                return converted;
            }
        }

        return amount;
    }

    public void refresh(String userId) {
        this.calculateYoYComparison();
        this.calculateMoMComparison();
        this.calculateQoQComparison();
        this.calculateAchievementPercentage();

        if (this.dataFreshness == null) {
            this.dataFreshness = DataFreshness.builder().build();
        }
        this.dataFreshness.setLastDataUpdate(Instant.now());
    }

    public void publish() {
        if (this.status != DashboardStatus.DRAFT) {
            throw new IllegalStateException("Only draft dashboards can be published");
        }
        this.status = DashboardStatus.PUBLISHED;
    }

    public void archive() {
        this.status = DashboardStatus.ARCHIVED;
    }

    public List<TerritoryBreakdown> getTopPerformingTerritories(int limit) {
        if (this.territories == null || this.territories.isEmpty()) {
            return new ArrayList<>();
        }
        return this.territories.stream()
                .filter(t -> t.getAchievementPercentage() != null)
                .sorted((t1, t2) -> t2.getAchievementPercentage().compareTo(t1.getAchievementPercentage()))
                .limit(limit)
                .toList();
    }

    public TerritoryBreakdown getTerritory(String territoryId) {
        if (this.territories == null) {
            return null;
        }
        return this.territories.stream()
                .filter(t -> t.getTerritoryId().equals(territoryId))
                .findFirst()
                .orElse(null);
    }

    // Private Helper Methods
    private void calculateAchievementPercentage() {
        if (this.metrics != null && this.quotaInfo != null &&
                this.metrics.totalRevenue != null && this.quotaInfo.annualQuota != null) {

            BigDecimal quota = this.quotaInfo.annualQuota.getAmount();
            BigDecimal actual = this.metrics.totalRevenue.getAmount();

            if (quota.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal achievement = actual.divide(quota, 4, BigDecimal.ROUND_HALF_UP)
                        .multiply(new BigDecimal("100"));
                this.metrics.setAchievementPercentage(achievement);
                this.quotaInfo.setYearToDateAchievement(achievement);

                BigDecimal remaining = BigDecimal.valueOf(100).subtract(achievement);
                this.quotaInfo.setRemainingPercentage(remaining);
                this.quotaInfo.setRemainingAmount(
                        this.quotaInfo.annualQuota.subtract(this.metrics.totalRevenue)
                );
            }
        }
    }

    private void rankTerritories() {
        if (this.territories == null || this.territories.isEmpty()) {
            return;
        }

        List<TerritoryBreakdown> sorted = this.territories.stream()
                .filter(t -> t.getRevenue() != null)
                .sorted((t1, t2) -> t2.getRevenue().getAmount().compareTo(t1.getRevenue().getAmount()))
                .toList();

        for (int i = 0; i < sorted.size(); i++) {
            TerritoryBreakdown t = sorted.get(i);
            t.setRank(i + 1);
        }
    }

    private void recalculateMetricsFromTerritories() {
        if (this.territories == null || this.territories.isEmpty()) {
            return;
        }

        BigDecimal totalRevenue = this.territories.stream()
                .filter(t -> t.getRevenue() != null)
                .map(t -> convertToBaseCurrency(t.getRevenue()).getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalDeals = this.territories.stream()
                .filter(t -> t.getDeals() != null)
                .mapToInt(TerritoryBreakdown::getDeals)
                .sum();

        int wonDeals = this.territories.stream()
                .filter(t -> t.getWonDeals() != null)
                .mapToInt(TerritoryBreakdown::getWonDeals)
                .sum();

        if (this.metrics == null) {
            this.metrics = createEmptyMetrics(this.baseCurrency);
        }

        this.metrics.setTotalRevenue(Money.of(totalRevenue, this.baseCurrency));
        this.metrics.setTotalDeals(totalDeals);
        this.metrics.setWonDeals(wonDeals);

        if (totalDeals > 0) {
            BigDecimal winRate = new BigDecimal(wonDeals)
                    .divide(new BigDecimal(totalDeals), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
            this.metrics.setWinRate(winRate);
        }

        this.calculateAchievementPercentage();
    }

    private Money calculateRevenueForPeriod(TimePeriod period) {
        if (this.trendData == null || this.trendData.isEmpty()) {
            return this.metrics != null && this.metrics.totalRevenue != null ?
                    this.metrics.totalRevenue : Money.zero(this.baseCurrency);
        }

        return this.trendData.stream()
                .filter(dp -> period.contains(dp.getDate()))
                .filter(dp -> dp.getRevenue() != null)
                .map(dp -> convertToBaseCurrency(dp.getRevenue()))
                .reduce(Money.zero(this.baseCurrency), Money::add);
    }

    private static CountryMetrics createEmptyMetrics(String currency) {
        return CountryMetrics.builder()
                .totalRevenue(Money.zero(currency))
                .targetRevenue(Money.zero(currency))
                .achievementPercentage(BigDecimal.ZERO)
                .totalDeals(0)
                .wonDeals(0)
                .lostDeals(0)
                .winRate(BigDecimal.ZERO)
                .averageDealSize(Money.zero(currency))
                .weightedPipeline(Money.zero(currency))
                .opportunitiesInPipeline(0)
                .newCustomers(0)
                .churnedCustomers(0)
                .retentionRate(BigDecimal.ZERO)
                .npsScore(BigDecimal.ZERO)
                .yearOverYearGrowth(BigDecimal.ZERO)
                .monthOverMonthGrowth(BigDecimal.ZERO)
                .quarterOverQuarterGrowth(BigDecimal.ZERO)
                .activeSalesReps(0)
                .revenuePerRep(Money.zero(currency))
                .build();
    }

    private static ComparisonData createEmptyComparisonData() {
        return ComparisonData.builder()
                .yearOverYear(YearOverYearComparison.builder()
                        .currentYearRevenue(Money.zero("USD"))
                        .previousYearRevenue(Money.zero("USD"))
                        .growthPercentage(BigDecimal.ZERO)
                        .variance(Money.zero("USD"))
                        .build())
                .monthOverMonth(MonthOverMonthComparison.builder()
                        .currentMonthRevenue(Money.zero("USD"))
                        .previousMonthRevenue(Money.zero("USD"))
                        .growthPercentage(BigDecimal.ZERO)
                        .variance(Money.zero("USD"))
                        .build())
                .quarterOverQuarter(QuarterOverQuarterComparison.builder()
                        .currentQuarterRevenue(Money.zero("USD"))
                        .previousQuarterRevenue(Money.zero("USD"))
                        .growthPercentage(BigDecimal.ZERO)
                        .variance(Money.zero("USD"))
                        .build())
                .regionalComparison(RegionalComparison.builder()
                        .countryMetrics(new ArrayList<>())
                        .build())
                .build();
    }

    private static QuotaInfo createEmptyQuotaInfo(String currency) {
        return QuotaInfo.builder()
                .annualQuota(Money.zero(currency))
                .quarterlyQuota(Money.zero(currency))
                .monthlyQuota(Money.zero(currency))
                .yearToDateRevenue(Money.zero(currency))
                .yearToDateAchievement(BigDecimal.ZERO)
                .remainingPercentage(BigDecimal.valueOf(100))
                .remainingAmount(Money.zero(currency))
                .projectedRevenue(Money.zero(currency))
                .forecastAccuracy(BigDecimal.ZERO)
                .build();
    }

    private static DashboardConfiguration createDefaultConfiguration() {
        return DashboardConfiguration.builder()
                .refreshIntervalMinutes(5)
                .autoRefresh(true)
                .enabledTerritories(new ArrayList<>())
                .enabledMetrics(Arrays.asList(
                        MetricType.TOTAL_REVENUE,
                        MetricType.WON_DEALS,
                        MetricType.WIN_RATE,
                        MetricType.QUOTA_ATTAINMENT
                ))
                .defaultPeriod(TimePeriod.PeriodType.MONTHLY)
                .comparisonMode(ComparisonMode.MONTH_OVER_MONTH)
                .showTargets(true)
                .showForecasts(true)
                .defaultView("OVERVIEW")
                .maxTerritoryRankings(10)
                .build();
    }

    private static ExecutiveSummary createEmptyExecutiveSummary() {
        return ExecutiveSummary.builder()
                .headline("Dashboard initialized")
                .keyHighlight("No data available")
                .topPerformingTerritories(new ArrayList<>())
                .underperformingTerritories(new ArrayList<>())
                .overallSentiment("NEUTRAL")
                .riskScore(BigDecimal.ZERO)
                .recommendations(new ArrayList<>())
                .primaryFocusArea("DATA_COLLECTION")
                .build();
    }

    public void addDomainEvent(Object event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }
}
