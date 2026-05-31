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
 * Win/Loss Metric Domain Entity
 * Tracks win/loss analysis and reasons
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "win_loss_metrics")
public class WinLossMetric extends BaseEntity {

    private String winLossMetricId;

    @Indexed
    private String tenantId;

    @Indexed
    private String entityType; // TEAM, INDIVIDUAL, REGION, PRODUCT

    @Indexed
    private String entityId;

    private String entityName;

    private MetricPeriod period;

    private LocalDate periodStartDate;

    private LocalDate periodEndDate;

    // Win/Loss counts
    private Integer totalDeals;

    private Integer dealsWon;

    private Integer dealsLost;

    private Integer dealsInProgress;

    // Win/Loss rates
    private BigDecimal winRate;

    private BigDecimal lossRate;

    private BigDecimal winLossRatio;

    // Value metrics
    private BigDecimal totalWonValue;

    private BigDecimal totalLostValue;

    private BigDecimal averageWonValue;

    private BigDecimal averageLostValue;

    private BigDecimal winValueRate;

    // Loss analysis
    private Map<String, Integer> lossReasons;

    private Map<String, Integer> lossReasonCounts;

    private Map<String, BigDecimal> lossReasonValues;

    private List<String> topLossReasons;

    // Win analysis
    private Map<String, Integer> winFactors;

    private List<String> topWinFactors;

    // Competitor analysis
    private Map<String, Integer> competitorLosses;

    private Map<String, BigDecimal> competitorLossValues;

    private String topCompetitor;

    // Stage analysis
    private Map<String, Integer> stageLosses;

    private Map<String, BigDecimal> stageLossValues;

    // Product/service analysis
    private Map<String, BigDecimal> productWinRates;

    private Map<String, BigDecimal> productLossRates;

    // Trend analysis
    private BigDecimal winRateTrend;

    private BigDecimal trendPercentage;

    // Recovery metrics
    private Integer recoveredDeals;

    private BigDecimal recoveredValue;

    private BigDecimal recoveryRate;

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

    /**
     * Creates a new win/loss metric
     */
    public static WinLossMetric create(String tenantId, String entityType, String entityId,
                                      String entityName, MetricPeriod period,
                                      LocalDate periodStart, LocalDate periodEnd,
                                      String calculatedBy) {
        return WinLossMetric.builder()
                .winLossMetricId(generateId())
                .tenantId(tenantId)
                .entityType(entityType)
                .entityId(entityId)
                .entityName(entityName)
                .period(period)
                .periodStartDate(periodStart)
                .periodEndDate(periodEnd)
                .totalDeals(0)
                .dealsWon(0)
                .dealsLost(0)
                .dealsInProgress(0)
                .winRate(BigDecimal.ZERO)
                .lossRate(BigDecimal.ZERO)
                .winLossRatio(BigDecimal.ZERO)
                .totalWonValue(BigDecimal.ZERO)
                .totalLostValue(BigDecimal.ZERO)
                .averageWonValue(BigDecimal.ZERO)
                .averageLostValue(BigDecimal.ZERO)
                .winValueRate(BigDecimal.ZERO)
                .lossReasons(new HashMap<>())
                .lossReasonCounts(new HashMap<>())
                .lossReasonValues(new HashMap<>())
                .topLossReasons(new ArrayList<>())
                .winFactors(new HashMap<>())
                .topWinFactors(new ArrayList<>())
                .competitorLosses(new HashMap<>())
                .competitorLossValues(new HashMap<>())
                .stageLosses(new HashMap<>())
                .stageLossValues(new HashMap<>())
                .productWinRates(new HashMap<>())
                .productLossRates(new HashMap<>())
                .winRateTrend(BigDecimal.ZERO)
                .trendPercentage(BigDecimal.ZERO)
                .recoveredDeals(0)
                .recoveredValue(BigDecimal.ZERO)
                .recoveryRate(BigDecimal.ZERO)
                .metadata(new HashMap<>())
                .calculatedAt(Instant.now())
                .calculatedBy(calculatedBy)
                .build();
    }

    /**
     * Updates win/loss counts and calculates rates
     */
    public void updateWinLossCounts(Integer won, Integer lost, Integer inProgress) {
        this.dealsWon = won;
        this.dealsLost = lost;
        this.dealsInProgress = inProgress;
        this.totalDeals = won + lost + inProgress;
        calculateRates();
    }

