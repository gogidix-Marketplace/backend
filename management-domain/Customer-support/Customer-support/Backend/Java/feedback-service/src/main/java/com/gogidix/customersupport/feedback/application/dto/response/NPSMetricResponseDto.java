package com.gogidix.customersupport.feedback.application.dto.response;

import com.gogidix.customersupport.feedback.domain.model.NPSMetric;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Response DTO for NPS Metric
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NPSMetricResponseDto {

    private String id;
    private String metricId;
    private String tenantId;
    private Instant periodStart;
    private Instant periodEnd;
    private NPSMetric.PeriodType periodType;
    private Integer npsScore;
    private Integer promotersCount;
    private Double promotersPercentage;
    private Integer passivesCount;
    private Double passivesPercentage;
    private Integer detractorsCount;
    private Double detractorsPercentage;
    private Integer totalResponses;
    private Double averageScore;
    private String countryCode;
    private String agentId;
    private String teamId;
    private String channel;
    private Integer previousNpsScore;
    private Integer scoreChange;
    private Instant createdAt;
    private Instant updatedAt;
}
