package com.gogidix.finance.accountsreceivable.domain.port.in;

import com.gogidix.finance.accountsreceivable.domain.model.Payment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
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
    class CreatePaymentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Customer ID is required")
        private String customerId;

        @NotBlank(message = "Customer name is required")
        private String customerName;

        private String invoiceId;

        private String invoiceNumber;

        @NotNull(message = "Payment type is required")
        private Payment.PaymentType paymentType;

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal amount;

        @NotBlank(message = "Currency is required")
        private String currency;

        @NotNull(message = "Payment date is required")
        private LocalDate paymentDate;

        @NotBlank(message = "Payment method is required")
        private String paymentMethod;

        private String referenceNumber;

        private String bankAccount;

        private String checkNumber;

        private String creditCardNumber;

        private String description;

        private String notes;

        private LocalDate depositDate;

        private String depositSlipNumber;

        private String batchId;

        private String exchangeRate;

        private String baseCurrency;

        private String gatewayCustomerId;

        private List<String> tags;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CompletePaymentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Payment ID is required")
        private String paymentId;

        private String transactionId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class FailPaymentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Payment ID is required")
        private String paymentId;

        @NotBlank(message = "Failure reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ApplyPaymentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Payment ID is required")
        private String paymentId;

        @NotBlank(message = "Invoice ID is required")
        private String invoiceId;

        private String invoiceNumber;

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal amount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ReversePaymentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Payment ID is required")
        private String paymentId;

        @NotBlank(message = "Reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RefundPaymentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Payment ID is required")
        private String paymentId;

        @NotNull(message = "Refund amount is required")
        @Positive(message = "Refund amount must be positive")
        private BigDecimal amount;

        @NotBlank(message = "Reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ReconcilePaymentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Payment ID is required")
        private String paymentId;

        @NotBlank(message = "Reconciled by is required")
        private String reconciledBy;
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
