package com.gogidix.finance.globalfinancedashboard.domain.model;

import com.gogidix.finance.globalfinancedashboard.domain.event.MetricCalculatedEvent;
import com.gogidix.finance.globalfinancedashboard.shared.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Metric Domain Entity
 * Represents calculated financial metrics for dashboards
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Document(collection = "metrics")
public class Metric {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    private String metricId;

    private String widgetId;

    private String dashboardId;

    private String name;

    private String description;

    private MetricType type;

    private MetricCategory category;

    private BigDecimal value;

    private String formattedValue;

    private BigDecimal previousValue;

    private BigDecimal targetValue;

    private BigDecimal variance;

    private BigDecimal variancePercentage;

    private Trend trend;

    private String currency;

    private TimeUnit timeUnit;

    private LocalDate periodStart;

    private LocalDate periodEnd;

    private String granularity;

    private List<MetricDataPoint> dataPoints;

    private Map<String, Object> metadata;

    private CalculationStatus status;

    private String errorMessage;

    private String calculatedBy;

    private Instant calculatedAt;

    private Instant validUntil;

    private Integer confidenceLevel;

    @Builder.Default
    private List<MetricCalculatedEvent> domainEvents = new ArrayList<>();

    public enum MetricType {
        REVENUE,
        EXPENSE,
        PROFIT,
        GROSS_MARGIN,
        NET_MARGIN,
        EBITDA,
        OPERATING_INCOME,
        CASH_FLOW,
        FREE_CASH_FLOW,
        WORKING_CAPITAL,
        CURRENT_RATIO,
        QUICK_RATIO,
        DEBT_TO_EQUITY,
        ROI,
        ROE,
        ROA,
        AR_TURNOVER_DAYS,
        AP_TURNOVER_DAYS,
        INVENTORY_TURNOVER_DAYS,
        BUDGET_VARIANCE,
        FORECAST_ACCURACY,
        CUSTOM
    }

    public enum MetricCategory {
        PROFITABILITY,
        LIQUIDITY,
        SOLVENCY,
        EFFICIENCY,
        GROWTH,
        VALUATION,
        CASH_FLOW,
        BUDGET,
        FORECAST,
        CUSTOM
    }

    public enum Trend {
        UP,
        DOWN,
        STABLE,
        NEUTRAL
    }

    public enum TimeUnit {
        DAILY,
        WEEKLY,
        MONTHLY,
        QUARTERLY,
        YEARLY
    }

    public enum CalculationStatus {
        PENDING,
        CALCULATED,
        ERROR,
        STALE
    }

    /**
     * Creates a new metric
     */
    public static Metric create(String tenantId, String widgetId, String dashboardId,
                               String name, MetricType type, MetricCategory category,
                               BigDecimal value, String currency) {
        Metric metric = Metric.builder()
                .metricId(generateMetricId())
                .tenantId(Objects.requireNonNull(tenantId, "tenantId is required"))
                .widgetId(widgetId)
                .dashboardId(dashboardId)
                .name(Objects.requireNonNull(name, "name is required"))
                .type(type != null ? type : MetricType.CUSTOM)
                .category(category != null ? category : MetricCategory.CUSTOM)
                .value(value)
                .currency(currency)
                .trend(Trend.NEUTRAL)
                .dataPoints(new ArrayList<>())
                .metadata(new HashMap<>())
                .status(CalculationStatus.PENDING)
                .calculatedAt(Instant.now())
                .build();

        metric.addDomainEvent(MetricCalculatedEvent.builder()
                .metricId(metric.getMetricId())
                .tenantId(tenantId)
                .widgetId(widgetId)
                .name(name)
                .type(type != null ? type.name() : MetricType.CUSTOM.name())
                .value(value)
                .timestamp(Instant.now())
                .eventType("METRIC_CREATED")
                .build());

        return metric;
    }

