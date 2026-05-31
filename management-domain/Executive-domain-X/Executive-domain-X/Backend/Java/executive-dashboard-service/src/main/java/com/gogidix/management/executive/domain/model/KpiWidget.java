package com.gogidix.management.executive.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "kpi_widgets")
public class KpiWidget extends BaseEntity {

    private String name;
    private String description;
    private String dashboardId;
    private MetricType metricType;
    private String dataSource;
    private String query;
    private Map<String, Object> config;
    private int position;
    private int row;
    private int column;
    private int width;
    private int height;
    private WidgetStatus status;

    public enum MetricType {
        COUNTER, GAUGE, TREND, PERCENTAGE, CURRENCY, NUMBER, TEXT
    }

    public enum WidgetStatus {
        ACTIVE, INACTIVE, ERROR
    }

    public boolean isActive() {
        return status == WidgetStatus.ACTIVE && active;
    }
}
