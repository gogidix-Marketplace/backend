package com.gogidix.sales.analytics.domain.model;

import com.gogidix.sales.analytics.domain.event.MetricUpdatedEvent;
import com.gogidix.sales.analytics.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Metric Domain Entity
 * Represents a calculated sales metric with period-based tracking
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "metrics")
public class Metric extends BaseEntity {

    private String metricId;

    @Indexed
    private String tenantId;

    @Indexed
    private String entityType;

    @Indexed
    private String entityId;

    private MetricType metricType;

    private BigDecimal value;

    private BigDecimal previousValue;

    private BigDecimal targetValue;

    private BigDecimal variance;

    private String period;

    private Instant periodStart;

    private Instant periodEnd;

    @Indexed
    private Instant calculatedAt;

    private String calculatedBy;

    private Map<String, Object> metadata;

    private Map<String, Object> breakdown;

    private MetricTrend trend;

    @Builder.Default
    private List<MetricUpdatedEvent> domainEvents = new ArrayList<>();

    public enum MetricType {
        // Sales Performance Metrics
        TOTAL_REVENUE,
        RECURRING_REVENUE,
        AVERAGE_DEAL_SIZE,
        REVENUE_PER_SALES_REP,
        QUOTA_ACHIEVEMENT_RATE,
        YEAR_OVER_YEAR_GROWTH,

        // Conversion Metrics
        LEAD_TO_OPPORTUNITY_RATE,
        OPPORTUNITY_TO_WIN_RATE,
        OVERALL_CONVERSION_RATE,
        STAGE_CONVERSION_RATE,
        FUNNEL_DROP_OFF_RATE,

        // Velocity Metrics
        SALES_CYCLE_LENGTH,
        STAGE_DURATION,
        DEAL_VELOCITY,
        RESPONSE_TIME,
        FOLLOW_UP_TIME,

        // Pipeline Metrics
        PIPELINE_VALUE,
        PIPELINE_COVERAGE,
        PIPELINE_VELOCITY,
        STAGE_DISTRIBUTION,
        PIPELINE_HEALTH_SCORE,

        // Win/Loss Metrics
        WIN_RATE,
        LOSS_RATE,
        WIN_LOSS_RATIO,
        CHURN_RATE,
        RETENTION_RATE,

        // Product Metrics
        PRODUCT_REVENUE,
        PRODUCT_MARGIN,
        PRODUCT_SALES_VOLUME,
        PRODUCT_CONVERSION_RATE,

        // Sales Rep Metrics
        REP_ACTIVITY_COUNT,
        REP_CONVERSION_RATE,
        REP_QUOTA_ATTAINMENT,
        REP_PRODUCTIVITY_SCORE,

        // Custom Metrics
        CUSTOM
    }

    public enum MetricTrend {
        UP,
        DOWN,
        STABLE,
        UNKNOWN
    }

    /**
     * Creates a new metric
     */
    public static Metric create(String tenantId, String entityType, String entityId,
                                MetricType metricType, BigDecimal value,
                                String period, Instant periodStart, Instant periodEnd,
                                String calculatedBy) {
        String metricId = generateMetricId();

        return Metric.builder()
                .metricId(metricId)
                .tenantId(tenantId)
                .entityType(entityType)
                .entityId(entityId)
                .metricType(metricType)
                .value(value)
                .period(period)
                .periodStart(periodStart)
                .periodEnd(periodEnd)
                .calculatedAt(Instant.now())
                .calculatedBy(calculatedBy)
                .metadata(new HashMap<>())
                .breakdown(new HashMap<>())
                .trend(MetricTrend.UNKNOWN)
                .build();
    }

    /**
     * Updates the metric value
     */
    public void updateValue(BigDecimal newValue) {
        this.previousValue = this.value;
        this.value = newValue;
        this.calculatedAt = Instant.now();
        calculateTrend();
        calculateVariance();

        addDomainEvent(MetricUpdatedEvent.create(
                this.metricId, this.tenantId, this.metricType.name(),
                this.entityType, this.entityId, newValue, this.period,
                this.periodStart, this.periodEnd, this.metadata, "METRIC_UPDATED"
        ));
    }

    /**
     * Sets the target value and calculates variance
     */
    public void setTargetValue(BigDecimal targetValue) {
        this.targetValue = targetValue;
        calculateVariance();
    }

    /**
     * Calculates the variance from target
     */
    private void calculateVariance() {
        if (this.targetValue != null && this.value != null) {
            if (this.targetValue.compareTo(BigDecimal.ZERO) != 0) {
                this.variance = this.value.subtract(this.targetValue)
                        .divide(this.targetValue, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            } else {
                this.variance = BigDecimal.ZERO;
            }
        }
    }

    /**
     * Calculates the trend based on value change
     */
    private void calculateTrend() {
        if (this.previousValue != null && this.value != null) {
            int comparison = this.value.compareTo(this.previousValue);
            if (comparison > 0) {
                this.trend = MetricTrend.UP;
            } else if (comparison < 0) {
                this.trend = MetricTrend.DOWN;
            } else {
                this.trend = MetricTrend.STABLE;
            }
        }
    }

    /**
     * Adds metadata to the metric
     */
    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }

    /**
     * Adds breakdown data
     */
    public void addBreakdown(String category, Object value) {
        if (this.breakdown == null) {
            this.breakdown = new HashMap<>();
        }
        this.breakdown.put(category, value);
    }

    /**
     * Calculates the percentage achievement against target
     */
    public BigDecimal getAchievementPercentage() {
        if (this.targetValue != null && this.targetValue.compareTo(BigDecimal.ZERO) > 0) {
            return this.value.divide(this.targetValue, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO;
    }

    /**
     * Checks if the target is met
     */
    public boolean isTargetMet() {
        return this.targetValue != null &&
               this.value != null &&
               this.value.compareTo(this.targetValue) >= 0;
    }

    /**
     * Gets the percentage change from previous value
     */
    public BigDecimal getPercentageChange() {
        if (this.previousValue != null && this.previousValue.compareTo(BigDecimal.ZERO) != 0) {
            return this.value.subtract(this.previousValue)
                    .divide(this.previousValue, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO;
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

    private static String generateMetricId() {
        return "MT-" + System.currentTimeMillis() + "-" +
               Integer.toHexString((int) (Math.random() * 0xFFFF)).toUpperCase();
    }
}
