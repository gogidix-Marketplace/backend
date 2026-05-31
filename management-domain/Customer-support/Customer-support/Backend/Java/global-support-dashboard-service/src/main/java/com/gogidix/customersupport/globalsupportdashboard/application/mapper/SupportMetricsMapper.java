package com.gogidix.customersupport.globalsupportdashboard.application.mapper;

import com.gogidix.customersupport.globalsupportdashboard.application.dto.AgentPerformanceDto;
import com.gogidix.customersupport.globalsupportdashboard.application.dto.RegionalMetricsDto;
import com.gogidix.customersupport.globalsupportdashboard.application.dto.SupportMetricsDto;
import com.gogidix.customersupport.globalsupportdashboard.domain.model.AgentPerformance;
import com.gogidix.customersupport.globalsupportdashboard.domain.model.RegionalMetrics;
import com.gogidix.customersupport.globalsupportdashboard.domain.model.SupportMetrics;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper for converting between domain models and DTOs
 */
@Component
public class SupportMetricsMapper {

    /**
     * Convert SupportMetrics entity to DTO
     */
    public SupportMetricsDto toDto(SupportMetrics entity) {
        if (entity == null) {
            return null;
        }

        return SupportMetricsDto.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .totalTickets(entity.getTotalTickets())
                .openTickets(entity.getOpenTickets())
                .inProgressTickets(entity.getInProgressTickets())
                .resolvedTickets(entity.getResolvedTickets())
                .closedTickets(entity.getClosedTickets())
                .escalatedTickets(entity.getEscalatedTickets())
                .avgFirstResponseTime(entity.getAvgFirstResponseTime())
                .avgResolutionTime(entity.getAvgResolutionTime())
                .avgFirstResponseTimeMinutes(entity.getAvgFirstResponseTimeMinutes())
                .avgResolutionTimeMinutes(entity.getAvgResolutionTimeMinutes())
                .ticketsWithinSla(entity.getTicketsWithinSla())
                .ticketsBreachedSla(entity.getTicketsBreachedSla())
                .slaCompliancePercentage(entity.getSlaCompliancePercentage())
                .avgCsatScore(entity.getAvgCsatScore())
                .totalCsatResponses(entity.getTotalCsatResponses())
                .npsScore(entity.getNpsScore())
                .totalAgents(entity.getTotalAgents())
                .activeAgents(entity.getActiveAgents())
                .ticketsPerAgent(entity.getTicketsPerAgent())
                .avgAgentUtilization(entity.getAvgAgentUtilization())
                .channelBreakdown(buildChannelBreakdown(entity))
                .regionalBreakdown(buildRegionalBreakdown(entity))
                .priorityBreakdown(buildPriorityBreakdown(entity))
                .avgQualityScore(entity.getAvgQualityScore())
                .totalQualityReviews(entity.getTotalQualityReviews())
                .metricStartDate(entity.getMetricStartDate())
                .metricEndDate(entity.getMetricEndDate())
                .aggregationType(entity.getAggregationType())
                .dataSource(entity.getDataSource())
                .isRealTime(entity.getIsRealTime())
                .isStale(entity.isStale())
                .lastRefreshedAt(entity.getLastRefreshedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Convert RegionalMetrics entity to DTO
     */
    public RegionalMetricsDto toDto(RegionalMetrics entity) {
        if (entity == null) {
            return null;
        }

        return RegionalMetricsDto.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .regionCode(entity.getRegionCode())
                .regionName(entity.getRegionName())
                .totalTickets(entity.getTotalTickets())
                .openTickets(entity.getOpenTickets())
                .resolvedTickets(entity.getResolvedTickets())
                .avgFirstResponseTimeMinutes(entity.getAvgFirstResponseTimeMinutes())
                .avgResolutionTimeMinutes(entity.getAvgResolutionTimeMinutes())
                .slaCompliancePercentage(entity.getSlaCompliancePercentage())
                .avgCsatScore(entity.getAvgCsatScore())
                .totalCsatResponses(entity.getTotalCsatResponses())
                .totalAgents(entity.getTotalAgents())
                .activeAgents(entity.getActiveAgents())
                .topCountryCode(entity.getTopCountryCode())
                .topCountryTicketCount(entity.getTopCountryTicketCount())
                .ticketsLast24Hours(entity.getTicketsLast24Hours())
                .ticketsLast7Days(entity.getTicketsLast7Days())
                .ticketsLast30Days(entity.getTicketsLast30Days())
                .trendPercentage(entity.getTrendPercentage())
                .metricDate(entity.getMetricDate())
                .aggregationType(entity.getAggregationType())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Convert AgentPerformance entity to DTO
     */
    public AgentPerformanceDto toDto(AgentPerformance entity) {
        if (entity == null) {
            return null;
        }

        return AgentPerformanceDto.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .agentId(entity.getAgentId())
                .agentName(entity.getAgentName())
                .agentEmail(entity.getAgentEmail())
                .teamId(entity.getTeamId())
                .teamName(entity.getTeamName())
                .regionCode(entity.getRegionCode())
                .totalTicketsAssigned(entity.getTotalTicketsAssigned())
                .ticketsResolved(entity.getTicketsResolved())
                .ticketsInProgress(entity.getTicketsInProgress())
                .ticketsReassigned(entity.getTicketsReassigned())
                .ticketsEscalated(entity.getTicketsEscalated())
                .avgFirstResponseTimeMinutes(entity.getAvgFirstResponseTimeMinutes())
                .avgResolutionTimeMinutes(entity.getAvgResolutionTimeMinutes())
                .avgHandlingTimeMinutes(entity.getAvgHandlingTimeMinutes())
                .avgCsatScore(entity.getAvgCsatScore())
                .totalCsatReceived(entity.getTotalCsatReceived())
                .qualityScore(entity.getQualityScore())
                .totalQaReviews(entity.getTotalQaReviews())
                .totalAvailableTimeMinutes(entity.getTotalAvailableTimeMinutes())
                .totalTalkTimeMinutes(entity.getTotalTalkTimeMinutes())
                .totalAwayTimeMinutes(entity.getTotalAwayTimeMinutes())
                .totalBreakTimeMinutes(entity.getTotalBreakTimeMinutes())
                .utilizationPercentage(entity.getUtilizationPercentage())
                .status(entity.getStatus())
                .agentTier(entity.calculateAgentTier())
                .statusLastUpdated(entity.getStatusLastUpdated())
                .ticketsResolvedLast7Days(entity.getTicketsResolvedLast7Days())
                .ticketsResolvedLast30Days(entity.getTicketsResolvedLast30Days())
                .performanceTrend(entity.getPerformanceTrend())
                .regionalRank(entity.getRegionalRank())
                .globalRank(entity.getGlobalRank())
                .performancePeriodStart(entity.getPerformancePeriodStart())
                .performancePeriodEnd(entity.getPerformancePeriodEnd())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Build channel breakdown map
     */
    private Map<String, Long> buildChannelBreakdown(SupportMetrics entity) {
        Map<String, Long> breakdown = new HashMap<>();
        breakdown.put("email", entity.getEmailTickets() != null ? entity.getEmailTickets() : 0L);
        breakdown.put("chat", entity.getChatTickets() != null ? entity.getChatTickets() : 0L);
        breakdown.put("phone", entity.getPhoneTickets() != null ? entity.getPhoneTickets() : 0L);
        breakdown.put("web", entity.getWebTickets() != null ? entity.getWebTickets() : 0L);
        breakdown.put("social", entity.getSocialTickets() != null ? entity.getSocialTickets() : 0L);
        return breakdown;
    }

    /**
     * Build regional breakdown map
     */
    private Map<String, Long> buildRegionalBreakdown(SupportMetrics entity) {
        Map<String, Long> breakdown = new HashMap<>();
        breakdown.put("north_america", entity.getTicketsByRegionNorthAmerica() != null ? entity.getTicketsByRegionNorthAmerica() : 0L);
        breakdown.put("europe", entity.getTicketsByRegionEurope() != null ? entity.getTicketsByRegionEurope() : 0L);
        breakdown.put("asia_pacific", entity.getTicketsByRegionAsiaPacific() != null ? entity.getTicketsByRegionAsiaPacific() : 0L);
        breakdown.put("latam", entity.getTicketsByRegionLatam() != null ? entity.getTicketsByRegionLatam() : 0L);
        breakdown.put("middle_east_africa", entity.getTicketsByRegionMiddleEastAfrica() != null ? entity.getTicketsByRegionMiddleEastAfrica() : 0L);
        return breakdown;
    }

    /**
     * Build priority breakdown map
     */
    private Map<String, Long> buildPriorityBreakdown(SupportMetrics entity) {
        Map<String, Long> breakdown = new HashMap<>();
        breakdown.put("critical", entity.getCriticalPriorityTickets() != null ? entity.getCriticalPriorityTickets() : 0L);
        breakdown.put("high", entity.getHighPriorityTickets() != null ? entity.getHighPriorityTickets() : 0L);
        breakdown.put("medium", entity.getMediumPriorityTickets() != null ? entity.getMediumPriorityTickets() : 0L);
        breakdown.put("low", entity.getLowPriorityTickets() != null ? entity.getLowPriorityTickets() : 0L);
        return breakdown;
    }
}
