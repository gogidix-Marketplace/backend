package com.gogidix.hr.globalhrdashboard.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.hr.globalhrdashboard.domain.model.MetricTrend;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Response DTO for Headcount Metrics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Country headcount response")
public class HeadcountResponseDto {

    @Schema(description = "Headcount ID")
    private String id;

    @Schema(description = "ISO country code")
    private String countryCode;

    @Schema(description = "Country name")
    private String countryName;

    @Schema(description = "Region code")
    private String regionCode;

    @Schema(description = "Total headcount")
    private Integer totalHeadcount;

    @Schema(description = "Permanent employees")
    private Integer permanentEmployees;

    @Schema(description = "Contractors")
    private Integer contractors;

    @Schema(description = "Interns")
    private Integer interns;

    @Schema(description = "Department")
    private String department;

    @Schema(description = "Period")
    private String period;

    @Schema(description = "Year-over-year change")
    private Integer yoyChange;

    @Schema(description = "Month-over-month change")
    private Integer momChange;

    @Schema(description = "Quarter-over-quarter change")
    private Integer qoqChange;

    @Schema(description = "Female percentage")
    private Double femalePercentage;

    @Schema(description = "Male percentage")
    private Double malePercentage;

    @Schema(description = "Other gender percentage")
    private Double otherGenderPercentage;

    @Schema(description = "Average age")
    private Double avgAge;

    @Schema(description = "Average tenure in years")
    private Double avgTenureYears;

    @Schema(description = "Is active")
    private Boolean isActive;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(description = "Last updated timestamp")
    private Instant lastUpdated;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(description = "Created at timestamp")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(description = "Updated at timestamp")
    private Instant updatedAt;

    @Schema(description = "Calculated non-permanent staff")
    public Integer getCalculatedNonPermanent() {
        int permanent = permanentEmployees != null ? permanentEmployees : 0;
        int total = totalHeadcount != null ? totalHeadcount : 0;
        return Math.max(0, total - permanent);
    }

    @Schema(description = "Contractor percentage")
    public Double getContractorPercentage() {
        if (totalHeadcount == null || totalHeadcount == 0) {
            return 0.0;
        }
        int contract = contractors != null ? contractors : 0;
        return ((double) contract / totalHeadcount) * 100;
    }

    @Schema(description = "Intern percentage")
    public Double getInternPercentage() {
        if (totalHeadcount == null || totalHeadcount == 0) {
            return 0.0;
        }
        int intern = interns != null ? interns : 0;
        return ((double) intern / totalHeadcount) * 100;
    }

    @Schema(description = "Permanent percentage")
    public Double getPermanentPercentage() {
        if (totalHeadcount == null || totalHeadcount == 0) {
            return 0.0;
        }
        int permanent = permanentEmployees != null ? permanentEmployees : 0;
        return ((double) permanent / totalHeadcount) * 100;
    }

    @Schema(description = "Year-over-year trend")
    public MetricTrend getYoyTrend() {
        if (yoyChange == null || yoyChange == 0) {
            return MetricTrend.STABLE;
        }
        return yoyChange > 0 ? MetricTrend.UP : MetricTrend.DOWN;
    }

    @Schema(description = "Month-over-month trend")
    public MetricTrend getMomTrend() {
        if (momChange == null || momChange == 0) {
            return MetricTrend.STABLE;
        }
        return momChange > 0 ? MetricTrend.UP : MetricTrend.DOWN;
    }

    @Schema(description = "Quarter-over-quarter trend")
    public MetricTrend getQoqTrend() {
        if (qoqChange == null || qoqChange == 0) {
            return MetricTrend.STABLE;
        }
        return qoqChange > 0 ? MetricTrend.UP : MetricTrend.DOWN;
    }

    @Schema(description = "Is headcount growing")
    public boolean isHeadcountGrowing() {
        if (yoyChange != null) {
            return yoyChange > 0;
        }
        if (momChange != null) {
            return momChange > 0;
        }
        return false;
    }
}
