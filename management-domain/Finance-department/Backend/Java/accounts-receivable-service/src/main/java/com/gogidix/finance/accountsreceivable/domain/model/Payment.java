package com.gogidix.finance.accountsreceivable.domain.model;

import com.gogidix.finance.accountsreceivable.domain.event.PaymentReceivedEvent;
import com.gogidix.finance.accountsreceivable.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Payment Domain Entity
 * Represents payments received from customers
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "ar_payments")
public class Payment extends BaseEntity {

    private String paymentId;

    private String tenantId;

    private String paymentNumber;

    private String customerId;

    private String customerName;

    private String invoiceId;

    private String invoiceNumber;

    private PaymentType paymentType;

    private PaymentStatus status;

    private BigDecimal amount;

    private String currency;

    private LocalDate paymentDate;

    private String paymentMethod;

    private String referenceNumber;

    private String bankAccount;

    private String transactionId;

    private String checkNumber;

    private String creditCardNumber;

    private String description;

    private String notes;

    private LocalDate depositDate;

    private String depositSlipNumber;

    private String batchId;

    private Boolean reconciled;

    private Instant reconciledAt;

    private String reconciledBy;

    private String bankReconciliationId;

    private LocalDate clearedDate;

    private Boolean autoApplied;

    @Builder.Default
    private List<PaymentAllocation> allocations = new ArrayList<>();

    private BigDecimal unappliedAmount;

    private String exchangeRate;

    private String baseCurrency;

    private BigDecimal baseCurrencyAmount;

    private String gatewayTransactionId;

    private String gatewayResponseCode;

    private String gatewayResponseMessage;

    private Instant processedAt;

    private String processedBy;

    private String approvedBy;

    private Instant approvedAt;

    private String rejectionReason;

    private String refundedTo;

    private BigDecimal refundAmount;

    private Instant refundedAt;

    private String refundReason;

    private String parentId;

    private Boolean isReversal;

    private List<String> tags;

    @Builder.Default
    private List<PaymentReceivedEvent> domainEvents = new ArrayList<>();

    public enum PaymentType {
        RECEIVED,
        REFUND,
        PREPAYMENT,
        ADVANCE,
        DEPOSIT,
        CREDIT_CARD_PAYMENT,
        BANK_TRANSFER,
        CASH,
        CHECK,
        ELECTRONIC_FUNDS_TRANSFER,
        ONLINE_PAYMENT,
        MOBILE_PAYMENT,
        DIRECT_DEBIT
    }

    public enum PaymentStatus {
        PENDING,
        COMPLETED,
        FAILED,
        CANCELLED,
        REVERSED,
        PARTIALLY_APPLIED,
        FULLY_APPLIED,
        ON_HOLD,
        PROCESSING
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentAllocation {
        private String allocationId;
        private String invoiceId;
        private String invoiceNumber;
        private BigDecimal allocatedAmount;
        private LocalDate allocationDate;
        private String notes;
    }

    /**
     * Creates a new payment
     */
    public static Payment create(String tenantId, String customerId, String customerName,
                                  String invoiceId, String invoiceNumber,
                                  PaymentType paymentType, BigDecimal amount,
                                  String currency, LocalDate paymentDate, String paymentMethod) {
        Payment payment = Payment.builder()
            .tenantId(tenantId)
            .customerId(customerId)
            .customerName(customerName)
            .invoiceId(invoiceId)
            .invoiceNumber(invoiceNumber)
            .paymentType(paymentType)
            .amount(amount)
            .currency(currency)
            .paymentDate(paymentDate)
            .paymentMethod(paymentMethod)
            .status(PaymentStatus.PENDING)
            .reconciled(false)
            .autoApplied(false)
            .unappliedAmount(amount)
            .allocations(new ArrayList<>())
            .tags(new ArrayList<>())
            .build();

        payment.addDomainEvent(PaymentReceivedEvent.builder()
            .paymentId(payment.getPaymentId())
            .tenantId(tenantId)
            .customerId(customerId)
            .invoiceId(invoiceId)
            .amount(amount)
            .currency(currency)
            .paymentMethod(paymentMethod)
            .eventType("PAYMENT_INITIATED")
            .timestamp(Instant.now())
            .build());

        return payment;
    }

    /**
     * Completes the payment
     */
    public void complete(String transactionId) {
        if (this.status != PaymentStatus.PENDING && this.status != PaymentStatus.PROCESSING) {
            throw new IllegalStateException("Can only complete pending or processing payments");
        }

        this.status = PaymentStatus.COMPLETED;
        this.transactionId = transactionId;
        this.processedAt = Instant.now();

        addDomainEvent(PaymentReceivedEvent.builder()
            .paymentId(this.paymentId)
            .tenantId(this.tenantId)
            .customerId(this.customerId)
            .invoiceId(this.invoiceId)
            .amount(this.amount)
            .currency(this.currency)
            .paymentMethod(this.paymentMethod)
            .transactionId(transactionId)
            .eventType("PAYMENT_COMPLETED")
            .timestamp(Instant.now())
            .build());
    }

