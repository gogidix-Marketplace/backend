package com.gogidix.finance.accountsreceivable.infrastructure.persistence.mongodb;

import com.gogidix.finance.accountsreceivable.domain.event.InvoiceGeneratedEvent;
import com.gogidix.finance.accountsreceivable.domain.model.Invoice;
import com.gogidix.finance.accountsreceivable.domain.model.Payment;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "ar_invoices")
public class InvoiceEntity {
    @Id private String id;
    @Indexed @Field("invoice_id") private String invoiceId;
    @Indexed @Field("tenant_id") private String tenantId;
    @Field("invoice_number") private String invoiceNumber;
    @Field("customer_id") private String customerId;
    @Field("customer_name") private String customerName;
    @Field("customer_email") private String customerEmail;
    @Field("invoice_type") private String invoiceType;
    @Indexed @Field("status") private String status;
    @Field("invoice_date") private LocalDate invoiceDate;
    @Field("due_date") private LocalDate dueDate;
    @Field("sales_date") private LocalDate salesDate;
    @Field("purchase_order_number") private String purchaseOrderNumber;
    @Field("currency") private String currency;
    @Field("subtotal") private BigDecimal subtotal;
    @Field("tax_amount") private BigDecimal taxAmount;
    @Field("discount_amount") private BigDecimal discountAmount;
    @Field("shipping_amount") private BigDecimal shippingAmount;
    @Field("total_amount") private BigDecimal totalAmount;
    @Field("amount_paid") private BigDecimal amountPaid;
    @Field("balance_due") private BigDecimal balanceDue;
    @Field("line_items") private List<Invoice.InvoiceLineItem> lineItems;
    @Field("billing_address_line1") private String billingAddressLine1;
    @Field("billing_address_line2") private String billingAddressLine2;
    @Field("billing_city") private String billingCity;
    @Field("billing_state") private String billingState;
    @Field("billing_postal_code") private String billingPostalCode;
    @Field("billing_country") private String billingCountry;
    @Field("shipping_address_line1") private String shippingAddressLine1;
    @Field("shipping_address_line2") private String shippingAddressLine2;
    @Field("shipping_city") private String shippingCity;
    @Field("shipping_state") private String shippingState;
    @Field("shipping_postal_code") private String shippingPostalCode;
    @Field("shipping_country") private String shippingCountry;
    @Field("payment_terms") private String paymentTerms;
    @Field("notes") private String notes;
    @Field("internal_notes") private String internalNotes;
    @Field("salesperson") private String salesperson;
    @Field("project_id") private String projectId;
    @Field("department_id") private String departmentId;
    @Field("location_id") private String locationId;
    @Field("template_id") private String templateId;
    @Field("tax_inclusive") private Boolean taxInclusive;
    @Field("tax_registered") private Boolean taxRegistered;
    @Field("tax_code") private String taxCode;
    @Field("tax_rate") private BigDecimal taxRate;
    @Field("discount_code") private String discountCode;
    @Field("discount_rate") private BigDecimal discountRate;
    @Field("shipping_method") private String shippingMethod;
    @Field("tracking_number") private String trackingNumber;
    @Field("recurring_invoice_id") private String recurringInvoiceId;
    @Field("is_recurring") private Boolean isRecurring;
    @Field("parent_id") private String parentId;
    @Field("is_credit_note") private Boolean isCreditNote;
    @Field("original_invoice_id") private String originalInvoiceId;
    @Field("last_reminder_sent") private LocalDate lastReminderSent;
    @Field("reminder_count") private Integer reminderCount;
    @Field("domain_events") private List<InvoiceGeneratedEvent> domainEvents;
    @Field("payments") private List<Payment> payments;
    @Field("sent_at") private Instant sentAt;
    @Field("viewed_at") private Instant viewedAt;
    @Field("approved_at") private Instant approvedAt;
    @Field("approved_by") private String approvedBy;
    @Field("rejection_reason") private String rejectionReason;
    @Field("currency_exchange_rate") private String currencyExchangeRate;
    @Field("base_currency") private String baseCurrency;
    @Field("base_currency_amount") private BigDecimal baseCurrencyAmount;
    @Field("tags") private List<String> tags;
    @Field("customer_reference") private String customerReference;
    @Field("group_id") private String groupId;
    @Field("apply_finance_charge") private Boolean applyFinanceCharge;
    @Field("finance_charge_rate") private BigDecimal financeChargeRate;
    @Field("finance_charge_applied_date") private LocalDate financeChargeAppliedDate;
    @Field("days_overdue") private Integer daysOverdue;
    @Field("write_off_amount") private BigDecimal writeOffAmount;
    @Field("write_off_date") private LocalDate writeOffDate;
    @Field("write_off_reason") private String writeOffReason;

    public InvoiceEntity() {}

