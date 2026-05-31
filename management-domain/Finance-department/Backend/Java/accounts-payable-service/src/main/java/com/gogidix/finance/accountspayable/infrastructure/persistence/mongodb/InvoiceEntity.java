package com.gogidix.finance.accountspayable.infrastructure.persistence.mongodb;

import com.gogidix.finance.accountspayable.domain.model.Invoice;
import com.gogidix.finance.accountspayable.domain.event.InvoiceCreatedEvent;
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
 * MongoDB document entity for storing Invoice domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "invoices")
public class InvoiceEntity {

    @Id
    private String id;

    @Indexed
    @Field("invoice_id")
    private String invoiceId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("vendor_id")
    private String vendorId;

    @Field("vendor_name")
    private String vendorName;

    @Field("vendor_code")
    private String vendorCode;

    @Indexed
    @Field("invoice_number")
    private String invoiceNumber;

    @Field("purchase_order_number")
    private String purchaseOrderNumber;

    @Field("invoice_date")
    private LocalDate invoiceDate;

    @Field("due_date")
    private LocalDate dueDate;

    @Field("received_date")
    private LocalDate receivedDate;

    @Field("amount")
    private BigDecimal amount;

    @Field("tax_amount")
    private BigDecimal taxAmount;

    @Field("discount_amount")
    private BigDecimal discountAmount;

    @Field("net_amount")
    private BigDecimal netAmount;

    @Field("currency")
    private String currency;

    @Indexed
    @Field("status")
    private String status;

    @Field("submitted_by")
    private String submittedBy;

    @Field("submitted_at")
    private Instant submittedAt;

    @Field("approved_by")
    private String approvedBy;

    @Field("approved_at")
    private Instant approvedAt;

    @Field("rejection_reason")
    private String rejectionReason;

    @Field("payment_reference")
    private String paymentReference;

    @Field("paid_at")
    private Instant paidAt;

    @Field("description")
    private String description;

    @Field("notes")
    private String notes;

    @Field("internal_reference")
    private String internalReference;

    @Field("department")
    private String department;

    @Field("cost_center")
    private String costCenter;

    @Field("project_id")
    private String projectId;

    @Field("line_items")
    private List<InvoiceLineItemEmbed> lineItems;

    @Field("attachments")
    private List<String> attachments;

    @Field("tags")
    private List<String> tags;

    @Field("requires_approval")
    private Boolean requiresApproval;

    @Field("approval_level")
    private String approvalLevel;

    @Field("gl_account")
    private String glAccount;

    @Field("tax_code")
    private String taxCode;

    @Field("tax_included")
    private Boolean taxIncluded;

    @Field("discount_valid_until")
    private LocalDate discountValidUntil;

    @Field("discount_percentage")
    private BigDecimal discountPercentage;

    @Field("payment_terms")
    private String paymentTerms;

    @Field("domain_events")
    private List<InvoiceCreatedEvent> domainEvents;

    // Default constructor for MongoDB
    public InvoiceEntity() {
    }

    // Constructor from domain model
    public InvoiceEntity(Invoice invoice) {
        this.invoiceId = invoice.getInvoiceId();
        this.tenantId = invoice.getTenantId();
        this.vendorId = invoice.getVendorId();
        this.vendorName = invoice.getVendorName();
        this.vendorCode = invoice.getVendorCode();
        this.invoiceNumber = invoice.getInvoiceNumber();
        this.purchaseOrderNumber = invoice.getPurchaseOrderNumber();
        this.invoiceDate = invoice.getInvoiceDate();
        this.dueDate = invoice.getDueDate();
        this.receivedDate = invoice.getReceivedDate();
        this.amount = invoice.getAmount();
        this.taxAmount = invoice.getTaxAmount();
        this.discountAmount = invoice.getDiscountAmount();
        this.netAmount = invoice.getNetAmount();
        this.currency = invoice.getCurrency();
        this.status = invoice.getStatus() != null ? invoice.getStatus().name() : null;
        this.submittedBy = invoice.getSubmittedBy();
        this.submittedAt = invoice.getSubmittedAt();
        this.approvedBy = invoice.getApprovedBy();
        this.approvedAt = invoice.getApprovedAt();
        this.rejectionReason = invoice.getRejectionReason();
        this.paymentReference = invoice.getPaymentReference();
        this.paidAt = invoice.getPaidAt();
        this.description = invoice.getDescription();
        this.notes = invoice.getNotes();
        this.internalReference = invoice.getInternalReference();
        this.department = invoice.getDepartment();
        this.costCenter = invoice.getCostCenter();
        this.projectId = invoice.getProjectId();
        this.requiresApproval = invoice.getRequiresApproval();
        this.approvalLevel = invoice.getApprovalLevel() != null ? invoice.getApprovalLevel().name() : null;
        this.glAccount = invoice.getGlAccount();
        this.taxCode = invoice.getTaxCode();
        this.taxIncluded = invoice.getTaxIncluded();
        this.discountValidUntil = invoice.getDiscountValidUntil();
        this.discountPercentage = invoice.getDiscountPercentage();
        this.paymentTerms = invoice.getPaymentTerms();

        // Convert line items
        if (invoice.getLineItems() != null) {
            this.lineItems = new ArrayList<>();
            for (Invoice.InvoiceLineItem item : invoice.getLineItems()) {
                this.lineItems.add(new InvoiceLineItemEmbed(item));
            }
        }

        this.attachments = invoice.getAttachments() != null ? new ArrayList<>(invoice.getAttachments()) : new ArrayList<>();
        this.tags = invoice.getTags() != null ? new ArrayList<>(invoice.getTags()) : new ArrayList<>();
        this.domainEvents = invoice.getDomainEvents() != null ? new ArrayList<>(invoice.getDomainEvents()) : new ArrayList<>();
    }

