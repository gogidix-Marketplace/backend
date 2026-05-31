package com.gogidix.finance.accountspayable.infrastructure.persistence.mongodb;

import com.gogidix.finance.accountspayable.domain.model.Payment;
import com.gogidix.finance.accountspayable.domain.event.PaymentProcessedEvent;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * MongoDB document entity for storing Payment domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "payments")
public class PaymentEntity {

    @Id
    private String id;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    @Indexed
    @Field("payment_id")
    private String paymentId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("vendor_id")
    private String vendorId;

    @Field("vendor_name")
    private String vendorName;

    @Field("invoice_id")
    private String invoiceId;

    @Field("invoice_number")
    private String invoiceNumber;

    @Field("amount")
    private BigDecimal amount;

    @Field("currency")
    private String currency;

    @Indexed
    @Field("status")
    private String status;

    @Field("payment_method")
    private String paymentMethod;

    @Field("payment_reference")
    private String paymentReference;

    @Field("payment_date")
    private LocalDate paymentDate;

    @Field("scheduled_date")
    private LocalDate scheduledDate;

    @Field("processed_at")
    private Instant processedAt;

    @Field("processed_by")
    private String processedBy;

    @Field("bank_account_number")
    private String bankAccountNumber;

    @Field("bank_routing_number")
    private String bankRoutingNumber;

    @Field("check_number")
    private String checkNumber;

    @Field("transaction_reference")
    private String transactionReference;

    @Field("description")
    private String description;

    @Field("notes")
    private String notes;

    @Field("batch_id")
    private String batchId;

    @Field("approval_reference")
    private String approvalReference;

    @Field("rejection_reason")
    private String rejectionReason;

    @Field("cancelled_at")
    private Instant cancelledAt;

    @Field("cancelled_by")
    private String cancelledBy;

    @Field("cancellation_reason")
    private String cancellationReason;

    @Field("invoice_ids")
    private List<String> invoiceIds;

    @Field("allocations")
    private List<PaymentAllocationEmbed> allocations;

    @Field("fee_amount")
    private BigDecimal feeAmount;

    @Field("exchange_rate")
    private String exchangeRate;

    @Field("original_currency")
    private String originalCurrency;

    @Field("original_amount")
    private BigDecimal originalAmount;

    @Field("attachment_url")
    private String attachmentUrl;

    @Field("created_by")
    private String createdBy;

    @Field("domain_events")
    private List<PaymentProcessedEvent> domainEvents;

    // Default constructor for MongoDB
    public PaymentEntity() {
    }

    // Constructor from domain model
    public PaymentEntity(Payment payment) {
        this.id = payment.getId();
        this.createdAt = payment.getCreatedAt();
        this.updatedAt = payment.getUpdatedAt();
        this.paymentId = payment.getPaymentId();
        this.tenantId = payment.getTenantId();
        this.vendorId = payment.getVendorId();
        this.vendorName = payment.getVendorName();
        this.invoiceId = payment.getInvoiceId();
        this.invoiceNumber = payment.getInvoiceNumber();
        this.amount = payment.getAmount();
        this.currency = payment.getCurrency();
        this.status = payment.getStatus() != null ? payment.getStatus().name() : null;
        this.paymentMethod = payment.getPaymentMethod() != null ? payment.getPaymentMethod().name() : null;
        this.paymentReference = payment.getPaymentReference();
        this.paymentDate = payment.getPaymentDate();
        this.scheduledDate = payment.getScheduledDate();
        this.processedAt = payment.getProcessedAt();
        this.processedBy = payment.getProcessedBy();
        this.bankAccountNumber = payment.getBankAccountNumber();
        this.bankRoutingNumber = payment.getBankRoutingNumber();
        this.checkNumber = payment.getCheckNumber();
        this.transactionReference = payment.getTransactionReference();
        this.description = payment.getDescription();
        this.notes = payment.getNotes();
        this.batchId = payment.getBatchId();
        this.approvalReference = payment.getApprovalReference();
        this.rejectionReason = payment.getRejectionReason();
        this.cancelledAt = payment.getCancelledAt();
        this.cancelledBy = payment.getCancelledBy();
        this.cancellationReason = payment.getCancellationReason();
        this.invoiceIds = payment.getInvoiceIds() != null ? new ArrayList<>(payment.getInvoiceIds()) : new ArrayList<>();
        this.feeAmount = payment.getFeeAmount();
        this.exchangeRate = payment.getExchangeRate();
        this.originalCurrency = payment.getOriginalCurrency();
        this.originalAmount = payment.getOriginalAmount();
        this.attachmentUrl = payment.getAttachmentUrl();
        this.createdBy = payment.getCreatedBy();
        this.domainEvents = payment.getDomainEvents() != null ? new ArrayList<>(payment.getDomainEvents()) : new ArrayList<>();

        // Convert allocations
        if (payment.getAllocations() != null) {
            this.allocations = new ArrayList<>();
            for (Payment.PaymentAllocation alloc : payment.getAllocations()) {
                this.allocations.add(new PaymentAllocationEmbed(alloc));
            }
        }
    }

