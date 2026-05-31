package com.gogidix.finance.globalfinancedashboard.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.gogidix.finance.globalfinancedashboard.domain.model.DashboardWidget;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Dashboard Widget Response DTO
 * Represents a dashboard widget response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardWidgetResponseDto {

    private String id;
    private String widgetId;
    private String tenantId;
    private String dashboardId;
    private String name;
    private String description;
    private String type;
    private String status;
    private Integer position;
    private WidgetSizeDto size;
    private WidgetLocationDto location;
    private DataSourceDto dataSource;
    private MetricConfigDto metricConfig;
    private VisualizationConfigDto visualizationConfig;
    private Map<String, Object> properties;
    private RefreshConfigDto refreshConfig;
    private DrilldownConfigDto drilldownConfig;
    private Boolean allowDrilldown;
    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant lastRefreshedAt;

    /**
     * Widget size DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class WidgetSizeDto {
        private String name;
        private Integer width;
        private Integer height;
    }

    /**
     * Widget location DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class WidgetLocationDto {
        private Integer row;
        private Integer column;
    }

    /**
     * Data source DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DataSourceDto {
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
     * Metric configuration DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MetricConfigDto {
        private String metricType;
        private String aggregationType;
        private List<String> metricFields;
        private String groupByField;
        private String timeField;
        private String timeGrain;
        private String filterExpression;
        private Map<String, String> aliases;
        private List<CalculationDto> calculations;
    }

    /**
     * Calculation DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CalculationDto {
        private String name;
        private String formula;
        private List<String> dependencies;
    }

    /**
     * Visualization configuration DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class VisualizationConfigDto {
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
    }

    /**
     * Refresh configuration DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RefreshConfigDto {
        private Boolean autoRefresh;
        private Integer intervalSeconds;
        private Boolean refreshOnLoad;
    }

    /**
     * Drilldown configuration DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DrilldownConfigDto {
        private String targetDashboard;
        private String targetWidget;
        private Map<String, String> parameterMapping;
        private Boolean openInNewTab;
    }

    /**
     * Converts from domain entity to DTO
     */
    public static DashboardWidgetResponseDto fromEntity(DashboardWidget widget) {
        return DashboardWidgetResponseDto.builder()
                .id(widget.getId())
                .widgetId(widget.getWidgetId())
                .tenantId(widget.getTenantId())
                .dashboardId(widget.getDashboardId())
                .name(widget.getName())
                .description(widget.getDescription())
                .type(widget.getType() != null ? widget.getType().name() : null)
                .status(widget.getStatus() != null ? widget.getStatus().name() : null)
                .position(widget.getPosition())
                .size(widget.getSize() != null
                        ? WidgetSizeDto.builder()
                                .name(widget.getSize().name())
                                .width(widget.getSize().getWidth())
                                .height(widget.getSize().getHeight())
                                .build()
                        : null)
                .location(widget.getLocation() != null
                        ? WidgetLocationDto.builder()
                                .row(widget.getLocation().getRow())
                                .column(widget.getLocation().getColumn())
                                .build()
                        : null)
                .dataSource(mapDataSource(widget.getDataSource()))
                .metricConfig(mapMetricConfig(widget.getMetricConfig()))
                .visualizationConfig(mapVisualizationConfig(widget.getVisualizationConfig()))
                .properties(widget.getProperties())
                .refreshConfig(mapRefreshConfig(widget.getRefreshConfig()))
                .drilldownConfig(mapDrilldownConfig(widget.getDrilldownConfig()))
                .allowDrilldown(widget.getAllowDrilldown())
                .createdBy(widget.getCreatedBy())
                .createdAt(widget.getCreatedAt())
                .updatedAt(widget.getUpdatedAt())
                .lastRefreshedAt(widget.getLastRefreshedAt())
                .build();
    }

    private static DataSourceDto mapDataSource(DashboardWidget.DataSource source) {
        if (source == null) return null;
        return DataSourceDto.builder()
                .type(source.getType())
                .endpoint(source.getEndpoint())
                .query(source.getQuery())
                .collection(source.getCollection())
                .parameters(source.getParameters())
                .fields(source.getFields())
                .aggregation(source.getAggregation())
                .cacheDuration(source.getCacheDuration())
                .build();
    }

    private static MetricConfigDto mapMetricConfig(DashboardWidget.MetricConfig config) {
        if (config == null) return null;
        return MetricConfigDto.builder()
                .metricType(config.getMetricType())
                .aggregationType(config.getAggregationType())
                .metricFields(config.getMetricFields())
                .groupByField(config.getGroupByField())
                .timeField(config.getTimeField())
                .timeGrain(config.getTimeGrain())
                .filterExpression(config.getFilterExpression())
                .aliases(config.getAliases())
                .calculations(config.getCalculations() != null
                        ? config.getCalculations().stream()
                            .map(c -> CalculationDto.builder()
                                    .name(c.getName())
                                    .formula(c.getFormula())
                                    .dependencies(c.getDependencies())
                                    .build())
                            .toList()
                        : null)
                .build();
    }

    private static VisualizationConfigDto mapVisualizationConfig(DashboardWidget.VisualizationConfig config) {
        if (config == null) return null;
        return VisualizationConfigDto.builder()
                .chartType(config.getChartType())
                .xAxis(config.getXAxis())
                .yAxis(config.getYAxis())
                .colorBy(config.getColorBy())
                .seriesBy(config.getSeriesBy())
                .showLegend(config.getShowLegend())
                .showDataLabels(config.getShowDataLabels())
                .showGridLines(config.getShowGridLines())
                .colorScheme(config.getColorScheme())
                .maxDataPoints(config.getMaxDataPoints())
                .sortOrder(config.getSortOrder())
                .limit(config.getLimit())
                .customOptions(config.getCustomOptions())
                .build();
    }

    private static RefreshConfigDto mapRefreshConfig(DashboardWidget.RefreshConfig config) {
        if (config == null) return null;
        return RefreshConfigDto.builder()
                .autoRefresh(config.getAutoRefresh())
                .intervalSeconds(config.getIntervalSeconds())
                .refreshOnLoad(config.getRefreshOnLoad())
                .build();
    }

    private static DrilldownConfigDto mapDrilldownConfig(DashboardWidget.DrilldownConfig config) {
        if (config == null) return null;
        return DrilldownConfigDto.builder()
                .targetDashboard(config.getTargetDashboard())
                .targetWidget(config.getTargetWidget())
                .parameterMapping(config.getParameterMapping())
                .openInNewTab(config.getOpenInNewTab())
                .build();
    }
}
