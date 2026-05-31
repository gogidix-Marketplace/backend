package com.gogidix.sales.dashboard.domain.model;

import com.gogidix.sales.dashboard.domain.event.AggregationCompletedEvent;
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
import java.util.List;

/**
 * Sales Aggregation Domain Entity
 * Stores aggregated sales data for different dimensions and time periods
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "sales_aggregations")
public class SalesAggregation extends BaseEntity {

    private String aggregationId;
    private String tenantId;
    private AggregationType aggregationType;
    private AggregationDimension dimension;
    private String dimensionValue;
    private TimePeriod timePeriod;

    // Aggregated metrics
    private AggregatedMetrics metrics;

    // Breakdown data
    private List<BreakdownItem> breakdown;

    // Comparison data
    private ComparisonData comparisonData;

    // Metadata
    private String dataSource;
    private Instant aggregationTime;
    private Integer dataVersion;
    private Boolean isComplete;

    // Domain events
    @Builder.Default
    private List<AggregationCompletedEvent> domainEvents = new ArrayList<>();

    public enum AggregationType {
        DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY, CUSTOM
    }

    public enum AggregationDimension {
        REGION, PRODUCT, SALES_REP, CUSTOMER_SEGMENT, INDUSTRY, DEAL_SIZE, CUSTOM
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimePeriod {
        private LocalDate startDate;
        private LocalDate endDate;
        private String periodType; // DAY, WEEK, MONTH, QUARTER, YEAR
        private Integer periodValue; // e.g., week number, month number
        private Integer year;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AggregatedMetrics {
        private Money totalRevenue;
        private Money targetRevenue;
        private BigDecimal achievementPercentage;
        private Integer totalDeals;
        private Integer wonDeals;
        private BigDecimal winRate;
        private Money averageDealSize;
        private Money averageDiscount;
        private Integer newOpportunities;
        private Integer closedOpportunities;
        private Money pipelineValue;
        private Money weightedPipeline;
        private BigDecimal conversionRate;
        private Integer salesActivities;
        private BigDecimal customerAcquisitionCost;
        private Money customerLifetimeValue;
        private Integer activeCustomers;
        private Integer churnedCustomers;
        private BigDecimal churnRate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BreakdownItem {
        private String key;
        private String label;
        private Money value;
        private Integer count;
        private BigDecimal percentage;
        private String color;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComparisonData {
        private Money previousPeriodRevenue;
        private BigDecimal growthRate;
        private BigDecimal growthValue;
        private Integer previousPeriodDeals;
        private BigDecimal dealGrowthRate;
        private Money difference;
        private String trend; // UP, DOWN, STABLE
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
     * Creates a new sales aggregation
     */
    public static SalesAggregation create(String tenantId, AggregationType type,
                                          AggregationDimension dimension, String dimensionValue,
                                          TimePeriod timePeriod, String currency) {
        SalesAggregation aggregation = SalesAggregation.builder()
                .aggregationId(java.util.UUID.randomUUID().toString())
                .tenantId(tenantId)
                .aggregationType(type)
                .dimension(dimension)
                .dimensionValue(dimensionValue)
                .timePeriod(timePeriod)
                .metrics(createEmptyMetrics(currency))
                .breakdown(new ArrayList<>())
                .isComplete(false)
                .aggregationTime(Instant.now())
                .dataVersion(1)
                .build();

        aggregation.addDomainEvent(AggregationCompletedEvent.builder()
                .aggregationId(aggregation.getAggregationId())
                .tenantId(tenantId)
                .eventType("AGGREGATION_STARTED")
                .timestamp(Instant.now())
                .build());

        return aggregation;
    }

    /**
     * Updates metrics
     */
    public void updateMetrics(AggregatedMetrics metrics) {
        this.metrics = metrics;
        this.calculateAchievementPercentage();
    }

    /**
     * Adds breakdown item
     */
    public void addBreakdownItem(BreakdownItem item) {
        if (this.breakdown == null) {
            this.breakdown = new ArrayList<>();
        }
        this.breakdown.add(item);
    }

    /**
     * Sets comparison data
     */
    public void setComparisonData(Money currentRevenue, Money previousRevenue) {
        this.comparisonData = ComparisonData.builder()
                .previousPeriodRevenue(previousRevenue)
                .growthRate(calculateGrowthRate(previousRevenue.getAmount(), currentRevenue.getAmount()))
                .difference(Money.builder()
                        .amount(currentRevenue.getAmount().subtract(previousRevenue.getAmount()))
                        .currency(currentRevenue.getCurrency())
                        .build())
                .trend(determineTrend(previousRevenue.getAmount(), currentRevenue.getAmount()))
                .build();
    }

    /**
     * Marks aggregation as complete
     */
    public void markAsComplete() {
        this.isComplete = true;
        this.aggregationTime = Instant.now();

        addDomainEvent(AggregationCompletedEvent.builder()
                .aggregationId(this.aggregationId)
                .tenantId(this.tenantId)
                .eventType("AGGREGATION_COMPLETED")
                .timestamp(Instant.now())
                .build());
    }

    /**
     * Increments data version
     */
    public void incrementVersion() {
        this.dataVersion = (this.dataVersion == null ? 0 : this.dataVersion) + 1;
        this.aggregationTime = Instant.now();
    }

    /**
     * Gets revenue for specific breakdown item
     */
    public Money getBreakdownValue(String key) {
        if (this.breakdown == null) {
            return null;
        }
        return this.breakdown.stream()
                .filter(b -> b.getKey().equals(key))
                .map(BreakdownItem::getValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * Calculates percentage of total for breakdown
     */
    public void calculateBreakdownPercentages() {
        if (this.breakdown == null || this.metrics == null) {
            return;
        }

        BigDecimal total = this.metrics.getTotalRevenue().getAmount();
        if (total.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        for (BreakdownItem item : this.breakdown) {
            if (item.getValue() != null) {
                BigDecimal percentage = item.getValue().getAmount()
                        .divide(total, 4, java.math.RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
                item.setPercentage(percentage);
            }
        }
    }

    private void calculateAchievementPercentage() {
        if (this.metrics != null &&
                this.metrics.getTargetRevenue() != null &&
                this.metrics.getTotalRevenue() != null) {

            BigDecimal target = this.metrics.getTargetRevenue().getAmount();
            BigDecimal actual = this.metrics.getTotalRevenue().getAmount();

            if (target.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal achievement = actual.divide(target, 4, java.math.RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
                this.metrics.setAchievementPercentage(achievement);
            }
        }
    }

    private BigDecimal calculateGrowthRate(BigDecimal previous, BigDecimal current) {
        if (previous == null || previous.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return current.subtract(previous)
                .divide(previous, 4, java.math.RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
    }

    private String determineTrend(BigDecimal previous, BigDecimal current) {
        int comparison = current.compareTo(previous);
        if (comparison > 0) return "UP";
        if (comparison < 0) return "DOWN";
        return "STABLE";
    }

    private static AggregatedMetrics createEmptyMetrics(String currency) {
        return AggregatedMetrics.builder()
                .totalRevenue(Money.builder().amount(BigDecimal.ZERO).currency(currency).build())
                .targetRevenue(Money.builder().amount(BigDecimal.ZERO).currency(currency).build())
                .achievementPercentage(BigDecimal.ZERO)
                .totalDeals(0)
                .wonDeals(0)
                .winRate(BigDecimal.ZERO)
                .averageDealSize(Money.builder().amount(BigDecimal.ZERO).currency(currency).build())
                .averageDiscount(Money.builder().amount(BigDecimal.ZERO).currency(currency).build())
                .newOpportunities(0)
                .closedOpportunities(0)
                .pipelineValue(Money.builder().amount(BigDecimal.ZERO).currency(currency).build())
                .weightedPipeline(Money.builder().amount(BigDecimal.ZERO).currency(currency).build())
                .conversionRate(BigDecimal.ZERO)
                .salesActivities(0)
                .customerAcquisitionCost(BigDecimal.ZERO)
                .customerLifetimeValue(Money.builder().amount(BigDecimal.ZERO).currency(currency).build())
                .activeCustomers(0)
                .churnedCustomers(0)
                .churnRate(BigDecimal.ZERO)
                .build();
    }

    public void addDomainEvent(AggregationCompletedEvent event) {
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
