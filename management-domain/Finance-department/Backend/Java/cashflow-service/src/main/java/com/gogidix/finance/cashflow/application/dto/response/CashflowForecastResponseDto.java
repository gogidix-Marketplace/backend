package com.gogidix.finance.cashflow.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Cashflow Forecast Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashflowForecastResponseDto {

    private String id;

    private String forecastId;

    private String tenantId;

    private String name;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private ForecastPeriodDto period;

    private ForecastScenarioDto scenario;

    private ForecastStatusDto status;

    private String generatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant generatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant lastUpdated;

    private BigDecimal totalInflow;

    private BigDecimal totalOutflow;

    private BigDecimal netCashflow;

    private BigDecimal openingBalance;

    private BigDecimal closingBalance;

    private BigDecimal minimumBalance;

    private BigDecimal maximumBalance;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate minimumBalanceDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate maximumBalanceDate;

    private Integer version;

    private String parentForecastId;

    private Boolean isBaseline;

    private List<ForecastPeriodDataDto> periodData;

    private List<ForecastVarianceDto> variances;

    private List<String> tags;

    private String notes;

    private ConfidenceLevelDto confidenceLevel;

    private BigDecimal variancePercentage;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum ForecastPeriodDto {
        DAILY, WEEKLY, MONTHLY, QUARTERLY, SEMI_ANNUALLY, ANNUALLY
    }

    public enum ForecastScenarioDto {
        BASELINE, OPTIMISTIC, PESSIMISTIC, STRESS_TEST, CUSTOM
    }

    public enum ForecastStatusDto {
        DRAFT, GENERATING, GENERATED, APPROVED, REJECTED, ARCHIVED
    }

    public enum ConfidenceLevelDto {
        LOW, MEDIUM, HIGH, VERY_HIGH
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForecastPeriodDataDto {
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate periodStart;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate periodEnd;

        private BigDecimal openingBalance;

        private BigDecimal inflow;

        private BigDecimal outflow;

        private BigDecimal netCashflow;

        private BigDecimal closingBalance;

        private Integer transactionCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForecastVarianceDto {
        private String category;

        private BigDecimal forecastedAmount;

        private BigDecimal actualAmount;

        private BigDecimal variance;

        private BigDecimal variancePercentage;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate periodDate;
    }
}