    // Convert to domain model
    public Payment toDomainModel() {
        List<Payment.PaymentAllocation> allocationList = new ArrayList<>();
        if (this.allocations != null) {
            for (PaymentAllocationEmbed embed : this.allocations) {
                allocationList.add(embed.toDomainModel());
            }
        }

        Payment payment = Payment.builder()
                .paymentId(this.paymentId)
                .tenantId(this.tenantId)
                .vendorId(this.vendorId)
                .vendorName(this.vendorName)
                .invoiceId(this.invoiceId)
                .invoiceNumber(this.invoiceNumber)
                .amount(this.amount)
                .currency(this.currency)
                .status(this.status != null ? Payment.PaymentStatus.valueOf(this.status) : null)
                .paymentMethod(this.paymentMethod != null ? Payment.PaymentMethod.valueOf(this.paymentMethod) : null)
                .paymentReference(this.paymentReference)
                .paymentDate(this.paymentDate)
                .scheduledDate(this.scheduledDate)
                .processedAt(this.processedAt)
                .processedBy(this.processedBy)
                .bankAccountNumber(this.bankAccountNumber)
                .bankRoutingNumber(this.bankRoutingNumber)
                .checkNumber(this.checkNumber)
                .transactionReference(this.transactionReference)
                .description(this.description)
                .notes(this.notes)
                .batchId(this.batchId)
                .approvalReference(this.approvalReference)
                .rejectionReason(this.rejectionReason)
                .cancelledAt(this.cancelledAt)
                .cancelledBy(this.cancelledBy)
                .cancellationReason(this.cancellationReason)
                .invoiceIds(this.invoiceIds != null ? new ArrayList<>(this.invoiceIds) : new ArrayList<>())
                .allocations(allocationList)
                .feeAmount(this.feeAmount)
                .exchangeRate(this.exchangeRate)
                .originalCurrency(this.originalCurrency)
                .originalAmount(this.originalAmount)
                .attachmentUrl(this.attachmentUrl)
                .createdBy(this.createdBy)
                .domainEvents(this.domainEvents != null ? new ArrayList<>(this.domainEvents) : new ArrayList<>())
                .build();
        payment.setId(this.id);
        payment.setCreatedAt(this.createdAt);
        payment.setUpdatedAt(this.updatedAt);
        return payment;
    }

