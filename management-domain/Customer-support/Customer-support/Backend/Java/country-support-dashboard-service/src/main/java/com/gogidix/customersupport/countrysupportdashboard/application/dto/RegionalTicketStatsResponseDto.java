package com.gogidix.customersupport.countrysupportdashboard.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionalTicketStatsResponseDto {

    private String id;
    private String tenantId;
    private String regionName;
    private List<String> countryCodes;
    private LocalDate statDate;
    private Integer totalTickets;
    private Integer newTickets;
    private Integer closedTickets;
    private Integer pendingTickets;
    private Map<String, CountryTicketSummaryDto> ticketsByCountry;
    private Map<String, Integer> ticketsByStatus;
    private Map<String, Integer> ticketsByPriority;
    private Map<String, Integer> ticketsByChannel;
    private Double averageResolutionTimeMinutes;
    private Double averageResponseTimeMinutes;
    private Double customerSatisfactionScore;
    private Double slaComplianceRate;
    private Double changePercentage;
    private Integer activeAgents;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CountryTicketSummaryDto {
        private String countryCode;
        private String countryName;
        private Integer totalTickets;
        private Integer openTickets;
        private Integer resolvedTickets;
        private Double satisfactionScore;
    }
}
