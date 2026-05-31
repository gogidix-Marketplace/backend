package com.gogidix.finance.accountspayable.domain.port.in;

import com.gogidix.finance.accountspayable.domain.model.Payment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Payment Commands (Input Port)
 * Defines the input commands for payment operations
 */
public interface PaymentCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class CreatePaymentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Vendor ID is required")
        private String vendorId;

        @NotBlank(message = "Vendor name is required")
        private String vendorName;

        private List<String> invoiceIds;

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal amount;

        @NotBlank(message = "Currency is required")
        private String currency;

        @NotNull(message = "Payment method is required")
        private Payment.PaymentMethod paymentMethod;

        private LocalDate paymentDate;

        private String description;

        private String notes;

        private String bankAccountNumber;

        private String bankRoutingNumber;

        private String checkNumber;

        private String batchId;

        private BigDecimal feeAmount;

        private String exchangeRate;

        private String originalCurrency;

        private BigDecimal originalAmount;

        private String attachmentUrl;

        @NotBlank(message = "Created by is required")
        private String createdBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SchedulePaymentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Payment ID is required")
        private String paymentId;

        @NotNull(message = "Scheduled date is required")
        private LocalDate scheduledDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ProcessPaymentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Payment ID is required")
        private String paymentId;

        @NotBlank(message = "Processed by is required")
        private String processedBy;

        private String paymentReference;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CompletePaymentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Payment ID is required")
        private String paymentId;

        private String transactionReference;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class FailPaymentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Payment ID is required")
        private String paymentId;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CancelPaymentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Payment ID is required")
        private String paymentId;

        @NotBlank(message = "Cancelled by is required")
        private String cancelledBy;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ReversePaymentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Payment ID is required")
        private String paymentId;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddAllocationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Payment ID is required")
        private String paymentId;

        @NotBlank(message = "Invoice ID is required")
        private String invoiceId;

        @NotBlank(message = "Invoice number is required")
        private String invoiceNumber;

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal amount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SetPaymentMethodDetailsCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Payment ID is required")
        private String paymentId;

        private String bankAccountNumber;

        private String bankRoutingNumber;

        private String checkNumber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SetFeeCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Payment ID is required")
        private String paymentId;

        @NotNull(message = "Fee amount is required")
        private BigDecimal feeAmount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SetCurrencyConversionCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Payment ID is required")
        private String paymentId;

        @NotBlank(message = "Exchange rate is required")
        private String exchangeRate;

        @NotBlank(message = "Original currency is required")
        private String originalCurrency;

        @NotNull(message = "Original amount is required")
        private BigDecimal originalAmount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeletePaymentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Payment ID is required")
        private String paymentId;
    }
}
