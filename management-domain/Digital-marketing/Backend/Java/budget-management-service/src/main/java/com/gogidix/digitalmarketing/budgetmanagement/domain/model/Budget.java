package com.gogidix.digitalmarketing.budgetmanagement.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "budgets")
public class Budget {

    @Id
    private String id;
    private String tenantId;
    private String name;
    private String fiscalYear;
    private BigDecimal totalAmount;
    private BigDecimal allocatedAmount;
    private BigDecimal committedAmount;
    private BigDecimal spentAmount;
    private String status;
    private String currency;
    private String budgetCategory;
    private String country;
    private String department;
    private String parentBudgetId;
    private String approver;
    private Instant approvedAt;
    private Instant startDate;
    private Instant endDate;
    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;
    private List<BudgetAllocation> allocations;
    private List<BudgetPeriod> periods;
    private Map<String, Object> metadata;

    public Budget(String tenantId, String name, String fiscalYear, BigDecimal totalAmount) {
        this.id = UUID.randomUUID().toString();
        this.tenantId = tenantId;
        this.name = name;
        this.fiscalYear = fiscalYear;
        this.totalAmount = totalAmount;
        this.allocatedAmount = BigDecimal.ZERO;
        this.committedAmount = BigDecimal.ZERO;
        this.spentAmount = BigDecimal.ZERO;
        this.status = "DRAFT";
        this.currency = "USD";
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.allocations = new ArrayList<>();
        this.periods = new ArrayList<>();
        this.metadata = new HashMap<>();
    }

    public void allocate(String category, BigDecimal amount, String startDate, String endDate) {
        this.allocations.add(new BudgetAllocation(category, amount, startDate, endDate));
        this.allocatedAmount = this.allocatedAmount.add(amount);
        this.updatedAt = Instant.now();
    }

    public void commit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Commit amount must be positive");
        }
        if (amount.compareTo(getRemainingAmount()) > 0) {
            throw new IllegalStateException("Insufficient remaining funds");
        }
        this.committedAmount = this.committedAmount.add(amount);
        this.updatedAt = Instant.now();
    }

    public void spend(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Spend amount must be positive");
        }
        this.spentAmount = this.spentAmount.add(amount);
        this.updatedAt = Instant.now();
    }

    public BigDecimal getRemainingAmount() {
        return totalAmount.subtract(allocatedAmount);
    }

    public BigDecimal getUnallocatedAmount() {
        return totalAmount.subtract(allocatedAmount);
    }

    public boolean hasRemainingFunds(BigDecimal amount) {
        return getRemainingAmount().compareTo(amount) >= 0;
    }

    public BigDecimal getUtilizationPercentage() {
        if (totalAmount.compareTo(BigDecimal.ZERO) == 0) return null;
        return spentAmount.multiply(new BigDecimal("100")).divide(totalAmount, 4, RoundingMode.HALF_UP);
    }

    public boolean isActive() { return "ACTIVE".equals(status); }
    public boolean isApproved() { return "APPROVED".equals(status); }

    public void approve(String userId) {
        this.status = "APPROVED";
        this.approver = userId;
        this.approvedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void activate() {
        if (!"APPROVED".equals(status)) {
            throw new IllegalStateException("Budget must be approved before activation");
        }
        this.status = "ACTIVE";
        this.updatedAt = Instant.now();
    }

    public List<BudgetAllocation> getAllocations() {
        if (this.allocations == null) this.allocations = new ArrayList<>();
        return this.allocations;
    }

    public List<BudgetPeriod> getPeriods() {
        if (this.periods == null) this.periods = new ArrayList<>();
        return this.periods;
    }

    public Map<String, Object> getMetadata() {
        if (this.metadata == null) this.metadata = new HashMap<>();
        return this.metadata;
    }

    @Data
    @NoArgsConstructor
    public static class BudgetAllocation {
        private String category;
        private BigDecimal amount;
        private String startDate;
        private String endDate;
        private String description;

        public BudgetAllocation(String category, BigDecimal amount, String startDate, String endDate) {
            this.category = category;
            this.amount = amount;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public BudgetAllocation(String category, BigDecimal amount, String startDate, String endDate, String description) {
            this(category, amount, startDate, endDate);
            this.description = description;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BudgetPeriod {
        private String period;
        private BigDecimal amount;
        private Instant startDate;
        private Instant endDate;
    }
}
