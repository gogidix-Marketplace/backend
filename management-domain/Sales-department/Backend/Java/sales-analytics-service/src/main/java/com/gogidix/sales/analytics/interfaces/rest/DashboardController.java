package com.gogidix.sales.analytics.interfaces.rest;

import com.gogidix.sales.analytics.application.service.DashboardService;
import com.gogidix.sales.analytics.domain.model.DashboardWidget;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for Dashboard Widget Operations
 */
@RestController
@RequestMapping("/dashboards")
@Tag(name = "Dashboard Widgets", description = "Dashboard widget management and configuration")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${ALLOWED_ORIGINS:http://localhost:3000}")
public class DashboardController {

    private final DashboardService dashboardService;

    @PostMapping("/{dashboardId}/widgets")
    @Operation(summary = "Create widget", description = "Creates a new dashboard widget")
    public ResponseEntity<DashboardWidget> createWidget(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @Valid @RequestBody CreateWidgetRequest request) {
        DashboardWidget widget = dashboardService.createWidget(
                dashboardId,
                request.name(),
                request.description(),
                request.widgetType(),
                request.position(),
                request.dataSource()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(widget);
    }

    @GetMapping("/widgets/{widgetId}")
    @Operation(summary = "Get widget by ID", description = "Returns a single dashboard widget")
    public ResponseEntity<DashboardWidget> getWidget(
            @Parameter(description = "Widget ID") @PathVariable String widgetId) {
        return ResponseEntity.ok(dashboardService.getWidget(widgetId));
    }

    @GetMapping("/{dashboardId}/widgets")
    @Operation(summary = "Get dashboard widgets", description = "Returns all widgets for a dashboard")
    public ResponseEntity<List<DashboardWidget>> getDashboardWidgets(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        return ResponseEntity.ok(dashboardService.getDashboardWidgets(dashboardId));
    }

    @GetMapping("/{dashboardId}/widgets/visible")
    @Operation(summary = "Get visible widgets", description = "Returns only visible widgets for a dashboard")
    public ResponseEntity<List<DashboardWidget>> getVisibleWidgets(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        return ResponseEntity.ok(dashboardService.getVisibleWidgets(dashboardId));
    }

    @GetMapping("/widgets/type/{widgetType}")
    @Operation(summary = "Get widgets by type", description = "Returns all widgets of a specific type")
    public ResponseEntity<List<DashboardWidget>> getWidgetsByType(
            @Parameter(description = "Widget type") @PathVariable DashboardWidget.WidgetType widgetType) {
        return ResponseEntity.ok(dashboardService.getWidgetsByType(widgetType));
    }

    @PutMapping("/widgets/{widgetId}/data")
    @Operation(summary = "Update widget data", description = "Updates the data displayed in a widget")
    public ResponseEntity<DashboardWidget> updateWidgetData(
            @Parameter(description = "Widget ID") @PathVariable String widgetId,
            @Valid @RequestBody UpdateDataRequest request) {
        return ResponseEntity.ok(dashboardService.updateWidgetData(widgetId, request.data()));
    }

    @PutMapping("/widgets/{widgetId}/config")
    @Operation(summary = "Update widget config", description = "Updates a widget configuration value")
    public ResponseEntity<DashboardWidget> updateWidgetConfig(
            @Parameter(description = "Widget ID") @PathVariable String widgetId,
            @Valid @RequestBody UpdateConfigRequest request) {
        return ResponseEntity.ok(dashboardService.updateWidgetConfig(widgetId, request.key(), request.value()));
    }

    @PutMapping("/widgets/{widgetId}/position")
    @Operation(summary = "Update widget position", description = "Updates the widget position on dashboard")
    public ResponseEntity<DashboardWidget> updateWidgetPosition(
            @Parameter(description = "Widget ID") @PathVariable String widgetId,
            @Valid @RequestBody UpdatePositionRequest request) {
        return ResponseEntity.ok(dashboardService.updateWidgetPosition(widgetId, request.position()));
    }

    @PutMapping("/widgets/{widgetId}/visibility")
    @Operation(summary = "Update widget visibility", description = "Shows or hides a widget")
    public ResponseEntity<DashboardWidget> setWidgetVisibility(
            @Parameter(description = "Widget ID") @PathVariable String widgetId,
            @Valid @RequestBody UpdateVisibilityRequest request) {
        return ResponseEntity.ok(dashboardService.setWidgetVisibility(widgetId, request.isVisible()));
    }

    @PutMapping("/widgets/{widgetId}/display-order")
    @Operation(summary = "Update display order", description = "Updates the display order of a widget")
    public ResponseEntity<DashboardWidget> updateDisplayOrder(
            @Parameter(description = "Widget ID") @PathVariable String widgetId,
            @Valid @RequestBody UpdateDisplayOrderRequest request) {
        return ResponseEntity.ok(dashboardService.updateDisplayOrder(widgetId, request.displayOrder()));
    }

    @PostMapping("/widgets/{widgetId}/metrics")
    @Operation(summary = "Add metric to widget", description = "Adds a metric to the widget")
    public ResponseEntity<DashboardWidget> addMetricToWidget(
            @Parameter(description = "Widget ID") @PathVariable String widgetId,
            @Valid @RequestBody AddMetricRequest request) {
        return ResponseEntity.ok(dashboardService.addMetricToWidget(widgetId, request.metricId()));
    }

    @DeleteMapping("/widgets/{widgetId}/metrics/{metricId}")
    @Operation(summary = "Remove metric from widget", description = "Removes a metric from the widget")
    public ResponseEntity<DashboardWidget> removeMetricFromWidget(
            @Parameter(description = "Widget ID") @PathVariable String widgetId,
            @Parameter(description = "Metric ID") @PathVariable String metricId) {
        return ResponseEntity.ok(dashboardService.removeMetricFromWidget(widgetId, metricId));
    }

    @PostMapping("/widgets/{widgetId}/filters")
    @Operation(summary = "Add filter to widget", description = "Adds a filter to the widget")
    public ResponseEntity<DashboardWidget> addWidgetFilter(
            @Parameter(description = "Widget ID") @PathVariable String widgetId,
            @Valid @RequestBody AddFilterRequest request) {
        return ResponseEntity.ok(dashboardService.addWidgetFilter(widgetId, request.key(), request.value()));
    }

    @DeleteMapping("/widgets/{widgetId}/filters/{key}")
    @Operation(summary = "Remove filter from widget", description = "Removes a filter from the widget")
    public ResponseEntity<DashboardWidget> removeWidgetFilter(
            @Parameter(description = "Widget ID") @PathVariable String widgetId,
            @Parameter(description = "Filter key") @PathVariable String key) {
        return ResponseEntity.ok(dashboardService.removeWidgetFilter(widgetId, key));
    }

    @PostMapping("/widgets/{widgetId}/refresh")
    @Operation(summary = "Refresh widget data", description = "Refreshes the data for a specific widget")
    public ResponseEntity<DashboardWidget> refreshWidgetData(
            @Parameter(description = "Widget ID") @PathVariable String widgetId) {
        dashboardService.refreshWidgetData(widgetId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{dashboardId}/refresh")
    @Operation(summary = "Refresh dashboard widgets", description = "Refreshes all widgets on a dashboard")
    public ResponseEntity<Void> refreshDashboardWidgets(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        dashboardService.refreshDashboardWidgets(dashboardId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/widgets/{widgetId}")
    @Operation(summary = "Delete widget", description = "Deletes a dashboard widget")
    public ResponseEntity<Void> deleteWidget(
            @Parameter(description = "Widget ID") @PathVariable String widgetId) {
        dashboardService.deleteWidget(widgetId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{dashboardId}/widgets")
    @Operation(summary = "Delete all dashboard widgets", description = "Deletes all widgets on a dashboard")
    public ResponseEntity<Void> deleteDashboardWidgets(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        dashboardService.deleteDashboardWidgets(dashboardId);
        return ResponseEntity.noContent().build();
    }

    // ========== Request/Response Records ==========

    public record CreateWidgetRequest(
            String name,
            String description,
            DashboardWidget.WidgetType widgetType,
            DashboardWidget.WidgetPosition position,
            DashboardWidget.WidgetDataSource dataSource
    ) {}

    public record UpdateDataRequest(
            Map<String, Object> data
    ) {}

    public record UpdateConfigRequest(
            String key,
            Object value
    ) {}

    public record UpdatePositionRequest(
            DashboardWidget.WidgetPosition position
    ) {}

    public record UpdateVisibilityRequest(
            Boolean isVisible
    ) {}

    public record UpdateDisplayOrderRequest(
            Integer displayOrder
    ) {}

    public record AddMetricRequest(
            String metricId
    ) {}

    public record AddFilterRequest(
            String key,
            Object value
    ) {}
}
