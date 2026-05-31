package com.gogidix.sales.dashboard.domain.model;

import com.gogidix.sales.dashboard.domain.event.MetricUpdatedEvent;
import com.gogidix.sales.dashboard.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Global Sales Dashboard Domain Entity
 * Multi-tenant global sales dashboard with aggregated metrics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "global_sales_dashboards")
public class GlobalSalesDashboard extends BaseEntity {

    private String dashboardId;
    private String tenantId;
    private String name;
    private String description;
    private DashboardStatus status;
    private DashboardType type;

    // Global Metrics (aggregated across all regions)
    private GlobalMetrics globalMetrics;

    // Regional breakdown
    private List<RegionalMetric> regionalMetrics;

    // KPI Widgets
    private List<KPIWidget> widgets;

    // Time series data for trends
    private List<TimeSeriesData> trendData;

    // Executive summary
    private ExecutiveSummary executiveSummary;

    // Dashboard configuration
    private DashboardConfiguration configuration;

    // Currency settings
    private String baseCurrency;
    private Map<String, BigDecimal> exchangeRates;

    // Last refresh time
    private Instant lastRefreshAt;
    private String lastRefreshedBy;

    // Data freshness info
    private DataFreshness dataFreshness;

    // Drill-down configurations
    private List<DrillDownConfig> drillDownConfigs;

    // Domain events
    @Builder.Default
    private List<MetricUpdatedEvent> domainEvents = new ArrayList<>();

    public enum DashboardStatus {
        ACTIVE, ARCHIVED, DRAFT, PUBLISHED
    }