    public InvoiceEntity(Invoice invoice) {
        this.invoiceId = invoice.getInvoiceId();
        this.tenantId = invoice.getTenantId();
        this.invoiceNumber = invoice.getInvoiceNumber();
        this.customerId = invoice.getCustomerId();
        this.customerName = invoice.getCustomerName();
        this.customerEmail = invoice.getCustomerEmail();
        this.invoiceType = invoice.getInvoiceType() != null ? invoice.getInvoiceType().name() : null;
        this.status = invoice.getStatus() != null ? invoice.getStatus().name() : null;
        this.invoiceDate = invoice.getInvoiceDate();
        this.dueDate = invoice.getDueDate();
        this.salesDate = invoice.getSalesDate();
        this.purchaseOrderNumber = invoice.getPurchaseOrderNumber();
        this.currency = invoice.getCurrency();
        this.subtotal = invoice.getSubtotal();
        this.taxAmount = invoice.getTaxAmount();
        this.discountAmount = invoice.getDiscountAmount();
        this.shippingAmount = invoice.getShippingAmount();
        this.totalAmount = invoice.getTotalAmount();
        this.amountPaid = invoice.getAmountPaid();
        this.balanceDue = invoice.getBalanceDue();
        this.lineItems = invoice.getLineItems() != null ? new ArrayList<>(invoice.getLineItems()) : new ArrayList<>();
        this.billingAddressLine1 = invoice.getBillingAddressLine1();
        this.billingAddressLine2 = invoice.getBillingAddressLine2();
        this.billingCity = invoice.getBillingCity();
        this.billingState = invoice.getBillingState();
        this.billingPostalCode = invoice.getBillingPostalCode();
        this.billingCountry = invoice.getBillingCountry();
        this.shippingAddressLine1 = invoice.getShippingAddressLine1();
        this.shippingAddressLine2 = invoice.getShippingAddressLine2();
        this.shippingCity = invoice.getShippingCity();
        this.shippingState = invoice.getShippingState();
        this.shippingPostalCode = invoice.getShippingPostalCode();
        this.shippingCountry = invoice.getShippingCountry();
        this.paymentTerms = invoice.getPaymentTerms();
        this.notes = invoice.getNotes();
        this.internalNotes = invoice.getInternalNotes();
        this.salesperson = invoice.getSalesperson();
        this.projectId = invoice.getProjectId();
        this.departmentId = invoice.getDepartmentId();
        this.locationId = invoice.getLocationId();
        this.templateId = invoice.getTemplateId();
        this.taxInclusive = invoice.getTaxInclusive();
        this.taxRegistered = invoice.getTaxRegistered();
        this.taxCode = invoice.getTaxCode();
        this.taxRate = invoice.getTaxRate();
        this.discountCode = invoice.getDiscountCode();
        this.discountRate = invoice.getDiscountRate();
        this.shippingMethod = invoice.getShippingMethod();
        this.trackingNumber = invoice.getTrackingNumber();
        this.recurringInvoiceId = invoice.getRecurringInvoiceId();
        this.isRecurring = invoice.getIsRecurring();
        this.parentId = invoice.getParentId();
        this.isCreditNote = invoice.getIsCreditNote();
        this.originalInvoiceId = invoice.getOriginalInvoiceId();
        this.lastReminderSent = invoice.getLastReminderSent();
        this.reminderCount = invoice.getReminderCount();
        this.domainEvents = invoice.getDomainEvents() != null ? new ArrayList<>(invoice.getDomainEvents()) : new ArrayList<>();
        this.payments = invoice.getPayments() != null ? new ArrayList<>(invoice.getPayments()) : new ArrayList<>();
        this.sentAt = invoice.getSentAt();
        this.viewedAt = invoice.getViewedAt();
        this.approvedAt = invoice.getApprovedAt();
        this.approvedBy = invoice.getApprovedBy();
        this.rejectionReason = invoice.getRejectionReason();
        this.currencyExchangeRate = invoice.getCurrencyExchangeRate();
        this.baseCurrency = invoice.getBaseCurrency();
        this.baseCurrencyAmount = invoice.getBaseCurrencyAmount();
        this.tags = invoice.getTags() != null ? new ArrayList<>(invoice.getTags()) : new ArrayList<>();
        this.customerReference = invoice.getCustomerReference();
        this.groupId = invoice.getGroupId();
        this.applyFinanceCharge = invoice.getApplyFinanceCharge();
        this.financeChargeRate = invoice.getFinanceChargeRate();
        this.financeChargeAppliedDate = invoice.getFinanceChargeAppliedDate();
        this.daysOverdue = invoice.getDaysOverdue();
        this.writeOffAmount = invoice.getWriteOffAmount();
        this.writeOffDate = invoice.getWriteOffDate();
        this.writeOffReason = invoice.getWriteOffReason();
    }

