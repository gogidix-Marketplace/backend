package com.gogidix.customersupport.globalsupportdashboard.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * DTO for Agent Performance response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgentPerformanceDto {

    private String id;
    private String tenantId;
    private String agentId;
    private String agentName;
    private String agentEmail;

    // Team/Region Assignment
    private String teamId;
    private String teamName;
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
    private Long avgHandlingTimeMinutes;

    // Quality Metrics
    private Double avgCsatScore;
    private Long totalCsatReceived;
    private Double qualityScore;
    private Long totalQaReviews;

    // Availability
    private Long totalAvailableTimeMinutes;
    private Long totalTalkTimeMinutes;
    private Long totalAwayTimeMinutes;
    private Long totalBreakTimeMinutes;
    private Double utilizationPercentage;

    // Status
    private String status;
    private String agentTier;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant statusLastUpdated;

    // Trend Analysis
    private Long ticketsResolvedLast7Days;
    private Long ticketsResolvedLast30Days;
    private Double performanceTrend;

    // Ranking
    private Integer regionalRank;
    private Integer globalRank;

    // Time Period
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant performancePeriodStart;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant performancePeriodEnd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;
}
