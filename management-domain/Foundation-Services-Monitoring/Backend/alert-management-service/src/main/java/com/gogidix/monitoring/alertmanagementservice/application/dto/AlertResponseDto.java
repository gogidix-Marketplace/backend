package com.gogidix.monitoring.alertmanagementservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Response DTO for alert.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertResponseDto {

    private String id;
    private String tenantId;
    private String ruleId;
    private String ruleName;
    private String serviceName;
    private String metricName;
    private String severity;
    private String status;
    private String message;
    private Double triggerValue;
    private Double threshold;
    private Instant triggeredAt;
    private Instant acknowledgedAt;
    private String acknowledgedBy;
    private String acknowledgmentComment;
    private Instant resolvedAt;
    private String resolvedBy;
    private String resolutionComment;
    private String notificationStatus;
    private Integer notificationAttempts;
    private Map<String, Object> context;
    private Map<String, String> tags;
    private Instant createdAt;
    private Instant updatedAt;
}
