package com.gogidix.customersupport.globalsupportdashboard.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * RegionalMetrics - Domain model for region-specific support metrics
 *
 * Tracks support performance by geographic region for global oversight.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "regional_metrics")
public class RegionalMetrics extends BaseEntity {

    @Indexed
    private String regionCode; // NA, EU, APAC, LATAM, MEA

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
    private Double trendPercentage; // positive = increasing, negative = decreasing

    // Timestamps
    private Instant metricDate;
    private String aggregationType; // HOURLY, DAILY, WEEKLY, MONTHLY

    public RegionalMetrics(String tenantId, String regionCode) {
        super(tenantId);
        this.regionCode = regionCode;
        this.metricDate = Instant.now();
    }
}