    // Convert to domain model
    public Invoice toDomainModel() {
        List<Invoice.InvoiceLineItem> lineItemList = new ArrayList<>();
        if (this.lineItems != null) {
            for (InvoiceLineItemEmbed embed : this.lineItems) {
                lineItemList.add(embed.toDomainModel());
            }
        }

        return Invoice.builder()
                .invoiceId(this.invoiceId)
                .tenantId(this.tenantId)
                .vendorId(this.vendorId)
                .vendorName(this.vendorName)
                .vendorCode(this.vendorCode)
                .invoiceNumber(this.invoiceNumber)
                .purchaseOrderNumber(this.purchaseOrderNumber)
                .invoiceDate(this.invoiceDate)
                .dueDate(this.dueDate)
                .receivedDate(this.receivedDate)
                .amount(this.amount)
                .taxAmount(this.taxAmount)
                .discountAmount(this.discountAmount)
                .netAmount(this.netAmount)
                .currency(this.currency)
                .status(this.status != null ? Invoice.InvoiceStatus.valueOf(this.status) : null)
                .submittedBy(this.submittedBy)
                .submittedAt(this.submittedAt)
                .approvedBy(this.approvedBy)
                .approvedAt(this.approvedAt)
                .rejectionReason(this.rejectionReason)
                .paymentReference(this.paymentReference)
                .paidAt(this.paidAt)
                .description(this.description)
                .notes(this.notes)
                .internalReference(this.internalReference)
                .department(this.department)
                .costCenter(this.costCenter)
                .projectId(this.projectId)
                .lineItems(lineItemList)
                .attachments(this.attachments != null ? new ArrayList<>(this.attachments) : new ArrayList<>())
                .tags(this.tags != null ? new ArrayList<>(this.tags) : new ArrayList<>())
                .requiresApproval(this.requiresApproval)
                .approvalLevel(this.approvalLevel != null ? Invoice.ApprovalLevel.valueOf(this.approvalLevel) : null)
                .glAccount(this.glAccount)
                .taxCode(this.taxCode)
                .taxIncluded(this.taxIncluded)
                .discountValidUntil(this.discountValidUntil)
                .discountPercentage(this.discountPercentage)
                .paymentTerms(this.paymentTerms)
                .domainEvents(this.domainEvents != null ? new ArrayList<>(this.domainEvents) : new ArrayList<>())
                .build();
    }

