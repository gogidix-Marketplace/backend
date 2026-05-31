package com.gogidix.customersupport.supportanalytics.application.mapper;

import com.gogidix.customersupport.supportanalytics.application.dto.AnalyticsReportRequestDto;
import com.gogidix.customersupport.supportanalytics.application.dto.AnalyticsReportResponseDto;
import com.gogidix.customersupport.supportanalytics.domain.model.AnalyticsReport;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AnalyticsReportMapper {

    public AnalyticsReport toEntity(AnalyticsReportRequestDto dto, String tenantId) {
        AnalyticsReport report = AnalyticsReport.create(
                tenantId,
                dto.getReportName(),
                dto.toEntityType(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getGeneratedBy()
        );

        if (dto.getAgentPerformanceMetrics() != null) {
            Map<String, AnalyticsReport.AgentPerformanceMetric> agentMetrics = dto.getAgentPerformanceMetrics().entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> AnalyticsReport.AgentPerformanceMetric.builder()
                                    .agentId(e.getValue().getAgentId())
                                    .agentName(e.getValue().getAgentName())
                                    .ticketsHandled(e.getValue().getTicketsHandled())
                                    .ticketsResolved(e.getValue().getTicketsResolved())
                                    .averageResolutionTime(e.getValue().getAverageResolutionTime())
                                    .averageResponseTime(e.getValue().getAverageResponseTime())
                                    .satisfactionScore(e.getValue().getSatisfactionScore())
                                    .escalations(e.getValue().getEscalations())
                                    .build()
                    ));
            report.setAgentPerformanceMetrics(agentMetrics);
        }

        if (dto.getChannelPerformance() != null) {
            Map<String, AnalyticsReport.ChannelPerformanceMetric> channelMetrics = dto.getChannelPerformance().entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> AnalyticsReport.ChannelPerformanceMetric.builder()
                                    .channel(e.getValue().getChannel())
                                    .ticketsReceived(e.getValue().getTicketsReceived())
                                    .ticketsResolved(e.getValue().getTicketsResolved())
                                    .averageResolutionTime(e.getValue().getAverageResolutionTime())
                                    .satisfactionScore(e.getValue().getSatisfactionScore())
                                    .build()
                    ));
            report.setChannelPerformance(channelMetrics);
        }

        return report;
    }

    public AnalyticsReportResponseDto toResponseDto(AnalyticsReport entity) {
        Map<String, AnalyticsReportResponseDto.AgentPerformanceMetricDto> agentMetrics = null;
        if (entity.getAgentPerformanceMetrics() != null) {
            agentMetrics = entity.getAgentPerformanceMetrics().entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> AnalyticsReportResponseDto.AgentPerformanceMetricDto.builder()
                                    .agentId(e.getValue().getAgentId())
                                    .agentName(e.getValue().getAgentName())
                                    .ticketsHandled(e.getValue().getTicketsHandled())
                                    .ticketsResolved(e.getValue().getTicketsResolved())
                                    .averageResolutionTime(e.getValue().getAverageResolutionTime())
                                    .averageResponseTime(e.getValue().getAverageResponseTime())
                                    .satisfactionScore(e.getValue().getSatisfactionScore())
                                    .escalations(e.getValue().getEscalations())
                                    .build()
                    ));
        }

        Map<String, AnalyticsReportResponseDto.ChannelPerformanceMetricDto> channelMetrics = null;
        if (entity.getChannelPerformance() != null) {
            channelMetrics = entity.getChannelPerformance().entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> AnalyticsReportResponseDto.ChannelPerformanceMetricDto.builder()
                                    .channel(e.getValue().getChannel())
                                    .ticketsReceived(e.getValue().getTicketsReceived())
                                    .ticketsResolved(e.getValue().getTicketsResolved())
                                    .averageResolutionTime(e.getValue().getAverageResolutionTime())
                                    .satisfactionScore(e.getValue().getSatisfactionScore())
                                    .build()
                    ));
        }

        return AnalyticsReportResponseDto.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .reportName(entity.getReportName())
                .reportType(AnalyticsReportResponseDto.fromEntityType(entity.getReportType()))
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .totalTickets(entity.getTotalTickets())
                .resolvedTickets(entity.getResolvedTickets())
                .openTickets(entity.getOpenTickets())
                .escalatedTickets(entity.getEscalatedTickets())
                .averageResolutionTimeMinutes(entity.getAverageResolutionTimeMinutes())
                .averageResponseTimeMinutes(entity.getAverageResponseTimeMinutes())
                .customerSatisfactionScore(entity.getCustomerSatisfactionScore())
                .firstContactResolutionRate(entity.getFirstContactResolutionRate())
                .agentPerformanceMetrics(agentMetrics)
                .channelPerformance(channelMetrics)
                .generatedAt(entity.getGeneratedAt())
                .generatedBy(entity.getGeneratedBy())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntityFromDto(AnalyticsReportRequestDto dto, AnalyticsReport entity) {
        entity.setReportName(dto.getReportName());
        entity.setReportType(dto.toEntityType());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setGeneratedBy(dto.getGeneratedBy());
        entity.updateTimestamp();
    }
}
