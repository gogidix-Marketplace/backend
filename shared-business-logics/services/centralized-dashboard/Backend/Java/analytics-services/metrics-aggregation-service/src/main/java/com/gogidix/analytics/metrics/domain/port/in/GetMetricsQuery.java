package com.gogidix.analytics.metrics.domain.port.in;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Input port: Query for retrieving metrics.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMetricsQuery {

    private String metricName;

    private String sourceService;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    private String aggregationType;

    private Integer aggregationWindowSeconds;

    private Integer limit;

    private Integer offset;

    private String orderBy;

    private String groupBy;
}
