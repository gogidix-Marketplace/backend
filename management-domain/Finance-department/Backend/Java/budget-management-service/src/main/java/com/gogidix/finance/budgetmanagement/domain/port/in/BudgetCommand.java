package com.gogidix.finance.budgetmanagement.domain.port.in;

import com.gogidix.finance.budgetmanagement.domain.model.Budget;
import com.gogidix.finance.budgetmanagement.domain.model.BudgetAllocation;
import com.gogidix.finance.budgetmanagement.domain.model.BudgetPeriod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Budget Commands (Input Port)
 * Defines the input commands for budget operations
 */
public interface BudgetCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateBudgetCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Budget name is required")
        private String name;

        private String description;

        @NotNull(message = "Period is required")
        private BudgetPeriod period;

        @NotNull(message = "Start date is required")
        private LocalDate startDate;

        @NotNull(message = "End date is required")
        private LocalDate endDate;

        private String fiscalYear;

        private String department;

        private String costCenter;

        @NotBlank(message = "Created by is required")
        private String createdBy;

        private List<CreateAllocationCommand> allocations;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateAllocationCommand {
        private BudgetAllocation.AllocationType allocationType;

        private String category;

        private String department;

        private String costCenter;

        private String projectId;

        @NotNull(message = "Allocated amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal allocatedAmount;

        private Boolean rollOverEnabled;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateBudgetCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Budget ID is required")
        private String budgetId;

        private String name;

        private String description;

        private LocalDate startDate;

        private LocalDate endDate;

        private String fiscalYear;

        private String department;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddAllocationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Budget ID is required")
        private String budgetId;

        private BudgetAllocation.AllocationType allocationType;

        private String category;

        private String department;

        private String costCenter;

        private String projectId;

        @NotNull(message = "Allocated amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal allocatedAmount;

        private Boolean rollOverEnabled;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateAllocationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Budget ID is required")
        private String budgetId;

        @NotBlank(message = "Allocation ID is required")
        private String allocationId;

        @NotNull(message = "Allocated amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal allocatedAmount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RemoveAllocationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Budget ID is required")
        private String budgetId;

        @NotBlank(message = "Allocation ID is required")
        private String allocationId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SubmitBudgetCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Budget ID is required")
        private String budgetId;

        @NotBlank(message = "Submitted by is required")
        private String submittedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ApproveBudgetCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Budget ID is required")
        private String budgetId;

        @NotBlank(message = "Approver is required")
        private String approver;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ActivateBudgetCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Budget ID is required")
        private String budgetId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CloseBudgetCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Budget ID is required")
        private String budgetId;

        @NotBlank(message = "Closed by is required")
        private String closedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateSpentAmountCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Budget ID is required")
        private String budgetId;

        @NotBlank(message = "Allocation ID is required")
        private String allocationId;

        @NotNull(message = "Spent amount is required")
        private BigDecimal spentAmount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteBudgetCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Budget ID is required")
        private String budgetId;
    }
}
