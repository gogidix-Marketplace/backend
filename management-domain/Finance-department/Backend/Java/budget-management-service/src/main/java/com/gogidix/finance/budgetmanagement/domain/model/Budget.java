package com.gogidix.finance.budgetmanagement.domain.model;

import com.gogidix.finance.budgetmanagement.domain.event.BudgetApprovedEvent;
import com.gogidix.finance.budgetmanagement.domain.event.BudgetCreatedEvent;
import com.gogidix.finance.budgetmanagement.domain.event.BudgetModifiedEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * Budget Domain Entity
 * Multi-tenant budget management with approval workflow
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "budgets")
public class Budget extends BaseEntity {

    private String budgetId;

    private String tenantId;

    private String name;

    private String description;

    private BudgetPeriod period;

    private LocalDate startDate;

    private LocalDate endDate;

    private BudgetStatus status;

    private BigDecimal totalAllocated;

    private BigDecimal totalSpent;

    private BigDecimal totalRemaining;

    private String fiscalYear;

    private String department;

    private String costCenter;

    private String createdBy;

    private String approvedBy;

    private Instant approvedAt;

    private String submittedBy;

    private Instant submittedAt;

    private String closedBy;

    private Instant closedAt;

    private String notes;

    @Builder.Default
    private List<BudgetAllocation> allocations = new ArrayList<>();

    @Builder.Default
    private List<Object> domainEvents = new ArrayList<>();

    public enum BudgetStatus {
        DRAFT,
        SUBMITTED,
        APPROVED,
        ACTIVE,
        CLOSED
    }

    /**
     * Creates a new budget
     */
    public static Budget create(String tenantId, String name, String description,
                                 BudgetPeriod period, LocalDate startDate, LocalDate endDate,
                                 String fiscalYear, String department, String createdBy) {
        Budget budget = Budget.builder()
                .tenantId(tenantId)
                .name(name)
                .description(description)
                .period(period)
                .startDate(startDate)
                .endDate(endDate)
                .fiscalYear(fiscalYear)
                .department(department)
                .createdBy(createdBy)
                .status(BudgetStatus.DRAFT)
                .totalAllocated(BigDecimal.ZERO)
                .totalSpent(BigDecimal.ZERO)
                .totalRemaining(BigDecimal.ZERO)
                .allocations(new ArrayList<>())
                .domainEvents(new ArrayList<>())
                .build();

        budget.addDomainEvent(BudgetCreatedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .budgetId(budget.getBudgetId())
                .tenantId(tenantId)
                .name(name)
                .period(period.name())
                .fiscalYear(fiscalYear)
                .department(department)
                .timestamp(Instant.now())
                .eventType("BUDGET_CREATED")
                .build());

        return budget;
    }

    /**
     * Submits the budget for approval
     */
    public void submit(String submittedBy) {
        if (this.status != BudgetStatus.DRAFT) {
            throw new IllegalStateException("Can only submit draft budgets");
        }

        validateForSubmission();

        this.status = BudgetStatus.SUBMITTED;
        this.submittedBy = submittedBy;
        this.submittedAt = Instant.now();

        addDomainEvent(BudgetModifiedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .budgetId(this.budgetId)
                .tenantId(this.tenantId)
                .name(this.name)
                .eventType("BUDGET_SUBMITTED")
                .timestamp(Instant.now())
                .build());
    }

    /**
     * Approves the budget
     */
    public void approve(String approver) {
        if (this.status != BudgetStatus.SUBMITTED) {
            throw new IllegalStateException("Can only approve submitted budgets");
        }

        this.status = BudgetStatus.APPROVED;
        this.approvedBy = approver;
        this.approvedAt = Instant.now();

        addDomainEvent(BudgetApprovedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .budgetId(this.budgetId)
                .tenantId(this.tenantId)
                .name(this.name)
                .totalAllocated(this.totalAllocated)
                .fiscalYear(this.fiscalYear)
                .department(this.department)
                .approvedBy(approver)
                .timestamp(Instant.now())
                .eventType("BUDGET_APPROVED")
                .build());
    }

    /**
     * Activates the budget
     */
    public void activate() {
        if (this.status != BudgetStatus.APPROVED) {
            throw new IllegalStateException("Can only activate approved budgets");
        }

        this.status = BudgetStatus.ACTIVE;

        addDomainEvent(BudgetModifiedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .budgetId(this.budgetId)
                .tenantId(this.tenantId)
                .name(this.name)
                .eventType("BUDGET_ACTIVATED")
                .timestamp(Instant.now())
                .build());
    }

