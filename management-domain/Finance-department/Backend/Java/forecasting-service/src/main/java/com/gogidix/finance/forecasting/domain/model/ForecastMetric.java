package com.gogidix.finance.forecasting.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Forecast Metric Domain Entity
 * Represents individual metrics within a forecast
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastMetric {

    private String metricId;

    private String forecastId;

    private String metricName;

    private String metricCode;

    private String category;

    private String subcategory;

    private BigDecimal amount;

    private BigDecimal previousAmount;

    private BigDecimal variance;

    private BigDecimal variancePercentage;

    private String period;

    private Instant periodStart;

    private Instant periodEnd;

    private MetricType metricType;

    private String unit;

    private BigDecimal weight;

    private Integer confidenceLevel;

    private String dataSource;

    private String notes;

    private Integer sortOrder;

    private Boolean isCalculated;

    private String calculationFormula;

    private Instant createdAt;

    private Instant updatedAt;

    /**
     * Metric Type Enum
     * Defines different types of forecast metrics
     */
    public enum MetricType {
        REVENUE,
        EXPENSE,
        PROFIT,
        CASH_FLOW,
        GROWTH_RATE,
        MARGIN,
        RATIO,
        HEADCOUNT,
        VOLUME,
        RATE,
        OTHER
    }

    /**
     * Creates a new forecast metric
     *
     * @param forecastId the parent forecast ID
     * @param metricName the metric name
     * @param amount the metric amount
     * @param metricType the metric type
     * @return a new ForecastMetric instance
     */
    public static ForecastMetric create(String forecastId, String metricName,
                                       BigDecimal amount, MetricType metricType) {
        String metricId = "FM-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        return ForecastMetric.builder()
                .metricId(metricId)
                .forecastId(forecastId)
                .metricName(metricName)
                .amount(amount)
                .metricType(metricType)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .isCalculated(false)
                .weight(BigDecimal.ONE)
                .build();
    }

    /**
     * Creates a calculated metric
     *
     * @param forecastId the parent forecast ID
     * @param metricName the metric name
     * @param formula the calculation formula
     * @param metricType the metric type
     * @return a new calculated ForecastMetric instance
     */
    public static ForecastMetric createCalculated(String forecastId, String metricName,
                                                  String formula, MetricType metricType) {
        String metricId = "FM-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        return ForecastMetric.builder()
                .metricId(metricId)
                .forecastId(forecastId)
                .metricName(metricName)
                .calculationFormula(formula)
                .metricType(metricType)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .isCalculated(true)
                .weight(BigDecimal.ONE)
                .build();
    }

    /**
     * Updates the metric amount
     *
     * @param newAmount the new amount
     * @param oldAmount the old amount for variance calculation
     */
    public void updateAmount(BigDecimal newAmount, BigDecimal oldAmount) {
        this.previousAmount = oldAmount;
        this.amount = newAmount;
        this.updatedAt = Instant.now();
        calculateVariance();
    }

    /**
     * Calculates variance against previous amount
     */
    public void calculateVariance() {
        if (this.previousAmount != null && this.amount != null) {
            this.variance = this.amount.subtract(this.previousAmount);
            if (this.previousAmount.compareTo(BigDecimal.ZERO) != 0) {
                this.variancePercentage = this.variance
                        .divide(this.previousAmount, 4, java.math.RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
            }
        }
    }

    /**
     * Sets period information
     *
     * @param period the period identifier
     * @param periodStart the period start date
     * @param periodEnd the period end date
     */
    public void setPeriodInfo(String period, Instant periodStart, Instant periodEnd) {
        this.period = period;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.updatedAt = Instant.now();
    }

    /**
     * Updates confidence level
     *
     * @param confidenceLevel the confidence level (0-100)
     */
    public void updateConfidenceLevel(Integer confidenceLevel) {
        if (confidenceLevel < 0 || confidenceLevel > 100) {
            throw new IllegalArgumentException("Confidence level must be between 0 and 100");
        }
        this.confidenceLevel = confidenceLevel;
        this.updatedAt = Instant.now();
    }

    /**
     * Sets the weight for weighted calculations
     *
     * @param weight the weight value
     */
    public void setWeight(BigDecimal weight) {
        if (weight == null || weight.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Weight must be a positive number");
        }
        this.weight = weight;
        this.updatedAt = Instant.now();
    }

    /**
     * Checks if metric is calculated
     *
     * @return true if calculated metric
     */
    public boolean isCalculatedMetric() {
        return Boolean.TRUE.equals(this.isCalculated);
    }

    /**
     * Gets metric category code
     *
     * @return the combined category code
     */
    public String getFullCategoryCode() {
        if (this.category != null && this.subcategory != null) {
            return this.category + "-" + this.subcategory;
        }
        return this.category != null ? this.category : "UNCATEGORIZED";
    }

    /**
     * Updates the metric data source
     *
     * @param dataSource the data source identifier
     */
    public void setDataSourceInfo(String dataSource) {
        this.dataSource = dataSource;
        this.updatedAt = Instant.now();
    }

    /**
     * Adds notes to the metric
     *
     * @param notes the notes to add
     */
    public void addNotes(String notes) {
        if (this.notes == null || this.notes.isBlank()) {
            this.notes = notes;
        } else {
            this.notes = this.notes + "\n" + notes;
        }
        this.updatedAt = Instant.now();
    }

    /**
     * Calculates weighted amount
     *
     * @return the weighted amount
     */
    public BigDecimal getWeightedAmount() {
        if (this.amount != null && this.weight != null) {
            return this.amount.multiply(this.weight);
        }
        return this.amount;
    }

    /**
     * Creates a copy of this metric
     *
     * @return a new ForecastMetric with copied values
     */
    public ForecastMetric copy() {
        return ForecastMetric.builder()
                .metricId("FM-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .forecastId(this.forecastId)
                .metricName(this.metricName)
                .metricCode(this.metricCode)
                .category(this.category)
                .subcategory(this.subcategory)
                .amount(this.amount)
                .previousAmount(this.previousAmount)
                .variance(this.variance)
                .variancePercentage(this.variancePercentage)
                .period(this.period)
                .periodStart(this.periodStart)
                .periodEnd(this.periodEnd)
                .metricType(this.metricType)
                .unit(this.unit)
                .weight(this.weight)
                .confidenceLevel(this.confidenceLevel)
                .dataSource(this.dataSource)
                .notes(this.notes)
                .sortOrder(this.sortOrder)
                .isCalculated(this.isCalculated)
                .calculationFormula(this.calculationFormula)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }
}
