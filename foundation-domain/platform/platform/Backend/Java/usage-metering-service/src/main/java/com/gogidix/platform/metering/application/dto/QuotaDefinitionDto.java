package com.gogidix.platform.metering.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for quota definition.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotaDefinitionDto {

    private String id;
    private String tenantId;
    private String quotaName;
    private String quotaDisplayName;
    private String metricName;
    private BigDecimal softLimit;
    private BigDecimal hardLimit;
    private String quotaPeriod;
    private String softLimitAction;
    private String hardLimitAction;
    private Integer[] notificationThresholds;
    private String[] notificationChannels;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