    // Update from domain model (for partial updates)
    public void updateFrom(Payment payment) {
        this.vendorId = payment.getVendorId();
        this.vendorName = payment.getVendorName();
        this.invoiceId = payment.getInvoiceId();
        this.invoiceNumber = payment.getInvoiceNumber();
        this.amount = payment.getAmount();
        this.currency = payment.getCurrency();
        this.status = payment.getStatus() != null ? payment.getStatus().name() : null;
        this.paymentMethod = payment.getPaymentMethod() != null ? payment.getPaymentMethod().name() : null;
        this.paymentReference = payment.getPaymentReference();
        this.paymentDate = payment.getPaymentDate();
        this.scheduledDate = payment.getScheduledDate();
        this.processedAt = payment.getProcessedAt();
        this.processedBy = payment.getProcessedBy();
        this.bankAccountNumber = payment.getBankAccountNumber();
        this.bankRoutingNumber = payment.getBankRoutingNumber();
        this.checkNumber = payment.getCheckNumber();
        this.transactionReference = payment.getTransactionReference();
        this.description = payment.getDescription();
        this.notes = payment.getNotes();
        this.batchId = payment.getBatchId();
        this.approvalReference = payment.getApprovalReference();
        this.rejectionReason = payment.getRejectionReason();
        this.cancelledAt = payment.getCancelledAt();
        this.cancelledBy = payment.getCancelledBy();
        this.cancellationReason = payment.getCancellationReason();
        this.invoiceIds = payment.getInvoiceIds() != null ? new ArrayList<>(payment.getInvoiceIds()) : new ArrayList<>();
        this.feeAmount = payment.getFeeAmount();
        this.exchangeRate = payment.getExchangeRate();
        this.originalCurrency = payment.getOriginalCurrency();
        this.originalAmount = payment.getOriginalAmount();
        this.attachmentUrl = payment.getAttachmentUrl();
        this.domainEvents = payment.getDomainEvents() != null ? new ArrayList<>(payment.getDomainEvents()) : new ArrayList<>();

        // Update allocations
        if (payment.getAllocations() != null) {
            this.allocations = new ArrayList<>();
            for (Payment.PaymentAllocation alloc : payment.getAllocations()) {
                this.allocations.add(new PaymentAllocationEmbed(alloc));
            }
        }
    }

    // Embedded class for payment allocations
    public static class PaymentAllocationEmbed {
        private String invoiceId;
        private String invoiceNumber;
        private BigDecimal amount;
        private LocalDate allocationDate;

        public PaymentAllocationEmbed() {
        }

        public PaymentAllocationEmbed(Payment.PaymentAllocation alloc) {
            this.invoiceId = alloc.getInvoiceId();
            this.invoiceNumber = alloc.getInvoiceNumber();
            this.amount = alloc.getAmount();
            this.allocationDate = alloc.getAllocationDate();
        }

        public Payment.PaymentAllocation toDomainModel() {
            return Payment.PaymentAllocation.builder()
                    .invoiceId(this.invoiceId)
                    .invoiceNumber(this.invoiceNumber)
                    .amount(this.amount)
                    .allocationDate(this.allocationDate)
                    .build();
        }

        // Getters and setters
        public String getInvoiceId() {
            return invoiceId;
        }

        public void setInvoiceId(String invoiceId) {
            this.invoiceId = invoiceId;
        }

        public String getInvoiceNumber() {
            return invoiceNumber;
        }

        public void setInvoiceNumber(String invoiceNumber) {
            this.invoiceNumber = invoiceNumber;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public LocalDate getAllocationDate() {
            return allocationDate;
        }

        public void setAllocationDate(LocalDate allocationDate) {
            this.allocationDate = allocationDate;
        }
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    public String getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(String processedBy) {
        this.processedBy = processedBy;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankRoutingNumber() {
        return bankRoutingNumber;
    }

    public void setBankRoutingNumber(String bankRoutingNumber) {
        this.bankRoutingNumber = bankRoutingNumber;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getApprovalReference() {
        return approvalReference;
    }

    public void setApprovalReference(String approvalReference) {
        this.approvalReference = approvalReference;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public Instant getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(Instant cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public List<String> getInvoiceIds() {
        return invoiceIds;
    }

    public void setInvoiceIds(List<String> invoiceIds) {
        this.invoiceIds = invoiceIds;
    }

    public List<PaymentAllocationEmbed> getAllocations() {
        return allocations;
    }

    public void setAllocations(List<PaymentAllocationEmbed> allocations) {
        this.allocations = allocations;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getOriginalCurrency() {
        return originalCurrency;
    }

    public void setOriginalCurrency(String originalCurrency) {
        this.originalCurrency = originalCurrency;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<PaymentProcessedEvent> getDomainEvents() {
        return domainEvents;
    }

    public void setDomainEvents(List<PaymentProcessedEvent> domainEvents) {
        this.domainEvents = domainEvents;
    }
}
