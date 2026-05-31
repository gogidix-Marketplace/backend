package com.gogidix.finance.budgetmanagement.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Budget Allocation Entity
 * Represents allocation of budget to specific categories or departments
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Document(collection = "budget_allocations")
public class BudgetAllocation extends BaseEntity {

    private String allocationId;

    private String budgetId;

    private String tenantId;

    private AllocationType allocationType;

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

    private AllocationStatus status;

    private String notes;

    private String parentAllocationId;

    @Builder.Default
    private Boolean rollOverEnabled = false;

    private BigDecimal rollOverAmount;

    public enum AllocationType {
        CATEGORY,
        DEPARTMENT,
        COST_CENTER,
        PROJECT,
        CUSTOM
    }

    public enum AllocationStatus {
        PENDING,
        ACTIVE,
        SUSPENDED,
        EXPIRED,
        FULLY_UTILIZED
    }

    /**
     * Creates a new budget allocation
     */
    public static BudgetAllocation create(String budgetId, String tenantId,
                                           AllocationType allocationType, String category,
                                           String department, BigDecimal allocatedAmount) {
        BudgetAllocation allocation = BudgetAllocation.builder()
                .budgetId(budgetId)
                .tenantId(tenantId)
                .allocationType(allocationType)
                .category(category)
                .department(department)
                .allocatedAmount(allocatedAmount)
                .spentAmount(BigDecimal.ZERO)
                .remainingAmount(allocatedAmount)
                .status(AllocationStatus.PENDING)
                .rollOverEnabled(false)
                .build();

        return allocation;
    }

    /**
     * Activates the allocation
     */
    public void activate() {
        if (this.status != AllocationStatus.PENDING) {
            throw new IllegalStateException("Can only activate pending allocations");
        }
        this.status = AllocationStatus.ACTIVE;
        this.effectiveDate = LocalDate.now();
    }

    /**
     * Suspends the allocation
     */
    public void suspend() {
        if (this.status != AllocationStatus.ACTIVE) {
            throw new IllegalStateException("Can only suspend active allocations");
        }
        this.status = AllocationStatus.SUSPENDED;
    }

    /**
     * Updates the spent amount
     */
    public void updateSpentAmount(BigDecimal amount) {
        if (this.status != AllocationStatus.ACTIVE) {
            throw new IllegalStateException("Can only update spent amount for active allocations");
        }

        this.spentAmount = this.spentAmount.add(amount);
        this.remainingAmount = this.allocatedAmount.subtract(this.spentAmount);

        if (this.remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
            this.status = AllocationStatus.FULLY_UTILIZED;
            this.remainingAmount = BigDecimal.ZERO;
        }
    }

    /**
     * Calculates the utilization percentage
     */
    public BigDecimal getUtilizationPercentage() {
        if (this.allocatedAmount == null || this.allocatedAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return this.spentAmount
                .divide(this.allocatedAmount, 2, java.math.RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
    }

    /**
     * Checks if the allocation is over budget
     */
    public boolean isOverBudget() {
        return this.spentAmount.compareTo(this.allocatedAmount) > 0;
    }

    /**
     * Checks if the allocation is near limit (90% utilized)
     */
    public boolean isNearLimit() {
        BigDecimal threshold = this.allocatedAmount
                .multiply(new BigDecimal("0.90"));
        return this.spentAmount.compareTo(threshold) >= 0;
    }

    /**
     * Enables rollover for unused amount
     */
    public void enableRollOver() {
        this.rollOverEnabled = true;
        if (this.remainingAmount.compareTo(BigDecimal.ZERO) > 0) {
            this.rollOverAmount = this.remainingAmount;
        }
    }

    /**
     * Sets the percentage of total budget
     */
    public void setPercentageOfTotal(BigDecimal totalBudget) {
        if (totalBudget != null && totalBudget.compareTo(BigDecimal.ZERO) > 0) {
            this.percentageOfTotal = this.allocatedAmount
                    .divide(totalBudget, 2, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
    }
}
