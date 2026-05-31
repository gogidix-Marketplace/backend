package com.gogidix.hr.globalhrdashboard.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.hr.globalhrdashboard.domain.model.MetricCategory;
import com.gogidix.hr.globalhrdashboard.domain.model.ExecutiveLevel;
import com.gogidix.hr.globalhrdashboard.domain.model.MetricTrend;
import com.gogidix.hr.globalhrdashboard.domain.model.AggregationLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Response DTO for Global Workforce Metrics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Global workforce metric response")
public class MetricResponseDto {

    @Schema(description = "Metric ID")
    private String id;

    @Schema(description = "Metric name")
    private String metricName;

    @Schema(description = "Metric category")
    private MetricCategory metricCategory;

    @Schema(description = "Executive level")
    private ExecutiveLevel executiveLevel;

    @Schema(description = "Current value")
    private Double value;

    @Schema(description = "Previous value")
    private Double previousValue;

    @Schema(description = "Target value")
    private Double targetValue;

    @Schema(description = "Period (e.g., 2024-01)")
    private String period;

    @Schema(description = "Trend direction")
    private MetricTrend trend;

    @Schema(description = "Aggregation level")
    private AggregationLevel aggregationLevel;

    @Schema(description = "Regional breakdown")
    private Map<String, Object> regionalBreakdown;

    @Schema(description = "Additional metadata")
    private Map<String, Object> metadata;

    @Schema(description = "Is metric active")
    private Boolean isActive;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(description = "Last aggregation timestamp")
    private Instant lastAggregated;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(description = "Created at timestamp")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(description = "Updated at timestamp")
    private Instant updatedAt;

    @Schema(description = "Variance from previous value")
    public Double getVariance() {
        if (value != null && previousValue != null) {
            return value - previousValue;
        }
        return 0.0;
    }

    @Schema(description = "Variance percentage from previous value")
    public Double getVariancePercent() {
        if (value != null && previousValue != null && previousValue != 0) {
            return ((value - previousValue) / previousValue) * 100;
        }
        return 0.0;
    }

    @Schema(description = "Target achievement percentage")
    public Double getTargetAchievement() {
        if (targetValue != null && targetValue != 0 && value != null) {
            return (value / targetValue) * 100;
        }
        return null;
    }
}
