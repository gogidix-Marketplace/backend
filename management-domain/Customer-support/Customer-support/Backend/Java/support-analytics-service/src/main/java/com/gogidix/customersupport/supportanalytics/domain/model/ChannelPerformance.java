package com.gogidix.customersupport.supportanalytics.domain.model;

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
@Document(collection = "channel_performance")
public class ChannelPerformance extends BaseEntity {

    @Field("channel_type")
    @Indexed
    private ChannelType channelType;

    @Field("metric_date")
    @Indexed
    private LocalDate metricDate;

    @Field("total_interactions")
    private Integer totalInteractions;

    @Field("resolved_interactions")
    private Integer resolvedInteractions;

    @Field("pending_interactions")
    private Integer pendingInteractions;

    @Field("average_response_time_seconds")
    private Long averageResponseTimeSeconds;

    @Field("average_resolution_time_seconds")
    private Long averageResolutionTimeSeconds;

    @Field("abandonment_rate")
    private Double abandonmentRate;

    @Field("customer_satisfaction_score")
    private Double customerSatisfactionScore;

    @Field("first_contact_resolution_rate")
    private Double firstContactResolutionRate;

    @Field("peak_hours")
    private Map<String, Integer> peakHours;

    @Field("agent_utilization")
    private Double agentUtilization;

    @Field("average_handle_time_seconds")
    private Long averageHandleTimeSeconds;

    @Field("total_handle_time_seconds")
    private Long totalHandleTimeSeconds;

    @Field("active_agents")
    private Integer activeAgents;

    public static ChannelPerformance create(String tenantId, ChannelType channelType, LocalDate metricDate) {
        ChannelPerformance performance = new ChannelPerformance();
        performance.setId(java.util.UUID.randomUUID().toString());
        performance.setTenantId(tenantId);
        performance.setChannelType(channelType);
        performance.setMetricDate(metricDate);
        performance.setCreatedAt(java.time.Instant.now());
        performance.setUpdatedAt(java.time.Instant.now());
        return performance;
    }

    public double calculateResolutionRate() {
        if (totalInteractions == null || totalInteractions == 0) {
            return 0.0;
        }
        int resolved = resolvedInteractions != null ? resolvedInteractions : 0;
        return (double) resolved / totalInteractions * 100;
    }

    public enum ChannelType {
        EMAIL, PHONE, LIVE_CHAT, WEB_PORTAL, MOBILE_APP, SOCIAL_MEDIA, SMS, WHATSAPP
    }
}
