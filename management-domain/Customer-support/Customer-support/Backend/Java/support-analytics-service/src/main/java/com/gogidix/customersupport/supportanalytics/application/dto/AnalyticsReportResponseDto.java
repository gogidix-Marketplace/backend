package com.gogidix.customersupport.supportanalytics.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.customersupport.supportanalytics.domain.model.AnalyticsReport;
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
public class AnalyticsReportResponseDto {

    private String id;
    private String tenantId;
    private String reportName;
    private ReportTypeDto reportType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalTickets;
    private Integer resolvedTickets;
    private Integer openTickets;
    private Integer escalatedTickets;
    private Double averageResolutionTimeMinutes;
    private Double averageResponseTimeMinutes;
    private Double customerSatisfactionScore;
    private Double firstContactResolutionRate;
    private Map<String, AgentPerformanceMetricDto> agentPerformanceMetrics;
    private Map<String, ChannelPerformanceMetricDto> channelPerformance;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant generatedAt;

    private String generatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgentPerformanceMetricDto {
        private String agentId;
        private String agentName;
        private Integer ticketsHandled;
        private Integer ticketsResolved;
        private Double averageResolutionTime;
        private Double averageResponseTime;
        private Double satisfactionScore;
        private Integer escalations;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChannelPerformanceMetricDto {
        private String channel;
        private Integer ticketsReceived;
        private Integer ticketsResolved;
        private Double averageResolutionTime;
        private Double satisfactionScore;
    }

    public enum ReportTypeDto {
        DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY, CUSTOM
    }

    public static ReportTypeDto fromEntityType(AnalyticsReport.ReportType reportType) {
        return ReportTypeDto.valueOf(reportType.name());
    }
}
