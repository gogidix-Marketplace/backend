package com.gogidix.sales.dashboard.domain.model;

import com.gogidix.sales.dashboard.domain.event.WidgetUpdatedEvent;
import com.gogidix.sales.dashboard.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * KPI Widget Domain Entity
 * Represents individual KPI widgets that can be displayed on dashboards
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "kpi_widgets")
public class KPIWidget extends BaseEntity {

    private String widgetId;
    private String tenantId;
    private String dashboardId;
    private String title;
    private String description;
    private WidgetType widgetType;
    private WidgetCategory category;
    private DataType dataType;

    // Widget configuration
    private WidgetConfiguration configuration;

    // Data sources
    private List<DataSource> dataSources;

    // Current value
    private WidgetValue currentValue;

    // Historical data for sparklines
    private List<HistoricalValue> historicalValues;

    // Thresholds and alerts
    private ThresholdConfig thresholdConfig;

    // Styling
    private WidgetStyle style;

    // Layout
    private LayoutInfo layout;

    // Permissions
    private WidgetPermissions permissions;

    // Metadata
    private String createdBy;
    private Instant lastUpdated;
    private Integer refreshFrequencyMinutes;
    private Boolean isActive;
    private String owner;
    private List<String> tags;

    // Domain events
    @Builder.Default
    private List<WidgetUpdatedEvent> domainEvents = new ArrayList<>();

    public enum WidgetType {
        METRIC_CARD, CHART_LINE, CHART_BAR, CHART_PIE, CHART_DONUT,
        GAUGE, TABLE, FUNNEL, HEATMAP, TREEMAP, NUMBER_CARD,
        PROGRESS_BAR, BULLET_CHART, SPARKLINE, SCORECARD,
        COMPARISON, RANKING, DISTRIBUTION
    }

    public enum WidgetCategory {
        REVENUE, DEALS, PIPELINE, ACTIVITY, PERFORMANCE, FORECAST,
        CUSTOMER, PRODUCT, REGION, CUSTOM
    }

