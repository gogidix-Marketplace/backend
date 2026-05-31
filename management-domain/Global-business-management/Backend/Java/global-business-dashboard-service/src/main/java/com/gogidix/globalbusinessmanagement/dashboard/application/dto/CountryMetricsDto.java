package com.gogidix.globalbusinessmanagement.dashboard.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
 * DTO for CountryMetrics domain model.
 * Used for API request/response operations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryMetricsDto {

    private String id;

    @NotBlank(message = "Country code is required")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Country code must be a valid ISO 3166-1 alpha-2 code")
    private String countryCode;

    @NotBlank(message = "Country name is required")
    private String countryName;

    @NotBlank(message = "Region code is required")
    private String regionCode;

    @NotBlank(message = "Period identifier is required")
    private String periodId;

    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
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

    private Long newCustomers;

    private Long churnedCustomers;

    private Long returningCustomers;

    @NotNull(message = "Average order value is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Average order value must be non-negative")
    private BigDecimal averageOrderValue;

    private BigDecimal customerLifetimeValue;

    private BigDecimal customerAcquisitionCost;

    private BigDecimal marketShare;

    private BigDecimal marketSize;

    private BigDecimal marketPenetration;

    private BigDecimal competitiveIndex;

    private BigDecimal growthRate;

    private BigDecimal yearOverYearGrowth;

    private List<ProductLineMetricsDto> productLines;

    private List<CityMetricsDto> topCities;

    private SalesChannelMetricsDto salesChannels;

    private CustomerMetricsDto customerMetrics;

    private OperationalMetricsDto operationalMetrics;

    private EconomicIndicatorsDto economicIndicators;

    private String baseCurrency;

    private String status;

    private String dataSource;

    private DataQualityScoreDto dataQuality;

    private CalculatedMetrics calculatedMetrics;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductLineMetricsDto {
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
    public static class CityMetricsDto {
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
    public static class SalesChannelMetricsDto {
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
    public static class CustomerMetricsDto {
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
    public static class OperationalMetricsDto {
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
    public static class EconomicIndicatorsDto {
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
    public static class DataQualityScoreDto {
        private BigDecimal completeness;
        private BigDecimal accuracy;
        private BigDecimal timeliness;
        private BigDecimal consistency;
        private BigDecimal overallScore;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CalculatedMetrics {
        private BigDecimal customerGrowthRate;
        private BigDecimal customerChurnRate;
        private BigDecimal customerRetentionRate;
        private BigDecimal ltvToCacRatio;
        private BigDecimal paybackPeriod;
    }
}
