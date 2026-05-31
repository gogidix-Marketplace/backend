package com.gogidix.finance.accountspayable.domain.model;

import com.gogidix.finance.accountspayable.domain.event.PaymentProcessedEvent;
import com.gogidix.finance.accountspayable.shared.base.BaseEntity;
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
 * Multi-tenant payment tracking for accounts payable
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "payments")
public class Payment extends BaseEntity {

    private String paymentId;

    private String tenantId;

    private String vendorId;

    private String vendorName;

    private String invoiceId;

    private String invoiceNumber;

    private BigDecimal amount;

    private String currency;

    private PaymentStatus status;

    private PaymentMethod paymentMethod;

    private String paymentReference;

    private LocalDate paymentDate;

    private LocalDate scheduledDate;

    private Instant processedAt;

    private String processedBy;

    private String bankAccountNumber;

    private String bankRoutingNumber;

    private String checkNumber;

    private String transactionReference;

    private String description;

    private String notes;

    private String batchId;

    private String approvalReference;

    private String rejectionReason;

    private Instant cancelledAt;

    private String cancelledBy;

    private String cancellationReason;

    private List<String> invoiceIds;

    private List<PaymentAllocation> allocations;

    private BigDecimal feeAmount;

    private String exchangeRate;

    private String originalCurrency;

    private BigDecimal originalAmount;

    private String attachmentUrl;

    private String createdBy;

    @Builder.Default
    private List<PaymentProcessedEvent> domainEvents = new ArrayList<>();

    public enum PaymentStatus {
        PENDING,
        SCHEDULED,
        PROCESSING,
        COMPLETED,
        FAILED,
        CANCELLED,
        REVERSED
    }

    public enum PaymentMethod {
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
    public static class PaymentAllocation {
        private String invoiceId;
        private String invoiceNumber;
        private BigDecimal amount;
        private LocalDate allocationDate;
    }

    /**
     * Creates a new payment
     */
    public static Payment create(String tenantId, String vendorId, String vendorName,
                                  List<String> invoiceIds, BigDecimal amount, String currency,
                                  PaymentMethod paymentMethod, String createdBy) {
        Payment payment = Payment.builder()
            .tenantId(tenantId)
            .vendorId(vendorId)
            .vendorName(vendorName)
            .invoiceIds(invoiceIds)
            .amount(amount)
            .currency(currency)
            .status(PaymentStatus.PENDING)
            .paymentMethod(paymentMethod)
            .createdBy(createdBy)
            .allocations(new ArrayList<>())
            .build();

        payment.addDomainEvent(PaymentProcessedEvent.builder()
            .paymentId(payment.getPaymentId())
            .tenantId(tenantId)
            .vendorId(vendorId)
            .amount(amount)
            .currency(currency)
            .timestamp(Instant.now())
            .eventType("PAYMENT_CREATED")
            .build());

        return payment;
    }

    /**
     * Schedules a payment
     */
    public void schedule(LocalDate scheduledDate) {
        if (this.status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Can only schedule pending payments");
        }

        this.status = PaymentStatus.SCHEDULED;
        this.scheduledDate = scheduledDate;

        addDomainEvent(PaymentProcessedEvent.builder()
            .paymentId(this.paymentId)
            .tenantId(this.tenantId)
            .vendorId(this.vendorId)
            .amount(this.amount)
            .currency(this.currency)
            .timestamp(Instant.now())
            .eventType("PAYMENT_SCHEDULED")
            .build());
    }

    /**
     * Processes the payment
     */
    public void process(String processedBy, String paymentReference) {
        if (this.status != PaymentStatus.PENDING && this.status != PaymentStatus.SCHEDULED) {
            throw new IllegalStateException("Can only process pending or scheduled payments");
        }

        this.status = PaymentStatus.PROCESSING;
        this.processedBy = processedBy;
        this.paymentReference = paymentReference;

        addDomainEvent(PaymentProcessedEvent.builder()
            .paymentId(this.paymentId)
            .tenantId(this.tenantId)
            .vendorId(this.vendorId)
            .amount(this.amount)
            .currency(this.currency)
            .timestamp(Instant.now())
            .eventType("PAYMENT_PROCESSING")
            .build());
    }

