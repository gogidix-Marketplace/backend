package com.gogidix.sales.countrydashboard.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Comparison Data Response DTO
 * Used for API responses with comparison data (YoY, MoM, QoQ)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComparisonDataResponseDto {

    private YearOverYearDto yearOverYear;
    private MonthOverMonthDto monthOverMonth;
    private QuarterOverQuarterDto quarterOverQuarter;
    private RegionalComparisonDto regionalComparison;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class YearOverYearDto {
        private BigDecimal currentYearRevenue;
        private BigDecimal previousYearRevenue;
        private BigDecimal growthPercentage;
        private BigDecimal variance;
        private Integer currentYearDeals;
        private Integer previousYearDeals;
        private BigDecimal dealsGrowthPercentage;
        private Instant comparisonDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthOverMonthDto {
        private BigDecimal currentMonthRevenue;
        private BigDecimal previousMonthRevenue;
        private BigDecimal growthPercentage;
        private BigDecimal variance;
        private Integer currentMonthDeals;
        private Integer previousMonthDeals;
        private BigDecimal dealsGrowthPercentage;
        private Instant comparisonDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuarterOverQuarterDto {
        private BigDecimal currentQuarterRevenue;
        private BigDecimal previousQuarterRevenue;
        private BigDecimal growthPercentage;
        private BigDecimal variance;
        private Integer currentQuarterDeals;
        private Integer previousQuarterDeals;
        private BigDecimal dealsGrowthPercentage;
        private Instant comparisonDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionalComparisonDto {
        private String region;
        private List<CountryMetricDto> countryMetrics;
        private BigDecimal regionalAverage;
        private String rank;
        private BigDecimal percentile;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CountryMetricDto {
        private String countryCode;
        private String countryName;
        private BigDecimal revenue;
        private BigDecimal achievement;
        private BigDecimal growth;
        private Integer rank;
    }
}
