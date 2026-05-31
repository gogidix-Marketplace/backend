package com.gogidix.shared.infrastructure.services.billing.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Payment domain model.
 * Represents a payment transaction for an invoice.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    private String paymentId;
    private String tenantId;
    private String invoiceId;
    private String subscriptionId;
    private PaymentStatus status;
    private PaymentMethod method;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime transactionDate;
    private LocalDateTime processedDate;
    private String transactionReference;
    private String gateway;
    private String gatewayTransactionId;
    private String gatewayResponse;
    private String failureReason;
    private String paymentMethodId;
    private String cardLastFour;
    private String cardBrand;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String metadata; // JSON string for additional data

    /**
     * Payment status.
     */
    public enum PaymentStatus {
        PENDING,
        PROCESSING,
        COMPLETED,
        FAILED,
        REFUNDED,
        PARTIALLY_REFUNDED,
        CANCELLED,
        EXPIRED
    }

    /**
     * Payment method types.
     */
    public enum PaymentMethod {
        CREDIT_CARD,
        DEBIT_CARD,
        BANK_TRANSFER,
        PAYPAL,
        STRIPE,
        CRYPTO,
        CHECK,
        WIRE_TRANSFER,
        OTHER
    }

    /**
     * Check if payment was successful.
     */
    public boolean isSuccessful() {
        return status == PaymentStatus.COMPLETED;
    }

    /**
     * Check if payment failed.
     */
    public boolean isFailed() {
        return status == PaymentStatus.FAILED ||
               status == PaymentStatus.CANCELLED ||
               status == PaymentStatus.EXPIRED;
    }

    /**
     * Check if payment can be refunded.
     */
    public boolean canBeRefunded() {
        return status == PaymentStatus.COMPLETED &&
               LocalDateTime.now().isBefore(transactionDate.plusDays(90));
    }

    /**
     * Generate payment ID.
     */
    public static String generatePaymentId() {
        return "pay-" + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Generate transaction reference.
     */
    public static String generateTransactionReference() {
        return "TXN-" + System.currentTimeMillis();
    }

    /**
     * Get masked card number for display.
     */
    public String getMaskedCardNumber() {
        if (cardLastFour == null || cardBrand == null) {
            return "N/A";
        }
        return cardBrand + " **** " + cardLastFour;
    }
}
