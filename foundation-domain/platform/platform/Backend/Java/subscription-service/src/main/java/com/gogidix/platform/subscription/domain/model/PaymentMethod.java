package com.gogidix.platform.subscription.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Payment method entity.
 *
 * Stores payment method information:
 * - Credit/debit cards
 * - Bank accounts
 * - PayPal
 * - Other payment methods
 *
 * Sensitive data is tokenized via Stripe
 */
@Entity
@Table(name = "payment_methods", indexes = {
    @Index(name = "idx_payment_methods_tenant", columnList = "tenant_id"),
    @Index(name = "idx_payment_methods_customer", columnList = "customer_id"),
    @Index(name = "idx_payment_methods_default", columnList = "customer_id, is_default")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    @Column(name = "customer_id", nullable = false, length = 255)
    private String customerId;

    /**
     * Payment method type
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "method_type", nullable = false, length = 50)
    private MethodType methodType;

    /**
     * Payment provider (stripe, paypal, etc.)
     */
    @Column(name = "provider", length = 100)
    private String provider;

    /**
     * Provider's payment method ID (token)
     */
    @Column(name = "provider_method_id", length = 255)
    private String providerMethodId;

    /**
     * Card details (last 4 only)
     */
    @Column(name = "card_last4", length = 4)
    private String cardLast4;

    /**
     * Card brand (VISA, MASTERCARD, AMEX)
     */
    @Column(name = "card_brand", length = 50)
    private String cardBrand;

    /**
     * Card expiry month
     */
    @Column(name = "card_expiry_month")
    private Integer cardExpiryMonth;

    /**
     * Card expiry year
     */
    @Column(name = "card_expiry_year")
    private Integer cardExpiryYear;

    /**
     * Cardholder name
     */
    @Column(name = "cardholder_name", length = 255)
    private String cardholderName;

    /**
     * Bank name (for bank accounts)
     */
    @Column(name = "bank_name", length = 255)
    private String bankName;

    /**
     * Bank account last 4
     */
    @Column(name = "bank_account_last4", length = 4)
    private String bankAccountLast4;

    /**
     * Is default payment method
     */
    @Column(name = "is_default", nullable = false)
    @Builder.Default
    private boolean isDefault = false;

    /**
     * Is verified
     */
    @Column(name = "is_verified", nullable = false)
    @Builder.Default
    private boolean isVerified = false;

    /**
     * Payment method status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    @Builder.Default
    private PaymentMethodStatus status = PaymentMethodStatus.ACTIVE;

    /**
     * Billing address (JSON)
     */
    @Convert(converter = com.gogidix.platform.subscription.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "billing_address")
    private Map<String, Object> billingAddress;

    /**
     * Additional metadata
     */
    @Convert(converter = com.gogidix.platform.subscription.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "metadata")
    private Map<String, Object> metadata;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Set as default payment method
     */
    public void setAsDefault() {
        this.isDefault = true;
    }

    /**
     * Mark as verified
     */
    public void verify() {
        this.isVerified = true;
    }

    /**
     * Check if card is expired
     */
    public boolean isExpired() {
        if (methodType != MethodType.CREDIT_CARD && methodType != MethodType.DEBIT_CARD) {
            return false;
        }

        if (cardExpiryYear == null || cardExpiryMonth == null) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiry = LocalDateTime.of(cardExpiryYear, cardExpiryMonth, 1, 0, 0)
                .plusMonths(1).minusDays(1);

        return now.isAfter(expiry);
    }

    public enum MethodType {
        CREDIT_CARD,
        DEBIT_CARD,
        BANK_TRANSFER,
        PAYPAL
    }

    public enum PaymentMethodStatus {
        ACTIVE,
        INACTIVE,
        EXPIRED
    }
}