    /**
     * Calculates win/loss rates
     */
    private void calculateRates() {
        int completedDeals = dealsWon + dealsLost;
        if (completedDeals > 0) {
            this.winRate = BigDecimal.valueOf(dealsWon)
                    .divide(BigDecimal.valueOf(completedDeals), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            this.lossRate = BigDecimal.valueOf(dealsLost)
                    .divide(BigDecimal.valueOf(completedDeals), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        if (dealsLost > 0) {
            this.winLossRatio = BigDecimal.valueOf(dealsWon)
                    .divide(BigDecimal.valueOf(dealsLost), 2, BigDecimal.ROUND_HALF_UP);
        } else if (dealsWon > 0) {
            this.winLossRatio = new BigDecimal("999.99"); // Infinite ratio representation
        }
    }

    /**
     * Updates value metrics
     */
    public void updateValueMetrics(BigDecimal wonValue, BigDecimal lostValue) {
        this.totalWonValue = wonValue != null ? wonValue : BigDecimal.ZERO;
        this.totalLostValue = lostValue != null ? lostValue : BigDecimal.ZERO;

        if (dealsWon != null && dealsWon > 0) {
            this.averageWonValue = totalWonValue.divide(BigDecimal.valueOf(dealsWon), 2, BigDecimal.ROUND_HALF_UP);
        }

        if (dealsLost != null && dealsLost > 0) {
            this.averageLostValue = totalLostValue.divide(BigDecimal.valueOf(dealsLost), 2, BigDecimal.ROUND_HALF_UP);
        }

        BigDecimal totalValue = totalWonValue.add(totalLostValue);
        if (totalValue.compareTo(BigDecimal.ZERO) > 0) {
            this.winValueRate = totalWonValue.divide(totalValue, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
    }

    /**
     * Adds a loss reason
     */
    public void addLossReason(String reason, Integer count, BigDecimal value) {
        if (this.lossReasonCounts == null) {
            this.lossReasonCounts = new HashMap<>();
        }
        if (this.lossReasonValues == null) {
            this.lossReasonValues = new HashMap<>();
        }

        this.lossReasonCounts.put(reason, this.lossReasonCounts.getOrDefault(reason, 0) + count);
        this.lossReasonValues.put(reason, this.lossReasonValues.getOrDefault(reason, BigDecimal.ZERO).add(value));

        updateTopLossReasons();
    }

    /**
     * Updates top loss reasons list
     */
    private void updateTopLossReasons() {
        if (lossReasonCounts == null || lossReasonCounts.isEmpty()) {
            return;
        }

        this.topLossReasons = lossReasonCounts.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * Adds a win factor
     */
    public void addWinFactor(String factor, Integer count) {
        if (this.winFactors == null) {
            this.winFactors = new HashMap<>();
        }

        this.winFactors.put(factor, this.winFactors.getOrDefault(factor, 0) + count);
        updateTopWinFactors();
    }

    /**
     * Updates top win factors list
     */
    private void updateTopWinFactors() {
        if (winFactors == null || winFactors.isEmpty()) {
            return;
        }

        this.topWinFactors = winFactors.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * Adds competitor loss data
     */
    public void addCompetitorLoss(String competitor, Integer count, BigDecimal value) {
        if (this.competitorLosses == null) {
            this.competitorLosses = new HashMap<>();
        }
        if (this.competitorLossValues == null) {
            this.competitorLossValues = new HashMap<>();
        }

        this.competitorLosses.put(competitor, this.competitorLosses.getOrDefault(competitor, 0) + count);
        this.competitorLossValues.put(competitor,
                this.competitorLossValues.getOrDefault(competitor, BigDecimal.ZERO).add(value));

        updateTopCompetitor();
    }

    /**
     * Updates top competitor
     */
    private void updateTopCompetitor() {
        if (competitorLosses == null || competitorLosses.isEmpty()) {
            return;
        }

        this.topCompetitor = competitorLosses.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Adds stage loss data
     */
    public void addStageLoss(String stage, Integer count, BigDecimal value) {
        if (this.stageLosses == null) {
            this.stageLosses = new HashMap<>();
        }
        if (this.stageLossValues == null) {
            this.stageLossValues = new HashMap<>();
        }

        this.stageLosses.put(stage, this.stageLosses.getOrDefault(stage, 0) + count);
        this.stageLossValues.put(stage,
                this.stageLossValues.getOrDefault(stage, BigDecimal.ZERO).add(value));
    }

    /**
     * Updates product win/loss rates
     */
    public void updateProductWinRate(String productId, BigDecimal winRate, BigDecimal lossRate) {
        if (this.productWinRates == null) {
            this.productWinRates = new HashMap<>();
        }
        if (this.productLossRates == null) {
            this.productLossRates = new HashMap<>();
        }

        this.productWinRates.put(productId, winRate);
        this.productLossRates.put(productId, lossRate);
    }

    /**
     * Updates recovery metrics
     */
    public void updateRecoveryMetrics(Integer recovered, BigDecimal recoveredValue) {
        this.recoveredDeals = recovered;
        this.recoveredValue = recoveredValue;

        if (dealsLost != null && dealsLost > 0) {
            this.recoveryRate = BigDecimal.valueOf(recovered)
                    .divide(BigDecimal.valueOf(dealsLost), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
    }

    /**
     * Sets trend information
     */
    public void setTrend(BigDecimal previousWinRate) {
        if (previousWinRate != null && winRate != null) {
            this.winRateTrend = winRate.subtract(previousWinRate);
            if (previousWinRate.compareTo(BigDecimal.ZERO) > 0) {
                this.trendPercentage = winRateTrend
                        .divide(previousWinRate, 2, BigDecimal.ROUND_HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            }
        }
    }

    /**
     * Gets the primary loss reason
     */
    public String getPrimaryLossReason() {
        return topLossReasons != null && !topLossReasons.isEmpty()
                ? topLossReasons.get(0)
                : null;
    }

    /**
     * Gets loss percentage for a specific reason
     */
    public BigDecimal getLossReasonPercentage(String reason) {
        if (lossReasonCounts == null || totalDeals == null || totalDeals == 0) {
            return BigDecimal.ZERO;
        }
        Integer count = lossReasonCounts.get(reason);
        if (count == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(count)
                .divide(BigDecimal.valueOf(totalDeals), 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    /**
     * Checks if win rate is improving
     */
    public boolean isWinRateImproving() {
        return winRateTrend != null && winRateTrend.compareTo(BigDecimal.ZERO) > 0;
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

    private static String generateId() {
        return "WLM-" + System.currentTimeMillis() + "-" +
               Integer.toHexString((int) (Math.random() * 0xFFFF)).toUpperCase();
    }
}
