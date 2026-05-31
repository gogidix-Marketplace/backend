package com.gogidix.finance.cashflow.domain.model;

import com.gogidix.finance.cashflow.domain.event.CashflowItemRecordedEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Cashflow Item Domain Entity
 * Represents individual cashflow items (inflow/outflow)
 * Multi-tenant cashflow management
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Document(collection = "cashflow_items")
public class CashflowItem {

    @Id
    private String id;

    @Indexed
    private String cashflowItemId;

    @Indexed
    private String tenantId;

    private String recordedBy;

    private String reference;

    private CashflowType type;

    private CashflowCategory category;

    private BigDecimal amount;

    private String currency;

    private LocalDate transactionDate;

    private LocalDate expectedDate;

    private LocalDate settledDate;

    private String description;

    private String counterparty;

    private String account;

    private String costCenter;

    private String projectId;

    private ItemStatus status;

    private Boolean recurring;

    private RecurringFrequency recurringFrequency;

    private String parentRecurringItemId;

    private Instant expectedAt;

    private Instant settledAt;

    private String paymentMethod;

    private String bankReference;

    private String invoiceReference;

    private BigDecimal taxAmount;

    private BigDecimal netAmount;

    private List<String> tags;

    private String notes;

    private String linkedExpenseId;

    private String linkedRevenueId;

    private BigDecimal allocationPercentage;

    private Instant createdAt;

    private Instant updatedAt;

    @Builder.Default
    private List<CashflowItemRecordedEvent> domainEvents = new ArrayList<>();

    public enum CashflowType {
        INFLOW,
        OUTFLOW
    }

    public enum CashflowCategory {
        // Inflow Categories
        OPERATING_REVENUE,
        INVESTMENT_RETURN,
        LOAN_PROCEEDS,
        CAPITAL_CONTRIBUTION,
        REFUND_RECEIVED,
        INTEREST_INCOME,
        DIVIDEND_INCOME,
        OTHER_INFLOW,

        // Outflow Categories
        OPERATING_EXPENSE,
        PAYROLL,
        TAX_PAYMENT,
        LOAN_REPAYMENT,
        CAPITAL_EXPENDITURE,
        DIVIDEND_PAYMENT,
        INTEREST_PAYMENT,
        REFUND_ISSUED,
        SUPPLIER_PAYMENT,
        RENT_PAYMENT,
        UTILITY_PAYMENT,
        INSURANCE_PAYMENT,
        MARKETING_EXPENSE,
        OTHER_OUTFLOW
    }

    public enum ItemStatus {
        PENDING,
        EXPECTED,
        COMMITTED,
        SETTLED,
        CANCELLED,
        FAILED
    }

    public enum RecurringFrequency {
        DAILY,
        WEEKLY,
        BI_WEEKLY,
        MONTHLY,
        QUARTERLY,
        SEMI_ANNUALLY,
        ANNUALLY
    }

    /**
     * Creates a new cashflow item
     */
    public static CashflowItem create(String tenantId, String recordedBy,
                                       CashflowType type, CashflowCategory category,
                                       BigDecimal amount, String currency,
                                       LocalDate transactionDate, String description) {
        CashflowItem item = CashflowItem.builder()
                .cashflowItemId(java.util.UUID.randomUUID().toString())
                .tenantId(tenantId)
                .recordedBy(recordedBy)
                .type(type)
                .category(category)
                .amount(amount)
                .currency(currency)
                .transactionDate(transactionDate)
                .expectedDate(transactionDate)
                .description(description)
                .status(ItemStatus.PENDING)
                .recurring(false)
                .tags(new ArrayList<>())
                .build();

        item.addDomainEvent(CashflowItemRecordedEvent.builder()
                .cashflowItemId(item.getCashflowItemId())
                .tenantId(tenantId)
                .recordedBy(recordedBy)
                .type(type.name())
                .category(category.name())
                .amount(amount)
                .currency(currency)
                .timestamp(Instant.now())
                .eventType("CASHFLOW_ITEM_CREATED")
                .build());

        return item;
    }