    public enum DataType {
        MONEY, NUMBER, PERCENTAGE, COUNT, RATING, DURATION, CUSTOM
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WidgetConfiguration {
        private String unit;
        private Integer decimalPlaces;
        private String prefix;
        private String suffix;
        private Boolean showTrend;
        private Boolean showTarget;
        private String comparisonPeriod;
        private Boolean showSparkline;
        private Integer sparklinePoints;
        private String aggregationType; // SUM, AVG, COUNT, MIN, MAX
        private Map<String, Object> customConfig;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataSource {
        private String sourceId;
        private String sourceType; // AGGREGATION, ROLLUP, EXTERNAL
        private String sourceKey;
        private String field;
        private Map<String, Object> filters;
        private String aggregation;
        private Integer weight;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WidgetValue {
        private Object value;
        private String displayValue;
        private MoneyValue moneyValue;
        private BigDecimal numberValue;
        private String stringValue;
        private Instant timestamp;
        private TrendInfo trendInfo;
        private TargetInfo targetInfo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MoneyValue {
        private BigDecimal amount;
        private String currency;
        private String formatted;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrendInfo {
        private String direction; // UP, DOWN, STABLE
        private BigDecimal value;
        private String percentage;
        private String label;
        private Boolean isPositive;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TargetInfo {
        private BigDecimal target;
        private BigDecimal actual;
        private BigDecimal achievement;
        private BigDecimal remaining;
        private Boolean isOnTrack;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HistoricalValue {
        private Instant timestamp;
        private Object value;
        private String label;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ThresholdConfig {
        private ThresholdType type;
        private List<Threshold> thresholds;
        private String alertConfig;
        private Boolean enableAlerts;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Threshold {
        private String label;
        private BigDecimal minValue;
        private BigDecimal maxValue;
        private String color;
        private String icon;
        private String severity; // INFO, WARNING, ERROR, CRITICAL
    }

    public enum ThresholdType {
        RANGE, BENCHMARK, TARGET, CUSTOM
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WidgetStyle {
        private String colorScheme;
        private String primaryColor;
        private String backgroundColor;
        private String borderColor;
        private Integer borderWidth;
        private String font;
        private Integer fontSize;
        private Boolean bold;
        private Map<String, String> customStyles;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LayoutInfo {
        private Integer row;
        private Integer column;
        private Integer rowSpan;
        private Integer columnSpan;
        private Integer zIndex;
        private Boolean isVisible;
        private Boolean isCollapsed;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WidgetPermissions {
        private Boolean isPublic;
        private List<String> viewableBy;
        private List<String> editableBy;
        private String accessLevel; // PRIVATE, TEAM, PUBLIC
    }

    /**
     * Creates a new KPI widget
     */
    public static KPIWidget create(String tenantId, String dashboardId, String title,
                                    WidgetType type, WidgetCategory category, String createdBy) {
        KPIWidget widget = KPIWidget.builder()
                .widgetId(java.util.UUID.randomUUID().toString())
                .tenantId(tenantId)
                .dashboardId(dashboardId)
                .title(title)
                .widgetType(type)
                .category(category)
                .configuration(createDefaultConfiguration())
                .dataSources(new ArrayList<>())
                .historicalValues(new ArrayList<>())
                .thresholdConfig(createDefaultThresholdConfig())
                .style(createDefaultStyle())
                .layout(createDefaultLayout())
                .permissions(createDefaultPermissions())
                .createdBy(createdBy)
                .lastUpdated(Instant.now())
                .refreshFrequencyMinutes(5)
                .isActive(true)
                .owner(createdBy)
                .tags(new ArrayList<>())
                .build();

        widget.addDomainEvent(WidgetUpdatedEvent.builder()
                .widgetId(widget.getWidgetId())
                .tenantId(tenantId)
                .dashboardId(dashboardId)
                .eventType("WIDGET_CREATED")
                .timestamp(Instant.now())
                .build());

        return widget;
    }

    /**
     * Updates widget value
     */
    public void updateValue(Object value, String displayValue) {
        this.currentValue = WidgetValue.builder()
                .value(value)
                .displayValue(displayValue)
                .timestamp(Instant.now())
                .build();

        this.lastUpdated = Instant.now();

        // Add to historical values
        this.addHistoricalValue(value);

        // Check thresholds
        this.checkThresholds();

        addDomainEvent(WidgetUpdatedEvent.builder()
                .widgetId(this.widgetId)
                .tenantId(this.tenantId)
                .dashboardId(this.dashboardId)
                .eventType("WIDGET_VALUE_UPDATED")
                .timestamp(Instant.now())
                .build());
    }

    /**
     * Updates widget value with trend info
     */
    public void updateValueWithTrend(Object value, String displayValue,
                                      String direction, BigDecimal changeValue) {
        TrendInfo trendInfo = TrendInfo.builder()
                .direction(direction)
                .value(changeValue)
                .percentage(changeValue + "%")
                .isPositive(isTrendPositive(direction, this.category))
                .build();

        this.currentValue = WidgetValue.builder()
                .value(value)
                .displayValue(displayValue)
                .trendInfo(trendInfo)
                .timestamp(Instant.now())
                .build();

        this.lastUpdated = Instant.now();
        this.addHistoricalValue(value);
    }

    /**
     * Updates widget value with target info
     */
    public void updateValueWithTarget(Object value, String displayValue,
                                       BigDecimal target, BigDecimal actual) {
        BigDecimal achievement = calculateAchievement(target, actual);
        BigDecimal remaining = target.subtract(actual);
        boolean isOnTrack = achievement.compareTo(new BigDecimal("75")) >= 0;

        TargetInfo targetInfo = TargetInfo.builder()
                .target(target)
                .actual(actual)
                .achievement(achievement)
                .remaining(remaining)
                .isOnTrack(isOnTrack)
                .build();

        this.currentValue = WidgetValue.builder()
                .value(value)
                .displayValue(displayValue)
                .targetInfo(targetInfo)
                .timestamp(Instant.now())
                .build();

        this.lastUpdated = Instant.now();
    }

    /**
     * Adds a data source
     */
    public void addDataSource(DataSource dataSource) {
        if (this.dataSources == null) {
            this.dataSources = new ArrayList<>();
        }
        this.dataSources.add(dataSource);
    }

    /**
     * Adds historical value
     */
    public void addHistoricalValue(Object value) {
        if (this.historicalValues == null) {
            this.historicalValues = new ArrayList<>();
        }

        HistoricalValue historicalValue = HistoricalValue.builder()
                .timestamp(Instant.now())
                .value(value)
                .label(formatTimestamp(Instant.now()))
                .build();

        this.historicalValues.add(historicalValue);

        // Keep only last 100 points
        if (this.historicalValues.size() > 100) {
            this.historicalValues.remove(0);
        }
    }

    /**
     * Updates thresholds
     */
    public void updateThresholds(List<Threshold> thresholds) {
        if (this.thresholdConfig == null) {
            this.thresholdConfig = createDefaultThresholdConfig();
        }
        this.thresholdConfig.setThresholds(thresholds);
    }

    /**
     * Updates layout
     */
    public void updateLayout(LayoutInfo layout) {
        this.layout = layout;
    }

    /**
     * Activates widget
     */
    public void activate() {
        this.isActive = true;
        this.lastUpdated = Instant.now();
    }

    /**
     * Deactivates widget
     */
    public void deactivate() {
        this.isActive = false;
        this.lastUpdated = Instant.now();
    }

    /**
     * Checks if value is within threshold
     */
    public boolean isWithinThreshold() {
        if (this.currentValue == null || this.currentValue.getValue() == null ||
                this.thresholdConfig == null || this.thresholdConfig.getThresholds() == null) {
            return true;
        }

        try {
            BigDecimal value = new BigDecimal(this.currentValue.getValue().toString());

            return this.thresholdConfig.getThresholds().stream()
                    .anyMatch(t -> value.compareTo(t.getMinValue()) >= 0 &&
                                    value.compareTo(t.getMaxValue()) <= 0);
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Gets current threshold
     */
    public Threshold getCurrentThreshold() {
        if (this.currentValue == null || this.currentValue.getValue() == null ||
                this.thresholdConfig == null || this.thresholdConfig.getThresholds() == null) {
            return null;
        }

        try {
            BigDecimal value = new BigDecimal(this.currentValue.getValue().toString());

            return this.thresholdConfig.getThresholds().stream()
                    .filter(t -> value.compareTo(t.getMinValue()) >= 0 &&
                                 value.compareTo(t.getMaxValue()) <= 0)
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Adds a tag
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Removes a tag
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }

    private void checkThresholds() {
        if (this.thresholdConfig != null &&
                this.thresholdConfig.getEnableAlerts() &&
                !this.isWithinThreshold()) {

            // Alert would be triggered here
            addDomainEvent(WidgetUpdatedEvent.builder()
                    .widgetId(this.widgetId)
                    .tenantId(this.tenantId)
                    .dashboardId(this.dashboardId)
                    .eventType("WIDGET_THRESHOLD_ALERT")
                    .timestamp(Instant.now())
                    .build());
        }
    }

    private BigDecimal calculateAchievement(BigDecimal target, BigDecimal actual) {
        if (target == null || target.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return actual.divide(target, 4, java.math.RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
    }

    private boolean isTrendPositive(String direction, WidgetCategory category) {
        if ("UP".equals(direction)) {
            return List.of(WidgetCategory.REVENUE, WidgetCategory.DEALS, WidgetCategory.PIPELINE)
                    .contains(category);
        }
        return false;
    }

    private String formatTimestamp(Instant timestamp) {
        return timestamp.toString().substring(0, 10);
    }

    private static WidgetConfiguration createDefaultConfiguration() {
        return WidgetConfiguration.builder()
                .decimalPlaces(2)
                .showTrend(true)
                .showTarget(true)
                .comparisonPeriod("PREVIOUS_PERIOD")
                .showSparkline(true)
                .sparklinePoints(30)
                .aggregationType("SUM")
                .customConfig(new HashMap<>())
                .build();
    }

    private static ThresholdConfig createDefaultThresholdConfig() {
        List<Threshold> thresholds = List.of(
                Threshold.builder()
                        .label("Critical")
                        .minValue(BigDecimal.ZERO)
                        .maxValue(new BigDecimal("25"))
                        .color("#dc3545")
                        .severity("CRITICAL")
                        .build(),
                Threshold.builder()
                        .label("Warning")
                        .minValue(new BigDecimal("25"))
                        .maxValue(new BigDecimal("50"))
                        .color("#ffc107")
                        .severity("WARNING")
                        .build(),
                Threshold.builder()
                        .label("Good")
                        .minValue(new BigDecimal("50"))
                        .maxValue(new BigDecimal("75"))
                        .color("#17a2b8")
                        .severity("INFO")
                        .build(),
                Threshold.builder()
                        .label("Excellent")
                        .minValue(new BigDecimal("75"))
                        .maxValue(new BigDecimal("1000"))
                        .color("#28a745")
                        .severity("INFO")
                        .build()
        );

        return ThresholdConfig.builder()
                .type(ThresholdType.RANGE)
                .thresholds(thresholds)
                .enableAlerts(false)
                .build();
    }

    private static WidgetStyle createDefaultStyle() {
        return WidgetStyle.builder()
                .colorScheme("default")
                .fontSize(14)
                .bold(false)
                .customStyles(new HashMap<>())
                .build();
    }

    private static LayoutInfo createDefaultLayout() {
        return LayoutInfo.builder()
                .row(0)
                .column(0)
                .rowSpan(1)
                .columnSpan(1)
                .zIndex(0)
                .isVisible(true)
                .isCollapsed(false)
                .build();
    }

    private static WidgetPermissions createDefaultPermissions() {
        return WidgetPermissions.builder()
                .isPublic(false)
                .viewableBy(new ArrayList<>())
                .editableBy(new ArrayList<>())
                .accessLevel("PRIVATE")
                .build();
    }

    public void addDomainEvent(WidgetUpdatedEvent event) {
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
}
