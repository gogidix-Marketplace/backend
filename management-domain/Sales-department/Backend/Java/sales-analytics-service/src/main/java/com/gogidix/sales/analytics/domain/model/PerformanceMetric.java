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
 * Performance Metric Domain Entity
 * Tracks sales performance metrics for teams and individuals
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "performance_metrics")
public class PerformanceMetric extends BaseEntity {

    private String performanceMetricId;

    @Indexed
    private String tenantId;

    @Indexed
    private String entityType; // TEAM, INDIVIDUAL, REGION, PRODUCT

    @Indexed
    private String entityId; // teamId, repId, regionId, productId

    private String entityName;

    private PerformancePeriod period;

    private LocalDate periodStartDate;

    private LocalDate periodEndDate;

    private BigDecimal totalRevenue;

    private BigDecimal targetRevenue;

    private BigDecimal revenueAchievedPercentage;

    private Integer dealsWon;

    private Integer dealsLost;

    private Integer totalDeals;

    private BigDecimal winRate;

    private BigDecimal averageDealSize;

    private BigDecimal quotaAchievement;

    private BigDecimal yearOverYearGrowth;

    private Integer rank;

    private Integer percentile;

    private MetricStatus status;

    private Map<String, Object> breakdown;

    private Map<String, Object> kpis;

    private List<String> achievementBadges;

    private String managerId;

    private String regionId;

    private Instant calculatedAt;

    private String calculatedBy;

    public enum PerformancePeriod {
        DAILY,
        WEEKLY,
        MONTHLY,
        QUARTERLY,
        YEARLY,
        CUSTOM
    }

    public enum MetricStatus {
        CALCULATING,
        COMPLETED,
        FAILED,
        PENDING
    }

    /**
     * Creates a new performance metric
     */
    public static PerformanceMetric create(String tenantId, String entityType, String entityId,
                                          String entityName, PerformancePeriod period,
                                          LocalDate periodStart, LocalDate periodEnd,
                                          String calculatedBy) {
        return PerformanceMetric.builder()
                .performanceMetricId(generateId())
                .tenantId(tenantId)
                .entityType(entityType)
                .entityId(entityId)
                .entityName(entityName)
                .period(period)
                .periodStartDate(periodStart)
                .periodEndDate(periodEnd)
                .totalRevenue(BigDecimal.ZERO)
                .targetRevenue(BigDecimal.ZERO)
                .revenueAchievedPercentage(BigDecimal.ZERO)
                .dealsWon(0)
                .dealsLost(0)
                .totalDeals(0)
                .winRate(BigDecimal.ZERO)
                .averageDealSize(BigDecimal.ZERO)
                .quotaAchievement(BigDecimal.ZERO)
                .yearOverYearGrowth(BigDecimal.ZERO)
                .status(MetricStatus.PENDING)
                .breakdown(new HashMap<>())
                .kpis(new HashMap<>())
                .achievementBadges(new ArrayList<>())
                .calculatedAt(Instant.now())
                .calculatedBy(calculatedBy)
                .build();
    }

    /**
     * Updates revenue metrics
     */
    public void updateRevenueMetrics(BigDecimal totalRevenue, BigDecimal targetRevenue) {
        this.totalRevenue = totalRevenue;
        this.targetRevenue = targetRevenue;

        if (targetRevenue != null && targetRevenue.compareTo(BigDecimal.ZERO) > 0) {
            this.revenueAchievedPercentage = totalRevenue
                    .divide(targetRevenue, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
        this.calculatedAt = Instant.now();
    }

    /**
     * Updates deal metrics and calculates win rate
     */
    public void updateDealMetrics(Integer dealsWon, Integer dealsLost) {
        this.dealsWon = dealsWon;
        this.dealsLost = dealsLost;
        this.totalDeals = dealsWon + dealsLost;

        if (this.totalDeals > 0) {
            this.winRate = BigDecimal.valueOf(dealsWon)
                    .divide(BigDecimal.valueOf(this.totalDeals), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
        this.calculatedAt = Instant.now();
    }

    /**
     * Updates average deal size
     */
    public void updateAverageDealSize(BigDecimal totalRevenue, Integer dealCount) {
        if (dealCount != null && dealCount > 0 && totalRevenue != null) {
            this.averageDealSize = totalRevenue
                    .divide(BigDecimal.valueOf(dealCount), 2, BigDecimal.ROUND_HALF_UP);
        }
    }

    /**
     * Updates quota achievement percentage
     */
    public void updateQuotaAchievement(BigDecimal quotaAchieved, BigDecimal quotaTarget) {
        if (quotaTarget != null && quotaTarget.compareTo(BigDecimal.ZERO) > 0) {
            this.quotaAchievement = quotaAchieved
                    .divide(quotaTarget, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
    }

    /**
     * Adds a KPI to the metric
     */
    public void addKpi(String key, Object value) {
        if (this.kpis == null) {
            this.kpis = new HashMap<>();
        }
        this.kpis.put(key, value);
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
     * Adds an achievement badge
     */
    public void addAchievementBadge(String badge) {
        if (this.achievementBadges == null) {
            this.achievementBadges = new ArrayList<>();
        }
        if (!this.achievementBadges.contains(badge)) {
            this.achievementBadges.add(badge);
        }
    }

    /**
     * Updates ranking information
     */
    public void updateRank(Integer rank, Integer totalEntities) {
        this.rank = rank;
        if (totalEntities != null && totalEntities > 0) {
            this.percentile = BigDecimal.valueOf(100)
                    .subtract(BigDecimal.valueOf(rank - 1)
                            .divide(BigDecimal.valueOf(totalEntities), 2, BigDecimal.ROUND_HALF_UP)
                            .multiply(BigDecimal.valueOf(100)))
                    .intValue();
        }
    }

    /**
     * Checks if target is achieved
     */
    public boolean isTargetAchieved() {
        return quotaAchievement != null && quotaAchievement.compareTo(BigDecimal.valueOf(100)) >= 0;
    }

    /**
     * Marks calculation as completed
     */
    public void markCompleted() {
        this.status = MetricStatus.COMPLETED;
        this.calculatedAt = Instant.now();
    }

    /**
     * Marks calculation as failed
     */
    public void markFailed() {
        this.status = MetricStatus.FAILED;
    }

    private static String generateId() {
        return "PM-" + System.currentTimeMillis() + "-" +
               Integer.toHexString((int) (Math.random() * 0xFFFF)).toUpperCase();
    }
}
