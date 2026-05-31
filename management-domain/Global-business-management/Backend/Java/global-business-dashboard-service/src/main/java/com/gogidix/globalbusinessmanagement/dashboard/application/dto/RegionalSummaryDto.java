package com.gogidix.globalbusinessmanagement.dashboard.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for RegionalSummary domain model.
 * Used for API request/response operations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionalSummaryDto {

    private String id;

    @NotBlank(message = "Region code is required")
    private String regionCode;

    @NotBlank(message = "Region name is required")
    private String regionName;

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

    private Long newCustomers;

    private Long churnedCustomers;

    private BigDecimal marketPenetration;

    private BigDecimal customerSatisfactionScore;

    @NotEmpty(message = "Country metrics cannot be empty")
    private List<CountryMetricSummaryDto> countries;

    private List<ProductPerformanceDto> topPerformingProducts;

    private RegionalTrendsDto trends;

    private RegionalDemographicsDto demographics;

    private String baseCurrency;

    private String status;

    private String notes;

    private CalculatedMetrics calculatedMetrics;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CountryMetricSummaryDto {
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
    public static class ProductPerformanceDto {
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
    public static class RegionalTrendsDto {
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
    public static class RegionalDemographicsDto {
        private Long totalPopulation;
        private Long targetMarketSize;
        private BigDecimal urbanizationRate;
        private BigDecimal averageIncome;
        private String dominantLanguage;
        private List<String> supportedLanguages;
        private String primaryCurrency;
        private List<String> acceptedCurrencies;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CalculatedMetrics {
        private BigDecimal revenuePerCustomer;
        private BigDecimal ordersPerCustomer;
        private BigDecimal customerGrowthRate;
        private BigDecimal customerChurnRate;
        private BigDecimal netGrowthRate;
    }
}