    /**
     * Fails the payment
     */
    public void fail(String reason) {
        if (this.status == PaymentStatus.COMPLETED || this.status == PaymentStatus.REVERSED) {
            throw new IllegalStateException("Cannot fail completed or reversed payments");
        }

        this.status = PaymentStatus.FAILED;
        this.rejectionReason = reason;

        addDomainEvent(PaymentReceivedEvent.builder()
            .paymentId(this.paymentId)
            .tenantId(this.tenantId)
            .customerId(this.customerId)
            .amount(this.amount)
            .currency(this.currency)
            .eventType("PAYMENT_FAILED")
            .rejectionReason(reason)
            .timestamp(Instant.now())
            .build());
    }

    /**
     * Applies payment to invoice
     */
    public void applyToInvoice(String invoiceId, String invoiceNumber, BigDecimal amount) {
        if (this.status != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Can only apply completed payments");
        }

        if (this.unappliedAmount.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient unapplied amount");
        }

        PaymentAllocation allocation = PaymentAllocation.builder()
            .allocationId(java.util.UUID.randomUUID().toString())
            .invoiceId(invoiceId)
            .invoiceNumber(invoiceNumber)
            .allocatedAmount(amount)
            .allocationDate(LocalDate.now())
            .build();

        this.allocations.add(allocation);
        this.unappliedAmount = this.unappliedAmount.subtract(amount);

        if (this.unappliedAmount.compareTo(BigDecimal.ZERO) == 0) {
            this.status = PaymentStatus.FULLY_APPLIED;
        } else {
            this.status = PaymentStatus.PARTIALLY_APPLIED;
        }

        addDomainEvent(PaymentReceivedEvent.builder()
            .paymentId(this.paymentId)
            .tenantId(this.tenantId)
            .customerId(this.customerId)
            .invoiceId(invoiceId)
            .amount(amount)
            .currency(this.currency)
            .eventType("PAYMENT_APPLIED")
            .timestamp(Instant.now())
            .build());
    }

    /**
     * Reverses the payment
     */
    public void reverse(String reason) {
        if (this.status != PaymentStatus.COMPLETED && this.status != PaymentStatus.PARTIALLY_APPLIED &&
            this.status != PaymentStatus.FULLY_APPLIED) {
            throw new IllegalStateException("Can only reverse completed or applied payments");
        }

        PaymentStatus previousStatus = this.status;
        this.status = PaymentStatus.REVERSED;
        this.rejectionReason = reason;
        this.isReversal = true;

        addDomainEvent(PaymentReceivedEvent.builder()
            .paymentId(this.paymentId)
            .tenantId(this.tenantId)
            .customerId(this.customerId)
            .invoiceId(this.invoiceId)
            .amount(this.amount)
            .currency(this.currency)
            .eventType("PAYMENT_REVERSED")
            .rejectionReason(reason)
            .previousStatus(previousStatus.name())
            .timestamp(Instant.now())
            .build());
    }

    /**
     * Refunds the payment
     */
    public void refund(BigDecimal amount, String reason) {
        if (this.status != PaymentStatus.COMPLETED && this.status != PaymentStatus.PARTIALLY_APPLIED &&
            this.status != PaymentStatus.FULLY_APPLIED) {
            throw new IllegalStateException("Can only refund completed or applied payments");
        }

        if (amount.compareTo(this.amount) > 0) {
            throw new IllegalArgumentException("Refund amount cannot exceed payment amount");
        }

        this.refundAmount = amount;
        this.refundReason = reason;
        this.refundedAt = Instant.now();

        if (this.amount.compareTo(amount) == 0) {
            this.status = PaymentStatus.REVERSED;
        }

        addDomainEvent(PaymentReceivedEvent.builder()
            .paymentId(this.paymentId)
            .tenantId(this.tenantId)
            .customerId(this.customerId)
            .amount(amount)
            .currency(this.currency)
            .eventType("PAYMENT_REFUNDED")
            .rejectionReason(reason)
            .timestamp(Instant.now())
            .build());
    }

    /**
     * Reconciles the payment
     */
    public void reconcile(String reconciledBy) {
        if (this.status != PaymentStatus.COMPLETED && this.status != PaymentStatus.PARTIALLY_APPLIED &&
            this.status != PaymentStatus.FULLY_APPLIED) {
            throw new IllegalStateException("Can only reconcile completed payments");
        }

        this.reconciled = true;
        this.reconciledAt = Instant.now();
        this.reconciledBy = reconciledBy;
    }

    /**
     * Adds a tag to the payment
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Removes a tag from the payment
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }

    /**
     * Adds an allocation
     */
    public void addAllocation(PaymentAllocation allocation) {
        if (this.allocations == null) {
            this.allocations = new ArrayList<>();
        }
        this.allocations.add(allocation);
    }

    public void addDomainEvent(PaymentReceivedEvent event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }
}
