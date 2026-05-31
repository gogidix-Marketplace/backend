package com.gogidix.globalbusinessmanagement.dashboard.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Domain model representing a summary of business activities for a specific region.
 * Regions can be continents, sub-continents, or custom business regions.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "regional_summaries")
public class RegionalSummary {

    @Id
    private String id;

    @NotBlank(message = "Region code is required")
    @Indexed
    private String regionCode;

    @NotBlank(message = "Region name is required")
    private String regionName;

    @NotBlank(message = "Period identifier is required")
    @Indexed
    private String periodId;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    @NotNull(message = "Revenue is required")
    private BigDecimal revenue;

    @NotNull(message = "Expenses is required")
    private BigDecimal expenses;

    private BigDecimal profit;

    private BigDecimal profitMargin;

    @NotNull(message = "Growth rate is required")
    private BigDecimal growthRate;

    @NotNull(message = "Order count is required")
    @Positive(message = "Order count must be positive")
    private Long orderCount;

    @NotNull(message = "Customer count is required")
    @Positive(message = "Customer count must be positive")
    private Long customerCount;

    @NotNull(message = "New customers is required")
    private Long newCustomers;

    @NotNull(message = "Churned customers is required")
    private Long churnedCustomers;

    @NotNull(message = "Market penetration is required")
    private BigDecimal marketPenetration;

    @NotNull(message = "Customer satisfaction score is required")
    private BigDecimal customerSatisfactionScore;

    @NotEmpty(message = "Country metrics cannot be empty")
    @Valid
    private List<CountryMetricSummary> countries;

    @NotNull(message = "Top performing products is required")
    @Valid
    private List<ProductPerformance> topPerformingProducts;

    @NotNull(message = "Regional trends is required")
    private RegionalTrends trends;

    private RegionalDemographics demographics;

    private String baseCurrency;

    @Builder.Default
    private SummaryStatus status = SummaryStatus.ACTIVE;

    private String notes;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CountryMetricSummary {
        private String countryCode;
        private String countryName;
        private BigDecimal revenue;
        private BigDecimal revenueContribution;
        private BigDecimal growthRate;
        private Long orders;
        private Long customers;
        private BigDecimal profitMargin;
        private BigDecimal marketShare;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductPerformance {
        private String productCode;
        private String productName;
        private String category;
        private BigDecimal revenue;
        private Long unitsSold;
        private BigDecimal growthRate;
        private BigDecimal marketShare;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionalTrends {
        private BigDecimal revenueTrend;
        private String revenueTrendDirection;
        private BigDecimal customerTrend;
        private String customerTrendDirection;
        private BigDecimal orderTrend;
        private String orderTrendDirection;
        private String topGrowthDriver;
        private String topRiskFactor;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionalDemographics {
        private Long totalPopulation;
        private Long targetMarketSize;
        private BigDecimal urbanizationRate;
        private BigDecimal averageIncome;
        private String dominantLanguage;
        private List<String> supportedLanguages;
        private String primaryCurrency;
        private List<String> acceptedCurrencies;
    }

    public enum SummaryStatus {
        ACTIVE,
        INACTIVE,
        ARCHIVED
    }

    public BigDecimal calculateRevenuePerCustomer() {
        if (customerCount == null || customerCount == 0 || revenue == null) {
            return BigDecimal.ZERO;
        }
        return revenue.divide(BigDecimal.valueOf(customerCount), 2, java.math.RoundingMode.HALF_UP);
    }

    public BigDecimal calculateOrdersPerCustomer() {
        if (customerCount == null || customerCount == 0 || orderCount == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf((double) orderCount / customerCount);
    }

    public BigDecimal calculateCustomerGrowthRate() {
        if (customerCount == null || customerCount == 0 || newCustomers == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf((double) newCustomers / customerCount * 100);
    }

    public BigDecimal calculateCustomerChurnRate() {
        if (customerCount == null || customerCount == 0 || churnedCustomers == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf((double) churnedCustomers / customerCount * 100);
    }

    public BigDecimal calculateNetGrowthRate() {
        BigDecimal growthRateValue = calculateCustomerGrowthRate();
        BigDecimal churnRateValue = calculateCustomerChurnRate();
        return growthRateValue.subtract(churnRateValue);
    }

    public BigDecimal getExpenses() {
        return expenses;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }
}
