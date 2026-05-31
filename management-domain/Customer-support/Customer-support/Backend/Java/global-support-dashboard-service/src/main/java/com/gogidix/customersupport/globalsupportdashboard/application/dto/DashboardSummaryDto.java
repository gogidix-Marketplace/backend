package com.gogidix.customersupport.globalsupportdashboard.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * DTO for Dashboard Summary response
 * Aggregates all key metrics for the global dashboard view
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardSummaryDto {

    private String tenantId;

    // Global Overview
    private GlobalOverviewDto globalOverview;

    // Regional Breakdown
    private List<RegionalMetricsDto> regionalMetrics;

    // Top Agents
    private List<AgentPerformanceDto> topAgents;

    // Agents Needing Attention
    private List<AgentPerformanceDto> agentsNeedingAttention;

    // Trends
    private TrendDto trends;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant generatedAt;

    private Boolean isStale;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GlobalOverviewDto {
        private Long totalTickets;
        private Long openTickets;
        private Double avgCsatScore;
        private Double slaCompliancePercentage;
        private Long totalAgents;
        private Long activeAgents;
        private Double avgResolutionTimeMinutes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrendDto {
        private Double ticketVolumeChange; // percentage
        private Double csatChange; // percentage
        private Double slaChange; // percentage
        private String period; // "24h", "7d", "30d"
    }
}
