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
 * Cashflow Statement Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashflowStatementResponseDto {

    private String id;

    private String statementId;

    private String tenantId;

    private String name;

    private String description;

    private StatementTypeDto statementType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private StatementPeriodDto period;

    private StatementStatusDto status;

    private String generatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant generatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant lastUpdated;

    private BigDecimal beginningCash;

    private BigDecimal endingCash;

    private BigDecimal netCashIncreaseDecrease;

    private CashflowSectionDto operatingActivities;

    private CashflowSectionDto investingActivities;

    private CashflowSectionDto financingActivities;

    private List<StatementItemDto> lineItems;

    private String currency;

    private Integer fiscalYear;

    private Integer fiscalPeriod;

    private Boolean isConsolidated;

    private List<String> subsidiaryIds;

    private String parentStatementId;

    private BigDecimal priorPeriodCash;

    private BigDecimal cashChangePercentage;

    private List<String> tags;

    private String notes;

    private String approvalStatus;

    private String approvedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant approvedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum StatementTypeDto {
        DIRECT, INDIRECT, CONSOLIDATED, PROJECTED, ACTUAL
    }

    public enum StatementPeriodDto {
        DAILY, WEEKLY, MONTHLY, QUARTERLY, SEMI_ANNUALLY, ANNUALLY, YTD, CUSTOM
    }

    public enum StatementStatusDto {
        DRAFT, IN_REVIEW, FINALIZED, APPROVED, REJECTED, ARCHIVED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CashflowSectionDto {
        private BigDecimal totalInflow;

        private BigDecimal totalOutflow;

        private BigDecimal netCashflow;

        private List<StatementItemDto> items;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatementItemDto {
        private String itemId;

        private String code;

        private String description;

        private BigDecimal amount;

        private ItemTypeDto type;

        private String category;

        private Integer sequence;

        private Boolean isSubtotal;

        private String parentItemId;
    }

    public enum ItemTypeDto {
        INFLOW, OUTFLOW, NET, SUBTOTAL, HEADER
    }
}