    /**
     * Marks payment as completed
     */
    public void complete(String transactionReference) {
        if (this.status != PaymentStatus.PROCESSING) {
            throw new IllegalStateException("Can only complete processing payments");
        }

        this.status = PaymentStatus.COMPLETED;
        this.processedAt = Instant.now();
        this.transactionReference = transactionReference;
        this.paymentDate = LocalDate.now();

        addDomainEvent(PaymentProcessedEvent.builder()
            .paymentId(this.paymentId)
            .tenantId(this.tenantId)
            .vendorId(this.vendorId)
            .amount(this.amount)
            .currency(this.currency)
            .timestamp(Instant.now())
            .eventType("PAYMENT_COMPLETED")
            .build());
    }

    /**
     * Marks payment as failed
     */
    public void fail(String reason) {
        this.status = PaymentStatus.FAILED;
        this.rejectionReason = reason;

        addDomainEvent(PaymentProcessedEvent.builder()
            .paymentId(this.paymentId)
            .tenantId(this.tenantId)
            .vendorId(this.vendorId)
            .amount(this.amount)
            .currency(this.currency)
            .timestamp(Instant.now())
            .eventType("PAYMENT_FAILED")
            .build());
    }

    /**
     * Cancels the payment
     */
    public void cancel(String cancelledBy, String reason) {
        if (this.status == PaymentStatus.COMPLETED || this.status == PaymentStatus.REVERSED) {
            throw new IllegalStateException("Cannot cancel completed or reversed payments");
        }

        this.status = PaymentStatus.CANCELLED;
        this.cancelledBy = cancelledBy;
        this.cancellationReason = reason;
        this.cancelledAt = Instant.now();

        addDomainEvent(PaymentProcessedEvent.builder()
            .paymentId(this.paymentId)
            .tenantId(this.tenantId)
            .vendorId(this.vendorId)
            .amount(this.amount)
            .currency(this.currency)
            .timestamp(Instant.now())
            .eventType("PAYMENT_CANCELLED")
            .build());
    }

    /**
     * Reverses the payment
     */
    public void reverse(String reason) {
        if (this.status != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Can only reverse completed payments");
        }

        this.status = PaymentStatus.REVERSED;
        this.notes = reason;

        addDomainEvent(PaymentProcessedEvent.builder()
            .paymentId(this.paymentId)
            .tenantId(this.tenantId)
            .vendorId(this.vendorId)
            .amount(this.amount)
            .currency(this.currency)
            .timestamp(Instant.now())
            .eventType("PAYMENT_REVERSED")
            .build());
    }

    /**
     * Adds an invoice allocation
     */
    public void addAllocation(String invoiceId, String invoiceNumber, BigDecimal amount) {
        if (this.allocations == null) {
            this.allocations = new ArrayList<>();
        }

        PaymentAllocation allocation = PaymentAllocation.builder()
            .invoiceId(invoiceId)
            .invoiceNumber(invoiceNumber)
            .amount(amount)
            .allocationDate(LocalDate.now())
            .build();

        this.allocations.add(allocation);
    }

    /**
     * Sets payment method details
     */
    public void setPaymentMethodDetails(String bankAccountNumber, String bankRoutingNumber,
                                        String checkNumber) {
        this.bankAccountNumber = bankAccountNumber;
        this.bankRoutingNumber = bankRoutingNumber;
        this.checkNumber = checkNumber;
    }

    /**
     * Sets fee amount
     */
    public void setFee(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    /**
     * Sets currency conversion details
     */
    public void setCurrencyConversion(String exchangeRate, String originalCurrency,
                                      BigDecimal originalAmount) {
        this.exchangeRate = exchangeRate;
        this.originalCurrency = originalCurrency;
        this.originalAmount = originalAmount;
    }

    /**
     * Checks if payment can be processed
     */
    public boolean canProcess() {
        return this.status == PaymentStatus.PENDING || this.status == PaymentStatus.SCHEDULED;
    }

    /**
     * Checks if payment can be cancelled
     */
    public boolean canCancel() {
        return this.status != PaymentStatus.COMPLETED && this.status != PaymentStatus.REVERSED;
    }

    /**
     * Gets total amount including fees
     */
    public BigDecimal getTotalAmount() {
        if (this.feeAmount != null) {
            return this.amount.add(this.feeAmount);
        }
        return this.amount;
    }

    public void addDomainEvent(PaymentProcessedEvent event) {
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
