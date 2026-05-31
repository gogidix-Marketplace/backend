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
 * Pipeline Metric Domain Entity
 * Tracks pipeline value, velocity, and health metrics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "pipeline_metrics")
public class PipelineMetric extends BaseEntity {

    private String pipelineMetricId;

    @Indexed
    private String tenantId;

    @Indexed
    private String entityType; // TEAM, INDIVIDUAL, REGION

    @Indexed
    private String entityId;

    private String entityName;

    private MetricPeriod period;

    private LocalDate periodStartDate;

    private LocalDate periodEndDate;

    // Pipeline value metrics
    private BigDecimal totalPipelineValue;

    private BigDecimal openPipelineValue;

    private BigDecimal wonPipelineValue;

    private BigDecimal lostPipelineValue;

    private BigDecimal stagnantPipelineValue;

    // Pipeline coverage
    private BigDecimal pipelineCoverage;

    private BigDecimal pipelineCoverageRatio;

    // Stage distribution
    private Map<String, StageMetric> stageMetrics;

    // Velocity metrics
    private BigDecimal pipelineVelocity;

    private BigDecimal averageStageDuration;

    private BigDecimal pipelineThroughput;

    // Pipeline health
    private PipelineHealth health;

    private Integer healthScore;

    private List<String> healthIssues;

    // Movement metrics
    private Integer dealsEntered;

    private Integer dealsAdvanced;

    private Integer dealsWon;

    private Integer dealsLost;

    private Integer dealsStagnant;

    // Forecast metrics
    private BigDecimal forecastedValue;

    private BigDecimal forecastAccuracy;

    private BigDecimal weightedPipelineValue;

    // Risk metrics
    private BigDecimal atRiskValue;

    private Integer atRiskDeals;

    private Map<String, BigDecimal> riskDistribution;

    private Instant calculatedAt;

    private String calculatedBy;

    public enum MetricPeriod {
        DAILY,
        WEEKLY,
        MONTHLY,
        QUARTERLY,
        YEARLY
    }

    public enum PipelineHealth {
        HEALTHY,
        ATTENTION_NEEDED,
        AT_RISK,
        CRITICAL
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StageMetric {
        private String stageName;
        private BigDecimal value;
        private Integer dealCount;
        private BigDecimal averageDealSize;
        private BigDecimal conversionRate;
        private BigDecimal averageDurationDays;
    }

    /**
     * Creates a new pipeline metric
     */
    public static PipelineMetric create(String tenantId, String entityType, String entityId,
                                       String entityName, MetricPeriod period,
                                       LocalDate periodStart, LocalDate periodEnd,
                                       String calculatedBy) {
        return PipelineMetric.builder()
                .pipelineMetricId(generateId())
                .tenantId(tenantId)
                .entityType(entityType)
                .entityId(entityId)
                .entityName(entityName)
                .period(period)
                .periodStartDate(periodStart)
                .periodEndDate(periodEnd)
                .totalPipelineValue(BigDecimal.ZERO)
                .openPipelineValue(BigDecimal.ZERO)
                .wonPipelineValue(BigDecimal.ZERO)
                .lostPipelineValue(BigDecimal.ZERO)
                .stagnantPipelineValue(BigDecimal.ZERO)
                .pipelineCoverage(BigDecimal.ZERO)
                .pipelineCoverageRatio(BigDecimal.ZERO)
                .stageMetrics(new HashMap<>())
                .pipelineVelocity(BigDecimal.ZERO)
                .averageStageDuration(BigDecimal.ZERO)
                .pipelineThroughput(BigDecimal.ZERO)
                .health(PipelineHealth.HEALTHY)
                .healthScore(100)
                .healthIssues(new ArrayList<>())
                .dealsEntered(0)
                .dealsAdvanced(0)
                .dealsWon(0)
                .dealsLost(0)
                .dealsStagnant(0)
                .forecastedValue(BigDecimal.ZERO)
                .forecastAccuracy(BigDecimal.ZERO)
                .weightedPipelineValue(BigDecimal.ZERO)
                .atRiskValue(BigDecimal.ZERO)
                .atRiskDeals(0)
                .riskDistribution(new HashMap<>())
                .calculatedAt(Instant.now())
                .calculatedBy(calculatedBy)
                .build();
    }

    /**
     * Updates pipeline value metrics
     */
    public void updatePipelineValue(BigDecimal total, BigDecimal open, BigDecimal won,
                                   BigDecimal lost, BigDecimal stagnant) {
        this.totalPipelineValue = total;
        this.openPipelineValue = open;
        this.wonPipelineValue = won;
        this.lostPipelineValue = lost;
        this.stagnantPipelineValue = stagnant;
    }

