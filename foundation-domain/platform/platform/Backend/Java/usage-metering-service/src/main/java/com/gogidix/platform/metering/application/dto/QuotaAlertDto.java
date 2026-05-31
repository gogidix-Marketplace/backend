package com.gogidix.platform.metering.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for quota alert.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotaAlertDto {

    private String id;
    private String tenantId;
    private String quotaId;
    private String alertType;
    private String severity;
    private BigDecimal currentUsage;
    private BigDecimal limitValue;
    private BigDecimal percentage;
    private String message;
    private String recommendedAction;
    private boolean isAcknowledged;
    private String acknowledgedBy;
    private LocalDateTime acknowledgedAt;
    private boolean notificationSent;
    private String[] notificationChannels;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private Map<String, Object> metadata;
    private LocalDateTime createdAt;
}
