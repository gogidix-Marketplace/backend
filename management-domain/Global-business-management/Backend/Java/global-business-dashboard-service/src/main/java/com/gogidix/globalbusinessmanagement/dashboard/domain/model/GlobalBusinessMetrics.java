package com.gogidix.globalbusinessmanagement.dashboard.domain.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Domain model representing global business metrics aggregated across all regions.
 * This model captures high-level KPIs and performance indicators for the entire organization.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "global_business_metrics")
public class GlobalBusinessMetrics {

    @Id
    private String id;

    @NotBlank(message = "Metrics period identifier is required")
    private String periodId;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    @NotNull(message = "Total revenue is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total revenue must be non-negative")
    private BigDecimal totalRevenue;

    @NotNull(message = "Total expenses is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total expenses must be non-negative")
    private BigDecimal totalExpenses;

    @NotNull(message = "Gross profit is required")
    private BigDecimal grossProfit;

    @NotNull(message = "Net profit is required")
    private BigDecimal netProfit;

    @NotNull(message = "Profit margin is required")
    private BigDecimal profitMargin;

    @NotNull(message = "Total orders is required")
    @Positive(message = "Total orders must be positive")
    private Long totalOrders;

    @NotNull(message = "Active customers is required")
    @Positive(message = "Active customers must be positive")
    private Long activeCustomers;

    @NotNull(message = "New customers is required")
    @Positive(message = "New customers must be positive or zero")
    private Long newCustomers;

    @NotNull(message = "Churned customers is required")
    @Positive(message = "Churned customers must be positive or zero")
    private Long churnedCustomers;

    @NotNull(message = "Customer retention rate is required")
    private BigDecimal customerRetentionRate;

    @NotNull(message = "Average order value is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Average order value must be non-negative")
    private BigDecimal averageOrderValue;

    @NotNull(message = "Total transactions is required")
    @Positive(message = "Total transactions must be positive")
    private Long totalTransactions;

    @NotNull(message = "Conversion rate is required")
    private BigDecimal conversionRate;

    @NotNull(message = "Cart abandonment rate is required")
    private BigDecimal cartAbandonmentRate;

    private String baseCurrency;

    @NotNull(message = "Regional breakdown is required")
    private Map<String, RegionalContribution> regionalBreakdown;

    @NotNull(message = "Product category performance is required")
    private Map<String, CategoryPerformance> productCategoryPerformance;

    private MarketTrends marketTrends;

    private OperationalMetrics operationalMetrics;

    @Builder.Default
    private MetricsStatus status = MetricsStatus.PUBLISHED;

    private String dataSource;

    private Integer version;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private String updatedBy;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionalContribution {
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
    public static class CategoryPerformance {
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
    public static class MarketTrends {
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
    public static class OperationalMetrics {
        private BigDecimal inventoryTurnover;
        private BigDecimal orderFulfillmentRate;
        private BigDecimal returnRate;
        private BigDecimal averageFulfillmentTime;
        private Long activeProducts;
        private Long discontinuedProducts;
    }

    public enum MetricsStatus {
        DRAFT,
        PENDING_REVIEW,
        PUBLISHED,
        ARCHIVED
    }

    public BigDecimal calculateCustomerGrowthRate() {
        if (activeCustomers == null || activeCustomers == 0 || newCustomers == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf((double) newCustomers / activeCustomers * 100);
    }

    public BigDecimal calculateCustomerChurnRate() {
        if (activeCustomers == null || activeCustomers == 0 || churnedCustomers == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf((double) churnedCustomers / activeCustomers * 100);
    }

    public BigDecimal calculateRevenuePerCustomer() {
        if (activeCustomers == null || activeCustomers == 0 || totalRevenue == null) {
            return BigDecimal.ZERO;
        }
        return totalRevenue.divide(BigDecimal.valueOf(activeCustomers), 2, java.math.RoundingMode.HALF_UP);
    }

    public BigDecimal calculateOrdersPerCustomer() {
        if (activeCustomers == null || activeCustomers == 0 || totalOrders == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf((double) totalOrders / activeCustomers);
    }
}