    /**
     * Updates metric value
     */
    public void updateValue(BigDecimal newValue, BigDecimal previousValue) {
        this.previousValue = this.value;
        this.value = newValue;

        if (previousValue != null && newValue != null && previousValue.compareTo(BigDecimal.ZERO) != 0) {
            this.variance = newValue.subtract(previousValue);
            this.variancePercentage = this.variance
                    .divide(previousValue, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));

            // Determine trend
            if (this.variance.compareTo(BigDecimal.ZERO) > 0) {
                this.trend = Trend.UP;
            } else if (this.variance.compareTo(BigDecimal.ZERO) < 0) {
                this.trend = Trend.DOWN;
            } else {
                this.trend = Trend.STABLE;
            }
        }

        this.formattedValue = formatValue(newValue, this.currency);
        this.calculatedAt = Instant.now();
        this.status = CalculationStatus.CALCULATED;

        addDomainEvent(MetricCalculatedEvent.builder()
                .metricId(this.metricId)
                .tenantId(this.tenantId)
                .widgetId(this.widgetId)
                .name(this.name)
                .value(newValue)
                .previousValue(previousValue)
                .variance(this.variance)
                .variancePercentage(this.variancePercentage)
                .trend(this.trend != null ? this.trend.name() : null)
                .timestamp(Instant.now())
                .eventType("METRIC_VALUE_UPDATED")
                .build());
    }

    /**
     * Sets target value
     */
    public void setTarget(BigDecimal target) {
        this.targetValue = target;
        if (this.value != null && target != null) {
            BigDecimal remaining = target.subtract(this.value);
            this.metadata.put("targetRemaining", remaining);
            this.metadata.put("targetAchievedPercentage",
                    this.value.divide(target, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
        }
    }

    /**
     * Adds a data point
     */
    public void addDataPoint(LocalDate date, BigDecimal value, String label) {
        if (this.dataPoints == null) {
            this.dataPoints = new ArrayList<>();
        }

        MetricDataPoint dataPoint = MetricDataPoint.builder()
                .date(date)
                .value(value)
                .label(label)
                .build();

        this.dataPoints.add(dataPoint);

        // Keep only last 100 data points
        if (this.dataPoints.size() > 100) {
            this.dataPoints.remove(0);
        }
    }

    /**
     * Sets data points
     */
    public void setDataPoints(List<MetricDataPoint> dataPoints) {
        this.dataPoints = dataPoints != null ? dataPoints : new ArrayList<>();
    }

    /**
     * Sets time period
     */
    public void setTimePeriod(LocalDate start, LocalDate end, String granularity) {
        this.periodStart = start;
        this.periodEnd = end;
        this.granularity = granularity;
    }

    /**
     * Sets monthly time period
     */
    public void setMonthlyPeriod(YearMonth yearMonth) {
        this.periodStart = yearMonth.atDay(1);
        this.periodEnd = yearMonth.atEndOfMonth();
        this.granularity = TimeUnit.MONTHLY.name();
    }

    /**
     * Sets quarterly time period
     */
    public void setQuarterlyPeriod(int year, int quarter) {
        int startMonth = (quarter - 1) * 3 + 1;
        this.periodStart = LocalDate.of(year, startMonth, 1);
        this.periodEnd = LocalDate.of(year, startMonth + 2, YearMonth.of(year, startMonth + 2).lengthOfMonth());
        this.granularity = TimeUnit.QUARTERLY.name();
    }

    /**
     * Sets yearly time period
     */
    public void setYearlyPeriod(int year) {
        this.periodStart = LocalDate.of(year, 1, 1);
        this.periodEnd = LocalDate.of(year, 12, 31);
        this.granularity = TimeUnit.YEARLY.name();
    }

    /**
     * Sets metadata
     */
    public void setMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }

    /**
     * Gets metadata value
     */
    @SuppressWarnings("unchecked")
    public <T> T getMetadata(String key, Class<T> type) {
        if (this.metadata == null) {
            return null;
        }
        Object value = this.metadata.get(key);
        if (value != null && type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

    /**
     * Marks metric as calculated
     */
    public void markAsCalculated(String calculatedBy) {
        this.status = CalculationStatus.CALCULATED;
        this.calculatedAt = Instant.now();
        this.calculatedBy = calculatedBy;
        this.errorMessage = null;
    }

    /**
     * Marks metric as error
     */
    public void markAsError(String errorMessage) {
        this.status = CalculationStatus.ERROR;
        this.errorMessage = errorMessage;
        this.calculatedAt = Instant.now();
    }

    /**
     * Marks metric as stale
     */
    public void markAsStale() {
        this.status = CalculationStatus.STALE;
    }

    /**
     * Sets validity period
     */
    public void setValidUntil(Instant validUntil) {
        this.validUntil = validUntil;
    }

    /**
     * Checks if metric is still valid
     */
    public boolean isValid() {
        if (this.status != CalculationStatus.CALCULATED) {
            return false;
        }
        if (this.validUntil != null && Instant.now().isAfter(this.validUntil)) {
            this.status = CalculationStatus.STALE;
            return false;
        }
        return true;
    }

    /**
     * Calculates percentage achievement against target
     */
    public BigDecimal getAchievementPercentage() {
        if (this.targetValue == null || this.value == null || this.targetValue.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        return this.value
                .divide(this.targetValue, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    /**
     * Checks if target is achieved
     */
    public boolean isTargetAchieved() {
        BigDecimal achievement = getAchievementPercentage();
        return achievement != null && achievement.compareTo(BigDecimal.valueOf(100)) >= 0;
    }

    /**
     * Gets formatted value with currency
     */
    public String getFormattedValueWithCurrency() {
        if (this.currency == null) {
            return this.formattedValue;
        }
        return this.currency + " " + this.formattedValue;
    }

    /**
     * Gets formatted variance
     */
    public String getFormattedVariance() {
        if (this.variance == null) {
            return "N/A";
        }
        String sign = this.variance.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "";
        return sign + formatValue(this.variance, null);
    }

    /**
     * Gets formatted variance percentage
     */
    public String getFormattedVariancePercentage() {
        if (this.variancePercentage == null) {
            return "N/A";
        }
        String sign = this.variancePercentage.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "";
        return sign + this.variancePercentage.setScale(2, RoundingMode.HALF_UP) + "%";
    }

    private String formatValue(BigDecimal value, String currency) {
        if (value == null) {
            return "N/A";
        }

        // Format based on magnitude
        BigDecimal absValue = value.abs();
        String suffix = "";
        BigDecimal divisor = BigDecimal.ONE;

        if (absValue.compareTo(BigDecimal.valueOf(1_000_000_000)) >= 0) {
            suffix = "B";
            divisor = BigDecimal.valueOf(1_000_000_000);
        } else if (absValue.compareTo(BigDecimal.valueOf(1_000_000)) >= 0) {
            suffix = "M";
            divisor = BigDecimal.valueOf(1_000_000);
        } else if (absValue.compareTo(BigDecimal.valueOf(1_000)) >= 0) {
            suffix = "K";
            divisor = BigDecimal.valueOf(1_000);
        }

        BigDecimal formattedValue = value.divide(divisor, 2, RoundingMode.HALF_UP);
        return formattedValue.toPlainString() + suffix;
    }

    private static String generateMetricId() {
        return "METR-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public void addDomainEvent(MetricCalculatedEvent event) {
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
     * Metric data point for time series
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetricDataPoint {
        private LocalDate date;
        private BigDecimal value;
        private String label;
        private String formattedValue;
        private Map<String, Object> attributes;

        public MetricDataPoint(LocalDate date, BigDecimal value, String label) {
            this.date = date;
            this.value = value;
            this.label = label;
            this.attributes = new HashMap<>();
        }
    }

    /**
     * Metric calculation result
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetricCalculationResult {
        private String metricId;
        private BigDecimal value;
        private BigDecimal previousValue;
        private BigDecimal variance;
        private BigDecimal variancePercentage;
        private Trend trend;
        private String formattedValue;
        private List<MetricDataPoint> dataPoints;
        private Map<String, Object> metadata;
        private CalculationStatus status;
        private String errorMessage;
    }
}
