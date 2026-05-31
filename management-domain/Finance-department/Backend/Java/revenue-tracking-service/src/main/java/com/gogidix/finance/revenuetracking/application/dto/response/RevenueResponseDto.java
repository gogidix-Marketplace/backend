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
 * Revenue Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueResponseDto {

    private String id;

    private String revenueId;

    private String tenantId;

    private String customerId;

    private String customerName;

    private String contractId;

    private String projectId;

    private RevenueTypeDto type;

    private BigDecimal totalAmount;

    private String currency;

    private BigDecimal recognizedAmount;

    private BigDecimal deferredAmount;

    private RevenueStatusDto status;

    private RecognitionMethodDto recognitionMethod;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate recognitionStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate recognitionEndDate;

    private Integer recognitionPeriods;

    private Integer currentPeriod;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;

    private String description;

    private String category;

    private String productCode;

    private String productSku;

    private String department;

    private String costCenter;

    private String salespersonId;

    private String region;

    private String territory;

    private PaymentTermsDto paymentTerms;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate invoiceDate;

    private String invoiceNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate paidDate;

    private Boolean fullyPaid;

    private List<RevenueMilestoneDto> milestones;

    private List<String> tags;

    private String notes;

    private Boolean isRecurring;

    private String recurringSchedule;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate nextRecognitionDate;

    private BigDecimal recognizedThisPeriod;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum RevenueTypeDto {
        RECURRING, ONE_TIME, USAGE_BASED, SUBSCRIPTION, PROJECT,
        SERVICE, PRODUCT, LICENSE, MAINTENANCE, SUPPORT
    }

    public enum RevenueStatusDto {
        PENDING, RECOGNIZED, DEFERRED, PARTIALLY_RECOGNIZED,
        CANCELLED, REFUNDED, ON_HOLD
    }

    public enum RecognitionMethodDto {
        POINT_IN_TIME, OVER_TIME, DEFERRED, MILESTONE, USAGE_BASED
    }

    public enum PaymentTermsDto {
        NET_15, NET_30, NET_45, NET_60, COD, PREPAID, INSTALLMENTS
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RevenueMilestoneDto {
        private String milestoneId;
        private String name;
        private String description;
        private BigDecimal amount;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate targetDate;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate completedDate;
        private MilestoneStatusDto status;

        public enum MilestoneStatusDto {
            PENDING, IN_PROGRESS, COMPLETED, CANCELLED
        }
    }
}
