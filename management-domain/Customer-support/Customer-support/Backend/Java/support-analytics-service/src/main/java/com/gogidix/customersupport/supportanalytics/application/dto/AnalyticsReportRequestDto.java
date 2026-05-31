package com.gogidix.customersupport.supportanalytics.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.customersupport.supportanalytics.domain.model.AnalyticsReport;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsReportRequestDto {

    @NotBlank(message = "Report name is required")
    private String reportName;

    @NotNull(message = "Report type is required")
    private ReportTypeDto reportType;

    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String generatedBy;

    private Map<String, AgentPerformanceMetricDto> agentPerformanceMetrics;

    private Map<String, ChannelPerformanceMetricDto> channelPerformance;

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

    public AnalyticsReport.ReportType toEntityType() {
        return AnalyticsReport.ReportType.valueOf(reportType.name());
    }
}
