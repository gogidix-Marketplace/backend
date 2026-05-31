package com.gogidix.dashboard.core.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Dashboard Widget entity.
 * Represents a widget on a dashboard that displays one or more KPIs.
 */
@Entity
@Table(name = "dashboard_widgets", indexes = {
    @Index(name = "idx_widget_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_widget_dashboard_id", columnList = "dashboard_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardWidget {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "dashboard_id", nullable = false)
    private UUID dashboardId;

    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    @Enumerated(EnumType.STRING)
    @Column(name = "widget_type", nullable = false, length = 50)
    private WidgetType widgetType;

    @Column(name = "position_x", nullable = false)
    @Builder.Default
    private Integer positionX = 0;

    @Column(name = "position_y", nullable = false)
    @Builder.Default
    private Integer positionY = 0;

    @Column(name = "width", nullable = false)
    @Builder.Default
    private Integer width = 4;

    @Column(name = "height", nullable = false)
    @Builder.Default
    private Integer height = 3;

    @Column(name = "kpi_ids", columnDefinition = "TEXT")
    private String kpiIds;

    @Column(name = "config", columnDefinition = "TEXT")
    private String config;

    @Column(name = "is_refreshable", nullable = false)
    @Builder.Default
    private Boolean isRefreshable = true;

    @Column(name = "refresh_interval_seconds")
    private Integer refreshIntervalSeconds;

    @Column(name = "is_visible", nullable = false)
    @Builder.Default
    private Boolean isVisible = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum WidgetType {
        LINE_CHART,
        BAR_CHART,
        PIE_CHART,
        GAUGE,
        METRIC_CARD,
        TABLE,
        STAT_CARD,
        TREND_CHART,
        HEATMAP,
        FUNNEL,
        SCORECARD,
        TEXT
    }
}
