package com.gogidix.sales.dashboard.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Dashboard Queries (Input Port)
 * Defines the query operations for dashboard data
 */
public interface DashboardQuery {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetDashboardQuery {
        private String tenantId;

        private String dashboardId;

        private Boolean includeWidgets;

        private Boolean includeTrendData;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetDashboardsByTypeQuery {
        private String tenantId;

        private com.gogidix.sales.dashboard.domain.model.GlobalSalesDashboard.DashboardType type;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetDashboardsByStatusQuery {
        private String tenantId;

        private com.gogidix.sales.dashboard.domain.model.GlobalSalesDashboard.DashboardStatus status;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetGlobalMetricsQuery {
        private String tenantId;

        private String dashboardId;

        private String currency;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetRegionalMetricsQuery {
        private String tenantId;

        private String dashboardId;

        private String regionCode;

        private String baseCurrency;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetTopPerformingRegionsQuery {
        private String tenantId;

        private String dashboardId;

        private Integer limit;

        private String sortBy; // REVENUE, GROWTH_RATE, ACHIEVEMENT
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetTrendDataQuery {
        private String tenantId;

        private String dashboardId;

        private LocalDate startDate;

        private LocalDate endDate;

        private String region;

        private String period;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetExecutiveSummaryQuery {
        private String tenantId;

        private String dashboardId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetWidgetQuery {
        private String tenantId;

        private String dashboardId;

        private String widgetId;

        private Boolean includeHistoricalData;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetWidgetsByCategoryQuery {
        private String tenantId;

        private String dashboardId;

        private com.gogidix.sales.dashboard.domain.model.KPIWidget.WidgetCategory category;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CompareRegionsQuery {
        private String tenantId;

        private String dashboardId;

        private java.util.List<String> regionCodes;

        private String baseCurrency;

        private String metric; // REVENUE, DEALS, WIN_RATE, etc.
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetPerformanceComparisonQuery {
        private String tenantId;

        private String dashboardId;

        private String comparisonType; // YOY, MOM, QOQ

        private String metric;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetDrillDownDataQuery {
        private String tenantId;

        private String dashboardId;

        private String drillDownId;

        private java.util.Map<String, Object> filters;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetAllDashboardsQuery {
        private String tenantId;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetDashboardSummaryQuery {
        private String tenantId;

        private String dashboardId;
    }
}
