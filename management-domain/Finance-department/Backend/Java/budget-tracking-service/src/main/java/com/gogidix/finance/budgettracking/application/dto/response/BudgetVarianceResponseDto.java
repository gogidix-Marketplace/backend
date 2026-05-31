package com.gogidix.finance.budgettracking.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Budget Variance Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetVarianceResponseDto {

    private String id;

    private String varianceId;

    private String tenantId;

    private String budgetId;

    private String budgetCode;

    private String budgetName;

    private String period;

    private YearMonth yearMonth;

    private BigDecimal budgetedAmount;

    private BigDecimal actualAmount;

    private BigDecimal committedAmount;

    private BigDecimal varianceAmount;

    private BigDecimal variancePercentage;

    private VarianceStatusDto varianceStatus;

    private String category;

    private String department;

    private String costCenter;

    private String fiscalYear;

    private String createdBy;

    private String lastUpdatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant calculatedAt;

    private String description;

    private String analysis;

    private List<VarianceBreakdownDto> breakdowns;

    private List<String> contributingFactors;

    private boolean requiresInvestigation;

    private String assignedTo;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant investigationDueBy;

    private boolean investigated;

    private String investigatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant investigatedAt;

    private String investigationNotes;

    private BigDecimal forecastedAmount;

    private BigDecimal forecastVariance;

    private List<String> tags;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum VarianceStatusDto {
        FAVORABLE,
        UNFAVORABLE,
        NEUTRAL,
        SIGNIFICANT_FAVORABLE,
        SIGNIFICANT_UNFAVORABLE,
        CRITICAL
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VarianceBreakdownDto {
        private String category;
        private String description;
        private BigDecimal budgetedAmount;
        private BigDecimal actualAmount;
        private BigDecimal varianceAmount;
        private BigDecimal variancePercentage;
        private String contribution;
    }
}
