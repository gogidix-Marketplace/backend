package com.gogidix.analytics.bi.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a widget within a dashboard.
 */
@Entity
@Table(name = "dashboard_widgets", indexes = {
    @Index(name = "idx_widget_dashboard", columnList = "dashboard_id"),
    @Index(name = "idx_widget_type", columnList = "widget_type")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardWidget {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "widget_name", nullable = false, length = 255)
    private String widgetName;

    @Enumerated(EnumType.STRING)
    @Column(name = "widget_type", nullable = false, length = 50)
    private WidgetType widgetType;

    @Column(name = "position", nullable = false)
    private Integer position;

    @Column(name = "row_index")
    private Integer rowIndex;

    @Column(name = "column_index")
    private Integer columnIndex;

    @Column(name = "row_span")
    @Builder.Default
    private Integer rowSpan = 1;

    @Column(name = "column_span")
    @Builder.Default
    private Integer columnSpan = 1;

    @Column(name = "data_source", columnDefinition = "JSONB")
    private String dataSource;

    @Column(name = "visualization_config", columnDefinition = "JSONB")
    private String visualizationConfig;

    @Column(name = "query_definition", columnDefinition = "TEXT")
    private String queryDefinition;

    @Column(name = "refresh_interval_seconds")
    private Integer refreshIntervalSeconds;

    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dashboard_id", nullable = false)
    private Dashboard dashboard;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum WidgetType {
        LINE_CHART,
        BAR_CHART,
        PIE_CHART,
        DONUT_CHART,
        AREA_CHART,
        SCATTER_PLOT,
        TABLE,
        SINGLE_VALUE,
        GAUGE,
        HEATMAP,
        FUNNEL,
        TREEMAP,
        GEOMAP,
        NUMBER_CARD,
        PROGRESS_BAR,
        SPARKLINE
    }
}
