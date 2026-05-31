package com.gogidix.finance.forecasting.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.finance.forecasting.domain.model.Forecast;
import com.gogidix.finance.forecasting.domain.model.ForecastMetric;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Forecast Response DTO
 * Represents the forecast data in API responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastResponseDto {

    private String id;

    private String forecastId;

    private String tenantId;

    private ForecastTypeDto forecastType;

    private ForecastHorizonDto forecastHorizon;

    private String name;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant startDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant endDate;

    private ForecastStatusDto status;

    private String createdBy;

    private String approvedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant approvedAt;

    private String rejectionReason;

    private String currency;

    private BigDecimal totalForecastAmount;

    private BigDecimal actualAmount;

    private BigDecimal varianceAmount;

    private BigDecimal variancePercentage;

    private Integer confidenceLevel;

    private String dataSource;

    private String department;

    private String category;

    private String scenario;

    private List<ForecastMetricsDto> metrics;

    private String notes;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant lastRegeneratedAt;

    private Integer regenerationCount;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    /**
     * Forecast Type DTO Enum
     */
    public enum ForecastTypeDto {
        REVENUE,
        EXPENSE,
        CASHFLOW,
        BUDGET_VARIANCE
    }

    /**
     * Forecast Horizon DTO Enum
     */
    public enum ForecastHorizonDto {
        MONTHLY,
        QUARTERLY,
        ANNUAL
    }

    /**
     * Forecast Status DTO Enum
     */
    public enum ForecastStatusDto {
        DRAFT,
        PENDING_APPROVAL,
        APPROVED,
        REJECTED,
        ARCHIVED
    }

    /**
     * Converts a Forecast entity to DTO
     *
     * @param forecast the forecast entity
     * @return the forecast response DTO
     */
    public static ForecastResponseDto fromEntity(Forecast forecast) {
        return ForecastResponseDto.builder()
                .id(forecast.getId())
                .forecastId(forecast.getForecastId())
                .tenantId(forecast.getTenantId())
                .forecastType(mapForecastType(forecast.getForecastType()))
                .forecastHorizon(mapForecastHorizon(forecast.getForecastHorizon()))
                .name(forecast.getName())
                .description(forecast.getDescription())
                .startDate(forecast.getStartDate())
                .endDate(forecast.getEndDate())
                .status(mapForecastStatus(forecast.getStatus()))
                .createdBy(forecast.getCreatedBy())
                .approvedBy(forecast.getApprovedBy())
                .approvedAt(forecast.getApprovedAt())
                .rejectionReason(forecast.getRejectionReason())
                .currency(forecast.getCurrency())
                .totalForecastAmount(forecast.getTotalForecastAmount())
                .actualAmount(forecast.getActualAmount())
                .varianceAmount(forecast.getVarianceAmount())
                .variancePercentage(forecast.getVariancePercentage())
                .confidenceLevel(forecast.getConfidenceLevel())
                .dataSource(forecast.getDataSource())
                .department(forecast.getDepartment())
                .category(forecast.getCategory())
                .scenario(forecast.getScenario())
                .metrics(mapMetrics(forecast.getMetrics()))
                .notes(forecast.getNotes())
                .lastRegeneratedAt(forecast.getLastRegeneratedAt())
                .regenerationCount(forecast.getRegenerationCount())
                .createdAt(forecast.getCreatedAt())
                .updatedAt(forecast.getUpdatedAt())
                .build();
    }

    /**
     * Converts a list of Forecast entities to DTOs
     *
     * @param forecasts the list of forecast entities
     * @return the list of forecast response DTOs
     */
    public static List<ForecastResponseDto> fromEntityList(List<Forecast> forecasts) {
        return forecasts.stream()
                .map(ForecastResponseDto::fromEntity)
                .toList();
    }

    private static ForecastTypeDto mapForecastType(Forecast.ForecastType type) {
        return type != null ? ForecastTypeDto.valueOf(type.name()) : null;
    }

    private static ForecastHorizonDto mapForecastHorizon(Forecast.ForecastHorizon horizon) {
        return horizon != null ? ForecastHorizonDto.valueOf(horizon.name()) : null;
    }

    private static ForecastStatusDto mapForecastStatus(Forecast.ForecastStatus status) {
        return status != null ? ForecastStatusDto.valueOf(status.name()) : null;
    }

    private static List<ForecastMetricsDto> mapMetrics(List<ForecastMetric> metrics) {
        if (metrics == null) {
            return null;
        }
        return metrics.stream()
                .map(ForecastMetricsDto::fromEntity)
                .toList();
    }

    /**
     * Creates a summary DTO with limited fields
     *
     * @param forecast the forecast entity
     * @return the summary DTO
     */
    public static ForecastResponseDto summary(Forecast forecast) {
        return ForecastResponseDto.builder()
                .forecastId(forecast.getForecastId())
                .name(forecast.getName())
                .forecastType(mapForecastType(forecast.getForecastType()))
                .forecastHorizon(mapForecastHorizon(forecast.getForecastHorizon()))
                .status(mapForecastStatus(forecast.getStatus()))
                .startDate(forecast.getStartDate())
                .endDate(forecast.getEndDate())
                .totalForecastAmount(forecast.getTotalForecastAmount())
                .currency(forecast.getCurrency())
                .department(forecast.getDepartment())
                .scenario(forecast.getScenario())
                .confidenceLevel(forecast.getConfidenceLevel())
                .createdAt(forecast.getCreatedAt())
                .build();
    }
}
