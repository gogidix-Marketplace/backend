package com.gogidix.monitoring.servicehealthservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO for overall health summary.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceHealthSummaryDto {

    private Long totalServices;
    private Long healthyServices;
    private Long degradedServices;
    private Long unhealthyServices;
    private Long downServices;
    private Long unknownServices;
    private Double averageHealthScore;
    private Map<String, Long> statusBreakdown;
    private Map<String, Long> serviceTypeBreakdown;
}
