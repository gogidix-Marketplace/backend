package com.gogidix.transaction.monitoring.dto;

import com.gogidix.transaction.monitoring.domain.entity.TransactionMetrics.MetricType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricCreateRequest {

    @NotNull(message = "Transaction ID is required")
    private UUID transactionId;

    @NotNull(message = "Metric type is required")
    private MetricType metricType;

    @NotBlank(message = "Metric name is required")
    private String metricName;

    @NotNull(message = "Metric value is required")
    private BigDecimal metricValue;

    private String metricUnit;

    private BigDecimal thresholdWarning;

    private BigDecimal thresholdCritical;

    private Map<String, Object> metadata;
}
