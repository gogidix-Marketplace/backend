package com.gogidix.globalbusinessmanagement.dashboard.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Domain model representing detailed business metrics for a specific country.
 * This model captures country-specific performance indicators and market data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "country_metrics")
@CompoundIndex(name = "country_period_idx", def = "{'countryCode': 1, 'periodId': 1}", unique = true)
public class CountryMetrics {

    @Id
    private String id;

    @NotBlank(message = "Country code is required")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Country code must be a valid ISO 3166-1 alpha-2 code")
    @Indexed
    private String countryCode;

    @NotBlank(message = "Country name is required")
    private String countryName;

    @NotBlank(message = "Region code is required")
    @Indexed
    private String regionCode;

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

    @NotNull(message = "Tax amount is required")
    private BigDecimal taxAmount;

    private BigDecimal profitBeforeTax;

    private BigDecimal profitAfterTax;

    private BigDecimal profitMargin;

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

    @NotNull(message = "Returning customers is required")
    private Long returningCustomers;

    @NotNull(message = "Average order value is required")
    private BigDecimal averageOrderValue;

    @NotNull(message = "Customer lifetime value is required")
    private BigDecimal customerLifetimeValue;

    @NotNull(message = "Customer acquisition cost is required")
    private BigDecimal customerAcquisitionCost;

    @NotNull(message = "Market share is required")
    private BigDecimal marketShare;

    @NotNull(message = "Market size is required")
    private BigDecimal marketSize;

    @NotNull(message = "Market penetration is required")
    private BigDecimal marketPenetration;

    @NotNull(message = "Competitive index is required")
    private BigDecimal competitiveIndex;

    @NotNull(message = "Growth rate is required")
    private BigDecimal growthRate;

    @NotNull(message = "Year over year growth is required")
    private BigDecimal yearOverYearGrowth;

    @Valid
    private List<ProductLineMetrics> productLines;

    @Valid
    private List<CityMetrics> topCities;

    @Valid
    private SalesChannelMetrics salesChannels;

    @Valid
    private CustomerMetrics customerMetrics;

    @Valid
    private OperationalMetrics operationalMetrics;

    @Valid
    private EconomicIndicators economicIndicators;

    private String baseCurrency;

    @Builder.Default
    private MetricsStatus status = MetricsStatus.ACTIVE;

    private String dataSource;

    private DataQualityScore dataQuality;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private String updatedBy;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductLineMetrics {
        private String productLineCode;
        private String productLineName;
        private BigDecimal revenue;
        private BigDecimal revenueContribution;
        private Long unitsSold;
        private BigDecimal growthRate;
        private BigDecimal profitMargin;
        private Integer productCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CityMetrics {
        private String cityName;
        private String regionCode;
        private BigDecimal revenue;
        private Long customers;
        private Long orders;
        private BigDecimal revenueContribution;
        private BigDecimal growthRate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SalesChannelMetrics {
        private BigDecimal onlineRevenue;
        private BigDecimal offlineRevenue;
        private BigDecimal marketplaceRevenue;
        private BigDecimal b2bRevenue;
        private BigDecimal onlineContribution;
        private BigDecimal offlineContribution;
        private Long onlineOrders;
        private Long offlineOrders;
        private Long marketplaceOrders;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerMetrics {
        private BigDecimal averageAge;
        private Map<String, Long> genderDistribution;
        private Map<String, Long> ageGroupDistribution;
        private BigDecimal satisfactionScore;
        private BigDecimal netPromoterScore;
        private BigDecimal repeatPurchaseRate;
        private BigDecimal averageSessionDuration;
        private Integer averagePagesPerSession;
        private BigDecimal bounceRate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OperationalMetrics {
        private BigDecimal inventoryTurnover;
        private BigDecimal fulfillmentRate;
        private BigDecimal onTimeDeliveryRate;
        private BigDecimal returnRate;
        private BigDecimal refundRate;
        private Long averageFulfillmentTime;
        private Long averageResponseTime;
        private BigDecimal firstContactResolution;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EconomicIndicators {
        private BigDecimal gdpGrowth;
        private BigDecimal inflationRate;
        private BigDecimal unemploymentRate;
        private BigDecimal exchangeRate;
        private BigDecimal interestRate;
        private BigDecimal consumerConfidenceIndex;
        private BigDecimal purchasingPowerIndex;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataQualityScore {
        private BigDecimal completeness;
        private BigDecimal accuracy;
        private BigDecimal timeliness;
        private BigDecimal consistency;
        private BigDecimal overallScore;
    }

    public enum MetricsStatus {
        ACTIVE,
        INACTIVE,
        ARCHIVED,
        UNDER_REVIEW
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

    public BigDecimal calculateCustomerRetentionRate() {
        if (customerCount == null || customerCount == 0 || returningCustomers == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf((double) returningCustomers / customerCount * 100);
    }

    public BigDecimal calculateLtvToCacRatio() {
        if (customerAcquisitionCost == null || customerAcquisitionCost.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (customerLifetimeValue == null) {
            return BigDecimal.ZERO;
        }
        return customerLifetimeValue.divide(customerAcquisitionCost, 2, java.math.RoundingMode.HALF_UP);
    }

    public BigDecimal calculatePaybackPeriod() {
        if (customerAcquisitionCost == null || customerAcquisitionCost.compareTo(BigDecimal.ZERO) == 0 ||
            averageOrderValue == null || averageOrderValue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return customerAcquisitionCost.divide(averageOrderValue, 2, java.math.RoundingMode.HALF_UP);
    }

    public Long getOrderCount() {
        return orderCount;
    }

    public Long getCustomerCount() {
        return customerCount;
    }

    public BigDecimal getProfitMargin() {
        return profitMargin;
    }

    public BigDecimal getMarketShare() {
        return marketShare;
    }
}
