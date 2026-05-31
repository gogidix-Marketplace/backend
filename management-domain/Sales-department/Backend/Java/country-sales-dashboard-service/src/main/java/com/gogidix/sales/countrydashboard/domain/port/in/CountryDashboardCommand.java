package com.gogidix.sales.countrydashboard.domain.port.in;

import com.gogidix.sales.countrydashboard.domain.model.CountrySalesDashboard;
import com.gogidix.sales.countrydashboard.domain.valueobject.MetricType;
import com.gogidix.sales.countrydashboard.domain.valueobject.TimePeriod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Country Dashboard Commands (Input Port)
 * Defines the input commands for country dashboard operations
 */
public interface CountryDashboardCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateDashboardCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Country code is required")
        private String countryCode;

        @NotBlank(message = "Country name is required")
        private String countryName;

        @NotNull(message = "Dashboard type is required")
        private CountrySalesDashboard.DashboardType type;

        private String localCurrency;

        private List<String> enabledTerritories;

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

        private CountrySalesDashboard.DashboardStatus status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateCountryMetricsCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Dashboard ID is required")
        private String dashboardId;

        private BigDecimal totalRevenue;

        private BigDecimal targetRevenue;

        private Integer totalDeals;

        private Integer wonDeals;

        private Integer lostDeals;

        private BigDecimal averageDealSize;

        private BigDecimal weightedPipeline;

        private Integer opportunitiesInPipeline;

        private Integer newCustomers;

        private Integer churnedCustomers;

        private BigDecimal retentionRate;

        private BigDecimal npsScore;

        private Integer activeSalesReps;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateTerritoryMetricCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Dashboard ID is required")
        private String dashboardId;

        @NotBlank(message = "Territory ID is required")
        private String territoryId;

        private String territoryName;

        private String territoryCode;

        @NotNull(message = "Revenue is required")
        private BigDecimal revenue;

        private String currency;

        private BigDecimal quota;

        private Integer deals;

        private Integer wonDeals;

        private BigDecimal growthRate;

        private Map<String, Object> attributes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateQuotaCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Dashboard ID is required")
        private String dashboardId;

        @NotNull(message = "Annual quota is required")
        private BigDecimal annualQuota;

        private String currency;

        @NotBlank(message = "Reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddKPICommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Dashboard ID is required")
        private String dashboardId;

        @NotBlank(message = "KPI name is required")
        private String name;

        @NotNull(message = "KPI type is required")
        private MetricType type;

        private Object value;

        private String target;

        private Integer weight;

        private Boolean isCritical;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateKPICommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Dashboard ID is required")
        private String dashboardId;

        @NotBlank(message = "KPI ID is required")
        private String kpiId;

        private Object value;
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

        private String currency;

        private Integer deals;

        private BigDecimal winRate;

        private BigDecimal averageDealSize;

        private Integer newCustomers;

        private BigDecimal growthRate;

        private String territory;
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

        private Boolean calculateYoY;

        private Boolean calculateMoM;

        private Boolean calculateQoQ;
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
    class UpdateExchangeRatesCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Dashboard ID is required")
        private String dashboardId;

        @NotNull(message = "Exchange rates are required")
        private Map<String, BigDecimal> exchangeRates;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GenerateComparisonCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Dashboard ID is required")
        private String dashboardId;

        private String comparisonType;

        private List<String> compareToCountries;

        private TimePeriod.PeriodType periodType;
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
