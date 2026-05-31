package com.gogidix.hr.globalhrdashboard.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Response DTO for Retention Metrics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Retention metric response")
public class RetentionResponseDto {

    @Schema(description = "Retention metric ID")
    private String id;

    @Schema(description = "ISO country code")
    private String countryCode;

    @Schema(description = "Country name")
    private String countryName;

    @Schema(description = "Region code")
    private String regionCode;

    @Schema(description = "Period")
    private String period;

    @Schema(description = "Retention rate percentage")
    private Double retentionRate;

    @Schema(description = "Turnover rate percentage")
    private Double turnoverRate;

    @Schema(description = "Total employees")
    private Integer totalEmployees;

    @Schema(description = "Voluntary departures")
    private Integer voluntaryDepartures;

    @Schema(description = "Involuntary departures")
    private Integer involuntaryDepartures;

    @Schema(description = "Average tenure in years")
    private Double avgTenure;

    @Schema(description = "Median tenure in years")
    private Double medianTenure;

    @Schema(description = "Departure reasons")
    private List<DepartureReasonDto> departureReasons;

    @Schema(description = "New hire retention rate")
    private Double newHireRetentionRate;

    @Schema(description = "Total new hires")
    private Integer totalNewHires;

    @Schema(description = "Retained new hires")
    private Integer retainedNewHires;

    @Schema(description = "Top performer retention rate")
    private Double topPerformerRetentionRate;

    @Schema(description = "Total top performers")
    private Integer totalTopPerformers;

    @Schema(description = "Retained top performers")
    private Integer retainedTopPerformers;

    @Schema(description = "Promotion rate")
    private Double promotionRate;

    @Schema(description = "Total promotions")
    private Integer totalPromotions;

    @Schema(description = "Internal mobility rate")
    private Double internalMobilityRate;

    @Schema(description = "Is active")
    private Boolean isActive;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(description = "Last updated timestamp")
    private Instant lastUpdated;

    @Schema(description = "Notes")
    private String notes;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(description = "Created at timestamp")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(description = "Updated at timestamp")
    private Instant updatedAt;

    @Schema(description = "Total departures")
    public Integer getTotalDepartures() {
        int voluntary = voluntaryDepartures != null ? voluntaryDepartures : 0;
        int involuntary = involuntaryDepartures != null ? involuntaryDepartures : 0;
        return voluntary + involuntary;
    }

    @Schema(description = "Voluntary turnover rate")
    public Double getVoluntaryTurnoverRate() {
        if (totalEmployees == null || totalEmployees == 0) {
            return 0.0;
        }
        int voluntary = voluntaryDepartures != null ? voluntaryDepartures : 0;
        return ((double) voluntary / totalEmployees) * 100;
    }

    @Schema(description = "Involuntary turnover rate")
    public Double getInvoluntaryTurnoverRate() {
        if (totalEmployees == null || totalEmployees == 0) {
            return 0.0;
        }
        int involuntary = involuntaryDepartures != null ? involuntaryDepartures : 0;
        return ((double) involuntary / totalEmployees) * 100;
    }

    @Schema(description = "Has healthy retention")
    public boolean hasHealthyRetention() {
        return retentionRate != null && retentionRate >= 80.0;
    }

    @Schema(description = "Has high turnover")
    public boolean hasHighTurnover() {
        return turnoverRate != null && turnoverRate > 20.0;
    }

    @Schema(description = "Has good tenure")
    public boolean hasGoodTenure() {
        return avgTenure != null && avgTenure >= 2.0;
    }

    /**
     * DTO for departure reasons
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Departure reason details")
    public static class DepartureReasonDto {

        @Schema(description = "Reason description")
        private String reason;

        @Schema(description = "Category")
        private String category;

        @Schema(description = "Count")
        private Integer count;

        @Schema(description = "Percentage")
        private Double percentage;

        @Schema(description = "Is preventable")
        private boolean preventable;

        @Schema(description = "Trend")
        private String trend;

        public enum Category {
            @Schema(description = "Compensation related")
            COMPENSATION,
            @Schema(description = "Career growth related")
            CAREER_GROWTH,
            @Schema(description = "Work life balance related")
            WORK_LIFE_BALANCE,
            @Schema(description = "Management related")
            MANAGEMENT,
            @Schema(description = "Company culture related")
            COMPANY_CULTURE,
            @Schema(description = "Relocation related")
            RELOCATION,
            @Schema(description = "Personal reasons")
            PERSONAL,
            @Schema(description = "Retirement")
            RETIREMENT,
            @Schema(description = "Other")
            OTHER
        }
    }
}
