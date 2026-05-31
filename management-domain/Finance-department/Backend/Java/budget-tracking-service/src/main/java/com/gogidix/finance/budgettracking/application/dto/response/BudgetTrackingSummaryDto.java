package com.gogidix.finance.budgettracking.application.dto.response;

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
 * Budget Tracking Summary Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetTrackingSummaryDto {

    private String tenantId;

    private String period;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private SummaryMetricsDto overallMetrics;

    private List<CategorySummaryDto> categorySummaries;

    private List<DepartmentSummaryDto> departmentSummaries;

    private List<BudgetHealthDto> budgetHealthStatus;

    private Integer totalBudgets;

    private Integer onTrackCount;

    private Integer attentionRequiredCount;

    private Integer criticalCount;

    private Integer exhaustedCount;

    private Integer overBudgetCount;

    private BigDecimal totalAllocated;

    private BigDecimal totalCommitted;

    private BigDecimal totalActualExpenditure;

    private BigDecimal totalAvailable;

    private BigDecimal overallUtilizationPercentage;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant generatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SummaryMetricsDto {
        private BigDecimal averageUtilizationPercentage;
        private BigDecimal medianVariance;
        private BigDecimal highestUtilization;
        private BigDecimal lowestUtilization;
        private Integer totalTransactions;
        private Integer pendingApprovals;
        private Integer alertsTriggered;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategorySummaryDto {
        private String category;
        private BigDecimal allocatedAmount;
        private BigDecimal committedAmount;
        private BigDecimal actualAmount;
        private BigDecimal availableAmount;
        private BigDecimal utilizationPercentage;
        private String status;
        private Integer budgetCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentSummaryDto {
        private String department;
        private String costCenter;
        private BigDecimal allocatedAmount;
        private BigDecimal committedAmount;
        private BigDecimal actualAmount;
        private BigDecimal availableAmount;
        private BigDecimal utilizationPercentage;
        private String status;
        private Integer budgetCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BudgetHealthDto {
        private String budgetCode;
        private String budgetName;
        private String category;
        private String department;
        private BigDecimal utilizationPercentage;
        private String healthStatus;
        private boolean requiresAttention;
        private Integer alertCount;
    }
}
