package com.gogidix.aiservices.aimonitoringservice.application.dto;

import com.gogidix.aiservices.aimonitoringservice.domain.model.AlertRuleStatus;
import com.gogidix.aiservices.aimonitoringservice.domain.model.ConditionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

/**
 * DTO for creating an alert rule.
 */
public record CreateAlertRuleRequestDto(
        @NotBlank(message = "name is required")
        String name,

        @NotBlank(message = "metric is required")
        String metric,

        @NotNull(message = "condition is required")
        ConditionType condition,

        @NotNull(message = "threshold is required")
        Double threshold,

        List<String> notificationChannels,

        @Positive(message = "minAlertInterval must be positive")
        Integer minAlertInterval
) {
    public CreateAlertRuleRequestDto {
        if (notificationChannels == null) {
            notificationChannels = List.of();
        }
    }
}
