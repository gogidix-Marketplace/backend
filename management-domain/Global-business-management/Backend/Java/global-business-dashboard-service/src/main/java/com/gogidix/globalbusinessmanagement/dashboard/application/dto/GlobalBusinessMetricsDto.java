package com.gogidix.globalbusinessmanagement.dashboard.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for GlobalBusinessMetrics domain model.
 * Used for API request/response operations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalBusinessMetricsDto {

    private String id;

    @NotBlank(message = "Period identifier is required")
    private String periodId;

    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    @NotNull(message = "Total revenue is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total revenue must be non-negative")
    private BigDecimal totalRevenue;

    @NotNull(message = "Total expenses is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total expenses must be non-negative")
    private BigDecimal totalExpenses;

    private BigDecimal grossProfit;

    private BigDecimal netProfit;

    private BigDecimal profitMargin;

    @NotNull(message = "Total orders is required")
    @Positive(message = "Total orders must be positive")
    private Long totalOrders;

    @NotNull(message = "Active customers is required")
    @Positive(message = "Active customers must be positive")
    private Long activeCustomers;

    private Long newCustomers;

    private Long churnedCustomers;

    private BigDecimal customerRetentionRate;

    private BigDecimal averageOrderValue;

    private Long totalTransactions;

    private BigDecimal conversionRate;

    private BigDecimal cartAbandonmentRate;

    private String baseCurrency;

    private Map<String, RegionalContributionDto> regionalBreakdown;

    private Map<String, CategoryPerformanceDto> productCategoryPerformance;

    private MarketTrendsDto marketTrends;

    private OperationalMetricsDto operationalMetrics;

    private String status;

    private String dataSource;

    private Integer version;

    private CalculatedMetrics calculatedMetrics;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionalContributionDto {
        private String regionCode;
        private String regionName;
        private BigDecimal revenue;
        private BigDecimal revenueContribution;
        private BigDecimal growthRate;
        private Long orders;
        private Long customers;
        private BigDecimal profitMargin;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryPerformanceDto {
        private String categoryCode;
        private String categoryName;
        private BigDecimal revenue;
        private BigDecimal revenueContribution;
        private BigDecimal growthRate;
        private Long unitsSold;
        private BigDecimal averagePrice;
        private Integer productCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MarketTrendsDto {
        private BigDecimal marketShare;
        private BigDecimal marketGrowthRate;
        private Integer competitorCount;
        private BigDecimal industryAverageMargin;
        private String trendDirection;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OperationalMetricsDto {
        private BigDecimal inventoryTurnover;
        private BigDecimal orderFulfillmentRate;
        private BigDecimal returnRate;
        private BigDecimal averageFulfillmentTime;
        private Long activeProducts;
        private Long discontinuedProducts;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CalculatedMetrics {
        private BigDecimal customerGrowthRate;
        private BigDecimal customerChurnRate;
        private BigDecimal revenuePerCustomer;
        private BigDecimal ordersPerCustomer;
        private BigDecimal netCustomerGrowthRate;
    }
}
