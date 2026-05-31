package com.gogidix.customersupport.globalsupportdashboard.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * DTO for Regional Metrics response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionalMetricsDto {

    private String id;
    private String tenantId;
    private String regionCode;
    private String regionName;

    // Ticket Volume
    private Long totalTickets;
    private Long openTickets;
    private Long resolvedTickets;

    // Performance Metrics
    private Double avgFirstResponseTimeMinutes;
    private Double avgResolutionTimeMinutes;
    private Double slaCompliancePercentage;

    // Customer Satisfaction
    private Double avgCsatScore;
    private Long totalCsatResponses;

    // Agent Metrics
    private Long totalAgents;
    private Long activeAgents;

    // Country Breakdown
    private String topCountryCode;
    private Long topCountryTicketCount;

    // Trend Data
    private Long ticketsLast24Hours;
    private Long ticketsLast7Days;
    private Long ticketsLast30Days;
    private Double trendPercentage;

    // Timestamps
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant metricDate;

    private String aggregationType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;
}
