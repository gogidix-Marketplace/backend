package com.gogidix.customersupport.supportanalytics.application.mapper;

import com.gogidix.customersupport.supportanalytics.application.dto.ChannelPerformanceResponseDto;
import com.gogidix.customersupport.supportanalytics.domain.model.ChannelPerformance;
import org.springframework.stereotype.Component;

@Component
public class ChannelPerformanceMapper {

    public ChannelPerformanceResponseDto toResponseDto(ChannelPerformance entity) {
        return ChannelPerformanceResponseDto.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .channelType(ChannelPerformanceResponseDto.fromEntityType(entity.getChannelType()))
                .metricDate(entity.getMetricDate())
                .totalInteractions(entity.getTotalInteractions())
                .resolvedInteractions(entity.getResolvedInteractions())
                .pendingInteractions(entity.getPendingInteractions())
                .averageResponseTimeSeconds(entity.getAverageResponseTimeSeconds())
                .averageResolutionTimeSeconds(entity.getAverageResolutionTimeSeconds())
                .abandonmentRate(entity.getAbandonmentRate())
                .customerSatisfactionScore(entity.getCustomerSatisfactionScore())
                .firstContactResolutionRate(entity.getFirstContactResolutionRate())
                .peakHours(entity.getPeakHours())
                .agentUtilization(entity.getAgentUtilization())
                .averageHandleTimeSeconds(entity.getAverageHandleTimeSeconds())
                .totalHandleTimeSeconds(entity.getTotalHandleTimeSeconds())
                .activeAgents(entity.getActiveAgents())
                .resolutionRate(entity.calculateResolutionRate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
