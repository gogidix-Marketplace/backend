package com.gogidix.sales.dashboard.domain.model;

import com.gogidix.sales.dashboard.domain.event.RollupCompletedEvent;
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
 * Metric Rollup Domain Entity
 * Stores pre-calculated rollup metrics for efficient dashboard queries
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "metric_rollups")
public class MetricRollup extends BaseEntity {

    private String rollupId;
    private String tenantId;
    private RollupType rollupType;
    private String rollupKey; // e.g., region code, product ID
    private String rollupName;
    private TimePeriod timePeriod;

    // Rollup hierarchy
    private String parentRollupId;
    private List<String> childRollupIds;

    // Metrics
    private RollupMetrics metrics;

    // Targets and quotas
    private Map<String, Target> targets;

    // Performance indicators
    private PerformanceIndicators performance;

    // Metadata
    private Instant rollupTime;
    private Integer dataVersion;
    private String sourceAggregationIds;
    private Boolean isRealtime;
    private Integer lagSeconds;

    // Domain events
    @Builder.Default
    private List<RollupCompletedEvent> domainEvents = new ArrayList<>();

    public enum RollupType {
        GLOBAL, REGIONAL, PRODUCT, SALES_REP, CUSTOMER_SEGMENT, TIME_BASED, CUSTOM
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimePeriod {
        private LocalDate startDate;
        private LocalDate endDate;
        private String periodType;
        private Integer periodValue;
        private Integer year;
        private String quarter;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RollupMetrics {
        private Money revenue;
        private Integer deals;
        private BigDecimal winRate;
        private Money pipelineValue;
        private Integer opportunities;
        private Money averageDealSize;
        private BigDecimal growthRate;
        private Integer newCustomers;
        private BigDecimal customerSatisfaction;
        private Money margin;
        private BigDecimal marginPercentage;
        private Integer activitiesCompleted;
        private BigDecimal forecastAccuracy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Target {
        private String targetType; // REVENUE, DEALS, ACTIVITIES
        private Money targetValue;
        private Money currentValue;
        private BigDecimal achievementPercentage;
        private BigDecimal remaining;
        private Boolean isOnTrack;
        private LocalDate targetDate;
        private String frequency; // DAILY, WEEKLY, MONTHLY, QUARTERLY
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceIndicators {
        private String overallStatus; // ON_TRACK, AT_RISK, BEHIND, AHEAD
        private BigDecimal score;
        private String trend; // IMPROVING, STABLE, DECLINING
        private List<String> strengths;
        private List<String> weaknesses;
        private List<String> opportunities;
        private List<String> threats;
        private String recommendation;
        private Integer riskLevel; // 1-5
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Money {
        private BigDecimal amount;
        private String currency;
    }

    /**
     * Creates a new metric rollup
     */
    public static MetricRollup create(String tenantId, RollupType type, String key, String name,
                                      TimePeriod period, String currency) {
        MetricRollup rollup = MetricRollup.builder()
                .rollupId(java.util.UUID.randomUUID().toString())
                .tenantId(tenantId)
                .rollupType(type)
                .rollupKey(key)
                .rollupName(name)
                .timePeriod(period)
                .metrics(createEmptyMetrics(currency))
                .targets(new HashMap<>())
                .childRollupIds(new ArrayList<>())
                .performance(createEmptyPerformance())
                .rollupTime(Instant.now())
                .dataVersion(1)
                .isRealtime(false)
                .lagSeconds(0)
                .build();

        rollup.addDomainEvent(RollupCompletedEvent.builder()
                .rollupId(rollup.getRollupId())
                .tenantId(tenantId)
                .eventType("ROLLUP_CREATED")
                .timestamp(Instant.now())
                .build());

        return rollup;
    }

    /**
     * Updates metrics
     */
    public void updateMetrics(RollupMetrics metrics) {
        this.metrics = metrics;
        this.rollupTime = Instant.now();
        this.updatePerformanceIndicators();
    }

    /**
     * Adds or updates a target
     */
    public void updateTarget(String targetType, Money targetValue, Money currentValue) {
        if (this.targets == null) {
            this.targets = new HashMap<>();
        }

        BigDecimal achievement = calculateAchievement(targetValue.getAmount(), currentValue.getAmount());
        BigDecimal remaining = targetValue.getAmount().subtract(currentValue.getAmount());
        boolean isOnTrack = achievement.compareTo(new BigDecimal("75")) >= 0;

        Target target = Target.builder()
                .targetType(targetType)
                .targetValue(targetValue)
                .currentValue(currentValue)
                .achievementPercentage(achievement)
                .remaining(remaining)
                .isOnTrack(isOnTrack)
                .build();

        this.targets.put(targetType, target);
        this.updatePerformanceIndicators();
    }

    /**
     * Adds a child rollup
     */
    public void addChildRollup(String childRollupId) {
        if (this.childRollupIds == null) {
            this.childRollupIds = new ArrayList<>();
        }
        if (!this.childRollupIds.contains(childRollupId)) {
            this.childRollupIds.add(childRollupId);
        }
    }

    /**
     * Aggregates data from child rollups
     */
    public void aggregateFromChildren(List<MetricRollup> children) {
        if (children == null || children.isEmpty()) {
            return;
        }

        BigDecimal totalRevenue = children.stream()
                .filter(c -> c.getMetrics() != null && c.getMetrics().getRevenue() != null)
                .map(c -> c.getMetrics().getRevenue().getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalDeals = children.stream()
                .filter(c -> c.getMetrics() != null)
                .mapToInt(c -> c.getMetrics().getDeals() != null ? c.getMetrics().getDeals() : 0)
                .sum();

        int totalOpportunities = children.stream()
                .filter(c -> c.getMetrics() != null)
                .mapToInt(c -> c.getMetrics().getOpportunities() != null ? c.getMetrics().getOpportunities() : 0)
                .sum();

        BigDecimal avgWinRate = children.stream()
                .filter(c -> c.getMetrics() != null && c.getMetrics().getWinRate() != null)
                .map(c -> c.getMetrics().getWinRate())
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(children.size()), 2, java.math.RoundingMode.HALF_UP);

        String currency = children.stream()
                .filter(c -> c.getMetrics() != null && c.getMetrics().getRevenue() != null)
                .map(c -> c.getMetrics().getRevenue().getCurrency())
                .findFirst()
                .orElse("USD");

        RollupMetrics aggregatedMetrics = RollupMetrics.builder()
                .revenue(Money.builder().amount(totalRevenue).currency(currency).build())
                .deals(totalDeals)
                .winRate(avgWinRate)
                .opportunities(totalOpportunities)
                .build();

        this.updateMetrics(aggregatedMetrics);

        addDomainEvent(RollupCompletedEvent.builder()
                .rollupId(this.rollupId)
                .tenantId(this.tenantId)
                .eventType("ROLLUP_AGGREGATED")
                .timestamp(Instant.now())
                .build());
    }

    /**
     * Increments version
     */
    public void incrementVersion() {
        this.dataVersion = (this.dataVersion == null ? 0 : this.dataVersion) + 1;
        this.rollupTime = Instant.now();
    }

    /**
     * Gets target by type
     */
    public Target getTarget(String targetType) {
        if (this.targets == null) {
            return null;
        }
        return this.targets.get(targetType);
    }

    /**
     * Checks if all targets are on track
     */
    public boolean isOverallOnTrack() {
        if (this.targets == null || this.targets.isEmpty()) {
            return true;
        }
        return this.targets.values().stream()
                .allMatch(t -> t.getIsOnTrack() != null && t.getIsOnTrack());
    }

    /**
     * Gets overall achievement percentage
     */
    public BigDecimal getOverallAchievement() {
        if (this.targets == null || this.targets.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return this.targets.values().stream()
                .map(Target::getAchievementPercentage)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(this.targets.size()), 2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Calculates rollup score (0-100)
     */
    public BigDecimal calculateScore() {
        BigDecimal achievementScore = getOverallAchievement();
        BigDecimal growthScore = this.metrics != null && this.metrics.getGrowthRate() != null ?
                this.metrics.getGrowthRate().max(BigDecimal.ZERO).min(new BigDecimal("100")) :
                BigDecimal.ZERO;

        return achievementScore.multiply(new BigDecimal("0.7"))
                .add(growthScore.multiply(new BigDecimal("0.3")))
                .min(new BigDecimal("100"));
    }

    private void updatePerformanceIndicators() {
        if (this.performance == null) {
            this.performance = createEmptyPerformance();
        }

        BigDecimal score = calculateScore();
        this.performance.setScore(score);

        // Determine overall status
        if (score.compareTo(new BigDecimal("90")) >= 0) {
            this.performance.setOverallStatus("AHEAD");
        } else if (score.compareTo(new BigDecimal("75")) >= 0) {
            this.performance.setOverallStatus("ON_TRACK");
        } else if (score.compareTo(new BigDecimal("50")) >= 0) {
            this.performance.setOverallStatus("AT_RISK");
        } else {
            this.performance.setOverallStatus("BEHIND");
        }

        // Determine trend
        if (this.metrics != null && this.metrics.getGrowthRate() != null) {
            BigDecimal growth = this.metrics.getGrowthRate();
            if (growth.compareTo(new BigDecimal("5")) > 0) {
                this.performance.setTrend("IMPROVING");
            } else if (growth.compareTo(new BigDecimal("-2")) < 0) {
                this.performance.setTrend("DECLINING");
            } else {
                this.performance.setTrend("STABLE");
            }
        }

        // Set risk level
        int riskLevel = 5 - score.divide(new BigDecimal("20"), 0, java.math.RoundingMode.UP).intValue();
        this.performance.setRiskLevel(Math.max(1, Math.min(5, riskLevel)));

        // Generate recommendation
        this.performance.setRecommendation(generateRecommendation(score));
    }

    private String generateRecommendation(BigDecimal score) {
        if (score.compareTo(new BigDecimal("90")) >= 0) {
            return "Excellent performance. Consider raising targets.";
        } else if (score.compareTo(new BigDecimal("75")) >= 0) {
            return "Good progress. Maintain current strategies.";
        } else if (score.compareTo(new BigDecimal("50")) >= 0) {
            return "Review pipeline and focus on closing opportunities.";
        } else {
            return "Critical intervention required. Assess all active deals.";
        }
    }

    private BigDecimal calculateAchievement(BigDecimal target, BigDecimal current) {
        if (target == null || target.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return current.divide(target, 4, java.math.RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
    }

    private static RollupMetrics createEmptyMetrics(String currency) {
        return RollupMetrics.builder()
                .revenue(Money.builder().amount(BigDecimal.ZERO).currency(currency).build())
                .deals(0)
                .winRate(BigDecimal.ZERO)
                .pipelineValue(Money.builder().amount(BigDecimal.ZERO).currency(currency).build())
                .opportunities(0)
                .averageDealSize(Money.builder().amount(BigDecimal.ZERO).currency(currency).build())
                .growthRate(BigDecimal.ZERO)
                .newCustomers(0)
                .customerSatisfaction(BigDecimal.ZERO)
                .margin(Money.builder().amount(BigDecimal.ZERO).currency(currency).build())
                .marginPercentage(BigDecimal.ZERO)
                .activitiesCompleted(0)
                .forecastAccuracy(BigDecimal.ZERO)
                .build();
    }

    private static PerformanceIndicators createEmptyPerformance() {
        return PerformanceIndicators.builder()
                .overallStatus("ON_TRACK")
                .score(BigDecimal.ZERO)
                .trend("STABLE")
                .strengths(new ArrayList<>())
                .weaknesses(new ArrayList<>())
                .opportunities(new ArrayList<>())
                .threats(new ArrayList<>())
                .recommendation("No data available")
                .riskLevel(3)
                .build();
    }

    public void addDomainEvent(RollupCompletedEvent event) {
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
