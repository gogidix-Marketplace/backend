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
@Document(collection = "country_specific_metrics")
public class CountrySpecificMetrics extends BaseEntity {

    @Field("country_code")
    @Indexed
    private String countryCode;

    @Field("country_name")
    private String countryName;

    @Field("metric_date")
    @Indexed
    private LocalDate metricDate;

    @Field("total_tickets")
    private Integer totalTickets;

    @Field("open_tickets")
    private Integer openTickets;

    @Field("resolved_tickets")
    private Integer resolvedTickets;

    @Field("escalated_tickets")
    private Integer escalatedTickets;

    @Field("average_resolution_time_minutes")
    private Double averageResolutionTimeMinutes;

    @Field("average_response_time_minutes")
    private Double averageResponseTimeMinutes;

    @Field("customer_satisfaction_score")
    private Double customerSatisfactionScore;

    @Field("active_agents")
    private Integer activeAgents;

    @Field("ticket_volume_by_channel")
    private Map<String, Integer> ticketVolumeByChannel;

    @Field("ticket_volume_by_priority")
    private Map<String, Integer> ticketVolumeByPriority;

    @Field("ticket_volume_by_category")
    private Map<String, Integer> ticketVolumeByCategory;

    @Field("sla_compliance_rate")
    private Double slaComplianceRate;

    @Field("first_contact_resolution_rate")
    private Double firstContactResolutionRate;

    @Field("peak_hours")
    private Map<String, Integer> peakHours;

    @Field("region")
    private String region;

    @Field("language")
    private String language;

    @Field("timezone")
    private String timezone;

    @Field("business_hours")
    private BusinessHours businessHours;

    public static CountrySpecificMetrics create(String tenantId, String countryCode, String countryName, LocalDate metricDate) {
        CountrySpecificMetrics metrics = new CountrySpecificMetrics();
        metrics.setId(java.util.UUID.randomUUID().toString());
        metrics.setTenantId(tenantId);
        metrics.setCountryCode(countryCode);
        metrics.setCountryName(countryName);
        metrics.setMetricDate(metricDate);
        metrics.setCreatedAt(java.time.Instant.now());
        metrics.setUpdatedAt(java.time.Instant.now());
        return metrics;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BusinessHours {
        private String startTime;
        private String endTime;
        private String timezone;
        private java.util.List<String> workingDays;
    }
}
