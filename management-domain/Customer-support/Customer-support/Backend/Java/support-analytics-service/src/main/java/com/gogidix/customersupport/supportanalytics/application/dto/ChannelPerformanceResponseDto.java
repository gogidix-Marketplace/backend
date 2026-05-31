package com.gogidix.customersupport.supportanalytics.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.customersupport.supportanalytics.domain.model.ChannelPerformance;
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
public class ChannelPerformanceResponseDto {

    private String id;
    private String tenantId;
    private ChannelTypeDto channelType;
    private LocalDate metricDate;
    private Integer totalInteractions;
    private Integer resolvedInteractions;
    private Integer pendingInteractions;
    private Long averageResponseTimeSeconds;
    private Long averageResolutionTimeSeconds;
    private Double abandonmentRate;
    private Double customerSatisfactionScore;
    private Double firstContactResolutionRate;
    private Map<String, Integer> peakHours;
    private Double agentUtilization;
    private Long averageHandleTimeSeconds;
    private Long totalHandleTimeSeconds;
    private Integer activeAgents;
    private Double resolutionRate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum ChannelTypeDto {
        EMAIL, PHONE, LIVE_CHAT, WEB_PORTAL, MOBILE_APP, SOCIAL_MEDIA, SMS, WHATSAPP
    }

    public static ChannelTypeDto fromEntityType(ChannelPerformance.ChannelType channelType) {
        return ChannelTypeDto.valueOf(channelType.name());
    }
}
