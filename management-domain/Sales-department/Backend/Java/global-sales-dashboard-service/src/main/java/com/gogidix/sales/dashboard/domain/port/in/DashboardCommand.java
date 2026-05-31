package com.gogidix.sales.dashboard.domain.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Dashboard Commands (Input Port)
 * Defines the input commands for dashboard operations
 */
public interface DashboardCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateDashboardCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Name is required")
        private String name;

        private String description;

        @NotNull(message = "Dashboard type is required")
        private com.gogidix.sales.dashboard.domain.model.GlobalSalesDashboard.DashboardType type;

        @NotBlank(message = "Base currency is required")
        private String baseCurrency;

        private List<String> enabledRegions;

        private Integer refreshIntervalMinutes;

        private String createdBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateDashboardCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Dashboard ID is required")
        private String dashboardId;

        private String name;

        private String description;

        private com.gogidix.sales.dashboard.domain.model.GlobalSalesDashboard.DashboardStatus status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateGlobalMetricsCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Dashboard ID is required")
        private String dashboardId;

        private BigDecimal totalRevenue;

        private BigDecimal targetRevenue;

        private Integer totalDeals;

        private Integer wonDeals;

        private BigDecimal averageDealSize;

        private BigDecimal weightedPipeline;

        private Integer opportunitiesInPipeline;

        private BigDecimal yearOverYearGrowth;

        private BigDecimal monthOverMonthGrowth;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateRegionalMetricCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Dashboard ID is required")
        private String dashboardId;

        @NotBlank(message = "Region code is required")
        private String regionCode;

        private String regionName;

        @NotNull(message = "Revenue is required")
        private BigDecimal revenue;

        @NotNull(message = "Currency is required")
        private String currency;

        private BigDecimal target;

        private Integer deals;

        private BigDecimal growthRate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddWidgetCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Dashboard ID is required")
        private String dashboardId;

        @NotBlank(message = "Widget title is required")
        private String title;

        @NotNull(message = "Widget type is required")
        private com.gogidix.sales.dashboard.domain.model.KPIWidget.WidgetType widgetType;

        @NotNull(message = "Widget category is required")
        private com.gogidix.sales.dashboard.domain.model.KPIWidget.WidgetCategory category;

        private String description;

        private Object initialValue;

        private Integer row;

        private Integer column;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateWidgetCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Dashboard ID is required")
        private String dashboardId;

        @NotBlank(message = "Widget ID is required")
        private String widgetId;

        private Object value;

        private String displayValue;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RefreshDashboardCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Dashboard ID is required")
        private String dashboardId;

        @NotBlank(message = "User ID is required")
        private String userId;

        private Boolean forceRefresh;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class PublishDashboardCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Dashboard ID is required")
        private String dashboardId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ArchiveDashboardCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Dashboard ID is required")
        private String dashboardId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateExecutiveSummaryCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Dashboard ID is required")
        private String dashboardId;

        private String headline;

        private String keyHighlight;

        private List<String> topPerformers;

        private List<String> areasForImprovement;

        private String overallSentiment;

        private BigDecimal riskScore;

        private List<String> recommendations;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddTrendDataCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Dashboard ID is required")
        private String dashboardId;

        @NotNull(message = "Period is required")
        private String period;

        @NotNull(message = "Date is required")
        private LocalDate date;

        @NotNull(message = "Revenue is required")
        private BigDecimal revenue;

        private Integer deals;

        private BigDecimal conversionRate;

        private String region;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateExchangeRatesCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Dashboard ID is required")
        private String dashboardId;

        @NotNull(message = "Exchange rates are required")
        private java.util.Map<String, BigDecimal> exchangeRates;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteDashboardCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Dashboard ID is required")
        private String dashboardId;
    }
}
