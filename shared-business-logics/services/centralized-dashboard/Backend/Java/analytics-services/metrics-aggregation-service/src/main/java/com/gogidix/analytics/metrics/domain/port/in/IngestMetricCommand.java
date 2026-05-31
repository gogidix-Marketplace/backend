package com.gogidix.analytics.metrics.domain.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Input port: Command to ingest a metric data point.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngestMetricCommand {

    @NotBlank(message = "Metric name is required")
    private String metricName;

    @NotNull(message = "Metric type is required")
    private com.gogidix.analytics.metrics.domain.model.MetricDataPoint.MetricType metricType;

    @NotNull(message = "Metric value is required")
    private BigDecimal metricValue;

    private String unit;

    private LocalDateTime timestamp;

    @NotBlank(message = "Source service is required")
    private String sourceService;

    private Map<String, String> tags;

    private Map<String, Object> dimensions;

    private String aggregationLevel;
}
