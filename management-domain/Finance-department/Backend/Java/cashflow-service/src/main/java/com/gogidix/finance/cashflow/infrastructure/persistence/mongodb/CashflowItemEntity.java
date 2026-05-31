package com.gogidix.finance.cashflow.infrastructure.persistence.mongodb;

import com.gogidix.finance.cashflow.domain.model.CashflowItem;
import com.gogidix.finance.cashflow.domain.event.CashflowItemRecordedEvent;
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
 * MongoDB document entity for storing CashflowItem domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "cashflow_items")
public class CashflowItemEntity {

    @Id
    private String id;

    @Indexed
    @Field("cashflow_item_id")
    private String cashflowItemId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("recorded_by")
    private String recordedBy;

    @Field("reference")
    private String reference;

    @Field("type")
    private String type;

    @Field("category")
    private String category;

    @Field("amount")
    private BigDecimal amount;

    @Field("currency")
    private String currency;

    @Field("transaction_date")
    private LocalDate transactionDate;

    @Field("expected_date")
    private LocalDate expectedDate;

    @Field("settled_date")
    private LocalDate settledDate;

    @Field("description")
    private String description;

    @Field("counterparty")
    private String counterparty;

    @Field("account")
    private String account;

    @Field("cost_center")
    private String costCenter;

    @Field("project_id")
    private String projectId;

    @Field("status")
    private String status;

    @Field("recurring")
    private Boolean recurring;

    @Field("recurring_frequency")
    private String recurringFrequency;

    @Field("parent_recurring_item_id")
    private String parentRecurringItemId;

    @Field("expected_at")
    private Instant expectedAt;

    @Field("settled_at")
    private Instant settledAt;

    @Field("payment_method")
    private String paymentMethod;

    @Field("bank_reference")
    private String bankReference;

    @Field("invoice_reference")
    private String invoiceReference;

    @Field("tax_amount")
    private BigDecimal taxAmount;

    @Field("net_amount")
    private BigDecimal netAmount;

    @Field("tags")
    private List<String> tags;

    @Field("notes")
    private String notes;

    @Field("linked_expense_id")
    private String linkedExpenseId;

    @Field("linked_revenue_id")
    private String linkedRevenueId;

    @Field("allocation_percentage")
    private BigDecimal allocationPercentage;

    @Field("domain_events")
    private List<CashflowItemRecordedEvent> domainEvents;

    // Default constructor for MongoDB
    public CashflowItemEntity() {
    }

    // Constructor from domain model
    public CashflowItemEntity(CashflowItem item) {
        this.cashflowItemId = item.getCashflowItemId();
        this.tenantId = item.getTenantId();
        this.recordedBy = item.getRecordedBy();
        this.reference = item.getReference();
        this.type = item.getType() != null ? item.getType().name() : null;
        this.category = item.getCategory() != null ? item.getCategory().name() : null;
        this.amount = item.getAmount();
        this.currency = item.getCurrency();
        this.transactionDate = item.getTransactionDate();
        this.expectedDate = item.getExpectedDate();
        this.settledDate = item.getSettledDate();
        this.description = item.getDescription();
        this.counterparty = item.getCounterparty();
        this.account = item.getAccount();
        this.costCenter = item.getCostCenter();
        this.projectId = item.getProjectId();
        this.status = item.getStatus() != null ? item.getStatus().name() : null;
        this.recurring = item.getRecurring();
        this.recurringFrequency = item.getRecurringFrequency() != null ? item.getRecurringFrequency().name() : null;
        this.parentRecurringItemId = item.getParentRecurringItemId();
        this.expectedAt = item.getExpectedAt();
        this.settledAt = item.getSettledAt();
        this.paymentMethod = item.getPaymentMethod();
        this.bankReference = item.getBankReference();
        this.invoiceReference = item.getInvoiceReference();
        this.taxAmount = item.getTaxAmount();
        this.netAmount = item.getNetAmount();
        this.tags = item.getTags() != null ? new ArrayList<>(item.getTags()) : new ArrayList<>();
        this.notes = item.getNotes();
        this.linkedExpenseId = item.getLinkedExpenseId();
        this.linkedRevenueId = item.getLinkedRevenueId();
        this.allocationPercentage = item.getAllocationPercentage();
        this.domainEvents = item.getDomainEvents() != null ? new ArrayList<>(item.getDomainEvents()) : new ArrayList<>();
    }