    /**
     * Marks the item as expected
     */
    public void markAsExpected() {
        if (this.status != ItemStatus.PENDING) {
            throw new IllegalStateException("Can only mark pending items as expected");
        }

        this.status = ItemStatus.EXPECTED;
        this.expectedAt = Instant.now();

        addDomainEvent(CashflowItemRecordedEvent.builder()
                .cashflowItemId(this.cashflowItemId)
                .tenantId(this.tenantId)
                .recordedBy(this.recordedBy)
                .type(this.type.name())
                .category(this.category.name())
                .amount(this.amount)
                .currency(this.currency)
                .timestamp(Instant.now())
                .eventType("CASHFLOW_ITEM_EXPECTED")
                .build());
    }

    /**
     * Commits the cashflow item
     */
    public void commit() {
        if (this.status != ItemStatus.EXPECTED) {
            throw new IllegalStateException("Can only commit expected items");
        }

        this.status = ItemStatus.COMMITTED;

        addDomainEvent(CashflowItemRecordedEvent.builder()
                .cashflowItemId(this.cashflowItemId)
                .tenantId(this.tenantId)
                .recordedBy(this.recordedBy)
                .type(this.type.name())
                .category(this.category.name())
                .amount(this.amount)
                .currency(this.currency)
                .timestamp(Instant.now())
                .eventType("CASHFLOW_ITEM_COMMITTED")
                .build());
    }

    /**
     * Settles the cashflow item
     */
    public void settle(String bankReference) {
        if (this.status != ItemStatus.COMMITTED && this.status != ItemStatus.EXPECTED) {
            throw new IllegalStateException("Can only settle committed or expected items");
        }

        this.status = ItemStatus.SETTLED;
        this.settledAt = Instant.now();
        this.settledDate = LocalDate.now();
        this.bankReference = bankReference;

        addDomainEvent(CashflowItemRecordedEvent.builder()
                .cashflowItemId(this.cashflowItemId)
                .tenantId(this.tenantId)
                .recordedBy(this.recordedBy)
                .type(this.type.name())
                .category(this.category.name())
                .amount(this.amount)
                .currency(this.currency)
                .timestamp(Instant.now())
                .eventType("CASHFLOW_ITEM_SETTLED")
                .build());
    }

    /**
     * Cancels the cashflow item
     */
    public void cancel(String reason) {
        if (this.status == ItemStatus.SETTLED || this.status == ItemStatus.FAILED) {
            throw new IllegalStateException("Cannot cancel settled or failed items");
        }

        this.status = ItemStatus.CANCELLED;
        this.notes = reason != null ? reason : this.notes;

        addDomainEvent(CashflowItemRecordedEvent.builder()
                .cashflowItemId(this.cashflowItemId)
                .tenantId(this.tenantId)
                .recordedBy(this.recordedBy)
                .type(this.type.name())
                .category(this.category.name())
                .amount(this.amount)
                .currency(this.currency)
                .timestamp(Instant.now())
                .eventType("CASHFLOW_ITEM_CANCELLED")
                .build());
    }

    /**
     * Marks the item as failed
     */
    public void markAsFailed(String reason) {
        if (this.status == ItemStatus.SETTLED) {
            throw new IllegalStateException("Cannot mark settled items as failed");
        }

        this.status = ItemStatus.FAILED;
        this.notes = reason != null ? reason : this.notes;

        addDomainEvent(CashflowItemRecordedEvent.builder()
                .cashflowItemId(this.cashflowItemId)
                .tenantId(this.tenantId)
                .recordedBy(this.recordedBy)
                .type(this.type.name())
                .category(this.category.name())
                .amount(this.amount)
                .currency(this.currency)
                .timestamp(Instant.now())
                .eventType("CASHFLOW_ITEM_FAILED")
                .build());
    }

    /**
     * Sets up recurring configuration
     */
    public void setupRecurring(RecurringFrequency frequency) {
        this.recurring = true;
        this.recurringFrequency = frequency;
    }

    /**
     * Adds a tag to the item
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
     * Removes a tag from the item
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }

    /**
     * Calculates net amount after tax
     */
    public void calculateNetAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
        if (this.type == CashflowType.INFLOW) {
            this.netAmount = this.amount.subtract(taxAmount);
        } else {
            this.netAmount = this.amount.add(taxAmount);
        }
    }

    /**
     * Checks if this is an inflow
     */
    public boolean isInflow() {
        return this.type == CashflowType.INFLOW;
    }

    /**
     * Checks if this is an outflow
     */
    public boolean isOutflow() {
        return this.type == CashflowType.OUTFLOW;
    }

    public void addDomainEvent(CashflowItemRecordedEvent event) {
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
