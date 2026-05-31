package com.gogidix.dashboard.gateway.api.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Aggregated dashboard data response DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AggregatedDashboardDataDto {

    private Map<String, Object> sagaStatistics;
    private Map<String, Object> chartData;
    private Map<String, Object> monitoringData;
    private Map<String, Object> onboardingData;
    private Map<String, Object> auditData;
    private Map<String, ServiceHealthDto> serviceHealth;
    private String timestamp;
    private String tenantId;
}
