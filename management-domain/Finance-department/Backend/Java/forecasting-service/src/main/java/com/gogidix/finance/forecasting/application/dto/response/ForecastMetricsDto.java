package com.gogidix.finance.forecasting.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.finance.forecasting.domain.model.ForecastMetric;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Forecast Metrics Response DTO
 * Represents individual forecast metrics in API responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastMetricsDto {

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

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant periodStart;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant periodEnd;

    private MetricTypeDto metricType;

    private String unit;

    private BigDecimal weight;

    private Integer confidenceLevel;

    private String dataSource;

    private String notes;

    private Integer sortOrder;

    private Boolean isCalculated;

    private String calculationFormula;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    /**
     * Metric Type DTO Enum
     */
    public enum MetricTypeDto {
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
     * Converts a ForecastMetric entity to DTO
     *
     * @param metric the forecast metric entity
     * @return the forecast metrics DTO
     */
    public static ForecastMetricsDto fromEntity(ForecastMetric metric) {
        if (metric == null) {
            return null;
        }

        return ForecastMetricsDto.builder()
                .metricId(metric.getMetricId())
                .forecastId(metric.getForecastId())
                .metricName(metric.getMetricName())
                .metricCode(metric.getMetricCode())
                .category(metric.getCategory())
                .subcategory(metric.getSubcategory())
                .amount(metric.getAmount())
                .previousAmount(metric.getPreviousAmount())
                .variance(metric.getVariance())
                .variancePercentage(metric.getVariancePercentage())
                .period(metric.getPeriod())
                .periodStart(metric.getPeriodStart())
                .periodEnd(metric.getPeriodEnd())
                .metricType(mapMetricType(metric.getMetricType()))
                .unit(metric.getUnit())
                .weight(metric.getWeight())
                .confidenceLevel(metric.getConfidenceLevel())
                .dataSource(metric.getDataSource())
                .notes(metric.getNotes())
                .sortOrder(metric.getSortOrder())
                .isCalculated(metric.getIsCalculated())
                .calculationFormula(metric.getCalculationFormula())
                .createdAt(metric.getCreatedAt())
                .updatedAt(metric.getUpdatedAt())
                .build();
    }

    /**
     * Converts a list of ForecastMetric entities to DTOs
     *
     * @param metrics the list of forecast metric entities
     * @return the list of forecast metrics DTOs
     */
    public static java.util.List<ForecastMetricsDto> fromEntityList(java.util.List<ForecastMetric> metrics) {
        if (metrics == null) {
            return java.util.Collections.emptyList();
        }
        return metrics.stream()
                .map(ForecastMetricsDto::fromEntity)
                .toList();
    }

    /**
     * Creates a summary DTO with limited fields
     *
     * @param metric the forecast metric entity
     * @return the summary DTO
     */
    public static ForecastMetricsDto summary(ForecastMetric metric) {
        if (metric == null) {
            return null;
        }

        return ForecastMetricsDto.builder()
                .metricId(metric.getMetricId())
                .metricName(metric.getMetricName())
                .metricCode(metric.getMetricCode())
                .category(metric.getCategory())
                .amount(metric.getAmount())
                .metricType(mapMetricType(metric.getMetricType()))
                .build();
    }

    private static MetricTypeDto mapMetricType(ForecastMetric.MetricType type) {
        return type != null ? MetricTypeDto.valueOf(type.name()) : null;
    }

    /**
     * Gets the full category path
     *
     * @return the combined category and subcategory
     */
    public String getFullCategoryPath() {
        if (subcategory != null && !subcategory.isBlank()) {
            return category + "-" + subcategory;
        }
        return category;
    }

    /**
     * Gets the weighted amount
     *
     * @return the amount multiplied by weight
     */
    public BigDecimal getWeightedAmount() {
        if (amount != null && weight != null) {
            return amount.multiply(weight);
        }
        return amount;
    }

    /**
     * Checks if variance is favorable (positive for revenue, negative for expense)
     *
     * @return true if variance is favorable
     */
    public boolean isFavorableVariance() {
        if (variance == null) {
            return false;
        }
        return switch (metricType) {
            case REVENUE, PROFIT -> variance.compareTo(BigDecimal.ZERO) > 0;
            case EXPENSE -> variance.compareTo(BigDecimal.ZERO) < 0;
            default -> false;
        };
    }

    /**
     * Gets variance status description
     *
     * @return the variance status
     */
    public String getVarianceStatus() {
        if (variance == null) {
            return "N/A";
        }
        if (variance.compareTo(BigDecimal.ZERO) == 0) {
            return "ON_TARGET";
        }
        return isFavorableVariance() ? "FAVORABLE" : "UNFAVORABLE";
    }
}