    // Convert to domain model
    public CashflowItem toDomainModel() {
        return CashflowItem.builder()
                .cashflowItemId(this.cashflowItemId)
                .tenantId(this.tenantId)
                .recordedBy(this.recordedBy)
                .reference(this.reference)
                .type(this.type != null ? CashflowItem.CashflowType.valueOf(this.type) : null)
                .category(this.category != null ? CashflowItem.CashflowCategory.valueOf(this.category) : null)
                .amount(this.amount)
                .currency(this.currency)
                .transactionDate(this.transactionDate)
                .expectedDate(this.expectedDate)
                .settledDate(this.settledDate)
                .description(this.description)
                .counterparty(this.counterparty)
                .account(this.account)
                .costCenter(this.costCenter)
                .projectId(this.projectId)
                .status(this.status != null ? CashflowItem.ItemStatus.valueOf(this.status) : null)
                .recurring(this.recurring)
                .recurringFrequency(this.recurringFrequency != null ? CashflowItem.RecurringFrequency.valueOf(this.recurringFrequency) : null)
                .parentRecurringItemId(this.parentRecurringItemId)
                .expectedAt(this.expectedAt)
                .settledAt(this.settledAt)
                .paymentMethod(this.paymentMethod)
                .bankReference(this.bankReference)
                .invoiceReference(this.invoiceReference)
                .taxAmount(this.taxAmount)
                .netAmount(this.netAmount)
                .tags(this.tags != null ? new ArrayList<>(this.tags) : new ArrayList<>())
                .notes(this.notes)
                .linkedExpenseId(this.linkedExpenseId)
                .linkedRevenueId(this.linkedRevenueId)
                .allocationPercentage(this.allocationPercentage)
                .domainEvents(this.domainEvents != null ? new ArrayList<>(this.domainEvents) : new ArrayList<>())
                .build();
    }

    // Update from domain model (for partial updates)
    public void updateFrom(CashflowItem item) {
        this.recordedBy = item.getRecordedBy();
        this.reference = item.getReference();
        this.type = item.getType() != null ? item.getType().name() : null;
        this.category = item.getCategory() != null ? item.getCategory().name() : null;
        this.amount = item.getAmount();
        this.currency = item.getCurrency();
        this.transactionDate = item.getTransactionDate();
        this.expectedDate = item.getExpectedDate();
        this.settledDate = item.getSettledDate();
        this.description = item.getDescription();
        this.counterparty = item.getCounterparty();
        this.account = item.getAccount();
        this.costCenter = item.getCostCenter();
        this.projectId = item.getProjectId();
        this.status = item.getStatus() != null ? item.getStatus().name() : null;
        this.recurring = item.getRecurring();
        this.recurringFrequency = item.getRecurringFrequency() != null ? item.getRecurringFrequency().name() : null;
        this.parentRecurringItemId = item.getParentRecurringItemId();
        this.expectedAt = item.getExpectedAt();
        this.settledAt = item.getSettledAt();
        this.paymentMethod = item.getPaymentMethod();
        this.bankReference = item.getBankReference();
        this.invoiceReference = item.getInvoiceReference();
        this.taxAmount = item.getTaxAmount();
        this.netAmount = item.getNetAmount();
        this.tags = item.getTags() != null ? new ArrayList<>(item.getTags()) : new ArrayList<>();
        this.notes = item.getNotes();
        this.linkedExpenseId = item.getLinkedExpenseId();
        this.linkedRevenueId = item.getLinkedRevenueId();
        this.allocationPercentage = item.getAllocationPercentage();
        this.domainEvents = item.getDomainEvents() != null ? new ArrayList<>(item.getDomainEvents()) : new ArrayList<>();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCashflowItemId() {
        return cashflowItemId;
    }

    public void setCashflowItemId(String cashflowItemId) {
        this.cashflowItemId = cashflowItemId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public LocalDate getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(LocalDate expectedDate) {
        this.expectedDate = expectedDate;
    }

    public LocalDate getSettledDate() {
        return settledDate;
    }

    public void setSettledDate(LocalDate settledDate) {
        this.settledDate = settledDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getRecurring() {
        return recurring;
    }

    public void setRecurring(Boolean recurring) {
        this.recurring = recurring;
    }

    public String getRecurringFrequency() {
        return recurringFrequency;
    }

    public void setRecurringFrequency(String recurringFrequency) {
        this.recurringFrequency = recurringFrequency;
    }

    public String getParentRecurringItemId() {
        return parentRecurringItemId;
    }

    public void setParentRecurringItemId(String parentRecurringItemId) {
        this.parentRecurringItemId = parentRecurringItemId;
    }

    public Instant getExpectedAt() {
        return expectedAt;
    }

    public void setExpectedAt(Instant expectedAt) {
        this.expectedAt = expectedAt;
    }

    public Instant getSettledAt() {
        return settledAt;
    }

    public void setSettledAt(Instant settledAt) {
        this.settledAt = settledAt;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getBankReference() {
        return bankReference;
    }

    public void setBankReference(String bankReference) {
        this.bankReference = bankReference;
    }

    public String getInvoiceReference() {
        return invoiceReference;
    }

    public void setInvoiceReference(String invoiceReference) {
        this.invoiceReference = invoiceReference;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLinkedExpenseId() {
        return linkedExpenseId;
    }

    public void setLinkedExpenseId(String linkedExpenseId) {
        this.linkedExpenseId = linkedExpenseId;
    }

    public String getLinkedRevenueId() {
        return linkedRevenueId;
    }

    public void setLinkedRevenueId(String linkedRevenueId) {
        this.linkedRevenueId = linkedRevenueId;
    }

    public BigDecimal getAllocationPercentage() {
        return allocationPercentage;
    }

    public void setAllocationPercentage(BigDecimal allocationPercentage) {
        this.allocationPercentage = allocationPercentage;
    }

    public List<CashflowItemRecordedEvent> getDomainEvents() {
        return domainEvents;
    }

    public void setDomainEvents(List<CashflowItemRecordedEvent> domainEvents) {
        this.domainEvents = domainEvents;
    }
}
