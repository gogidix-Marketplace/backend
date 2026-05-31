package com.gogidix.finance.budgettracking.infrastructure.persistence.mongodb;

import com.gogidix.finance.budgettracking.domain.model.BudgetTransaction;
import com.gogidix.finance.budgettracking.domain.event.BudgetTransactionRecordedEvent;
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
 * MongoDB document entity for storing BudgetTransaction domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "budget_transactions")
public class BudgetTransactionEntity {

    @Id
    private String id;

    @Indexed
    @Field("transaction_id")
    private String transactionId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("budget_id")
    private String budgetId;

    @Field("budget_code")
    private String budgetCode;

    @Field("reference_type")
    private String referenceType;

    @Field("reference_id")
    private String referenceId;

    @Field("transaction_type")
    private String transactionType;

    @Field("amount")
    private BigDecimal amount;

    @Field("currency")
    private String currency;

    @Field("description")
    private String description;

    @Field("status")
    private String status;

    @Field("transaction_date")
    private LocalDate transactionDate;

    @Field("category")
    private String category;

    @Field("department")
    private String department;

    @Field("cost_center")
    private String costCenter;

    @Field("project_id")
    private String projectId;

    @Field("recorded_by")
    private String recordedBy;

    @Field("approved_by")
    private String approvedBy;

    @Field("approved_at")
    private Instant approvedAt;

    @Field("rejection_reason")
    private String rejectionReason;

    @Field("related_budget_period")
    private String relatedBudgetPeriod;

    @Field("balance_before")
    private BigDecimal balanceBefore;

    @Field("balance_after")
    private BigDecimal balanceAfter;

    @Field("tags")
    private List<String> tags;

    @Field("notes")
    private String notes;

    @Field("correlation_id")
    private String correlationId;

    @Field("domain_events")
    private List<BudgetTransactionRecordedEvent> domainEvents;

    // Default constructor for MongoDB
    public BudgetTransactionEntity() {
    }

    // Constructor from domain model
    public BudgetTransactionEntity(BudgetTransaction transaction) {
        this.transactionId = transaction.getTransactionId();
        this.tenantId = transaction.getTenantId();
        this.budgetId = transaction.getBudgetId();
        this.budgetCode = transaction.getBudgetCode();
        this.referenceType = transaction.getReferenceType();
        this.referenceId = transaction.getReferenceId();
        this.transactionType = transaction.getTransactionType() != null ? transaction.getTransactionType().name() : null;
        this.amount = transaction.getAmount();
        this.currency = transaction.getCurrency();
        this.description = transaction.getDescription();
        this.status = transaction.getStatus() != null ? transaction.getStatus().name() : null;
        this.transactionDate = transaction.getTransactionDate();
        this.category = transaction.getCategory();
        this.department = transaction.getDepartment();
        this.costCenter = transaction.getCostCenter();
        this.projectId = transaction.getProjectId();
        this.recordedBy = transaction.getRecordedBy();
        this.approvedBy = transaction.getApprovedBy();
        this.approvedAt = transaction.getApprovedAt();
        this.rejectionReason = transaction.getRejectionReason();
        this.relatedBudgetPeriod = transaction.getRelatedBudgetPeriod();
        this.balanceBefore = transaction.getBalanceBefore();
        this.balanceAfter = transaction.getBalanceAfter();
        this.tags = transaction.getTags() != null ? new ArrayList<>(transaction.getTags()) : new ArrayList<>();
        this.notes = transaction.getNotes();
        this.correlationId = transaction.getCorrelationId();
        this.domainEvents = transaction.getDomainEvents() != null ? new ArrayList<>(transaction.getDomainEvents()) : new ArrayList<>();
    }

    // Convert to domain model
    public BudgetTransaction toDomainModel() {
        return BudgetTransaction.builder()
            .transactionId(this.transactionId)
            .tenantId(this.tenantId)
            .budgetId(this.budgetId)
            .budgetCode(this.budgetCode)
            .referenceType(this.referenceType)
            .referenceId(this.referenceId)
            .transactionType(this.transactionType != null ? BudgetTransaction.TransactionType.valueOf(this.transactionType) : null)
            .amount(this.amount)
            .currency(this.currency)
            .description(this.description)
            .status(this.status != null ? BudgetTransaction.TransactionStatus.valueOf(this.status) : null)
            .transactionDate(this.transactionDate)
            .category(this.category)
            .department(this.department)
            .costCenter(this.costCenter)
            .projectId(this.projectId)
            .recordedBy(this.recordedBy)
            .approvedBy(this.approvedBy)
            .approvedAt(this.approvedAt)
            .rejectionReason(this.rejectionReason)
            .relatedBudgetPeriod(this.relatedBudgetPeriod)
            .balanceBefore(this.balanceBefore)
            .balanceAfter(this.balanceAfter)
            .tags(this.tags != null ? new ArrayList<>(this.tags) : new ArrayList<>())
            .notes(this.notes)
            .correlationId(this.correlationId)
            .domainEvents(this.domainEvents != null ? new ArrayList<>(this.domainEvents) : new ArrayList<>())
            .build();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public String getBudgetCode() {
        return budgetCode;
    }

    public void setBudgetCode(String budgetCode) {
        this.budgetCode = budgetCode;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
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

    public String getRelatedBudgetPeriod() {
        return relatedBudgetPeriod;
    }

    public void setRelatedBudgetPeriod(String relatedBudgetPeriod) {
        this.relatedBudgetPeriod = relatedBudgetPeriod;
    }

    public BigDecimal getBalanceBefore() {
        return balanceBefore;
    }

    public void setBalanceBefore(BigDecimal balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
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

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public List<BudgetTransactionRecordedEvent> getDomainEvents() {
        return domainEvents;
    }

    public void setDomainEvents(List<BudgetTransactionRecordedEvent> domainEvents) {
        this.domainEvents = domainEvents;
    }
}
