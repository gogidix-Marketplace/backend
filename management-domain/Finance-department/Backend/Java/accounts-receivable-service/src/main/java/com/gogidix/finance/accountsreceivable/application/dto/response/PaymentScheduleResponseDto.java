package com.gogidix.finance.accountsreceivable.application.dto.response;

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

    private String scheduleNumber;

    private String customerId;

    private String customerName;

    private String invoiceId;

    private String invoiceNumber;

    private BigDecimal totalAmount;

    private BigDecimal amountPaid;

    private BigDecimal balanceRemaining;

    private String currency;

    private ScheduleStatusDto status;

    private ScheduleTypeDto scheduleType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private Integer installmentCount;

    private String frequency;

    private String paymentMethod;

    private Boolean autoCharge;

    private String description;

    private String notes;

    private List<ScheduledPaymentDto> scheduledPayments;

    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant lastPaymentDate;

    private String nextPaymentDate;

    private Integer totalInstallments;

    private Integer completedInstallments;

    private BigDecimal installmentAmount;

    private String templateId;

    private String originalScheduleId;

    private Boolean isRescheduled;

    private String rescheduleReason;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate rescheduleDate;

    private List<String> tags;

    private String projectId;

    private String departmentId;

    private String bankAccountId;

    private String paymentGatewayCustomerId;

    private String paymentGatewaySubscriptionId;

    private Boolean sendReminder;

    private Integer reminderDaysBefore;

    private Boolean prorateFirstInstallment;

    private BigDecimal prorationAmount;

    private String agreedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant agreedAt;

    private String agreementReference;

    private String contractId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum ScheduleStatusDto {
        PENDING, ACTIVE, PAUSED, COMPLETED, CANCELLED, DEFAULTED, SUSPENDED
    }

    public enum ScheduleTypeDto {
        FIXED_INSTALLMENTS, PERCENTAGE_BASED, CUSTOM, SEASONAL, STEP_UP, STEP_DOWN, BALLOON_PAYMENT
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduledPaymentDto {
        private String scheduledPaymentId;
        private Integer installmentNumber;
        private BigDecimal amount;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate dueDate;
        private PaymentStatusDto status;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
        private Instant paidAt;
        private String transactionId;
        private String paymentMethod;
        private BigDecimal paidAmount;
        private BigDecimal remainingAmount;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate paidDate;
        private String failureReason;
        private Integer attemptCount;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate nextAttemptDate;
        private String notes;

        public enum PaymentStatusDto {
            PENDING, PROCESSING, PAID, PARTIALLY_PAID, FAILED, SKIPPED, CANCELLED
        }
    }
}
