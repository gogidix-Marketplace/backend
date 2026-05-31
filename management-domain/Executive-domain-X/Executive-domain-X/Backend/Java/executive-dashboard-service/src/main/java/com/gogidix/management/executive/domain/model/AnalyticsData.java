package com.gogidix.management.executive.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

/**
 * Analytics Data domain model
 * Stores aggregated analytics data for dashboards
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "analytics_data")
public class AnalyticsData extends BaseEntity {

    private String dashboardId;
    private String metricId;
    private String metricName;
    private Object value;
    private String valueType;
    private Map<String, Object> metadata;
    private Map<String, Object> dimensions;
    private Instant timestamp;
    private Instant periodStart;
    private Instant periodEnd;
    private String dataSource;
}
