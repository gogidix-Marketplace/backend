package com.gogidix.monitoring.monitoringdataservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO for collecting a single metric.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectMetricRequestDto {

    @NotBlank(message = "Service name is required")
    private String serviceName;

    private String serviceType;

    @NotBlank(message = "Metric name is required")
    private String metricName;

    @NotNull(message = "Metric value is required")
    private Double value;

    private String unit;

    private String metricType;

    private Map<String, String> tags;

    private String host;

    private String instanceId;

    private String correlationId;

    private String category;

    private Long timestamp;
}
