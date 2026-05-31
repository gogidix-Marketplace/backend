package com.gogidix.analytics.metrics.domain.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Input port: Command to create a metric alert.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMetricAlertCommand {

    @NotBlank(message = "Alert name is required")
    private String alertName;

    @NotBlank(message = "Metric name is required")
    private String metricName;

    @NotNull(message = "Condition type is required")
    private com.gogidix.analytics.metrics.domain.model.MetricAlert.ConditionType conditionType;

    private BigDecimal thresholdValue;

    private Integer evaluationWindowSeconds;

    @Builder.Default
    private com.gogidix.analytics.metrics.domain.model.MetricAlert.AlertSeverity severity =
        com.gogidix.analytics.metrics.domain.model.MetricAlert.AlertSeverity.WARNING;

    private String notificationChannels;

    private String description;

    private Integer cooldownSeconds;

    private String createdBy;
}
