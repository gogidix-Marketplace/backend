package com.gogidix.platform.metering.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for usage record.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsageRecordDto {

    private String id;
    private String tenantId;
    private String metricName;
    private String metricType;
    private BigDecimal quantity;
    private String unit;
    private LocalDateTime eventTime;
    private LocalDateTime receivedAt;
    private Map<String, Object> dimensions;
    private String serviceName;
    private String resourceId;
    private String userId;
    private String correlationId;
    private Map<String, Object> metadata;
}
