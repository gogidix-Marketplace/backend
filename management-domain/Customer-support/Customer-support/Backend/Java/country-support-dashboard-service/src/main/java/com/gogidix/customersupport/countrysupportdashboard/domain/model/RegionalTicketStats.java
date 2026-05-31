package com.gogidix.customersupport.countrysupportdashboard.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "regional_ticket_stats")
public class RegionalTicketStats extends BaseEntity {

    @Field("region_name")
    @Indexed
    private String regionName;

    @Field("country_codes")
    private java.util.List<String> countryCodes;

    @Field("stat_date")
    @Indexed
    private LocalDate statDate;

    @Field("total_tickets")
    private Integer totalTickets;

    @Field("new_tickets")
    private Integer newTickets;

    @Field("closed_tickets")
    private Integer closedTickets;

    @Field("pending_tickets")
    private Integer pendingTickets;

    @Field("tickets_by_country")
    private Map<String, CountryTicketSummary> ticketsByCountry;

    @Field("tickets_by_status")
    private Map<String, Integer> ticketsByStatus;

    @Field("tickets_by_priority")
    private Map<String, Integer> ticketsByPriority;

    @Field("tickets_by_channel")
    private Map<String, Integer> ticketsByChannel;

    @Field("average_resolution_time_minutes")
    private Double averageResolutionTimeMinutes;

    @Field("average_response_time_minutes")
    private Double averageResponseTimeMinutes;

    @Field("customer_satisfaction_score")
    private Double customerSatisfactionScore;

    @Field("sla_compliance_rate")
    private Double slaComplianceRate;

    @Field("change_percentage")
    private Double changePercentage;

    @Field("active_agents")
    private Integer activeAgents;

    public static RegionalTicketStats create(String tenantId, String regionName, java.util.List<String> countryCodes, LocalDate statDate) {
        RegionalTicketStats stats = new RegionalTicketStats();
        stats.setId(java.util.UUID.randomUUID().toString());
        stats.setTenantId(tenantId);
        stats.setRegionName(regionName);
        stats.setCountryCodes(countryCodes);
        stats.setStatDate(statDate);
        stats.setCreatedAt(java.time.Instant.now());
        stats.setUpdatedAt(java.time.Instant.now());
        return stats;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CountryTicketSummary {
        private String countryCode;
        private String countryName;
        private Integer totalTickets;
        private Integer openTickets;
        private Integer resolvedTickets;
        private Double satisfactionScore;
    }
}
