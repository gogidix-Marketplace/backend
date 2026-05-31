package com.gogidix.finance.globalfinancedashboard.domain.model;

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
 * Dashboard Layout Domain Entity
 * Represents the layout configuration for dashboard widgets
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Document(collection = "dashboard_layouts")
public class DashboardLayout {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    private String layoutId;

    private String dashboardId;

    private String name;

    private String description;

    private LayoutType layoutType;

    private LayoutConfig config;

    private List<WidgetPosition> widgetPositions;

    private ResponsiveBreakpoints responsiveBreakpoints;

    private String theme;

    private Map<String, Object> customSettings;

    private Boolean isDefault;

    private String createdBy;

    private Instant createdAt;

    private Instant updatedAt;

    public enum LayoutType {
        GRID,
        FLEX,
        ABSOLUTE,
        MASONRY,
        TABS,
        ACCORDION,
        CUSTOM
    }

    /**
     * Creates a new dashboard layout
     */
    public static DashboardLayout create(String tenantId, String dashboardId,
                                         String name, LayoutType layoutType) {
        DashboardLayout layout = DashboardLayout.builder()
                .layoutId(generateLayoutId())
                .tenantId(Objects.requireNonNull(tenantId, "tenantId is required"))
                .dashboardId(Objects.requireNonNull(dashboardId, "dashboardId is required"))
                .name(Objects.requireNonNull(name, "name is required"))
                .layoutType(layoutType != null ? layoutType : LayoutType.GRID)
                .config(LayoutConfig.createDefault())
                .widgetPositions(new ArrayList<>())
                .responsiveBreakpoints(new ResponsiveBreakpoints())
                .theme("default")
                .customSettings(new HashMap<>())
                .isDefault(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return layout;
    }

    /**
     * Updates layout configuration
     */
    public void updateConfig(LayoutConfig config) {
        this.config = Objects.requireNonNull(config, "config is required");
        this.updatedAt = Instant.now();
    }

    /**
     * Adds a widget position
     */
    public void addWidgetPosition(WidgetPosition position) {
        if (this.widgetPositions == null) {
            this.widgetPositions = new ArrayList<>();
        }
        this.widgetPositions.add(position);
        this.updatedAt = Instant.now();
    }

    /**
     * Removes a widget position
     */
    public void removeWidgetPosition(String widgetId) {
        if (this.widgetPositions != null) {
            this.widgetPositions.removeIf(wp -> wp.getWidgetId().equals(widgetId));
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Updates widget position
     */
    public void updateWidgetPosition(String widgetId, WidgetPosition newPosition) {
        if (this.widgetPositions != null) {
            for (int i = 0; i < this.widgetPositions.size(); i++) {
                if (this.widgetPositions.get(i).getWidgetId().equals(widgetId)) {
                    this.widgetPositions.set(i, newPosition);
                    this.updatedAt = Instant.now();
                    return;
                }
            }
        }
    }

    /**
     * Updates responsive breakpoints
     */
    public void updateResponsiveBreakpoints(ResponsiveBreakpoints breakpoints) {
        this.responsiveBreakpoints = Objects.requireNonNull(breakpoints,
                "breakpoints is required");
        this.updatedAt = Instant.now();
    }

    /**
     * Sets a custom setting
     */
    public void setCustomSetting(String key, Object value) {
        if (this.customSettings == null) {
            this.customSettings = new HashMap<>();
        }
        this.customSettings.put(key, value);
        this.updatedAt = Instant.now();
    }

    /**
     * Gets a custom setting
     */
    @SuppressWarnings("unchecked")
    public <T> T getCustomSetting(String key, Class<T> type) {
        if (this.customSettings == null) {
            return null;
        }
        Object value = this.customSettings.get(key);
        if (value != null && type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

    /**
     * Sets as default layout
     */
    public void setAsDefault() {
        this.isDefault = true;
        this.updatedAt = Instant.now();
    }

    /**
     * Validates the layout configuration
     */
    public void validate() {
        if (this.name == null || this.name.isBlank()) {
            throw new com.gogidix.finance.globalfinancedashboard.shared.exception.ValidationException(
                    "name", "Layout name is required");
        }
        if (this.layoutType == null) {
            throw new com.gogidix.finance.globalfinancedashboard.shared.exception.ValidationException(
                    "layoutType", "Layout type is required");
        }
        if (this.config == null) {
            throw new com.gogidix.finance.globalfinancedashboard.shared.exception.ValidationException(
                    "config", "Layout configuration is required");
        }
    }

    private static String generateLayoutId() {
        return "LAY-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Layout configuration
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LayoutConfig {
        private Integer columns;
        private Integer rows;
        private Integer rowHeight;
        private Integer margin;
        private Integer padding;
        private String gap;
        private Boolean isDraggable;
        private Boolean isResizable;
        private Boolean autoArrange;
        private String alignment;
        private String direction;
        private Boolean wrapContent;
        private Integer minWidth;
        private Integer maxWidth;
        private Integer minHeight;
        private Integer maxHeight;

        public static LayoutConfig createDefault() {
            return LayoutConfig.builder()
                    .columns(12)
                    .rows(null)
                    .rowHeight(100)
                    .margin(10)
                    .padding(10)
                    .gap("10px")
                    .isDraggable(true)
                    .isResizable(true)
                    .autoArrange(false)
                    .alignment("start")
                    .direction("row")
                    .wrapContent(true)
                    .minWidth(300)
                    .maxWidth(null)
                    .minHeight(200)
                    .maxHeight(null)
                    .build();
        }
    }

    /**
     * Widget position
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WidgetPosition {
        private String widgetId;
        private Integer column;
        private Integer row;
        private Integer width;
        private Integer height;
        private Integer zIndex;
        private Boolean isPinned;
        private Boolean isHidden;
        private Map<String, Object> customProperties;

        public static WidgetPosition create(String widgetId, int column, int row,
                                           int width, int height) {
            return WidgetPosition.builder()
                    .widgetId(widgetId)
                    .column(column)
                    .row(row)
                    .width(width)
                    .height(height)
                    .zIndex(1)
                    .isPinned(false)
                    .isHidden(false)
                    .customProperties(new HashMap<>())
                    .build();
        }
    }

    /**
     * Responsive breakpoints
     */
    @Data
    @Builder
    @AllArgsConstructor
    public static class ResponsiveBreakpoints {
        private Breakpoint xs;
        private Breakpoint sm;
        private Breakpoint md;
        private Breakpoint lg;
        private Breakpoint xl;
        private Breakpoint xxl;

        public ResponsiveBreakpoints() {
            this.xs = new Breakpoint(0, 3);
            this.sm = new Breakpoint(576, 6);
            this.md = new Breakpoint(768, 9);
            this.lg = new Breakpoint(992, 12);
            this.xl = new Breakpoint(1200, 12);
            this.xxl = new Breakpoint(1400, 12);
        }
    }

    /**
     * Breakpoint definition
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Breakpoint {
        private Integer minWidth;
        private Integer columns;
    }
}
