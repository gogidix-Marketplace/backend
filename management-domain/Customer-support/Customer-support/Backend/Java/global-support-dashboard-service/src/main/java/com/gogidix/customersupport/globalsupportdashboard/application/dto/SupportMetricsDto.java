package com.gogidix.customersupport.globalsupportdashboard.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * DTO for Support Metrics response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupportMetricsDto {

    private String id;
    private String tenantId;

    // Ticket Metrics
    private Long totalTickets;
    private Long openTickets;
    private Long inProgressTickets;
    private Long resolvedTickets;
    private Long closedTickets;
    private Long escalatedTickets;

    // Response Time Metrics
    private Double avgFirstResponseTime;
    private Double avgResolutionTime;
    private Long avgFirstResponseTimeMinutes;
    private Long avgResolutionTimeMinutes;

    // SLA Compliance
    private Long ticketsWithinSla;
    private Long ticketsBreachedSla;
    private Double slaCompliancePercentage;

    // Customer Satisfaction
    private Double avgCsatScore;
    private Long totalCsatResponses;
    private Double npsScore;

    // Agent Productivity
    private Long totalAgents;
    private Long activeAgents;
    private Double ticketsPerAgent;
    private Double avgAgentUtilization;

    // Channel Metrics
    private Map<String, Long> channelBreakdown;

    // Regional Metrics
    private Map<String, Long> regionalBreakdown;

    // Priority Metrics
    private Map<String, Long> priorityBreakdown;

    // Quality Metrics
    private Double avgQualityScore;
    private Long totalQualityReviews;

    // Time Period
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant metricStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant metricEndDate;

    private String aggregationType;

    // Metadata
    private String dataSource;
    private Boolean isRealTime;
    private Boolean isStale;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant lastRefreshedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;
}
