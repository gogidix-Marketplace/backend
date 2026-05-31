package com.gogidix.management.executive.analytics.application.dto;

import com.gogidix.management.executive.analytics.domain.model.Analytics;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;
/**
 * DTO for Analytics responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsDto {
    private String id;
    private String tenantId;
    private String name;
    private String description;
    private String ownerId;
    private List<WidgetDto> widgets;
    private Analytics.AnalyticsStatus status;
    private String layout;
    private Instant createdAt;
    private Instant updatedAt;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WidgetDto {
        private String id;
        private String name;
        private Analytics.Widget.WidgetType type;
        private int position;
        private int row;
        private int column;
        private int width;
        private int height;
        private String config;
        private String dataSource;
    }
}