    public Invoice toDomainModel() {
        return Invoice.builder()
                .invoiceId(this.invoiceId)
                .tenantId(this.tenantId)
                .invoiceNumber(this.invoiceNumber)
                .customerId(this.customerId)
                .customerName(this.customerName)
                .customerEmail(this.customerEmail)
                .invoiceType(this.invoiceType != null ? Invoice.InvoiceType.valueOf(this.invoiceType) : null)
                .status(this.status != null ? Invoice.InvoiceStatus.valueOf(this.status) : null)
                .invoiceDate(this.invoiceDate)
                .dueDate(this.dueDate)
                .salesDate(this.salesDate)
                .purchaseOrderNumber(this.purchaseOrderNumber)
                .currency(this.currency)
                .subtotal(this.subtotal)
                .taxAmount(this.taxAmount)
                .discountAmount(this.discountAmount)
                .shippingAmount(this.shippingAmount)
                .totalAmount(this.totalAmount)
                .amountPaid(this.amountPaid)
                .balanceDue(this.balanceDue)
                .lineItems(this.lineItems != null ? new ArrayList<>(this.lineItems) : new ArrayList<>())
                .billingAddressLine1(this.billingAddressLine1)
                .billingAddressLine2(this.billingAddressLine2)
                .billingCity(this.billingCity)
                .billingState(this.billingState)
                .billingPostalCode(this.billingPostalCode)
                .billingCountry(this.billingCountry)
                .shippingAddressLine1(this.shippingAddressLine1)
                .shippingAddressLine2(this.shippingAddressLine2)
                .shippingCity(this.shippingCity)
                .shippingState(this.shippingState)
                .shippingPostalCode(this.shippingPostalCode)
                .shippingCountry(this.shippingCountry)
                .paymentTerms(this.paymentTerms)
                .notes(this.notes)
                .internalNotes(this.internalNotes)
                .salesperson(this.salesperson)
                .projectId(this.projectId)
                .departmentId(this.departmentId)
                .locationId(this.locationId)
                .templateId(this.templateId)
                .taxInclusive(this.taxInclusive)
                .taxRegistered(this.taxRegistered)
                .taxCode(this.taxCode)
                .taxRate(this.taxRate)
                .discountCode(this.discountCode)
                .discountRate(this.discountRate)
                .shippingMethod(this.shippingMethod)
                .trackingNumber(this.trackingNumber)
                .recurringInvoiceId(this.recurringInvoiceId)
                .isRecurring(this.isRecurring)
                .parentId(this.parentId)
                .isCreditNote(this.isCreditNote)
                .originalInvoiceId(this.originalInvoiceId)
                .lastReminderSent(this.lastReminderSent)
                .reminderCount(this.reminderCount)
                .domainEvents(this.domainEvents != null ? new ArrayList<>(this.domainEvents) : new ArrayList<>())
                .payments(this.payments != null ? new ArrayList<>(this.payments) : new ArrayList<>())
                .sentAt(this.sentAt)
                .viewedAt(this.viewedAt)
                .approvedAt(this.approvedAt)
                .approvedBy(this.approvedBy)
                .rejectionReason(this.rejectionReason)
                .currencyExchangeRate(this.currencyExchangeRate)
                .baseCurrency(this.baseCurrency)
                .baseCurrencyAmount(this.baseCurrencyAmount)
                .tags(this.tags != null ? new ArrayList<>(this.tags) : new ArrayList<>())
                .customerReference(this.customerReference)
                .groupId(this.groupId)
                .applyFinanceCharge(this.applyFinanceCharge)
                .financeChargeRate(this.financeChargeRate)
                .financeChargeAppliedDate(this.financeChargeAppliedDate)
                .daysOverdue(this.daysOverdue)
                .writeOffAmount(this.writeOffAmount)
                .writeOffDate(this.writeOffDate)
                .writeOffReason(this.writeOffReason)
                .build();
    }

    // Getters and setters
    public String getId() { return id; } public void setId(String id) { this.id = id; }
    public String getInvoiceId() { return invoiceId; } public void setInvoiceId(String invoiceId) { this.invoiceId = invoiceId; }
    public String getTenantId() { return tenantId; } public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getInvoiceNumber() { return invoiceNumber; } public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public String getCustomerId() { return customerId; } public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; } public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerEmail() { return customerEmail; } public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public String getInvoiceType() { return invoiceType; } public void setInvoiceType(String invoiceType) { this.invoiceType = invoiceType; }
    public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
    public LocalDate getInvoiceDate() { return invoiceDate; } public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }
    public LocalDate getDueDate() { return dueDate; } public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public String getCurrency() { return currency; } public void setCurrency(String currency) { this.currency = currency; }
    public BigDecimal getTotalAmount() { return totalAmount; } public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public BigDecimal getBalanceDue() { return balanceDue; } public void setBalanceDue(BigDecimal balanceDue) { this.balanceDue = balanceDue; }
    public List<Invoice.InvoiceLineItem> getLineItems() { return lineItems; } public void setLineItems(List<Invoice.InvoiceLineItem> lineItems) { this.lineItems = lineItems; }
    public List<InvoiceGeneratedEvent> getDomainEvents() { return domainEvents; } public void setDomainEvents(List<InvoiceGeneratedEvent> domainEvents) { this.domainEvents = domainEvents; }
    public List<String> getTags() { return tags; } public void setTags(List<String> tags) { this.tags = tags; }
}
