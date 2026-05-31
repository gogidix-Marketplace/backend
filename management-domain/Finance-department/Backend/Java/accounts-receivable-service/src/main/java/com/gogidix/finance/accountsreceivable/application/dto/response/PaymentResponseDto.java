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
 * Payment Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {

    private String id;

    private String paymentId;

    private String tenantId;

    private String paymentNumber;

    private String customerId;

    private String customerName;

    private String invoiceId;

    private String invoiceNumber;

    private PaymentTypeDto paymentType;

    private PaymentStatusDto status;

    private BigDecimal amount;

    private String currency;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate paymentDate;

    private String paymentMethod;

    private String referenceNumber;

    private String bankAccount;

    private String transactionId;

    private String checkNumber;

    private String creditCardNumber;

    private String description;

    private String notes;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate depositDate;

    private String depositSlipNumber;

    private String batchId;

    private Boolean reconciled;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant reconciledAt;

    private String reconciledBy;

    private String bankReconciliationId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate clearedDate;

    private Boolean autoApplied;

    private List<PaymentAllocationDto> allocations;

    private BigDecimal unappliedAmount;

    private String exchangeRate;

    private String baseCurrency;

    private BigDecimal baseCurrencyAmount;

    private String gatewayTransactionId;

    private String gatewayResponseCode;

    private String gatewayResponseMessage;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant processedAt;

    private String processedBy;

    private String approvedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant approvedAt;

    private String rejectionReason;

    private String refundedTo;

    private BigDecimal refundAmount;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant refundedAt;

    private String refundReason;

    private String parentId;

    private Boolean isReversal;

    private List<String> tags;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum PaymentTypeDto {
        RECEIVED, REFUND, PREPAYMENT, ADVANCE, DEPOSIT, CREDIT_CARD_PAYMENT, BANK_TRANSFER,
        CASH, CHECK, ELECTRONIC_FUNDS_TRANSFER, ONLINE_PAYMENT, MOBILE_PAYMENT, DIRECT_DEBIT
    }

    public enum PaymentStatusDto {
        PENDING, COMPLETED, FAILED, CANCELLED, REVERSED, PARTIALLY_APPLIED, FULLY_APPLIED, ON_HOLD, PROCESSING
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentAllocationDto {
        private String allocationId;
        private String invoiceId;
        private String invoiceNumber;
        private BigDecimal allocatedAmount;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate allocationDate;
        private String notes;
    }
}
