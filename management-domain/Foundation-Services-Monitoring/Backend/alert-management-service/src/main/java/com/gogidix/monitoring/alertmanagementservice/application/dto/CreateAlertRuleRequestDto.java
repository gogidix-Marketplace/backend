package com.gogidix.monitoring.alertmanagementservice.application.dto;

import com.gogidix.monitoring.alertmanagementservice.domain.model.AlertRule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO for creating an alert rule.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAlertRuleRequestDto {

    @NotBlank(message = "Rule name is required")
    private String name;

    private String description;

    @Builder.Default
    private Boolean enabled = true;

    private String serviceName;

    @NotBlank(message = "Metric name is required")
    private String metricName;

    @NotNull(message = "Condition type is required")
    private String conditionType;

    @NotNull(message = "Threshold is required")
    private Double threshold;

    @NotNull(message = "Operator is required")
    private String operator;

    @Positive(message = "Duration must be positive")
    private Integer durationSeconds;

    @NotNull(message = "Severity is required")
    private String severity;

    private List<String> notificationChannels;

    private List<String> recipients;

    @Builder.Default
    private Integer cooldownSeconds = 300;

    private String messageTemplate;

    private Map<String, Object> metadata;

    private Map<String, String> tags;
}
