package com.gogidix.sales.dashboard.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Widget Response DTO
 * Used for sending widget data to clients
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WidgetResponseDto {

    private String id;
    private String widgetId;
    private String tenantId;
    private String dashboardId;
    private String title;
    private String description;
    private String widgetType;
    private String category;
    private Object value;
    private String displayValue;
    private TrendInfoDto trendInfo;
    private TargetInfoDto targetInfo;
    private Integer row;
    private Integer column;
    private Integer rowSpan;
    private Integer columnSpan;
    private Boolean isVisible;
    private Boolean isActive;
    private Integer refreshFrequencyMinutes;
    private String owner;
    private List<String> tags;
    private Instant lastUpdated;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrendInfoDto {
        private String direction;
        private Double value;
        private String percentage;
        private Boolean isPositive;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TargetInfoDto {
        private Double target;
        private Double actual;
        private Double achievement;
        private Double remaining;
        private Boolean isOnTrack;
    }
}
