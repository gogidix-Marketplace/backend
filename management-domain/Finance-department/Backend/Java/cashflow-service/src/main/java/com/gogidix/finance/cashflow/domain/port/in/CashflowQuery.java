package com.gogidix.finance.cashflow.domain.port.in;

import com.gogidix.finance.cashflow.domain.model.CashflowItem;
import com.gogidix.finance.cashflow.domain.model.CashflowForecast;
import com.gogidix.finance.cashflow.domain.model.CashflowStatement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Cashflow Queries (Input Port)
 * Defines the query operations for cashflow data
 */
public interface CashflowQuery {

    // Cashflow Item Queries

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetCashflowItemQuery {
        private String tenantId;

        private String cashflowItemId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetCashflowItemsByDateRangeQuery {
        private String tenantId;

        private LocalDate startDate;

        private LocalDate endDate;

        private CashflowItem.CashflowType type;

        private List<CashflowItem.ItemStatus> statuses;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetCashflowItemsByTypeQuery {
        private String tenantId;

        private CashflowItem.CashflowType type;

        private LocalDate startDate;

        private LocalDate endDate;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetCashflowItemsByCategoryQuery {
        private String tenantId;

        private CashflowItem.CashflowCategory category;

        private LocalDate startDate;

        private LocalDate endDate;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetCashflowItemsByStatusQuery {
        private String tenantId;

        private CashflowItem.ItemStatus status;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetRecurringCashflowItemsQuery {
        private String tenantId;

        private Boolean recurring;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetCashflowItemsByCostCenterQuery {
        private String tenantId;

        private String costCenter;

        private LocalDate startDate;

        private LocalDate endDate;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetCashflowItemsByProjectQuery {
        private String tenantId;

        private String projectId;

        private LocalDate startDate;

        private LocalDate endDate;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetPendingCashflowItemsQuery {
        private String tenantId;

        private LocalDate dueDate;

        private Integer page;

        private Integer size;
    }

    // Cashflow Forecast Queries

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetForecastQuery {
        private String tenantId;

        private String forecastId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetForecastsByDateRangeQuery {
        private String tenantId;

        private LocalDate startDate;

        private LocalDate endDate;

        private CashflowForecast.ForecastScenario scenario;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetForecastsByScenarioQuery {
        private String tenantId;

        private CashflowForecast.ForecastScenario scenario;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetForecastsByStatusQuery {
        private String tenantId;

        private CashflowForecast.ForecastStatus status;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetBaselineForecastQuery {
        private String tenantId;

        private LocalDate date;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetForecastVersionsQuery {
        private String tenantId;

        private String parentForecastId;

        private Integer page;

        private Integer size;
    }

    // Cashflow Statement Queries

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetCashflowStatementQuery {
        private String tenantId;

        private String statementId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetCashflowStatementsByPeriodQuery {
        private String tenantId;

        private LocalDate startDate;

        private LocalDate endDate;

        private CashflowStatement.StatementPeriod period;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetCashflowStatementsByFiscalYearQuery {
        private String tenantId;

        private Integer fiscalYear;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetCashflowStatementsByStatusQuery {
        private String tenantId;

        private CashflowStatement.StatementStatus status;

        private Integer page;

        private Integer size;
    }

    // Summary Queries

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetCashflowSummaryQuery {
        private String tenantId;

        private LocalDate startDate;

        private LocalDate endDate;

        private String costCenter;

        private String project;

        private CashflowItem.CashflowType type;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetCashflowPositionQuery {
        private String tenantId;

        private LocalDate asOfDate;

        private String currency;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetCashflowTrendQuery {
        private String tenantId;

        private LocalDate startDate;

        private LocalDate endDate;

        private CashflowForecast.ForecastPeriod period;

        private List<String> categories;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetCashflowVarianceReportQuery {
        private String tenantId;

        private String forecastId;

        private LocalDate comparisonDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetLiquidityForecastQuery {
        private String tenantId;

        private LocalDate startDate;

        private LocalDate endDate;

        private Integer horizonDays;

        private BigDecimal openingBalance;
    }
}
