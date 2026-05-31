package com.gogidix.finance.globalfinancedashboard.domain.model;

import com.gogidix.finance.globalfinancedashboard.domain.event.DashboardEvent;
import com.gogidix.finance.globalfinancedashboard.domain.event.WidgetCreatedEvent;
import com.gogidix.finance.globalfinancedashboard.domain.event.WidgetUpdatedEvent;
import com.gogidix.finance.globalfinancedashboard.shared.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Dashboard Widget Domain Entity
 * Represents a visual component for displaying financial data on a dashboard
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Document(collection = "dashboard_widgets")
public class DashboardWidget {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    private String widgetId;

    private String dashboardId;

    private String name;

    private String description;

    private WidgetType type;

    private WidgetStatus status;

    private Integer position;

    private WidgetSize size;

    private WidgetLocation location;

    private DataSource dataSource;

    private MetricConfig metricConfig;

    private VisualizationConfig visualizationConfig;

    private Map<String, Object> properties;

    private RefreshConfig refreshConfig;

    private DrilldownConfig drilldownConfig;

    private Boolean allowDrilldown;

    private String createdBy;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant lastRefreshedAt;

    @Builder.Default
    private List<DashboardEvent> domainEvents = new ArrayList<>();

    public enum WidgetType {
        CHART_LINE,
        CHART_BAR,
        CHART_PIE,
        CHART_AREA,
        CHART_DONUT,
        CHART_SCATTER,
        KPI_CARD,
        KPI_NUMBER,
        KPI_PROGRESS,
        GAUGE,
        TABLE,
        TABLE_PIVOT,
        HEATMAP,
        TREEMAP,
        FUNNEL,
        GEOMAP,
        SPARKLINE,
        INDICATOR,
        COMPARISON,
        TREND,
        METRIC_SUMMARY,
        CUSTOM
    }

    public enum WidgetStatus {
        ACTIVE,
        HIDDEN,
        ERROR,
        LOADING,
        DISABLED
    }

    /**
     * Creates a new dashboard widget
     */
    public static DashboardWidget create(String tenantId, String dashboardId, String name,
                                         WidgetType type, DataSource dataSource) {
        DashboardWidget widget = DashboardWidget.builder()
                .widgetId(generateWidgetId())
                .tenantId(Objects.requireNonNull(tenantId, "tenantId is required"))
                .dashboardId(Objects.requireNonNull(dashboardId, "dashboardId is required"))
                .name(Objects.requireNonNull(name, "name is required"))
                .type(Objects.requireNonNull(type, "type is required"))
                .status(WidgetStatus.ACTIVE)
                .position(1)
                .size(WidgetSize.MEDIUM)
                .location(new WidgetLocation(0, 0))
                .dataSource(dataSource)
                .metricConfig(new MetricConfig())
                .visualizationConfig(VisualizationConfig.createDefaultForType(type))
                .properties(new HashMap<>())
                .refreshConfig(new RefreshConfig())
                .allowDrilldown(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        widget.addDomainEvent(WidgetCreatedEvent.builder()
                .widgetId(widget.getWidgetId())
                .tenantId(tenantId)
                .dashboardId(dashboardId)
                .name(name)
                .type(type.name())
                .timestamp(Instant.now())
                .eventType("WIDGET_CREATED")
                .build());

        return widget;
    }

    /**
     * Updates widget information
     */
    public void updateInfo(String name, String description) {
        this.name = name != null ? name : this.name;
        this.description = description;
        this.updatedAt = Instant.now();

        addDomainEvent(WidgetUpdatedEvent.builder()
                .widgetId(this.widgetId)
                .tenantId(this.tenantId)
                .dashboardId(this.dashboardId)
                .timestamp(Instant.now())
                .eventType("WIDGET_INFO_UPDATED")
                .build());
    }

    /**
     * Updates widget size
     */
    public void updateSize(WidgetSize size) {
        this.size = Objects.requireNonNull(size, "size is required");
        this.updatedAt = Instant.now();
    }

    /**
     * Updates widget location
     */
    public void updateLocation(int row, int column) {
        if (this.location == null) {
            this.location = new WidgetLocation(row, column);
        } else {
            this.location.row = row;
            this.location.column = column;
        }
        this.updatedAt = Instant.now();
    }

    /**
     * Updates data source
     */
    public void updateDataSource(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource, "dataSource is required");
        this.updatedAt = Instant.now();
    }

    /**
     * Updates metric configuration
     */
    public void updateMetricConfig(MetricConfig config) {
        this.metricConfig = Objects.requireNonNull(config, "config is required");
        this.updatedAt = Instant.now();
    }

    /**
     * Updates visualization configuration
     */
    public void updateVisualizationConfig(VisualizationConfig config) {
        this.visualizationConfig = Objects.requireNonNull(config, "config is required");
        this.updatedAt = Instant.now();
    }

    /**
     * Sets a property value
     */
    public void setProperty(String key, Object value) {
        if (this.properties == null) {
            this.properties = new HashMap<>();
        }
        this.properties.put(key, value);
        this.updatedAt = Instant.now();
    }

