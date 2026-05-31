package com.gogidix.monitoring.alertmanagementservice.application.mapper;

import com.gogidix.monitoring.alertmanagementservice.application.dto.AlertHistoryResponseDto;
import com.gogidix.monitoring.alertmanagementservice.application.dto.AlertResponseDto;
import com.gogidix.monitoring.alertmanagementservice.application.dto.AlertRuleResponseDto;
import com.gogidix.monitoring.alertmanagementservice.domain.model.Alert;
import com.gogidix.monitoring.alertmanagementservice.domain.model.AlertHistory;
import com.gogidix.monitoring.alertmanagementservice.domain.model.AlertRule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for alert entities.
 */
@Component
public class AlertMapper {

    public AlertRuleResponseDto toAlertRuleResponseDto(AlertRule domain) {
        return AlertRuleResponseDto.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .name(domain.getName())
                .description(domain.getDescription())
                .enabled(domain.getEnabled())
                .serviceName(domain.getServiceName())
                .metricName(domain.getMetricName())
                .conditionType(domain.getConditionType() != null ? domain.getConditionType().name() : null)
                .threshold(domain.getThreshold())
                .operator(domain.getOperator() != null ? domain.getOperator().name() : null)
                .durationSeconds(domain.getDurationSeconds())
                .severity(domain.getSeverity() != null ? domain.getSeverity().name() : null)
                .notificationChannels(domain.getNotificationChannels() != null
                        ? domain.getNotificationChannels().stream()
                        .map(Enum::name)
                        .collect(Collectors.toList())
                        : null)
                .recipients(domain.getRecipients())
                .cooldownSeconds(domain.getCooldownSeconds())
                .messageTemplate(domain.getMessageTemplate())
                .metadata(domain.getMetadata())
                .tags(domain.getTags())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .createdBy(domain.getCreatedBy())
                .updatedBy(domain.getUpdatedBy())
                .build();
    }

    public AlertResponseDto toAlertResponseDto(Alert domain) {
        return AlertResponseDto.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .ruleId(domain.getRuleId())
                .ruleName(domain.getRuleName())
                .serviceName(domain.getServiceName())
                .metricName(domain.getMetricName())
                .severity(domain.getSeverity() != null ? domain.getSeverity().name() : null)
                .status(domain.getStatus() != null ? domain.getStatus().name() : null)
                .message(domain.getMessage())
                .triggerValue(domain.getTriggerValue())
                .threshold(domain.getThreshold())
                .triggeredAt(domain.getTriggeredAt())
                .acknowledgedAt(domain.getAcknowledgedAt())
                .acknowledgedBy(domain.getAcknowledgedBy())
                .acknowledgmentComment(domain.getAcknowledgmentComment())
                .resolvedAt(domain.getResolvedAt())
                .resolvedBy(domain.getResolvedBy())
                .resolutionComment(domain.getResolutionComment())
                .notificationStatus(domain.getNotificationStatus() != null ? domain.getNotificationStatus().name() : null)
                .notificationAttempts(domain.getNotificationAttempts())
                .context(domain.getContext())
                .tags(domain.getTags())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

    public AlertHistoryResponseDto toAlertHistoryResponseDto(AlertHistory domain) {
        return AlertHistoryResponseDto.builder()
                .id(domain.getId())
                .alertId(domain.getAlertId())
                .tenantId(domain.getTenantId())
                .stateChangeType(domain.getStateChangeType() != null ? domain.getStateChangeType().name() : null)
                .previousState(domain.getPreviousState())
                .newState(domain.getNewState())
                .changedBy(domain.getChangedBy())
                .comment(domain.getComment())
                .context(domain.getContext())
                .changedAt(domain.getChangedAt())
                .build();
    }
}