    public enum DashboardType {
        EXECUTIVE, REGIONAL, PRODUCT, CUSTOM
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GlobalMetrics {
        private Money totalRevenue;
        private Money targetRevenue;
        private BigDecimal achievementPercentage;
        private Integer totalDeals;
        private Integer wonDeals;
        private BigDecimal winRate;
        private Money averageDealSize;
        private Integer activeSalesReps;
        private Money weightedPipeline;
        private Integer opportunitiesInPipeline;
        private BigDecimal yearOverYearGrowth;
        private BigDecimal monthOverMonthGrowth;
        private BigDecimal quarterOverQuarterGrowth;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionalMetric {
        private String regionCode;
        private String regionName;
        private Money revenue;
        private Money target;
        private BigDecimal achievementPercentage;
        private Integer deals;
        private BigDecimal growthRate;
        private Integer rank;
        private String currency;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KPIWidget {
        private String widgetId;
        private String title;
        private com.gogidix.sales.dashboard.domain.model.KPIWidget.WidgetType type;
        private com.gogidix.sales.dashboard.domain.model.KPIWidget.WidgetType widgetType;
        private com.gogidix.sales.dashboard.domain.model.KPIWidget.WidgetCategory category;
        private String description;
        private Object value;
        private String format;
        private String trend;
        private BigDecimal trendValue;
        private String dataSource;
        private Integer position;
        private WidgetSize size;
        private Boolean isVisible;
        private Map<String, Object> metadata;
        private Instant lastUpdated;

        public enum WidgetSize {
            SMALL, MEDIUM, LARGE, WIDE
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeSeriesData {
        private String period;
        private LocalDate date;
        private Money revenue;
        private Integer deals;
        private BigDecimal conversionRate;
        private Money averageDealSize;
        private String region;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExecutiveSummary {
        private String headline;
        private String keyHighlight;
        private List<String> topPerformers;
        private List<String> areasForImprovement;
        private String overallSentiment;
        private BigDecimal riskScore;
        private List<String> recommendations;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardConfiguration {
        private Integer refreshIntervalMinutes;
        private Boolean autoRefresh;
        private List<String> enabledRegions;
        private List<String> enabledMetrics;
        private String dateRange;
        private BooleanComparisionMode comparisonMode;
        private Boolean showTargets;
        private Boolean showForecasts;
        private String defaultView;
    }

    public enum BooleanComparisionMode {
        YEAR_OVER_YEAR, MONTH_OVER_MONTH, QUARTER_OVER_QUARTER, NONE
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataFreshness {
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
    public static class DrillDownConfig {
        private String id;
        private String name;
        private String targetDashboardId;
        private List<String> filters;
        private DrillDownType type;
    }

    public enum DrillDownType {
        REGION, PRODUCT, SALES_REP, TIME_PERIOD
    }

    /**
     * Creates a new global sales dashboard
     */
    public static GlobalSalesDashboard create(String tenantId, String name, String description,
                                               DashboardType type, String baseCurrency) {
        GlobalSalesDashboard dashboard = GlobalSalesDashboard.builder()
                .dashboardId(java.util.UUID.randomUUID().toString())
                .tenantId(tenantId)
                .name(name)
                .description(description)
                .status(DashboardStatus.DRAFT)
                .type(type)
                .baseCurrency(baseCurrency)
                .globalMetrics(createEmptyGlobalMetrics())
                .regionalMetrics(new ArrayList<>())
                .widgets(new ArrayList<>())
                .trendData(new ArrayList<>())
                .executiveSummary(createEmptyExecutiveSummary())
                .configuration(createDefaultConfiguration())
                .exchangeRates(new HashMap<>())
                .drillDownConfigs(new ArrayList<>())
                .build();

        dashboard.addDomainEvent(MetricUpdatedEvent.builder()
                .dashboardId(dashboard.getDashboardId())
                .tenantId(tenantId)
                .eventType("DASHBOARD_CREATED")
                .timestamp(Instant.now())
                .build());

        return dashboard;
    }

    /**
     * Updates global metrics
     */
    public void updateGlobalMetrics(GlobalMetrics metrics) {
        this.globalMetrics = metrics;
        this.lastRefreshAt = Instant.now();
        this.calculateAchievementPercentage();

        addDomainEvent(MetricUpdatedEvent.builder()
                .dashboardId(this.dashboardId)
                .tenantId(this.tenantId)
                .eventType("GLOBAL_METRICS_UPDATED")
                .timestamp(Instant.now())
                .build());
    }

    /**
     * Updates regional metrics
     */
    public void updateRegionalMetric(RegionalMetric regionalMetric) {
        if (this.regionalMetrics == null) {
            this.regionalMetrics = new ArrayList<>();
        }

        this.regionalMetrics.removeIf(r -> r.getRegionCode().equals(regionalMetric.getRegionCode()));
        this.regionalMetrics.add(regionalMetric);
        this.recalculateGlobalFromRegional();

        addDomainEvent(MetricUpdatedEvent.builder()
                .dashboardId(this.dashboardId)
                .tenantId(this.tenantId)
                .eventType("REGIONAL_METRIC_UPDATED")
                .regionCode(regionalMetric.getRegionCode())
                .timestamp(Instant.now())
                .build());
    }

    /**
     * Adds a KPI widget
     */
    public void addWidget(KPIWidget widget) {
        if (this.widgets == null) {
            this.widgets = new ArrayList<>();
        }

        widget.setWidgetId(java.util.UUID.randomUUID().toString());
        widget.setLastUpdated(Instant.now());
        this.widgets.add(widget);
    }

    /**
     * Updates a KPI widget
     */
    public void updateWidget(String widgetId, Object value) {
        if (this.widgets != null) {
            this.widgets.stream()
                    .filter(w -> w.getWidgetId().equals(widgetId))
                    .findFirst()
                    .ifPresent(w -> {
                        w.setValue(value);
                        w.setLastUpdated(Instant.now());
                    });
        }
    }

    /**
     * Adds trend data point
     */
    public void addTrendData(TimeSeriesData dataPoint) {
        if (this.trendData == null) {
            this.trendData = new ArrayList<>();
        }
        this.trendData.add(dataPoint);
    }

    /**
     * Refreshes dashboard data
     */
    public void refresh(String userId) {
        this.lastRefreshAt = Instant.now();
        this.lastRefreshedBy = userId;
        this.status = DashboardStatus.PUBLISHED;

        addDomainEvent(MetricUpdatedEvent.builder()
                .dashboardId(this.dashboardId)
                .tenantId(this.tenantId)
                .eventType("DASHBOARD_REFRESHED")
                .timestamp(Instant.now())
                .build());
    }

    /**
     * Publishes the dashboard
     */
    public void publish() {
        if (this.status != DashboardStatus.DRAFT) {
            throw new IllegalStateException("Only draft dashboards can be published");
        }
        this.status = DashboardStatus.PUBLISHED;

        addDomainEvent(MetricUpdatedEvent.builder()
                .dashboardId(this.dashboardId)
                .tenantId(this.tenantId)
                .eventType("DASHBOARD_PUBLISHED")
                .timestamp(Instant.now())
                .build());
    }

    /**
     * Archives the dashboard
     */
    public void archive() {
        this.status = DashboardStatus.ARCHIVED;

        addDomainEvent(MetricUpdatedEvent.builder()
                .dashboardId(this.dashboardId)
                .tenantId(this.tenantId)
                .eventType("DASHBOARD_ARCHIVED")
                .timestamp(Instant.now())
                .build());
    }

    /**
     * Updates executive summary
     */
    public void updateExecutiveSummary(ExecutiveSummary summary) {
        this.executiveSummary = summary;
    }

    /**
     * Updates data freshness
     */
    public void updateDataFreshness(DataFreshness freshness) {
        this.dataFreshness = freshness;
    }

    /**
     * Updates exchange rates
     */
    public void updateExchangeRates(Map<String, BigDecimal> rates) {
        if (this.exchangeRates == null) {
            this.exchangeRates = new HashMap<>();
        }
        this.exchangeRates.putAll(rates);
    }

    /**
     * Converts amount to base currency
     */
    public BigDecimal convertToBaseCurrency(BigDecimal amount, String fromCurrency) {
        if (fromCurrency.equals(this.baseCurrency)) {
            return amount;
        }

        BigDecimal rate = this.exchangeRates.get(fromCurrency);
        if (rate == null) {
            throw new IllegalStateException("Exchange rate not available for " + fromCurrency);
        }

        return amount.multiply(rate);
    }

    /**
     * Gets top performing regions
     */
    public List<RegionalMetric> getTopPerformingRegions(int limit) {
        if (this.regionalMetrics == null) {
            return new ArrayList<>();
        }
        return this.regionalMetrics.stream()
                .sorted((r1, r2) -> r2.getRevenue().getAmount().compareTo(r1.getRevenue().getAmount()))
                .limit(limit)
                .toList();
    }

    /**
     * Gets regional metrics by code
     */
    public RegionalMetric getRegionalMetric(String regionCode) {
        if (this.regionalMetrics == null) {
            return null;
        }
        return this.regionalMetrics.stream()
                .filter(r -> r.getRegionCode().equals(regionCode))
                .findFirst()
                .orElse(null);
    }

    private void calculateAchievementPercentage() {
        if (this.globalMetrics != null &&
                this.globalMetrics.getTargetRevenue() != null &&
                this.globalMetrics.getTotalRevenue() != null) {

            BigDecimal target = this.globalMetrics.getTargetRevenue().getAmount();
            BigDecimal actual = this.globalMetrics.getTotalRevenue().getAmount();

            if (target.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal achievement = actual.divide(target, 4, java.math.RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
                this.globalMetrics.setAchievementPercentage(achievement);
            }
        }
    }

    private void recalculateGlobalFromRegional() {
        if (this.regionalMetrics == null || this.regionalMetrics.isEmpty()) {
            return;
        }

        BigDecimal totalRevenue = this.regionalMetrics.stream()
                .map(r -> convertToBaseCurrency(r.getRevenue().getAmount(), r.getCurrency()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalDeals = this.regionalMetrics.stream()
                .mapToInt(RegionalMetric::getDeals)
                .sum();

        int wonDeals = this.regionalMetrics.stream()
                .mapToInt(r -> (int) (r.getDeals() * (r.getGrowthRate() != null ?
                        r.getGrowthRate().doubleValue() / 100 : 0.5)))
                .sum();

        if (this.globalMetrics == null) {
            this.globalMetrics = createEmptyGlobalMetrics();
        }

        this.globalMetrics.setTotalRevenue(Money.of(totalRevenue, this.baseCurrency));
        this.globalMetrics.setTotalDeals(totalDeals);
        this.globalMetrics.setWonDeals(wonDeals);

        if (totalDeals > 0) {
            this.globalMetrics.setWinRate(new BigDecimal(wonDeals)
                    .divide(new BigDecimal(totalDeals), 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100")));
        }

        this.calculateAchievementPercentage();
    }

    private static GlobalMetrics createEmptyGlobalMetrics() {
        return GlobalMetrics.builder()
                .totalRevenue(Money.zero("USD"))
                .targetRevenue(Money.zero("USD"))
                .achievementPercentage(BigDecimal.ZERO)
                .totalDeals(0)
                .wonDeals(0)
                .winRate(BigDecimal.ZERO)
                .averageDealSize(Money.zero("USD"))
                .activeSalesReps(0)
                .weightedPipeline(Money.zero("USD"))
                .opportunitiesInPipeline(0)
                .yearOverYearGrowth(BigDecimal.ZERO)
                .monthOverMonthGrowth(BigDecimal.ZERO)
                .quarterOverQuarterGrowth(BigDecimal.ZERO)
                .build();
    }

    private static ExecutiveSummary createEmptyExecutiveSummary() {
        return ExecutiveSummary.builder()
                .headline("Dashboard initialized")
                .keyHighlight("No data available")
                .topPerformers(new ArrayList<>())
                .areasForImprovement(new ArrayList<>())
                .overallSentiment("NEUTRAL")
                .riskScore(BigDecimal.ZERO)
                .recommendations(new ArrayList<>())
                .build();
    }

    private static DashboardConfiguration createDefaultConfiguration() {
        return DashboardConfiguration.builder()
                .refreshIntervalMinutes(5)
                .autoRefresh(true)
                .enabledRegions(List.of("NA", "EU", "APAC", "LATAM", "MEA"))
                .enabledMetrics(List.of("REVENUE", "DEALS", "WIN_RATE", "PIPELINE"))
                .dateRange("LAST_30_DAYS")
                .comparisonMode(BooleanComparisionMode.MONTH_OVER_MONTH)
                .showTargets(true)
                .showForecasts(true)
                .defaultView("OVERVIEW")
                .build();
    }

    public void addDomainEvent(MetricUpdatedEvent event) {
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

    /**
     * Money value object helper
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Money {
        private BigDecimal amount;
        private String currency;

        public static Money of(BigDecimal amount, String currency) {
            return new Money(amount, currency);
        }

        public static Money zero(String currency) {
            return new Money(BigDecimal.ZERO, currency);
        }
    }
}
