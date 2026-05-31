package com.gogidix.finance.budgetmanagement.domain.port.in;

import com.gogidix.finance.budgetmanagement.domain.model.Budget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Budget Queries (Input Port)
 * Defines the query operations for budget data
 */
public interface BudgetQuery {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetBudgetQuery {
        private String tenantId;

        private String budgetId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetBudgetsByDepartmentQuery {
        private String tenantId;

        private String department;

        private Integer page;

        private Integer size;

        private String sortBy;

        private String sortDirection;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetBudgetsByFiscalYearQuery {
        private String tenantId;

        private String fiscalYear;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetBudgetsByStatusQuery {
        private String tenantId;

        private Budget.BudgetStatus status;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetBudgetsByDateRangeQuery {
        private String tenantId;

        private LocalDate startDate;

        private LocalDate endDate;

        private List<Budget.BudgetStatus> statuses;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetActiveBudgetsQuery {
        private String tenantId;

        private String department;

        private LocalDate asOfDate;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetBudgetSummaryQuery {
        private String tenantId;

        private String fiscalYear;

        private String department;

        private String costCenter;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetBudgetVarianceQuery {
        private String tenantId;

        private String budgetId;

        private String allocationId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SearchBudgetsQuery {
        private String tenantId;

        private String searchTerm;

        private String department;

        private String fiscalYear;

        private Budget.BudgetStatus status;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetAllocationsByBudgetQuery {
        private String tenantId;

        private String budgetId;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetAllocationByIdQuery {
        private String tenantId;

        private String budgetId;

        private String allocationId;
    }
}
