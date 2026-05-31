package com.gogidix.finance.globalfinancedashboard.infrastructure.persistence.mongodb;

import com.gogidix.finance.globalfinancedashboard.domain.model.Widget;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MongoDB document entity for storing Widget domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "widgets")
public class WidgetEntity {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    private String widgetId;
    private String dashboardId;
    private String name;
    private String description;
    private String type;
    private String status;
    private Integer position;
    private String size;
    private WidgetLocationEmbed location;
    private DataSourceEmbed dataSource;
    private MetricConfigEmbed metricConfig;
    private VisualizationConfigEmbed visualizationConfig;
    private Map<String, Object> properties;
    private RefreshConfigEmbed refreshConfig;
    private DrilldownConfigEmbed drilldownConfig;
    private Boolean allowDrilldown;
    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastRefreshedAt;

    // Default constructor for MongoDB
    public WidgetEntity() {
    }

    // Constructor from domain model
    public WidgetEntity(Widget widget) {
        this.id = widget.getId();
        this.tenantId = widget.getTenantId();
        this.widgetId = widget.getWidgetId();
        this.dashboardId = widget.getDashboardId();
        this.name = widget.getName();
        this.description = widget.getDescription();
        this.type = widget.getType() != null ? widget.getType().name() : null;
        this.status = widget.getStatus() != null ? widget.getStatus().name() : null;
        this.position = widget.getPosition();
        this.size = widget.getSize() != null ? widget.getSize().name() : null;
        this.location = widget.getLocation() != null ? new WidgetLocationEmbed(widget.getLocation()) : null;
        this.dataSource = widget.getDataSource() != null ? new DataSourceEmbed(widget.getDataSource()) : null;
        this.metricConfig = widget.getMetricConfig() != null ? new MetricConfigEmbed(widget.getMetricConfig()) : null;
        this.visualizationConfig = widget.getVisualizationConfig() != null ? new VisualizationConfigEmbed(widget.getVisualizationConfig()) : null;
        this.properties = widget.getProperties() != null ? new HashMap<>(widget.getProperties()) : new HashMap<>();
        this.refreshConfig = widget.getRefreshConfig() != null ? new RefreshConfigEmbed(widget.getRefreshConfig()) : null;
        this.drilldownConfig = widget.getDrilldownConfig() != null ? new DrilldownConfigEmbed(widget.getDrilldownConfig()) : null;
        this.allowDrilldown = widget.getAllowDrilldown();
        this.createdBy = widget.getCreatedBy();
        this.createdAt = widget.getCreatedAt();
        this.updatedAt = widget.getUpdatedAt();
        this.lastRefreshedAt = widget.getLastRefreshedAt();
    }

