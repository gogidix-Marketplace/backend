package com.gogidix.finance.budgetmanagement.infrastructure.persistence.mongodb;

import com.gogidix.finance.budgetmanagement.domain.model.Budget;
import com.gogidix.finance.budgetmanagement.domain.model.BudgetAllocation;
import com.gogidix.finance.budgetmanagement.domain.model.BudgetPeriod;
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
 * MongoDB document entity for storing Budget domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "budgets")
public class BudgetEntity {

    @Id
    private String id;

    @Indexed
    @Field("budget_id")
    private String budgetId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("period")
    private String period;

    @Field("start_date")
    private LocalDate startDate;

    @Field("end_date")
    private LocalDate endDate;

    @Indexed
    @Field("status")
    private String status;

    @Field("total_allocated")
    private BigDecimal totalAllocated;

    @Field("total_spent")
    private BigDecimal totalSpent;

    @Field("total_remaining")
    private BigDecimal totalRemaining;

    @Indexed
    @Field("fiscal_year")
    private String fiscalYear;

    @Indexed
    @Field("department")
    private String department;

    @Field("cost_center")
    private String costCenter;

    @Field("created_by")
    private String createdBy;

    @Field("approved_by")
    private String approvedBy;

    @Field("approved_at")
    private Instant approvedAt;

    @Field("submitted_by")
    private String submittedBy;

    @Field("submitted_at")
    private Instant submittedAt;

    @Field("closed_by")
    private String closedBy;

    @Field("closed_at")
    private Instant closedAt;

    @Field("notes")
    private String notes;

    @Field("allocations")
    private List<BudgetAllocationEmbed> allocations;

    @Field("domain_events")
    private List<Object> domainEvents;

    // Default constructor for MongoDB
    public BudgetEntity() {
    }

    // Constructor from domain model
    public BudgetEntity(Budget budget) {
        this.budgetId = budget.getBudgetId();
        this.tenantId = budget.getTenantId();
        this.name = budget.getName();
        this.description = budget.getDescription();
        this.period = budget.getPeriod() != null ? budget.getPeriod().name() : null;
        this.startDate = budget.getStartDate();
        this.endDate = budget.getEndDate();
        this.status = budget.getStatus() != null ? budget.getStatus().name() : null;
        this.totalAllocated = budget.getTotalAllocated();
        this.totalSpent = budget.getTotalSpent();
        this.totalRemaining = budget.getTotalRemaining();
        this.fiscalYear = budget.getFiscalYear();
        this.department = budget.getDepartment();
        this.costCenter = budget.getCostCenter();
        this.createdBy = budget.getCreatedBy();
        this.approvedBy = budget.getApprovedBy();
        this.approvedAt = budget.getApprovedAt();
        this.submittedBy = budget.getSubmittedBy();
        this.submittedAt = budget.getSubmittedAt();
        this.closedBy = budget.getClosedBy();
        this.closedAt = budget.getClosedAt();
        this.notes = budget.getNotes();

        // Convert allocations
        if (budget.getAllocations() != null) {
            this.allocations = new ArrayList<>();
            for (BudgetAllocation allocation : budget.getAllocations()) {
                this.allocations.add(new BudgetAllocationEmbed(allocation));
            }
        }

        this.domainEvents = budget.getDomainEvents() != null ? new ArrayList<>(budget.getDomainEvents()) : new ArrayList<>();
    }

    // Convert to domain model
    public Budget toDomainModel() {
        List<BudgetAllocation> allocationList = new ArrayList<>();
        if (this.allocations != null) {
            for (BudgetAllocationEmbed embed : this.allocations) {
                allocationList.add(embed.toDomainModel());
            }
        }

        return Budget.builder()
                .budgetId(this.budgetId)
                .tenantId(this.tenantId)
                .name(this.name)
                .description(this.description)
                .period(this.period != null ? BudgetPeriod.valueOf(this.period) : null)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .status(this.status != null ? Budget.BudgetStatus.valueOf(this.status) : null)
                .totalAllocated(this.totalAllocated)
                .totalSpent(this.totalSpent)
                .totalRemaining(this.totalRemaining)
                .fiscalYear(this.fiscalYear)
                .department(this.department)
                .costCenter(this.costCenter)
                .createdBy(this.createdBy)
                .approvedBy(this.approvedBy)
                .approvedAt(this.approvedAt)
                .submittedBy(this.submittedBy)
                .submittedAt(this.submittedAt)
                .closedBy(this.closedBy)
                .closedAt(this.closedAt)
                .notes(this.notes)
                .allocations(allocationList)
                .domainEvents(this.domainEvents != null ? new ArrayList<>(this.domainEvents) : new ArrayList<>())
                .build();
    }

