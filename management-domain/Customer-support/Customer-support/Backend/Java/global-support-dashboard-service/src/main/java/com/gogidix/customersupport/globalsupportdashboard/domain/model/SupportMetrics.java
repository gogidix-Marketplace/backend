package com.gogidix.customersupport.globalsupportdashboard.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * SupportMetrics - Domain model representing global support metrics
 *
 * Aggregates key performance indicators for customer support operations
 * across all regions and channels.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SupportMetrics extends BaseEntity {

    // Ticket Metrics
    private Long totalTickets;
    private Long openTickets;
    private Long inProgressTickets;
    private Long resolvedTickets;
    private Long closedTickets;
    private Long escalatedTickets;

    // Response Time Metrics (in minutes)
    private Double avgFirstResponseTime;
    private Double avgResolutionTime;
    private Long avgFirstResponseTimeMinutes; // in minutes
    private Long avgResolutionTimeMinutes; // in minutes

    // SLA Compliance
    private Long ticketsWithinSla;
    private Long ticketsBreachedSla;
    private Double slaCompliancePercentage;

    // Customer Satisfaction
    private Double avgCsatScore; // 1-5 scale
    private Long totalCsatResponses;
    private Double npsScore; // Net Promoter Score

    // Agent Productivity
    private Long totalAgents;
    private Long activeAgents;
    private Double ticketsPerAgent;
    private Double avgAgentUtilization;

    // Channel Metrics
    private Long emailTickets;
    private Long chatTickets;
    private Long phoneTickets;
    private Long webTickets;
    private Long socialTickets;

    // Regional Metrics
    private Long ticketsByRegionNorthAmerica;
    private Long ticketsByRegionEurope;
    private Long ticketsByRegionAsiaPacific;
    private Long ticketsByRegionLatam;
    private Long ticketsByRegionMiddleEastAfrica;

    // Priority Metrics
    private Long criticalPriorityTickets;
    private Long highPriorityTickets;
    private Long mediumPriorityTickets;
    private Long lowPriorityTickets;

    // Quality Metrics
    private Double avgQualityScore; // 0-100 scale
    private Long totalQualityReviews;

    // Time Period
    private Instant metricStartDate;
    private Instant metricEndDate;
    private String aggregationType; // HOURLY, DAILY, WEEKLY, MONTHLY

    // Metadata
    private String dataSource;
    private Boolean isRealTime;
    private Instant lastRefreshedAt;

    public SupportMetrics(String tenantId) {
        super(tenantId);
        this.isRealTime = false;
        this.lastRefreshedAt = Instant.now();
    }

    /**
     * Calculate SLA compliance percentage
     */
    public void calculateSlaCompliance() {
        if (totalTickets != null && totalTickets > 0) {
            this.slaCompliancePercentage = ((ticketsWithinSla != null ? ticketsWithinSla : 0.0) / totalTickets) * 100.0;
        }
    }

    /**
     * Calculate tickets per agent
     */
    public void calculateTicketsPerAgent() {
        if (totalAgents != null && totalAgents > 0 && totalTickets != null) {
            this.ticketsPerAgent = (double) totalTickets / totalAgents;
        }
    }

    /**
     * Check if metrics are stale (older than 5 minutes)
     */
    public boolean isStale() {
        if (lastRefreshedAt == null) return true;
        return Instant.now().minusSeconds(300).isAfter(lastRefreshedAt);
    }
}