    // Update from domain model (for partial updates)
    public void updateFrom(Invoice invoice) {
        this.vendorId = invoice.getVendorId();
        this.vendorName = invoice.getVendorName();
        this.vendorCode = invoice.getVendorCode();
        this.invoiceNumber = invoice.getInvoiceNumber();
        this.purchaseOrderNumber = invoice.getPurchaseOrderNumber();
        this.invoiceDate = invoice.getInvoiceDate();
        this.dueDate = invoice.getDueDate();
        this.receivedDate = invoice.getReceivedDate();
        this.amount = invoice.getAmount();
        this.taxAmount = invoice.getTaxAmount();
        this.discountAmount = invoice.getDiscountAmount();
        this.netAmount = invoice.getNetAmount();
        this.currency = invoice.getCurrency();
        this.status = invoice.getStatus() != null ? invoice.getStatus().name() : null;
        this.submittedBy = invoice.getSubmittedBy();
        this.submittedAt = invoice.getSubmittedAt();
        this.approvedBy = invoice.getApprovedBy();
        this.approvedAt = invoice.getApprovedAt();
        this.rejectionReason = invoice.getRejectionReason();
        this.paymentReference = invoice.getPaymentReference();
        this.paidAt = invoice.getPaidAt();
        this.description = invoice.getDescription();
        this.notes = invoice.getNotes();
        this.internalReference = invoice.getInternalReference();
        this.department = invoice.getDepartment();
        this.costCenter = invoice.getCostCenter();
        this.projectId = invoice.getProjectId();
        this.requiresApproval = invoice.getRequiresApproval();
        this.approvalLevel = invoice.getApprovalLevel() != null ? invoice.getApprovalLevel().name() : null;
        this.glAccount = invoice.getGlAccount();
        this.taxCode = invoice.getTaxCode();
        this.taxIncluded = invoice.getTaxIncluded();
        this.discountValidUntil = invoice.getDiscountValidUntil();
        this.discountPercentage = invoice.getDiscountPercentage();
        this.paymentTerms = invoice.getPaymentTerms();

        // Update line items
        if (invoice.getLineItems() != null) {
            this.lineItems = new ArrayList<>();
            for (Invoice.InvoiceLineItem item : invoice.getLineItems()) {
                this.lineItems.add(new InvoiceLineItemEmbed(item));
            }
        }

        this.attachments = invoice.getAttachments() != null ? new ArrayList<>(invoice.getAttachments()) : new ArrayList<>();
        this.tags = invoice.getTags() != null ? new ArrayList<>(invoice.getTags()) : new ArrayList<>();
        this.domainEvents = invoice.getDomainEvents() != null ? new ArrayList<>(invoice.getDomainEvents()) : new ArrayList<>();
    }

    // Embedded class for line items
    public static class InvoiceLineItemEmbed {
        private String lineItemId;
        private String description;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private BigDecimal amount;
        private String accountCode;
        private String taxCode;

        public InvoiceLineItemEmbed() {
        }

        public InvoiceLineItemEmbed(Invoice.InvoiceLineItem item) {
            this.lineItemId = item.getLineItemId();
            this.description = item.getDescription();
            this.quantity = item.getQuantity();
            this.unitPrice = item.getUnitPrice();
            this.amount = item.getAmount();
            this.accountCode = item.getAccountCode();
            this.taxCode = item.getTaxCode();
        }

        public Invoice.InvoiceLineItem toDomainModel() {
            return Invoice.InvoiceLineItem.builder()
                    .lineItemId(this.lineItemId)
                    .description(this.description)
                    .quantity(this.quantity)
                    .unitPrice(this.unitPrice)
                    .amount(this.amount)
                    .accountCode(this.accountCode)
                    .taxCode(this.taxCode)
                    .build();
        }

        // Getters and setters
        public String getLineItemId() {
            return lineItemId;
        }

        public void setLineItemId(String lineItemId) {
            this.lineItemId = lineItemId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public BigDecimal getQuantity() {
            return quantity;
        }

        public void setQuantity(BigDecimal quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getAccountCode() {
            return accountCode;
        }

        public void setAccountCode(String accountCode) {
            this.accountCode = accountCode;
        }

        public String getTaxCode() {
            return taxCode;
        }

        public void setTaxCode(String taxCode) {
            this.taxCode = taxCode;
        }
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
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

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
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

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Instant getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Instant approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Instant paidAt) {
        this.paidAt = paidAt;
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

    public String getInternalReference() {
        return internalReference;
    }

    public void setInternalReference(String internalReference) {
        this.internalReference = internalReference;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public List<InvoiceLineItemEmbed> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<InvoiceLineItemEmbed> lineItems) {
        this.lineItems = lineItems;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Boolean getRequiresApproval() {
        return requiresApproval;
    }

    public void setRequiresApproval(Boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
    }

    public String getApprovalLevel() {
        return approvalLevel;
    }

    public void setApprovalLevel(String approvalLevel) {
        this.approvalLevel = approvalLevel;
    }

    public String getGlAccount() {
        return glAccount;
    }

    public void setGlAccount(String glAccount) {
        this.glAccount = glAccount;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public Boolean getTaxIncluded() {
        return taxIncluded;
    }

    public void setTaxIncluded(Boolean taxIncluded) {
        this.taxIncluded = taxIncluded;
    }

    public LocalDate getDiscountValidUntil() {
        return discountValidUntil;
    }

    public void setDiscountValidUntil(LocalDate discountValidUntil) {
        this.discountValidUntil = discountValidUntil;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public List<InvoiceCreatedEvent> getDomainEvents() {
        return domainEvents;
    }

    public void setDomainEvents(List<InvoiceCreatedEvent> domainEvents) {
        this.domainEvents = domainEvents;
    }
}
