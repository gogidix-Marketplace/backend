package com.gogidix.customersupport.countrysupportdashboard.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountrySpecificMetricsResponseDto {

    private String id;
    private String tenantId;
    private String countryCode;
    private String countryName;
    private LocalDate metricDate;
    private Integer totalTickets;
    private Integer openTickets;
    private Integer resolvedTickets;
    private Integer escalatedTickets;
    private Double averageResolutionTimeMinutes;
    private Double averageResponseTimeMinutes;
    private Double customerSatisfactionScore;
    private Integer activeAgents;
    private Map<String, Integer> ticketVolumeByChannel;
    private Map<String, Integer> ticketVolumeByPriority;
    private Map<String, Integer> ticketVolumeByCategory;
    private Double slaComplianceRate;
    private Double firstContactResolutionRate;
    private Map<String, Integer> peakHours;
    private String region;
    private String language;
    private String timezone;
    private BusinessHoursDto businessHours;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BusinessHoursDto {
        private String startTime;
        private String endTime;
        private String timezone;
        private java.util.List<String> workingDays;
    }
}