    /**
     * Gets a property value
     */
    @SuppressWarnings("unchecked")
    public <T> T getProperty(String key, Class<T> type) {
        if (this.properties == null) {
            return null;
        }
        Object value = this.properties.get(key);
        if (value != null && type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

    /**
     * Updates refresh configuration
     */
    public void updateRefreshConfig(RefreshConfig config) {
        this.refreshConfig = Objects.requireNonNull(config, "config is required");
        this.updatedAt = Instant.now();
    }

    /**
     * Enables drilldown
     */
    public void enableDrilldown(DrilldownConfig config) {
        this.allowDrilldown = true;
        this.drilldownConfig = config;
        this.updatedAt = Instant.now();
    }

    /**
     * Disables drilldown
     */
    public void disableDrilldown() {
        this.allowDrilldown = false;
        this.drilldownConfig = null;
        this.updatedAt = Instant.now();
    }

    /**
     * Marks widget as active
     */
    public void activate() {
        this.status = WidgetStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    /**
     * Marks widget as hidden
     */
    public void hide() {
        this.status = WidgetStatus.HIDDEN;
        this.updatedAt = Instant.now();
    }

    /**
     * Marks widget as disabled
     */
    public void disable() {
        this.status = WidgetStatus.DISABLED;
        this.updatedAt = Instant.now();
    }

    /**
     * Marks widget as in error state
     */
    public void markAsError(String errorMessage) {
        this.status = WidgetStatus.ERROR;
        setProperty("errorMessage", errorMessage);
        this.updatedAt = Instant.now();
    }

    /**
     * Marks widget as loading
     */
    public void markAsLoading() {
        this.status = WidgetStatus.LOADING;
        this.updatedAt = Instant.now();
    }

    /**
     * Records widget refresh
     */
    public void recordRefresh() {
        this.lastRefreshedAt = Instant.now();
        if (this.status == WidgetStatus.LOADING || this.status == WidgetStatus.ERROR) {
            this.status = WidgetStatus.ACTIVE;
        }
    }

    /**
     * Validates widget configuration
     */
    public void validate() {
        if (this.name == null || this.name.isBlank()) {
            throw new ValidationException("name", "Widget name is required");
        }
        if (this.type == null) {
            throw new ValidationException("type", "Widget type is required");
        }
        if (this.dataSource == null) {
            throw new ValidationException("dataSource", "Data source is required");
        }
    }

    private static String generateWidgetId() {
        return "DWID-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public void addDomainEvent(DashboardEvent event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }

    /**
     * Widget size enumeration
     */
    public enum WidgetSize {
        SMALL(1, 1),
        MEDIUM(2, 1),
        LARGE(2, 2),
        XLARGE(3, 2),
        WIDE(3, 1),
        TALL(1, 2),
        FULL(3, 3);

        private final int width;
        private final int height;

        WidgetSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() { return width; }
        public int getHeight() { return height; }
    }

    /**
     * Widget location
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WidgetLocation {
        private int row;
        private int column;
    }

    /**
     * Data source configuration
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataSource {
        private String type;
        private String endpoint;
        private String query;
        private String collection;
        private Map<String, Object> parameters;
        private List<String> fields;
        private String aggregation;
        private Long cacheDuration;
    }

    /**
     * Metric configuration
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetricConfig {
        private String metricType;
        private String aggregationType;
        private List<String> metricFields;
        private String groupByField;
        private String timeField;
        private String timeGrain;
        private String filterExpression;
        private Map<String, String> aliases;
        private List<Calculation> calculations;

        public enum AggregationType {
            SUM, AVG, COUNT, MIN, MAX, MEDIAN, STD_DEV, PERCENTILE
        }

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Calculation {
            private String name;
            private String formula;
            private List<String> dependencies;
        }
    }

    /**
     * Visualization configuration
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VisualizationConfig {
        private String chartType;
        private String xAxis;
        private String yAxis;
        private String colorBy;
        private String seriesBy;
        private Boolean showLegend;
        private Boolean showDataLabels;
        private Boolean showGridLines;
        private String colorScheme;
        private Integer maxDataPoints;
        private String sortOrder;
        private Integer limit;
        private Map<String, Object> customOptions;

        public static VisualizationConfig createDefaultForType(WidgetType type) {
            VisualizationConfigBuilder builder = VisualizationConfig.builder()
                    .showLegend(true)
                    .showDataLabels(false)
                    .showGridLines(true)
                    .colorScheme("default")
                    .sortOrder("desc")
                    .limit(100);

            switch (type) {
                case KPI_CARD:
                case KPI_NUMBER:
                case METRIC_SUMMARY:
                    return builder
                            .chartType("kpi")
                            .showLegend(false)
                            .showDataLabels(true)
                            .build();
                case GAUGE:
                    return builder
                            .chartType("gauge")
                            .showLegend(false)
                            .build();
                case TABLE:
                case TABLE_PIVOT:
                    return builder
                            .chartType("table")
                            .showGridLines(true)
                            .limit(50)
                            .build();
                case CHART_LINE:
                case CHART_AREA:
                    return builder
                            .chartType("line")
                            .maxDataPoints(100)
                            .build();
                case CHART_BAR:
                    return builder
                            .chartType("bar")
                            .maxDataPoints(50)
                            .build();
                case CHART_PIE:
                case CHART_DONUT:
                    return builder
                            .chartType("pie")
                            .showDataLabels(true)
                            .maxDataPoints(10)
                            .build();
                default:
                    return builder.build();
            }
        }
    }

    /**
     * Refresh configuration
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefreshConfig {
        private Boolean autoRefresh;
        private Integer intervalSeconds;
        private Boolean refreshOnLoad;
    }

    /**
     * Drilldown configuration
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DrilldownConfig {
        private String targetDashboard;
        private String targetWidget;
        private Map<String, String> parameterMapping;
        private Boolean openInNewTab;
    }
}
