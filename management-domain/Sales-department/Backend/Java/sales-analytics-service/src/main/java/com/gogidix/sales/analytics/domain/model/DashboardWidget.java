package com.gogidix.sales.analytics.domain.model;

import com.gogidix.sales.analytics.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dashboard Widget Domain Entity
 * Represents a customizable widget for displaying analytics on dashboards
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "dashboard_widgets")
public class DashboardWidget extends BaseEntity {

    private String widgetId;

    @Indexed
    private String tenantId;

    private String dashboardId;

    private String name;

    private String description;

    private WidgetType widgetType;

    private WidgetPosition position;

    private Map<String, Object> config;

    private Map<String, Object> data;

    private Instant lastRefreshed;

    private String createdBy;

    private List<String> metricIds;

    private WidgetDataSource dataSource;

    private Map<String, Object> filters;

    private String refreshInterval;

    private Boolean isVisible;

    private Integer displayOrder;

    private String colorScheme;

    private List<String> drillDownReportIds;

    public enum WidgetType {
        LINE_CHART,
        BAR_CHART,
        PIE_CHART,
        DONUT_CHART,
        AREA_CHART,
        TABLE,
        METRIC_CARD,
        GAUGE,
        FUNNEL,
        HEATMAP,
        TREEMAP,
        SCATTER_PLOT,
        BUBBLE_CHART,
        KPI_CARD,
        PROGRESS_BAR,
        SPARKLINE,
        NUMBER_CARD,
        COMPARISON_CHART
    }

    public enum WidgetDataSource {
        METRIC,
        REPORT,
        CUSTOM_QUERY,
        EXTERNAL_API,
        AGGREGATION
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WidgetPosition {
        private Integer row;
        private Integer column;
        private Integer rowSpan;
        private Integer columnSpan;
    }

    /**
     * Creates a new dashboard widget
     */
    public static DashboardWidget create(String tenantId, String dashboardId,
                                         String name, String description,
                                         WidgetType widgetType, WidgetPosition position,
                                         WidgetDataSource dataSource, String createdBy) {
        return DashboardWidget.builder()
                .widgetId(generateWidgetId())
                .tenantId(tenantId)
                .dashboardId(dashboardId)
                .name(name)
                .description(description)
                .widgetType(widgetType)
                .position(position)
                .config(new HashMap<>())
                .data(new HashMap<>())
                .dataSource(dataSource)
                .filters(new HashMap<>())
                .metricIds(new ArrayList<>())
                .drillDownReportIds(new ArrayList<>())
                .createdBy(createdBy)
                .isVisible(true)
                .displayOrder(0)
                .refreshInterval("PT15M")
                .build();
    }

    /**
     * Updates the widget data
     */
    public void updateData(Map<String, Object> newData) {
        this.data = newData;
        this.lastRefreshed = Instant.now();
    }

    /**
     * Updates the widget configuration
     */
    public void updateConfig(String key, Object value) {
        if (this.config == null) {
            this.config = new HashMap<>();
        }
        this.config.put(key, value);
    }

    /**
     * Adds a metric to the widget
     */
    public void addMetric(String metricId) {
        if (this.metricIds == null) {
            this.metricIds = new ArrayList<>();
        }
        if (!this.metricIds.contains(metricId)) {
            this.metricIds.add(metricId);
        }
    }

    /**
     * Removes a metric from the widget
     */
    public void removeMetric(String metricId) {
        if (this.metricIds != null) {
            this.metricIds.remove(metricId);
        }
    }

    /**
     * Adds a drill-down report
     */
    public void addDrillDownReport(String reportId) {
        if (this.drillDownReportIds == null) {
            this.drillDownReportIds = new ArrayList<>();
        }
        if (!this.drillDownReportIds.contains(reportId)) {
            this.drillDownReportIds.add(reportId);
        }
    }

    /**
     * Updates the widget position
     */
    public void updatePosition(WidgetPosition newPosition) {
        this.position = newPosition;
    }

    /**
     * Sets the visibility
     */
    public void setVisibility(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    /**
     * Updates the display order
     */
    public void updateDisplayOrder(Integer order) {
        this.displayOrder = order;
    }

    /**
     * Checks if data needs refresh
     */
    public boolean needsRefresh() {
        if (this.lastRefreshed == null) {
            return true;
        }

        try {
            java.time.Duration interval = java.time.Duration.parse(this.refreshInterval);
            Instant nextRefresh = this.lastRefreshed.plus(interval);
            return Instant.now().isAfter(nextRefresh);
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Adds a filter to the widget
     */
    public void addFilter(String key, Object value) {
        if (this.filters == null) {
            this.filters = new HashMap<>();
        }
        this.filters.put(key, value);
    }

    /**
     * Removes a filter from the widget
     */
    public void removeFilter(String key) {
        if (this.filters != null) {
            this.filters.remove(key);
        }
    }

    /**
     * Clears all filters
     */
    public void clearFilters() {
        if (this.filters != null) {
            this.filters.clear();
        }
    }

    /**
     * Validates the widget configuration
     */
    public boolean isValidConfiguration() {
        return this.widgetType != null &&
               this.position != null &&
               this.position.row != null &&
               this.position.column != null;
    }

    private static String generateWidgetId() {
        return "WDG-" + System.currentTimeMillis() + "-" +
               Integer.toHexString((int) (Math.random() * 0xFFFF)).toUpperCase();
    }
}
