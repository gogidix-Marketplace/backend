package com.gogidix.sales.analytics.domain.model;

import com.gogidix.sales.analytics.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sales Cycle Metric Domain Entity
 * Tracks sales cycle duration and velocity metrics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "sales_cycle_metrics")
public class SalesCycleMetric extends BaseEntity {

    private String salesCycleMetricId;

    @Indexed
    private String tenantId;

    @Indexed
    private String entityType; // TEAM, INDIVIDUAL, REGION, STAGE

    @Indexed
    private String entityId;

    private String entityName;

    private MetricPeriod period;

    private LocalDate periodStartDate;

    private LocalDate periodEndDate;

    // Cycle duration metrics
    private BigDecimal averageCycleDurationDays;

    private BigDecimal medianCycleDurationDays;

    private BigDecimal shortestCycleDays;

    private BigDecimal longestCycleDays;

    // Stage duration breakdown
    private Map<String, BigDecimal> stageDurations;

    // Velocity metrics
    private BigDecimal pipelineVelocity;

    private BigDecimal dealVelocity;

    private BigDecimal responseTimeHours;

    private BigDecimal followUpTimeHours;

    // Conversion by timeframe
    private Map<String, BigDecimal> conversionByTimeframe;

    // Aging analysis
    private Map<String, Integer> agingBuckets;

    // Targets and thresholds
    private BigDecimal targetCycleDays;

    private BigDecimal cycleVariance;

    private CycleHealthScore healthScore;

    private Map<String, Object> metadata;

    private Instant calculatedAt;

    private String calculatedBy;

    public enum MetricPeriod {
        DAILY,
        WEEKLY,
        MONTHLY,
        QUARTERLY,
        YEARLY
    }

    public enum CycleHealthScore {
        EXCELLENT,
        GOOD,
        AVERAGE,
        POOR,
        CRITICAL
    }

    /**
     * Creates a new sales cycle metric
     */
    public static SalesCycleMetric create(String tenantId, String entityType, String entityId,
                                          String entityName, MetricPeriod period,
                                          LocalDate periodStart, LocalDate periodEnd,
                                          String calculatedBy) {
        return SalesCycleMetric.builder()
                .salesCycleMetricId(generateId())
                .tenantId(tenantId)
                .entityType(entityType)
                .entityId(entityId)
                .entityName(entityName)
                .period(period)
                .periodStartDate(periodStart)
                .periodEndDate(periodEnd)
                .averageCycleDurationDays(BigDecimal.ZERO)
                .medianCycleDurationDays(BigDecimal.ZERO)
                .shortestCycleDays(BigDecimal.ZERO)
                .longestCycleDays(BigDecimal.ZERO)
                .stageDurations(new HashMap<>())
                .pipelineVelocity(BigDecimal.ZERO)
                .dealVelocity(BigDecimal.ZERO)
                .responseTimeHours(BigDecimal.ZERO)
                .followUpTimeHours(BigDecimal.ZERO)
                .conversionByTimeframe(new HashMap<>())
                .agingBuckets(new HashMap<>())
                .targetCycleDays(BigDecimal.ZERO)
                .cycleVariance(BigDecimal.ZERO)
                .healthScore(CycleHealthScore.AVERAGE)
                .metadata(new HashMap<>())
                .calculatedAt(Instant.now())
                .calculatedBy(calculatedBy)
                .build();
    }

    /**
     * Updates cycle duration metrics
     */
    public void updateCycleDuration(BigDecimal average, BigDecimal median,
                                    BigDecimal shortest, BigDecimal longest) {
        this.averageCycleDurationDays = average;
        this.medianCycleDurationDays = median;
        this.shortestCycleDays = shortest;
        this.longestCycleDays = longest;
        calculateVariance();
        calculateHealthScore();
    }

    /**
     * Updates stage duration
     */
    public void updateStageDuration(String stage, BigDecimal duration) {
        if (this.stageDurations == null) {
            this.stageDurations = new HashMap<>();
        }
        this.stageDurations.put(stage, duration);
    }

    /**
     * Updates velocity metrics
     */
    public void updateVelocityMetrics(BigDecimal pipelineVelocity, BigDecimal dealVelocity) {
        this.pipelineVelocity = pipelineVelocity;
        this.dealVelocity = dealVelocity;
    }

    /**
     * Updates response time metrics
     */
    public void updateResponseTimeMetrics(BigDecimal responseTime, BigDecimal followUpTime) {
        this.responseTimeHours = responseTime;
        this.followUpTimeHours = followUpTime;
    }

    /**
     * Sets target cycle days and recalculates variance
     */
    public void setTargetCycleDays(BigDecimal target) {
        this.targetCycleDays = target;
        calculateVariance();
        calculateHealthScore();
    }

    /**
     * Calculates variance from target
     */
    private void calculateVariance() {
        if (targetCycleDays != null && targetCycleDays.compareTo(BigDecimal.ZERO) > 0
                && averageCycleDurationDays != null) {
            this.cycleVariance = averageCycleDurationDays
                    .subtract(targetCycleDays)
                    .divide(targetCycleDays, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
    }

    /**
     * Calculates health score based on cycle metrics
     */
    private void calculateHealthScore() {
        if (targetCycleDays == null || averageCycleDurationDays == null) {
            this.healthScore = CycleHealthScore.AVERAGE;
            return;
        }

        BigDecimal ratio = averageCycleDurationDays.divide(targetCycleDays, 2, BigDecimal.ROUND_HALF_UP);

        if (ratio.compareTo(BigDecimal.valueOf(0.8)) <= 0) {
            this.healthScore = CycleHealthScore.EXCELLENT;
        } else if (ratio.compareTo(BigDecimal.valueOf(1.0)) <= 0) {
            this.healthScore = CycleHealthScore.GOOD;
        } else if (ratio.compareTo(BigDecimal.valueOf(1.2)) <= 0) {
            this.healthScore = CycleHealthScore.AVERAGE;
        } else if (ratio.compareTo(BigDecimal.valueOf(1.5)) <= 0) {
            this.healthScore = CycleHealthScore.POOR;
        } else {
            this.healthScore = CycleHealthScore.CRITICAL;
        }
    }

    /**
     * Adds aging bucket data
     */
    public void addAgingBucket(String bucket, Integer count) {
        if (this.agingBuckets == null) {
            this.agingBuckets = new HashMap<>();
        }
        this.agingBuckets.put(bucket, count);
    }

    /**
     * Adds conversion by timeframe
     */
    public void addConversionByTimeframe(String timeframe, BigDecimal rate) {
        if (this.conversionByTimeframe == null) {
            this.conversionByTimeframe = new HashMap<>();
        }
        this.conversionByTimeframe.put(timeframe, rate);
    }

    /**
     * Adds metadata
     */
    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }

    /**
     * Checks if cycle is within target
     */
    public boolean isWithinTarget() {
        return targetCycleDays != null && averageCycleDurationDays != null
                && averageCycleDurationDays.compareTo(targetCycleDays) <= 0;
    }

    /**
     * Gets the percentage of deals over target
     */
    public BigDecimal getPercentageOverTarget() {
        if (targetCycleDays != null && targetCycleDays.compareTo(BigDecimal.ZERO) > 0
                && averageCycleDurationDays != null) {
            return averageCycleDurationDays
                    .divide(targetCycleDays, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .subtract(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO;
    }

    private static String generateId() {
        return "SCM-" + System.currentTimeMillis() + "-" +
               Integer.toHexString((int) (Math.random() * 0xFFFF)).toUpperCase();
    }
}
