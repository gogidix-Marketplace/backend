package com.gogidix.monitoring.alertmanagementservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Response DTO for alert history.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertHistoryResponseDto {

    private String id;
    private String alertId;
    private String tenantId;
    private String stateChangeType;
    private String previousState;
    private String newState;
    private String changedBy;
    private String comment;
    private Map<String, Object> context;
    private Instant changedAt;
}
