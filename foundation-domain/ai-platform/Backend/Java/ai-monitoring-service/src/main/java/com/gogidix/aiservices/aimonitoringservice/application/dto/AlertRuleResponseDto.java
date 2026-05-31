package com.gogidix.aiservices.aimonitoringservice.application.dto;

import com.gogidix.aiservices.aimonitoringservice.domain.model.AlertRuleStatus;
import com.gogidix.aiservices.aimonitoringservice.domain.model.ConditionType;

import java.time.Instant;
import java.util.List;

/**
 * DTO for alert rule response.
 */
public record AlertRuleResponseDto(
        String id,
        String alertId,
        String tenantId,
        String name,
        String metric,
        ConditionType condition,
        Double threshold,
        List<String> notificationChannels,
        AlertRuleStatus status,
        Integer minAlertInterval,
        Instant createdAt,
        Instant updatedAt,
        Instant lastTriggeredAt,
        Long triggerCount
) {
    public static AlertRuleResponseDto from(com.gogidix.aiservices.aimonitoringservice.domain.model.AlertRule rule) {
        return new AlertRuleResponseDto(
                rule.getId(),
                rule.getAlertId(),
                rule.getTenantId(),
                rule.getName(),
                rule.getMetric(),
                rule.getCondition(),
                rule.getThreshold(),
                rule.getNotificationChannels(),
                rule.getStatus(),
                rule.getMinAlertInterval(),
                rule.getCreatedAt(),
                rule.getUpdatedAt(),
                rule.getLastTriggeredAt(),
                rule.getTriggerCount()
        );
    }
}
