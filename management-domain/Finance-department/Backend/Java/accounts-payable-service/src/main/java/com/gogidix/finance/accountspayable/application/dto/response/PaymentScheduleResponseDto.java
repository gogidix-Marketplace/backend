package com.gogidix.finance.accountspayable.application.dto.response;

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
 * Payment Schedule Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentScheduleResponseDto {

    private String id;

    private String scheduleId;

    private String tenantId;

    private String vendorId;

    private String vendorName;

    private String invoiceId;

    private String invoiceNumber;

    private ScheduleTypeDto scheduleType;

    private BigDecimal totalAmount;

    private String currency;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private ScheduleFrequencyDto frequency;

    private Integer installments;

    private BigDecimal installmentAmount;

    private List<ScheduledPaymentDto> scheduledPayments;

    private ScheduleStatusDto status;

    private String autoPaymentMethod;

    private String bankAccountId;

    private String description;

    private String notes;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate nextPaymentDate;

    private Integer remainingInstallments;

    private BigDecimal paidAmount;

    private BigDecimal remainingAmount;

    private String createdBy;

    private String approvedBy;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastPaymentDate;

    private Integer progressPercentage;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum ScheduleTypeDto {
        INVOICE_BASED,
        RECURRING,
        INSTALLMENT_PLAN,
        MILESTONE_BASED
    }

    public enum ScheduleFrequencyDto {
        DAILY,
        WEEKLY,
        BI_WEEKLY,
        MONTHLY,
        QUARTERLY,
        SEMI_ANNUALLY,
        ANNUALLY,
        CUSTOM
    }

    public enum ScheduleStatusDto {
        PENDING,
        ACTIVE,
        PAUSED,
        COMPLETED,
        CANCELLED,
        FAILED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduledPaymentDto {
        private String paymentId;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate scheduledDate;
        private BigDecimal amount;
        private PaymentStatusDto status;
        private String paymentReference;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
        private Instant processedAt;
        private String failureReason;

        public enum PaymentStatusDto {
            PENDING,
            PROCESSED,
            FAILED,
            SKIPPED,
            CANCELLED
        }
    }
}
