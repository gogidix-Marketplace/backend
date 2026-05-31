package com.gogidix.monitoring.monitoringdataservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * DTO for querying metrics.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryMetricsRequestDto {

    @NotBlank(message = "Service name is required")
    private String serviceName;

    private String metricName;

    @NotNull(message = "Start time is required")
    private Instant startTime;

    @NotNull(message = "End time is required")
    private Instant endTime;

    private String aggregation;

    private Long intervalSeconds;
}