    /**
     * Closes the budget
     */
    public void close(String closedBy) {
        if (this.status != BudgetStatus.ACTIVE) {
            throw new IllegalStateException("Can only close active budgets");
        }

        this.status = BudgetStatus.CLOSED;
        this.closedBy = closedBy;
        this.closedAt = Instant.now();

        addDomainEvent(BudgetModifiedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .budgetId(this.budgetId)
                .tenantId(this.tenantId)
                .name(this.name)
                .eventType("BUDGET_CLOSED")
                .timestamp(Instant.now())
                .build());
    }

    /**
     * Adds an allocation to the budget
     */
    public void addAllocation(BudgetAllocation allocation) {
        if (this.status == BudgetStatus.ACTIVE || this.status == BudgetStatus.CLOSED) {
            throw new IllegalStateException("Cannot add allocations to active or closed budgets");
        }

        if (this.allocations == null) {
            this.allocations = new ArrayList<>();
        }

        this.allocations.add(allocation);
        recalculateTotals();

        addDomainEvent(BudgetModifiedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .budgetId(this.budgetId)
                .tenantId(this.tenantId)
                .name(this.name)
                .eventType("ALLOCATION_ADDED")
                .timestamp(Instant.now())
                .build());
    }

    /**
     * Updates an existing allocation
     */
    public void updateAllocation(String allocationId, BigDecimal newAmount) {
        if (this.status == BudgetStatus.ACTIVE || this.status == BudgetStatus.CLOSED) {
            throw new IllegalStateException("Cannot update allocations in active or closed budgets");
        }

        if (this.allocations == null) {
            throw new IllegalStateException("No allocations found");
        }

        BudgetAllocation allocation = this.allocations.stream()
                .filter(a -> a.getAllocationId().equals(allocationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Allocation not found"));

        allocation.setAllocatedAmount(newAmount);
        recalculateTotals();

        addDomainEvent(BudgetModifiedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .budgetId(this.budgetId)
                .tenantId(this.tenantId)
                .name(this.name)
                .eventType("ALLOCATION_UPDATED")
                .timestamp(Instant.now())
                .build());
    }

    /**
     * Removes an allocation from the budget
     */
    public void removeAllocation(String allocationId) {
        if (this.status == BudgetStatus.ACTIVE || this.status == BudgetStatus.CLOSED) {
            throw new IllegalStateException("Cannot remove allocations from active or closed budgets");
        }

        if (this.allocations == null) {
            throw new IllegalStateException("No allocations found");
        }

        boolean removed = this.allocations.removeIf(a -> a.getAllocationId().equals(allocationId));
        if (!removed) {
            throw new IllegalArgumentException("Allocation not found");
        }

        recalculateTotals();

        addDomainEvent(BudgetModifiedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .budgetId(this.budgetId)
                .tenantId(this.tenantId)
                .name(this.name)
                .eventType("ALLOCATION_REMOVED")
                .timestamp(Instant.now())
                .build());
    }

    /**
     * Updates the spent amount for a specific allocation
     */
    public void updateSpentAmount(String allocationId, BigDecimal spentAmount) {
        if (this.status != BudgetStatus.ACTIVE) {
            throw new IllegalStateException("Can only update spent amounts for active budgets");
        }

        if (this.allocations == null) {
            throw new IllegalStateException("No allocations found");
        }

        BudgetAllocation allocation = this.allocations.stream()
                .filter(a -> a.getAllocationId().equals(allocationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Allocation not found"));

        allocation.setSpentAmount(spentAmount);
        recalculateTotals();
    }

    /**
     * Gets the variance percentage for the budget
     */
    public BigDecimal getVariancePercentage() {
        if (totalAllocated == null || totalAllocated.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return totalRemaining.divide(totalAllocated, 2, java.math.RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
    }

    private void validateForSubmission() {
        if (this.name == null || this.name.isBlank()) {
            throw new com.gogidix.finance.budgetmanagement.shared.exception.ValidationException("name", "Budget name is required");
        }
        if (this.startDate == null) {
            throw new com.gogidix.finance.budgetmanagement.shared.exception.ValidationException("startDate", "Start date is required");
        }
        if (this.endDate == null) {
            throw new com.gogidix.finance.budgetmanagement.shared.exception.ValidationException("endDate", "End date is required");
        }
        if (this.endDate.isBefore(this.startDate)) {
            throw new com.gogidix.finance.budgetmanagement.shared.exception.ValidationException("endDate", "End date must be after start date");
        }
        if (this.allocations == null || this.allocations.isEmpty()) {
            throw new com.gogidix.finance.budgetmanagement.shared.exception.ValidationException("allocations", "At least one allocation is required");
        }
    }

    private void recalculateTotals() {
        this.totalAllocated = this.allocations.stream()
                .map(BudgetAllocation::getAllocatedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalSpent = this.allocations.stream()
                .map(BudgetAllocation::getSpentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalRemaining = this.totalAllocated.subtract(this.totalSpent);
    }

    public void addDomainEvent(Object event) {
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
