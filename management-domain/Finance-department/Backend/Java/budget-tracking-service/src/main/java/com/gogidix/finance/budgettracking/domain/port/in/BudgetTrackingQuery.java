package com.gogidix.finance.budgettracking.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Budget Tracking Queries (Input Port)
 * Defines the query operations for budget tracking data
 */
public interface BudgetTrackingQuery {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetTransactionQuery {
        private String tenantId;

        private String transactionId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetTransactionsByBudgetQuery {
        private String tenantId;

        private String budgetId;

        private Integer page;

        private Integer size;

        private String sortBy;

        private String sortDirection;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetTransactionsByTypeQuery {
        private String tenantId;

        private String transactionType;

        private Integer page;

        private Integer size;

        private String sortBy;

        private String sortDirection;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetTransactionsByDateRangeQuery {
        private String tenantId;

        private LocalDate startDate;

        private LocalDate endDate;

        private String status;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetMonitorQuery {
        private String tenantId;

        private String monitorId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetMonitorsByBudgetQuery {
        private String tenantId;

        private String budgetId;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetMonitorsByPeriodQuery {
        private String tenantId;

        private YearMonth period;

        private String status;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetMonitorsByDepartmentQuery {
        private String tenantId;

        private String department;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetAlertQuery {
        private String tenantId;

        private String alertId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetAlertsByBudgetQuery {
        private String tenantId;

        private String budgetId;

        private boolean enabledOnly;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetBudgetSummaryQuery {
        private String tenantId;

        private String budgetId;

        private YearMonth period;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetUtilizationReportQuery {
        private String tenantId;

        private String department;

        private String category;

        private YearMonth fromPeriod;

        private YearMonth toPeriod;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetVarianceAnalysisQuery {
        private String tenantId;

        private String budgetId;

        private YearMonth period;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SearchTransactionsQuery {
        private String tenantId;

        private String searchTerm;

        private String transactionType;

        private String status;

        private LocalDate startDate;

        private LocalDate endDate;

        private BigDecimal minAmount;

        private BigDecimal maxAmount;

        private String category;

        private String department;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetThresholdAlertsQuery {
        private String tenantId;

        private String thresholdLevel;

        private boolean breachedOnly;

        private boolean unacknowledgedOnly;

        private Integer page;

        private Integer size;
    }
}
