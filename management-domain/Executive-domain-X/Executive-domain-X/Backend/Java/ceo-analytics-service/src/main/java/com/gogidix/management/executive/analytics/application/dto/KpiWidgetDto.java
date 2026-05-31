package com.gogidix.management.executive.analytics.application.dto;

import com.gogidix.management.executive.analytics.domain.model.KpiWidget;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * DTO for KPI Widget responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KpiWidgetDto {
    private String id;
    private String tenantId;
    private String dashboardId;
    private String name;
    private String description;
    private String metricType;
    private String dataSource;
    private int position;
    private int row;
    private int column;
    private int width;
    private int height;
    private Map<String, Object> config;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
}