    /**
     * Updates pipeline coverage ratio
     */
    public void updatePipelineCoverage(BigDecimal quota, BigDecimal pipeline) {
        if (quota != null && quota.compareTo(BigDecimal.ZERO) > 0) {
            this.pipelineCoverage = pipeline;
            this.pipelineCoverageRatio = pipeline.divide(quota, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
    }

    /**
     * Adds or updates a stage metric
     */
    public void updateStageMetric(String stageName, BigDecimal value, Integer dealCount,
                                 BigDecimal avgDealSize, BigDecimal conversionRate,
                                 BigDecimal avgDuration) {
        if (this.stageMetrics == null) {
            this.stageMetrics = new HashMap<>();
        }
        StageMetric metric = StageMetric.builder()
                .stageName(stageName)
                .value(value)
                .dealCount(dealCount)
                .averageDealSize(avgDealSize)
                .conversionRate(conversionRate)
                .averageDurationDays(avgDuration)
                .build();
        this.stageMetrics.put(stageName, metric);
    }

    /**
     * Updates deal movement metrics
     */
    public void updateDealMovement(Integer entered, Integer advanced, Integer won,
                                  Integer lost, Integer stagnant) {
        this.dealsEntered = entered;
        this.dealsAdvanced = advanced;
        this.dealsWon = won;
        this.dealsLost = lost;
        this.dealsStagnant = stagnant;
    }

    /**
     * Updates velocity metrics
     */
    public void updateVelocity(BigDecimal velocity, BigDecimal avgStageDuration,
                              BigDecimal throughput) {
        this.pipelineVelocity = velocity;
        this.averageStageDuration = avgStageDuration;
        this.pipelineThroughput = throughput;
    }

    /**
     * Updates forecast metrics
     */
    public void updateForecast(BigDecimal forecasted, BigDecimal accuracy, BigDecimal weighted) {
        this.forecastedValue = forecasted;
        this.forecastAccuracy = accuracy;
        this.weightedPipelineValue = weighted;
    }

    /**
     * Updates risk metrics
     */
    public void updateRiskMetrics(BigDecimal atRiskValue, Integer atRiskDeals,
                                  Map<String, BigDecimal> riskDistribution) {
        this.atRiskValue = atRiskValue;
        this.atRiskDeals = atRiskDeals;
        this.riskDistribution = riskDistribution != null ? riskDistribution : new HashMap<>();
    }

    /**
     * Calculates pipeline health score
     */
    public void calculateHealthScore(BigDecimal targetCoverage) {
        int score = 100;

        // Coverage penalty
        if (targetCoverage != null && pipelineCoverageRatio != null) {
            if (pipelineCoverageRatio.compareTo(targetCoverage.multiply(BigDecimal.valueOf(0.5))) < 0) {
                score -= 30;
            } else if (pipelineCoverageRatio.compareTo(targetCoverage.multiply(BigDecimal.valueOf(0.75))) < 0) {
                score -= 15;
            }
        }

        // Stagnant deals penalty
        if (dealsStagnant != null && dealsEntered != null && dealsEntered > 0) {
            BigDecimal stagnantRatio = BigDecimal.valueOf(dealsStagnant)
                    .divide(BigDecimal.valueOf(dealsEntered), 2, BigDecimal.ROUND_HALF_UP);
            if (stagnantRatio.compareTo(BigDecimal.valueOf(0.3)) > 0) {
                score -= 20;
            } else if (stagnantRatio.compareTo(BigDecimal.valueOf(0.15)) > 0) {
                score -= 10;
            }
        }

        // At-risk value penalty
        if (atRiskValue != null && openPipelineValue != null && openPipelineValue.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal atRiskRatio = atRiskValue.divide(openPipelineValue, 2, BigDecimal.ROUND_HALF_UP);
            if (atRiskRatio.compareTo(BigDecimal.valueOf(0.4)) > 0) {
                score -= 25;
            } else if (atRiskRatio.compareTo(BigDecimal.valueOf(0.2)) > 0) {
                score -= 10;
            }
        }

        this.healthScore = Math.max(0, score);
        updateHealthStatus();
    }

    /**
     * Updates health status based on score
     */
    private void updateHealthStatus() {
        if (healthScore >= 80) {
            this.health = PipelineHealth.HEALTHY;
            this.healthIssues.clear();
        } else if (healthScore >= 60) {
            this.health = PipelineHealth.ATTENTION_NEEDED;
            addHealthIssue("Pipeline score below optimal");
        } else if (healthScore >= 40) {
            this.health = PipelineHealth.AT_RISK;
            addHealthIssue("Pipeline requires immediate attention");
        } else {
            this.health = PipelineHealth.CRITICAL;
            addHealthIssue("Critical pipeline health");
        }
    }

    /**
     * Adds a health issue
     */
    public void addHealthIssue(String issue) {
        if (this.healthIssues == null) {
            this.healthIssues = new ArrayList<>();
        }
        if (!this.healthIssues.contains(issue)) {
            this.healthIssues.add(issue);
        }
    }

    /**
     * Gets conversion rate for the pipeline
     */
    public BigDecimal getOverallConversionRate() {
        if (dealsEntered != null && dealsWon != null && dealsEntered > 0) {
            return BigDecimal.valueOf(dealsWon)
                    .divide(BigDecimal.valueOf(dealsEntered), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO;
    }

    /**
     * Checks if pipeline needs attention
     */
    public boolean needsAttention() {
        return health != PipelineHealth.HEALTHY;
    }

    private static String generateId() {
        return "PLM-" + System.currentTimeMillis() + "-" +
               Integer.toHexString((int) (Math.random() * 0xFFFF)).toUpperCase();
    }
}