    // Convert to domain model
    public Widget toDomainModel() {
        return Widget.builder()
                .id(this.id)
                .tenantId(this.tenantId)
                .widgetId(this.widgetId)
                .dashboardId(this.dashboardId)
                .name(this.name)
                .description(this.description)
                .type(this.type != null ? Widget.WidgetType.valueOf(this.type) : null)
                .status(this.status != null ? Widget.WidgetStatus.valueOf(this.status) : null)
                .position(this.position)
                .size(this.size != null ? Widget.WidgetSize.valueOf(this.size) : null)
                .location(this.location != null ? this.location.toDomainModel() : null)
                .dataSource(this.dataSource != null ? this.dataSource.toDomainModel() : null)
                .metricConfig(this.metricConfig != null ? this.metricConfig.toDomainModel() : null)
                .visualizationConfig(this.visualizationConfig != null ? this.visualizationConfig.toDomainModel() : null)
                .properties(this.properties != null ? new HashMap<>(this.properties) : new HashMap<>())
                .refreshConfig(this.refreshConfig != null ? this.refreshConfig.toDomainModel() : null)
                .drilldownConfig(this.drilldownConfig != null ? this.drilldownConfig.toDomainModel() : null)
                .allowDrilldown(this.allowDrilldown)
                .createdBy(this.createdBy)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .lastRefreshedAt(this.lastRefreshedAt)
                .build();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getWidgetId() { return widgetId; }
    public void setWidgetId(String widgetId) { this.widgetId = widgetId; }

    public String getDashboardId() { return dashboardId; }
    public void setDashboardId(String dashboardId) { this.dashboardId = dashboardId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public WidgetLocationEmbed getLocation() { return location; }
    public void setLocation(WidgetLocationEmbed location) { this.location = location; }

    public DataSourceEmbed getDataSource() { return dataSource; }
    public void setDataSource(DataSourceEmbed dataSource) { this.dataSource = dataSource; }

    public MetricConfigEmbed getMetricConfig() { return metricConfig; }
    public void setMetricConfig(MetricConfigEmbed metricConfig) { this.metricConfig = metricConfig; }

    public VisualizationConfigEmbed getVisualizationConfig() { return visualizationConfig; }
    public void setVisualizationConfig(VisualizationConfigEmbed visualizationConfig) { this.visualizationConfig = visualizationConfig; }

    public Map<String, Object> getProperties() { return properties; }
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }

    public RefreshConfigEmbed getRefreshConfig() { return refreshConfig; }
    public void setRefreshConfig(RefreshConfigEmbed refreshConfig) { this.refreshConfig = refreshConfig; }

    public DrilldownConfigEmbed getDrilldownConfig() { return drilldownConfig; }
    public void setDrilldownConfig(DrilldownConfigEmbed drilldownConfig) { this.drilldownConfig = drilldownConfig; }

    public Boolean getAllowDrilldown() { return allowDrilldown; }
    public void setAllowDrilldown(Boolean allowDrilldown) { this.allowDrilldown = allowDrilldown; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public Instant getLastRefreshedAt() { return lastRefreshedAt; }
    public void setLastRefreshedAt(Instant lastRefreshedAt) { this.lastRefreshedAt = lastRefreshedAt; }

    /**
     * Embedded widget location
     */
    public static class WidgetLocationEmbed {
        private int row;
        private int column;

        public WidgetLocationEmbed() {
        }

        public WidgetLocationEmbed(Widget.WidgetLocation location) {
            this.row = location.getRow();
            this.column = location.getColumn();
        }

        public Widget.WidgetLocation toDomainModel() {
            return new Widget.WidgetLocation(row, column);
        }

        public int getRow() { return row; }
        public void setRow(int row) { this.row = row; }

        public int getColumn() { return column; }
        public void setColumn(int column) { this.column = column; }
    }

    /**
     * Embedded data source configuration
     */
    public static class DataSourceEmbed {
        private String type;
        private String endpoint;
        private String query;
        private String collection;
        private Map<String, Object> parameters;
        private List<String> fields;
        private String aggregation;
        private Long cacheDuration;

        public DataSourceEmbed() {
        }

        public DataSourceEmbed(Widget.DataSource dataSource) {
            this.type = dataSource.getType();
            this.endpoint = dataSource.getEndpoint();
            this.query = dataSource.getQuery();
            this.collection = dataSource.getCollection();
            this.parameters = dataSource.getParameters() != null ? new HashMap<>(dataSource.getParameters()) : new HashMap<>();
            this.fields = dataSource.getFields() != null ? new ArrayList<>(dataSource.getFields()) : new ArrayList<>();
            this.aggregation = dataSource.getAggregation();
            this.cacheDuration = dataSource.getCacheDuration();
        }

        public Widget.DataSource toDomainModel() {
            return Widget.DataSource.builder()
                    .type(type)
                    .endpoint(endpoint)
                    .query(query)
                    .collection(collection)
                    .parameters(parameters != null ? new HashMap<>(parameters) : new HashMap<>())
                    .fields(fields != null ? new ArrayList<>(fields) : new ArrayList<>())
                    .aggregation(aggregation)
                    .cacheDuration(cacheDuration)
                    .build();
        }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getEndpoint() { return endpoint; }
        public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

        public String getQuery() { return query; }
        public void setQuery(String query) { this.query = query; }

        public String getCollection() { return collection; }
        public void setCollection(String collection) { this.collection = collection; }

        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }

        public List<String> getFields() { return fields; }
        public void setFields(List<String> fields) { this.fields = fields; }

        public String getAggregation() { return aggregation; }
        public void setAggregation(String aggregation) { this.aggregation = aggregation; }

        public Long getCacheDuration() { return cacheDuration; }
        public void setCacheDuration(Long cacheDuration) { this.cacheDuration = cacheDuration; }
    }

    /**
     * Embedded metric configuration
     */
    public static class MetricConfigEmbed {
        private String metricType;
        private String aggregationType;
        private List<String> metricFields;
        private String groupByField;
        private String timeField;
        private String timeGrain;
        private String filterExpression;
        private Map<String, String> aliases;
        private List<CalculationEmbed> calculations;

        public MetricConfigEmbed() {
        }

        public MetricConfigEmbed(Widget.MetricConfig config) {
            this.metricType = config.getMetricType();
            this.aggregationType = config.getAggregationType();
            this.metricFields = config.getMetricFields() != null ? new ArrayList<>(config.getMetricFields()) : new ArrayList<>();
            this.groupByField = config.getGroupByField();
            this.timeField = config.getTimeField();
            this.timeGrain = config.getTimeGrain();
            this.filterExpression = config.getFilterExpression();
            this.aliases = config.getAliases() != null ? new HashMap<>(config.getAliases()) : new HashMap<>();
            this.calculations = new ArrayList<>();
            if (config.getCalculations() != null) {
                for (Widget.MetricConfig.Calculation calc : config.getCalculations()) {
                    this.calculations.add(new CalculationEmbed(calc));
                }
            }
        }

        public Widget.MetricConfig toDomainModel() {
            return new Widget.MetricConfig();
        }

        public String getMetricType() { return metricType; }
        public void setMetricType(String metricType) { this.metricType = metricType; }

        public String getAggregationType() { return aggregationType; }
        public void setAggregationType(String aggregationType) { this.aggregationType = aggregationType; }

        public List<String> getMetricFields() { return metricFields; }
        public void setMetricFields(List<String> metricFields) { this.metricFields = metricFields; }

        public String getGroupByField() { return groupByField; }
        public void setGroupByField(String groupByField) { this.groupByField = groupByField; }

        public String getTimeField() { return timeField; }
        public void setTimeField(String timeField) { this.timeField = timeField; }

        public String getTimeGrain() { return timeGrain; }
        public void setTimeGrain(String timeGrain) { this.timeGrain = timeGrain; }

        public String getFilterExpression() { return filterExpression; }
        public void setFilterExpression(String filterExpression) { this.filterExpression = filterExpression; }

        public Map<String, String> getAliases() { return aliases; }
        public void setAliases(Map<String, String> aliases) { this.aliases = aliases; }

        public List<CalculationEmbed> getCalculations() { return calculations; }
        public void setCalculations(List<CalculationEmbed> calculations) { this.calculations = calculations; }

        public static class CalculationEmbed {
            private String name;
            private String formula;
            private List<String> dependencies;

            public CalculationEmbed() {
            }

            public CalculationEmbed(Widget.MetricConfig.Calculation calculation) {
                this.name = calculation.getName();
                this.formula = calculation.getFormula();
                this.dependencies = calculation.getDependencies() != null ? new ArrayList<>(calculation.getDependencies()) : new ArrayList<>();
            }

            public String getName() { return name; }
            public void setName(String name) { this.name = name; }

            public String getFormula() { return formula; }
            public void setFormula(String formula) { this.formula = formula; }

            public List<String> getDependencies() { return dependencies; }
            public void setDependencies(List<String> dependencies) { this.dependencies = dependencies; }
        }
    }

    /**
     * Embedded visualization configuration
     */
    public static class VisualizationConfigEmbed {
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

        public VisualizationConfigEmbed() {
        }

        public VisualizationConfigEmbed(Widget.VisualizationConfig config) {
            this.chartType = config.getChartType();
            this.xAxis = config.getXAxis();
            this.yAxis = config.getYAxis();
            this.colorBy = config.getColorBy();
            this.seriesBy = config.getSeriesBy();
            this.showLegend = config.getShowLegend();
            this.showDataLabels = config.getShowDataLabels();
            this.showGridLines = config.getShowGridLines();
            this.colorScheme = config.getColorScheme();
            this.maxDataPoints = config.getMaxDataPoints();
            this.sortOrder = config.getSortOrder();
            this.limit = config.getLimit();
            this.customOptions = config.getCustomOptions() != null ? new HashMap<>(config.getCustomOptions()) : new HashMap<>();
        }

        public Widget.VisualizationConfig toDomainModel() {
            return Widget.VisualizationConfig.builder()
                    .chartType(chartType)
                    .xAxis(xAxis)
                    .yAxis(yAxis)
                    .colorBy(colorBy)
                    .seriesBy(seriesBy)
                    .showLegend(showLegend)
                    .showDataLabels(showDataLabels)
                    .showGridLines(showGridLines)
                    .colorScheme(colorScheme)
                    .maxDataPoints(maxDataPoints)
                    .sortOrder(sortOrder)
                    .limit(limit)
                    .customOptions(customOptions != null ? new HashMap<>(customOptions) : new HashMap<>())
                    .build();
        }

        public String getChartType() { return chartType; }
        public void setChartType(String chartType) { this.chartType = chartType; }

        public String getXAxis() { return xAxis; }
        public void setXAxis(String xAxis) { this.xAxis = xAxis; }

        public String getYAxis() { return yAxis; }
        public void setYAxis(String yAxis) { this.yAxis = yAxis; }

        public String getColorBy() { return colorBy; }
        public void setColorBy(String colorBy) { this.colorBy = colorBy; }

        public String getSeriesBy() { return seriesBy; }
        public void setSeriesBy(String seriesBy) { this.seriesBy = seriesBy; }

        public Boolean getShowLegend() { return showLegend; }
        public void setShowLegend(Boolean showLegend) { this.showLegend = showLegend; }

        public Boolean getShowDataLabels() { return showDataLabels; }
        public void setShowDataLabels(Boolean showDataLabels) { this.showDataLabels = showDataLabels; }

        public Boolean getShowGridLines() { return showGridLines; }
        public void setShowGridLines(Boolean showGridLines) { this.showGridLines = showGridLines; }

        public String getColorScheme() { return colorScheme; }
        public void setColorScheme(String colorScheme) { this.colorScheme = colorScheme; }

        public Integer getMaxDataPoints() { return maxDataPoints; }
        public void setMaxDataPoints(Integer maxDataPoints) { this.maxDataPoints = maxDataPoints; }

        public String getSortOrder() { return sortOrder; }
        public void setSortOrder(String sortOrder) { this.sortOrder = sortOrder; }

        public Integer getLimit() { return limit; }
        public void setLimit(Integer limit) { this.limit = limit; }

        public Map<String, Object> getCustomOptions() { return customOptions; }
        public void setCustomOptions(Map<String, Object> customOptions) { this.customOptions = customOptions; }
    }

    /**
     * Embedded refresh configuration
     */
    public static class RefreshConfigEmbed {
        private Boolean autoRefresh;
        private Integer intervalSeconds;
        private Boolean refreshOnLoad;

        public RefreshConfigEmbed() {
        }

        public RefreshConfigEmbed(Widget.RefreshConfig config) {
            this.autoRefresh = config.getAutoRefresh();
            this.intervalSeconds = config.getIntervalSeconds();
            this.refreshOnLoad = config.getRefreshOnLoad();
        }

        public Widget.RefreshConfig toDomainModel() {
            return Widget.RefreshConfig.builder()
                    .autoRefresh(autoRefresh)
                    .intervalSeconds(intervalSeconds)
                    .refreshOnLoad(refreshOnLoad)
                    .build();
        }

        public Boolean getAutoRefresh() { return autoRefresh; }
        public void setAutoRefresh(Boolean autoRefresh) { this.autoRefresh = autoRefresh; }

        public Integer getIntervalSeconds() { return intervalSeconds; }
        public void setIntervalSeconds(Integer intervalSeconds) { this.intervalSeconds = intervalSeconds; }

        public Boolean getRefreshOnLoad() { return refreshOnLoad; }
        public void setRefreshOnLoad(Boolean refreshOnLoad) { this.refreshOnLoad = refreshOnLoad; }
    }

    /**
     * Embedded drilldown configuration
     */
    public static class DrilldownConfigEmbed {
        private String targetDashboard;
        private String targetWidget;
        private Map<String, String> parameterMapping;
        private Boolean openInNewTab;

        public DrilldownConfigEmbed() {
        }

        public DrilldownConfigEmbed(Widget.DrilldownConfig config) {
            this.targetDashboard = config.getTargetDashboard();
            this.targetWidget = config.getTargetWidget();
            this.parameterMapping = config.getParameterMapping() != null ? new HashMap<>(config.getParameterMapping()) : new HashMap<>();
            this.openInNewTab = config.getOpenInNewTab();
        }

        public Widget.DrilldownConfig toDomainModel() {
            return Widget.DrilldownConfig.builder()
                    .targetDashboard(targetDashboard)
                    .targetWidget(targetWidget)
                    .parameterMapping(parameterMapping != null ? new HashMap<>(parameterMapping) : new HashMap<>())
                    .openInNewTab(openInNewTab)
                    .build();
        }

        public String getTargetDashboard() { return targetDashboard; }
        public void setTargetDashboard(String targetDashboard) { this.targetDashboard = targetDashboard; }

        public String getTargetWidget() { return targetWidget; }
        public void setTargetWidget(String targetWidget) { this.targetWidget = targetWidget; }

        public Map<String, String> getParameterMapping() { return parameterMapping; }
        public void setParameterMapping(Map<String, String> parameterMapping) { this.parameterMapping = parameterMapping; }

        public Boolean getOpenInNewTab() { return openInNewTab; }
        public void setOpenInNewTab(Boolean openInNewTab) { this.openInNewTab = openInNewTab; }
    }
}
