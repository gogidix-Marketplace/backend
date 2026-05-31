package com.gogidix.finance.budgettracking.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.YearMonth;
import java.util.List;

/**
 * Budget Monitor Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetMonitorResponseDto {

    private String id;

    private String monitorId;

    private String tenantId;

    private String budgetId;

    private String budgetCode;

    private String budgetName;

    private String budgetPeriod;

    private YearMonth period;

    private BigDecimal allocatedAmount;

    private BigDecimal committedAmount;

    private BigDecimal actualExpenditure;

    private BigDecimal availableBalance;

    private BigDecimal variance;

    private BigDecimal utilizationPercentage;

    private MonitorStatusDto status;

    private String category;

    private String department;

    private String costCenter;

    private String fiscalYear;

    private String createdBy;

    private String lastUpdatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant lastCalculatedAt;

    private List<ThresholdStatusDto> thresholdStatuses;

    private List<String> alertRecipients;

    private String currency;

    private boolean thresholdBreached;

    private Integer warningCount;

    private Integer criticalCount;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum MonitorStatusDto {
        ON_TRACK,
        ATTENTION,
        WARNING,
        CRITICAL,
        EXHAUSTED,
        OVER_BUDGET
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ThresholdStatusDto {
        private String thresholdType;
        private BigDecimal thresholdValue;
        private BigDecimal currentValue;
        private ThresholdLevelDto level;
        private boolean breached;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
        private Instant breachedAt;
        private boolean acknowledged;
        private String acknowledgedBy;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
        private Instant acknowledgedAt;
    }

    public enum ThresholdLevelDto {
        INFO,
        WARNING,
        CRITICAL
    }
}
