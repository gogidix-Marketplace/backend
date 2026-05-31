package com.gogidix.finance.revenuetracking.application.dto.response;

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
 * Revenue Recognition Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueRecognitionResponseDto {

    private String id;

    private String recognitionId;

    private String tenantId;

    private String revenueId;

    private String contractId;

    private RecognitionScheduleTypeDto scheduleType;

    private RecognitionStatusDto status;

    private BigDecimal totalAmount;

    private String currency;

    private BigDecimal recognizedAmount;

    private BigDecimal remainingAmount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private Integer totalPeriods;

    private Integer completedPeriods;

    private List<RecognitionPeriodDto> periods;

    private RecognitionMethodDto recognitionMethod;

    private String description;

    private String createdBy;

    private String approvedBy;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate approvedDate;

    private String accountingStandard;

    private String performanceObligation;

    private String transactionPrice;

    private List<String> allocationKeys;

    private Boolean standaloneSellingPrice;

    private String contractAssetId;

    private String contractLiabilityId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum RecognitionScheduleTypeDto {
        STRAIGHT_LINE, RATABLY, POINT_IN_TIME, MILESTONE_BASED,
        USAGE_BASED, PROPORTIONAL_PERFORMANCE, CUSTOM
    }

    public enum RecognitionStatusDto {
        PENDING, ACTIVE, SUSPENDED, COMPLETED, CANCELLED, ADJUSTED
    }

    public enum RecognitionMethodDto {
        ASC_606, IFRS_15, GAAP, CASH_BASIS, ACCRUAL_BASIS
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecognitionPeriodDto {
        private Integer periodNumber;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate startDate;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate endDate;
        private BigDecimal amount;
        private BigDecimal recognizedAmount;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate recognizedDate;
        private String recognizedBy;
        private PeriodStatusDto status;
        private String notes;

        public enum PeriodStatusDto {
            PENDING, RECOGNIZED, SKIPPED, ADJUSTED
        }
    }
}
