package com.gogidix.globalbusinessmanagement.regionaldashboard.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Domain model representing a dashboard widget definition.
 * Stores widget configuration for reuse across dashboards.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "widgets")
public class Widget {

    @Id
    private String id;

    @NotBlank(message = "Widget ID is required")
    @Indexed(unique = true)
    private String widgetId;

    @NotBlank(message = "Widget name is required")
    @Indexed
    private String name;

    private String description;

    @NotNull(message = "Widget type is required")
    @Indexed
    private WidgetType widgetType;

    @NotNull(message = "Data source is required")
    @Valid
    private DataSource dataSource;

    @Valid
    private WidgetConfig config;

    @Valid
    private WidgetStyle style;

    @Valid
    private WidgetBehavior behavior;

    @Valid
    private List<WidgetParameter> parameters;

    @Valid
    private List<WidgetFilter> filters;

    @NotNull(message = "Widget status is required")
    @Builder.Default
    @Indexed
    private WidgetStatus status = WidgetStatus.ACTIVE;

    @Indexed
    private String category;

    @Indexed
    private List<String> tags;

    @Indexed
    private Boolean isPublic;

    @Indexed
    private String createdBy;

    @Indexed
    private List<String> allowedRoles;

    @Indexed
    private String regionCode;

    private String templateId;

    @Indexed
    private Integer version;

    private Map<String, Object> metadata;

    @Indexed
    private Instant createdAt;

    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataSource {
        @NotBlank(message = "Source type is required")
        private String sourceType;

        private String endpoint;

        private String query;

        private String collection;

        private String method;

        private Map<String, Object> headers;

        private Map<String, Object> body;

        private String cacheKey;

        private Integer cacheTtl;

        private Integer timeout;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WidgetConfig {
        private String chartType;

        private Integer refreshInterval;

        private Boolean autoRefresh;

        private Boolean showLegend;

        private Boolean showGrid;

        private Boolean showLabels;

        private Boolean showTooltip;

        private String xAxis;

        private String yAxis;

        private String groupBy;

        private String aggregateBy;

        private String timeGranularity;

        private Integer limit;

        private String sortBy;

        private String sortOrder;

        private Map<String, Object> customConfig;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WidgetStyle {
        private String backgroundColor;

        private String borderColor;

        private Integer borderWidth;

        private String borderRadius;

        private Integer padding;

        private Integer margin;

        private Integer height;

        private Integer width;

        private String titleColor;

        private Integer titleSize;

        private String fontFamily;

        private Map<String, String> customStyles;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WidgetBehavior {
        private Boolean isDraggable;

        private Boolean isResizable;

        private Boolean isCollapsible;

        private Boolean isClosable;

        private Boolean isEditable;

        private Boolean isExportable;

        private List<String> exportFormats;

        private Boolean enableDrillDown;

        private String drillDownTarget;

        private Boolean enableZoom;

        private Boolean enablePan;

        private Map<String, Object> customBehavior;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WidgetParameter {
        @NotBlank(message = "Parameter name is required")
        private String name;

        private String displayName;

        private String type;

        private String defaultValue;

        private Boolean isRequired;

        private List<String> options;

        private String validation;

        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WidgetFilter {
        @NotBlank(message = "Filter name is required")
        private String name;

        private String field;

        private String operator;

        private String value;

        private String type;

        private Boolean isDynamic;

        private String dataSource;
    }

    public enum WidgetType {
        SINGLE_VALUE,
        LINE_CHART,
        BAR_CHART,
        PIE_CHART,
        DONUT_CHART,
        AREA_CHART,
        SCATTER_CHART,
        TABLE,
        GRID,
        MAP,
        HEATMAP,
        GAUGE,
        PROGRESS_BAR,
        METRIC_CARD,
        TREND,
        COMPARISON,
        LIST,
        KPI_CARD,
        SPARKLINE,
        FUNNEL,
        SANKEY,
        TREE_MAP,
        CUSTOM
    }

    public enum WidgetStatus {
        ACTIVE,
        INACTIVE,
        DEPRECATED,
        DRAFT
    }

    public boolean isActive() {
        return WidgetStatus.ACTIVE.equals(status);
    }

    public boolean isPublicWidget() {
        return Boolean.TRUE.equals(isPublic);
    }

    public boolean canAccess(String userId, List<String> userRoles) {
        if (isPublicWidget()) {
            return true;
        }
        if (createdBy != null && createdBy.equals(userId)) {
            return true;
        }
        if (allowedRoles != null && userRoles != null) {
            return userRoles.stream().anyMatch(allowedRoles::contains);
        }
        return false;
    }

    public DataSource getSource() {
        return dataSource;
    }

    public WidgetParameter getParameter(String name) {
        return parameters != null ?
            parameters.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElse(null) :
            null;
    }

    public boolean hasParameter(String name) {
        return getParameter(name) != null;
    }
}
