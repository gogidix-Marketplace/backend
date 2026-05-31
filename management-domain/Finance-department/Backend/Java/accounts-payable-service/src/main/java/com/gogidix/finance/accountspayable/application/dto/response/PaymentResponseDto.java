package com.gogidix.finance.accountspayable.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.finance.accountspayable.domain.model.Payment;
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

    private String vendorId;

    private String vendorName;

    private String invoiceId;

    private String invoiceNumber;

    private BigDecimal amount;

    private String currency;

    private PaymentStatusDto status;

    private PaymentMethodDto paymentMethod;

    private String paymentReference;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate paymentDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate scheduledDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant processedAt;

    private String processedBy;

    private String transactionReference;

    private String description;

    private String notes;

    private String batchId;

    private String approvalReference;

    private String rejectionReason;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant cancelledAt;

    private String cancelledBy;

    private String cancellationReason;

    private List<String> invoiceIds;

    private List<PaymentAllocationDto> allocations;

    private BigDecimal feeAmount;

    private String exchangeRate;

    private String originalCurrency;

    private BigDecimal originalAmount;

    private String attachmentUrl;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    private BigDecimal totalAmount;

    public enum PaymentStatusDto {
        PENDING,
        SCHEDULED,
        PROCESSING,
        COMPLETED,
        FAILED,
        CANCELLED,
        REVERSED
    }

    public enum PaymentMethodDto {
        BANK_TRANSFER,
        CHECK,
        WIRE_TRANSFER,
        ACH,
        CREDIT_CARD,
        DEBIT_CARD,
        ELECTRONIC_FUNDS_TRANSFER,
        STANDING_ORDER
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentAllocationDto {
        private String invoiceId;
        private String invoiceNumber;
        private BigDecimal amount;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate allocationDate;
    }
}
