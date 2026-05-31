package com.gogidix.aiservices.aifrauddetectionservice.application.dto;

import com.gogidix.aiservices.aifrauddetectionservice.application.query.GetTenantStatisticsQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * DTO for Tenant Statistics results.
 * Contains aggregated fraud detection statistics.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantStatisticsDTO {

    private String tenantId;

    private GetTenantStatisticsQuery.StatisticsPeriod period;

    private Instant startDate;

    private Instant endDate;

    private Instant generatedAt;

    private AnalysisMetrics metrics;

    private RiskDistribution riskDistribution;

    private List<TrendData> trends;

    private ComparisonData comparisons;

    private Map<String, Object> additionalMetrics;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnalysisMetrics {
        private long totalAnalyses;
        private long fraudulentTransactions;
        private long suspiciousTransactions;
        private long legitimateTransactions;
        private double fraudRate;
        private double averageFraudScore;
        private double medianFraudScore;
        private long totalPatterns;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RiskDistribution {
        private long highRisk;
        private long mediumRisk;
        private long lowRisk;
        private Map<String, Long> distributionByType;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrendData {
        private String period;
        private long count;
        private double fraudScore;
        private double fraudRate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComparisonData {
        private double previousPeriodFraudRate;
        private double fraudRateChange;
        private double fraudRateChangePercent;
        private String trend; // INCREASING, DECREASING, STABLE
    }
}
