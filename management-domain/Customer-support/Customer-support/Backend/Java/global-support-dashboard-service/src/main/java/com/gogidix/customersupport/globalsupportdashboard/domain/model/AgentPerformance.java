package com.gogidix.customersupport.globalsupportdashboard.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;

/**
 * AgentPerformance - Domain model tracking individual and aggregate agent performance
 *
 * Provides visibility into agent productivity and quality across the global support team.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AgentPerformance extends BaseEntity {

    @Indexed
    private String agentId;
    private String agentName;
    private String agentEmail;

    // Team/Region Assignment
    @Indexed
    private String teamId;
    private String teamName;
    @Indexed
    private String regionCode;

    // Ticket Metrics
    private Long totalTicketsAssigned;
    private Long ticketsResolved;
    private Long ticketsInProgress;
    private Long ticketsReassigned;
    private Long ticketsEscalated;

    // Performance Metrics
    private Double avgFirstResponseTimeMinutes;
    private Double avgResolutionTimeMinutes;
    private Long avgHandlingTimeMinutes; // AHT - Average Handle Time

    // Quality Metrics
    private Double avgCsatScore;
    private Long totalCsatReceived;
    private Double qualityScore; // QA score (0-100)
    private Long totalQaReviews;

    // Availability
    private Long totalAvailableTimeMinutes;
    private Long totalTalkTimeMinutes;
    private Long totalAwayTimeMinutes;
    private Long totalBreakTimeMinutes;
    private Double utilizationPercentage;

    // Status
    @Indexed
    private String status; // ACTIVE, AWAY, OFFLINE, IN_CALL, IN_CHAT
    private Instant statusLastUpdated;

    // Trend Analysis
    private Long ticketsResolvedLast7Days;
    private Long ticketsResolvedLast30Days;
    private Double performanceTrend; // positive = improving, negative = declining

    // Ranking
    private Integer regionalRank;
    private Integer globalRank;

    // Time Period
    private Instant performancePeriodStart;
    private Instant performancePeriodEnd;

    public AgentPerformance(String tenantId, String agentId) {
        super(tenantId);
        this.agentId = agentId;
        this.status = "OFFLINE";
        this.statusLastUpdated = Instant.now();
    }

    /**
     * Calculate agent utilization percentage
     */
    public void calculateUtilization() {
        if (totalAvailableTimeMinutes != null && totalAvailableTimeMinutes > 0) {
            long productiveTime = (totalTalkTimeMinutes != null ? totalTalkTimeMinutes : 0);
            this.utilizationPercentage = ((double) productiveTime / totalAvailableTimeMinutes) * 100.0;
        }
    }

    /**
     * Calculate agent tier based on performance
     */
    public String calculateAgentTier() {
        if (avgCsatScore == null || qualityScore == null) {
            return "UNRATED";
        }

        double combinedScore = (avgCsatScore * 20 + qualityScore) / 2; // CSAT (1-5 to 0-100) + Quality (0-100) / 2

        if (combinedScore >= 90) return "ELITE";
        if (combinedScore >= 80) return "EXPERT";
        if (combinedScore >= 70) return "PROFICIENT";
        if (combinedScore >= 60) return "DEVELOPING";
        return "NEEDS_IMPROVEMENT";
    }

    /**
     * Check if agent is currently available
     */
    public boolean isAvailable() {
        return "ACTIVE".equals(status) || "IN_CALL".equals(status) || "IN_CHAT".equals(status);
    }
}
