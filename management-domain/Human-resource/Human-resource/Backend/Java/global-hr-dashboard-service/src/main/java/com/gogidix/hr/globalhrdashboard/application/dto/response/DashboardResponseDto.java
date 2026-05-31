package com.gogidix.hr.globalhrdashboard.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.hr.globalhrdashboard.domain.model.ComplianceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Response DTO for Dashboard Views
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dashboard response")
public class DashboardResponseDto {

    @Schema(description = "Dashboard period")
    private String period;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(description = "Last refreshed timestamp")
    private Instant lastRefreshed;

    @Schema(description = "Headcount summary")
    private HeadcountSummary headcountSummary;

    @Schema(description = "Compliance summary")
    private ComplianceSummary complianceSummary;

    @Schema(description = "Diversity summary")
    private DiversitySummary diversitySummary;

    @Schema(description = "Retention summary")
    private RetentionSummary retentionSummary;

    @Schema(description = "Regional breakdown")
    private Map<String, RegionalData> regionalBreakdown;

    @Schema(description = "Top metrics")
    private TopMetrics topMetrics;

    @Schema(description = "Alerts and warnings")
    private Alerts alerts;

    /**
     * Headcount summary
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Headcount summary")
    public static class HeadcountSummary {
        @Schema(description = "Total global headcount")
        private Integer totalHeadcount;

        @Schema(description = "Permanent employees")
        private Integer permanentEmployees;

        @Schema(description = "Contractors")
        private Integer contractors;

        @Schema(description = "Interns")
        private Integer interns;

        @Schema(description = "Year-over-year change")
        private Integer yearOverYearChange;

        @Schema(description = "Month-over-month change")
        private Integer monthOverMonthChange;

        @Schema(description = "Total countries")
        private Integer totalCountries;

        @Schema(description = "Total regions")
        private Integer totalRegions;
    }

    /**
     * Compliance summary
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Compliance summary")
    public static class ComplianceSummary {
        @Schema(description = "Average compliance score")
        private Double averageScore;

        @Schema(description = "Global compliance status")
        private ComplianceStatus globalStatus;

        @Schema(description = "Compliant countries count")
        private Long compliantCount;

        @Schema(description = "At-risk countries count")
        private Long atRiskCount;

        @Schema(description = "Non-compliant countries count")
        private Long nonCompliantCount;

        @Schema(description = "Countries with critical issues")
        private Long withCriticalIssues;
    }

    /**
     * Diversity summary
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Diversity summary")
    public static class DiversitySummary {
        @Schema(description = "Average gender diversity score")
        private Double averageGenderDiversityScore;

        @Schema(description = "Average national diversity score")
        private Double averageNationalDiversityScore;

        @Schema(description = "Average overall diversity score")
        private Double averageOverallDiversityScore;

        @Schema(description = "Global gender distribution")
        private Map<String, Integer> globalGenderDistribution;

        @Schema(description = "Women in leadership percentage")
        private Double womenInLeadershipPercentage;
    }

    /**
     * Retention summary
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Retention summary")
    public static class RetentionSummary {
        @Schema(description = "Average retention rate")
        private Double averageRetentionRate;

        @Schema(description = "Average turnover rate")
        private Double averageTurnoverRate;

        @Schema(description = "Average tenure in years")
        private Double averageTenure;

        @Schema(description = "Total departures")
        private Integer totalDepartures;

        @Schema(description = "Healthy retention countries count")
        private Long healthyRetentionCount;

        @Schema(description = "High turnover countries count")
        private Long highTurnoverCount;
    }

    /**
     * Regional data
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Regional data")
    public static class RegionalData {
        @Schema(description = "Region code")
        private String regionCode;

        @Schema(description = "Region name")
        private String regionName;

        @Schema(description = "Headcount")
        private Integer headcount;

        @Schema(description = "Compliance score")
        private Double complianceScore;

        @Schema(description = "Retention rate")
        private Double retentionRate;

        @Schema(description = "Diversity score")
        private Double diversityScore;
    }

    /**
     * Top metrics
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Top performing metrics")
    public static class TopMetrics {
        @Schema(description = "Top countries by headcount")
        private Map<String, Integer> topCountriesByHeadcount;

        @Schema(description = "Top countries by retention")
        private Map<String, Double> topCountriesByRetention;

        @Schema(description = "Top countries by diversity")
        private Map<String, Double> topCountriesByDiversity;

        @Schema(description = "Top countries by compliance")
        private Map<String, Double> topCountriesByCompliance;
    }

    /**
     * Alerts
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Dashboard alerts")
    public static class Alerts {
        @Schema(description = "Critical compliance issues")
        private Long criticalComplianceIssues;

        @Schema(description = "High turnover countries")
        private Long highTurnoverCountries;

        @Schema(description = "Low diversity countries")
        private Long lowDiversityCountries;

        @Schema(description = "At-risk countries")
        private Long atRiskCountries;

        @Schema(description = "Has critical alerts")
        public boolean hasCriticalAlerts() {
            return (criticalComplianceIssues != null && criticalComplianceIssues > 0) ||
                   (highTurnoverCountries != null && highTurnoverCountries > 0);
        }
    }
}
