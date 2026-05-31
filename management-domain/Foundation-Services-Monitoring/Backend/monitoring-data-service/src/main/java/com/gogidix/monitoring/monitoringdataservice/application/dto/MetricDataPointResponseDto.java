package com.gogidix.monitoring.monitoringdataservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Response DTO for metric data point.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricDataPointResponseDto {

    private String id;
    private String tenantId;
    private String serviceName;
    private String serviceType;
    private String metricName;
    private Double value;
    private String unit;
    private String metricType;
    private Map<String, String> tags;
    private Instant timestamp;
    private String host;
    private String instanceId;
    private String correlationId;
    private String category;
    private Instant createdAt;
}
