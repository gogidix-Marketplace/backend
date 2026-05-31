package com.gogidix.monitoring.servicehealthservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceHealthStatusResponseDto {

    private String id;
    private String tenantId;
    private String serviceName;
    private String serviceType;
    private String status;
    private Object healthScore;
    private Object scoreComponents;
    private Map<String, Object> details;
    private Instant lastCheckAt;
    private Integer consecutiveFailures;
    private Instant lastSuccessAt;
    private Instant lastFailureAt;
    private Double averageResponseTime;
    private Double errorRate;
    private Double uptimePercentage;
    private String instanceId;
    private String host;
}
