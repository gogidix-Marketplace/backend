package com.gogidix.monitoring.alertmanagementservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Response DTO for alert rule.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertRuleResponseDto {

    private String id;
    private String tenantId;
    private String name;
    private String description;
    private Boolean enabled;
    private String serviceName;
    private String metricName;
    private String conditionType;
    private Double threshold;
    private String operator;
    private Integer durationSeconds;
    private String severity;
    private List<String> notificationChannels;
    private List<String> recipients;
    private Integer cooldownSeconds;
    private String messageTemplate;
    private Map<String, Object> metadata;
    private Map<String, String> tags;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}
