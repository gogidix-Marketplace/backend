package com.gogidix.management.executive.analytics.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMetricRequest {
    @NotNull(message = "Metric name is required")
    private String metricName;
    @NotNull(message = "Source domain is required")
    private String sourceDomain;
    private String sourceService;
    @NotNull(message = "Value is required")
    private Object value;
    private String unit;
    private Instant timestamp;
    private String granularity;
    private Map<String, String> dimensions;
    private Map<String, Object> metadata;
    private Double qualityScore;
    private Boolean isAggregate;
    private String parentMetricId;
}
