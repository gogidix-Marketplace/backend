package com.gogidix.globalbusinessmanagement.businessintelligence.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Domain model for business forecasts.
 * Stores forecasted values with confidence intervals.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "forecasts")
@CompoundIndex(name = "metric_period_type_idx", def = "{'metricCode': 1, 'periodId': 1, 'forecastType': 1}")
public class Forecast {

    @Id
    private String id;

    @NotBlank(message = "Forecast name is required")
    @Indexed
    private String forecastName;

    @NotBlank(message = "Metric code is required")
    @Indexed
    private String metricCode;

    @NotBlank(message = "Metric name is required")
    private String metricName;

    @Indexed
    private String entityCode;

    @Indexed
    private String entityType;

    @Indexed
    private String regionCode;

    @NotBlank(message = "Forecast type is required")
    @Indexed
    private String forecastType;

    @NotNull(message = "Forecast method is required")
    private String forecastMethod;

    @NotNull(message = "Forecast period start is required")
    @Indexed
    private LocalDateTime forecastPeriodStart;

    @NotNull(message = "Forecast period end is required")
    @Indexed
    private LocalDateTime forecastPeriodEnd;

    @NotNull(message = "Historical period start is required")
    private LocalDateTime historicalPeriodStart;

    @NotNull(message = "Historical period end is required")
    private LocalDateTime historicalPeriodEnd;

    @Valid
    @NotEmpty(message = "At least one forecast value is required")
    private List<ForecastValue> forecastValues;

    @Valid
    private ForecastMetadata metadata;

    @Valid
    private ForecastAccuracy accuracy;

    @Valid
    private ForecastScenarios scenarios;

    @Valid
    private List<Assumption> assumptions;

    @NotNull(message = "Forecast status is required")
    @Builder.Default
    private ForecastStatus status = ForecastStatus.ACTIVE;

    @Indexed
    private BigDecimal confidenceLevel;

    @Indexed
    private Instant generatedAt;

    @Indexed
    private Instant validUntil;

    @Indexed
    private String generatedBy;

    @Indexed
    private String modelVersion;

    private Map<String, Object> parameters;

    private String notes;

    private Instant createdAt;

    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForecastValue {
        @NotNull(message = "Period is required")
        private String period;

        @NotNull(message = "Timestamp is required")
        private Instant timestamp;

        @NotNull(message = "Forecast value is required")
        private BigDecimal forecastValue;

        private BigDecimal lowerBound;

        private BigDecimal upperBound;

        private BigDecimal standardError;

        private BigDecimal predictionInterval;

        private BigDecimal probability;

        private Boolean isActual;

        private BigDecimal actualValue;

        private BigDecimal accuracy;

        private Map<String, Object> attributes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForecastMetadata {
        private Integer dataPointsUsed;

        private Integer forecastHorizon;

        private String frequency;

        private String currency;

        private String unit;

        private BigDecimal scale;

        private String dataSource;

        private BigDecimal dataQualityScore;

        private Boolean hasOutliers;

        private Boolean hasMissingData;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForecastAccuracy {
        private BigDecimal meanAbsoluteError;

        private BigDecimal meanSquaredError;

        private BigDecimal rootMeanSquaredError;

        private BigDecimal meanAbsolutePercentageError;

        private BigDecimal symmetricMeanAbsolutePercentageError;

        private BigDecimal theilUStatistic;

        private BigDecimal trackingSignal;

        private BigDecimal bias;

        private String accuracyRating;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForecastScenarios {
        @Valid
        private ForecastValue baseline;

        @Valid
        private ForecastValue optimistic;

        @Valid
        private ForecastValue pessimistic;

        @Valid
        private List<ForecastValue> customScenarios;

        @Valid
        private ScenarioAnalysis analysis;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScenarioAnalysis {
        private BigDecimal bestCaseValue;

        private BigDecimal worstCaseValue;

        private BigDecimal expectedValue;

        private BigDecimal range;

        private BigDecimal variability;

        private String riskLevel;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Assumption {
        @NotBlank(message = "Assumption name is required")
        private String name;

        private String description;

        private BigDecimal value;

        private String valueType;

        private String category;

        private BigDecimal sensitivity;

        private String impactLevel;

        private Boolean isCritical;
    }

    public enum ForecastStatus {
        ACTIVE,
        SUPERSEDED,
        EXPIRED,
        DRAFT,
        ARCHIVED
    }

    public boolean isActive() {
        return ForecastStatus.ACTIVE.equals(status);
    }

    public boolean isExpired(Instant asOfDate) {
        return validUntil != null && asOfDate != null && asOfDate.isAfter(validUntil);
    }

    public boolean isStillValid(Instant asOfDate) {
        return isActive() && !isExpired(asOfDate);
    }

    public boolean hasHighConfidence() {
        return confidenceLevel != null && confidenceLevel.compareTo(new BigDecimal("0.7")) >= 0;
    }

    public ForecastValue getForecastForPeriod(String period) {
        return forecastValues.stream()
            .filter(fv -> fv.getPeriod().equals(period))
            .findFirst()
            .orElse(null);
    }

    public BigDecimal getAverageForecastValue() {
        return forecastValues.stream()
            .map(ForecastValue::getForecastValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(forecastValues.size()), 2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getTotalForecastValue() {
        return forecastValues.stream()
            .map(ForecastValue::getForecastValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
