package com.gogidix.platform.subscription.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Payment transaction entity.
 *
 * Records all payment transactions:
 * - Charges
 * - Refunds
 * - Partial refunds
 *
 * Links to Stripe payment intents
 */
@Entity
@Table(name = "payment_transactions", indexes = {
    @Index(name = "idx_payment_transactions_tenant", columnList = "tenant_id"),
    @Index(name = "idx_payment_transactions_invoice", columnList = "invoice_id"),
    @Index(name = "idx_payment_transactions_customer", columnList = "customer_id"),
    @Index(name = "idx_payment_transactions_status", columnList = "status"),
    @Index(name = "idx_payment_transactions_number", columnList = "transaction_number")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    /**
     * Human-readable transaction number
     */
    @Column(name = "transaction_number", nullable = false, unique = true, length = 100)
    private String transactionNumber;

    /**
     * Reference to invoice
     */
    @Column(name = "invoice_id")
    private String invoiceId;

    /**
     * Customer ID
     */
    @Column(name = "customer_id", nullable = false, length = 255)
    private String customerId;

    /**
     * Reference to payment method
     */
    @Column(name = "payment_method_id")
    private String paymentMethodId;

    /**
     * Transaction amount
     */
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", length = 3)
    @Builder.Default
    private String currency = "USD";

    /**
     * Transaction type
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 50)
    private TransactionType transactionType;

    /**
     * Payment provider
     */
    @Column(name = "provider", length = 100)
    private String provider;

    /**
     * Provider's transaction ID
     */
    @Column(name = "provider_transaction_id", length = 255)
    private String providerTransactionId;

    /**
     * Provider response (JSON)
     */
    @Convert(converter = com.gogidix.platform.subscription.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "provider_response")
    private Map<String, Object> providerResponse;

    /**
     * Transaction status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private TransactionStatus status;

    /**
     * Failure reason
     */
    @Lob
    @Column(name = "failure_reason")
    private String failureReason;

    /**
     * For refunds: reference to original transaction
     */
    @Column(name = "original_transaction_id")
    private String originalTransactionId;

    /**
     * Refund reason
     */
    @Lob
    @Column(name = "refund_reason")
    private String refundReason;

    /**
     * When payment was processed
     */
    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (transactionNumber == null) {
            transactionNumber = generateTransactionNumber();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Mark as completed
     */
    public void markCompleted(String providerTransactionId) {
        this.status = TransactionStatus.COMPLETED;
        this.providerTransactionId = providerTransactionId;
        this.processedAt = LocalDateTime.now();
    }

    /**
     * Mark as failed
     */
    public void markFailed(String reason) {
        this.status = TransactionStatus.FAILED;
        this.failureReason = reason;
        this.processedAt = LocalDateTime.now();
    }

    /**
     * Mark as cancelled
     */
    public void markCancelled() {
        this.status = TransactionStatus.CANCELLED;
        this.processedAt = LocalDateTime.now();
    }

    /**
     * Generate transaction number
     */
    private String generateTransactionNumber() {
        String prefix = switch (transactionType) {
            case CHARGE -> "CHG";
            case REFUND -> "REF";
            case PARTIAL_REFUND -> "PRF";
        };
        return prefix + "-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }

    public enum TransactionType {
        CHARGE,
        REFUND,
        PARTIAL_REFUND
    }

    public enum TransactionStatus {
        PENDING,
        COMPLETED,
        FAILED,
        CANCELLED
    }
}
