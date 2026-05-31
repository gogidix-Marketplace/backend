package com.gogidix.management.executive.analytics.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;
/**
 * DTO for analytics query requests
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsQueryRequest {
    @NotBlank(message = "Metric ID is required")
    private String metricId;
    @NotNull(message = "Start date is required")
    private Instant startDate;
    @NotNull(message = "End date is required")
    private Instant endDate;
    private List<String> dashboardIds;
    private List<String> dimensions;
    private String aggregation;
    private Integer limit;
    private String sortBy;
    private String sortOrder;
}