    // Update from domain model (for partial updates)
    public void updateFrom(Budget budget) {
        this.name = budget.getName();
        this.description = budget.getDescription();
        this.period = budget.getPeriod() != null ? budget.getPeriod().name() : null;
        this.startDate = budget.getStartDate();
        this.endDate = budget.getEndDate();
        this.status = budget.getStatus() != null ? budget.getStatus().name() : null;
        this.totalAllocated = budget.getTotalAllocated();
        this.totalSpent = budget.getTotalSpent();
        this.totalRemaining = budget.getTotalRemaining();
        this.fiscalYear = budget.getFiscalYear();
        this.department = budget.getDepartment();
        this.costCenter = budget.getCostCenter();
        this.approvedBy = budget.getApprovedBy();
        this.approvedAt = budget.getApprovedAt();
        this.submittedBy = budget.getSubmittedBy();
        this.submittedAt = budget.getSubmittedAt();
        this.closedBy = budget.getClosedBy();
        this.closedAt = budget.getClosedAt();
        this.notes = budget.getNotes();

        // Update allocations
        if (budget.getAllocations() != null) {
            this.allocations = new ArrayList<>();
            for (BudgetAllocation allocation : budget.getAllocations()) {
                this.allocations.add(new BudgetAllocationEmbed(allocation));
            }
        }

        this.domainEvents = budget.getDomainEvents() != null ? new ArrayList<>(budget.getDomainEvents()) : new ArrayList<>();
    }

    // Embedded class for budget allocations
    public static class BudgetAllocationEmbed {
        private String allocationId;
        private String budgetId;
        private String tenantId;
        private String allocationType;
        private String category;
        private String department;
        private String costCenter;
        private String projectId;
        private BigDecimal allocatedAmount;
        private BigDecimal spentAmount;
        private BigDecimal remainingAmount;
        private BigDecimal percentageOfTotal;
        private LocalDate effectiveDate;
        private LocalDate expirationDate;
        private String status;
        private String notes;
        private String parentAllocationId;
        private Boolean rollOverEnabled;
        private BigDecimal rollOverAmount;

        public BudgetAllocationEmbed() {
        }

        public BudgetAllocationEmbed(BudgetAllocation allocation) {
            this.allocationId = allocation.getAllocationId();
            this.budgetId = allocation.getBudgetId();
            this.tenantId = allocation.getTenantId();
            this.allocationType = allocation.getAllocationType() != null ? allocation.getAllocationType().name() : null;
            this.category = allocation.getCategory();
            this.department = allocation.getDepartment();
            this.costCenter = allocation.getCostCenter();
            this.projectId = allocation.getProjectId();
            this.allocatedAmount = allocation.getAllocatedAmount();
            this.spentAmount = allocation.getSpentAmount();
            this.remainingAmount = allocation.getRemainingAmount();
            this.percentageOfTotal = allocation.getPercentageOfTotal();
            this.effectiveDate = allocation.getEffectiveDate();
            this.expirationDate = allocation.getExpirationDate();
            this.status = allocation.getStatus() != null ? allocation.getStatus().name() : null;
            this.notes = allocation.getNotes();
            this.parentAllocationId = allocation.getParentAllocationId();
            this.rollOverEnabled = allocation.getRollOverEnabled();
            this.rollOverAmount = allocation.getRollOverAmount();
        }

