package com.gogidix.customersupport.supportanalytics.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "analytics_reports")
public class AnalyticsReport extends BaseEntity {

    @Field("report_name")
    private String reportName;

    @Field("report_type")
    @Indexed
    private ReportType reportType;

    @Field("start_date")
    @Indexed
    private LocalDate startDate;

    @Field("end_date")
    @Indexed
    private LocalDate endDate;

    @Field("total_tickets")
    private Integer totalTickets;

    @Field("resolved_tickets")
    private Integer resolvedTickets;

    @Field("open_tickets")
    private Integer openTickets;

    @Field("escalated_tickets")
    private Integer escalatedTickets;

    @Field("average_resolution_time_minutes")
    private Double averageResolutionTimeMinutes;

    @Field("average_response_time_minutes")
    private Double averageResponseTimeMinutes;

    @Field("customer_satisfaction_score")
    private Double customerSatisfactionScore;

    @Field("first_contact_resolution_rate")
    private Double firstContactResolutionRate;

    @Field("agent_performance_metrics")
    private Map<String, AgentPerformanceMetric> agentPerformanceMetrics;

    @Field("channel_performance")
    private Map<String, ChannelPerformanceMetric> channelPerformance;

    @Field("generated_at")
    private Instant generatedAt;

    @Field("generated_by")
    private String generatedBy;

    public static AnalyticsReport create(String tenantId, String reportName, ReportType reportType,
                                         LocalDate startDate, LocalDate endDate, String generatedBy) {
        AnalyticsReport report = new AnalyticsReport();
        report.setId(java.util.UUID.randomUUID().toString());
        report.setTenantId(tenantId);
        report.setReportName(reportName);
        report.setReportType(reportType);
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        report.setGeneratedAt(Instant.now());
        report.setGeneratedBy(generatedBy);
        report.setCreatedAt(Instant.now());
        report.setUpdatedAt(Instant.now());
        return report;
    }

    public enum ReportType {
        DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY, CUSTOM
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgentPerformanceMetric {
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
    public static class ChannelPerformanceMetric {
        private String channel;
        private Integer ticketsReceived;
        private Integer ticketsResolved;
        private Double averageResolutionTime;
        private Double satisfactionScore;
    }
}