        public BudgetAllocation toDomainModel() {
            return BudgetAllocation.builder()
                    .allocationId(this.allocationId)
                    .budgetId(this.budgetId)
                    .tenantId(this.tenantId)
                    .allocationType(this.allocationType != null ? BudgetAllocation.AllocationType.valueOf(this.allocationType) : null)
                    .category(this.category)
                    .department(this.department)
                    .costCenter(this.costCenter)
                    .projectId(this.projectId)
                    .allocatedAmount(this.allocatedAmount)
                    .spentAmount(this.spentAmount)
                    .remainingAmount(this.remainingAmount)
                    .percentageOfTotal(this.percentageOfTotal)
                    .effectiveDate(this.effectiveDate)
                    .expirationDate(this.expirationDate)
                    .status(this.status != null ? BudgetAllocation.AllocationStatus.valueOf(this.status) : null)
                    .notes(this.notes)
                    .parentAllocationId(this.parentAllocationId)
                    .rollOverEnabled(this.rollOverEnabled)
                    .rollOverAmount(this.rollOverAmount)
                    .build();
        }

        // Getters and setters
        public String getAllocationId() {
            return allocationId;
        }

        public void setAllocationId(String allocationId) {
            this.allocationId = allocationId;
        }

        public String getBudgetId() {
            return budgetId;
        }

        public void setBudgetId(String budgetId) {
            this.budgetId = budgetId;
        }

        public String getTenantId() {
            return tenantId;
        }

        public void setTenantId(String tenantId) {
            this.tenantId = tenantId;
        }

        public String getAllocationType() {
            return allocationType;
        }

        public void setAllocationType(String allocationType) {
            this.allocationType = allocationType;
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

        public BigDecimal getAllocatedAmount() {
            return allocatedAmount;
        }

        public void setAllocatedAmount(BigDecimal allocatedAmount) {
            this.allocatedAmount = allocatedAmount;
        }

        public BigDecimal getSpentAmount() {
            return spentAmount;
        }

        public void setSpentAmount(BigDecimal spentAmount) {
            this.spentAmount = spentAmount;
        }

        public BigDecimal getRemainingAmount() {
            return remainingAmount;
        }

        public void setRemainingAmount(BigDecimal remainingAmount) {
            this.remainingAmount = remainingAmount;
        }

        public BigDecimal getPercentageOfTotal() {
            return percentageOfTotal;
        }

        public void setPercentageOfTotal(BigDecimal percentageOfTotal) {
            this.percentageOfTotal = percentageOfTotal;
        }

        public LocalDate getEffectiveDate() {
            return effectiveDate;
        }

        public void setEffectiveDate(LocalDate effectiveDate) {
            this.effectiveDate = effectiveDate;
        }

        public LocalDate getExpirationDate() {
            return expirationDate;
        }

        public void setExpirationDate(LocalDate expirationDate) {
            this.expirationDate = expirationDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getParentAllocationId() {
            return parentAllocationId;
        }

        public void setParentAllocationId(String parentAllocationId) {
            this.parentAllocationId = parentAllocationId;
        }

        public Boolean getRollOverEnabled() {
            return rollOverEnabled;
        }

        public void setRollOverEnabled(Boolean rollOverEnabled) {
            this.rollOverEnabled = rollOverEnabled;
        }

        public BigDecimal getRollOverAmount() {
            return rollOverAmount;
        }

        public void setRollOverAmount(BigDecimal rollOverAmount) {
            this.rollOverAmount = rollOverAmount;
        }
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalAllocated() {
        return totalAllocated;
    }

    public void setTotalAllocated(BigDecimal totalAllocated) {
        this.totalAllocated = totalAllocated;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }

    public BigDecimal getTotalRemaining() {
        return totalRemaining;
    }

    public void setTotalRemaining(BigDecimal totalRemaining) {
        this.totalRemaining = totalRemaining;
    }

    public String getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public String getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(String closedBy) {
        this.closedBy = closedBy;
    }

    public Instant getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Instant closedAt) {
        this.closedAt = closedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<BudgetAllocationEmbed> getAllocations() {
        return allocations;
    }

    public void setAllocations(List<BudgetAllocationEmbed> allocations) {
        this.allocations = allocations;
    }

    public List<Object> getDomainEvents() {
        return domainEvents;
    }

    public void setDomainEvents(List<Object> domainEvents) {
        this.domainEvents = domainEvents;
    }
}
