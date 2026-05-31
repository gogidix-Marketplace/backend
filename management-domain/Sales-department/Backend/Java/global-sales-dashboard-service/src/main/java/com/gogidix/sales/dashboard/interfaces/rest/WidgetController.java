package com.gogidix.sales.dashboard.interfaces.rest;

import com.gogidix.sales.dashboard.application.service.WidgetCommandService;
import com.gogidix.sales.dashboard.application.service.WidgetQueryService;
import com.gogidix.sales.dashboard.application.dto.response.WidgetResponseDto;
import com.gogidix.sales.dashboard.domain.model.KPIWidget;
import com.gogidix.sales.dashboard.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for KPI Widget Operations
 * Handles individual KPI widgets that can be displayed on dashboards
 */
@RestController
@RequestMapping("/widgets")
@Tag(name = "KPI Widgets", description = "KPI widget management operations")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${ALLOWED_ORIGINS:http://localhost:3000}")
public class WidgetController {

    private final WidgetCommandService commandService;
    private final WidgetQueryService queryService;

    @GetMapping
    @Operation(summary = "Get all widgets", description = "Returns all widgets for current tenant")
    public ResponseEntity<List<WidgetResponseDto>> getAllWidgets() {
        log.info("Getting all widgets for tenant: {}", RequestContextHolder.getTenantId());
        return ResponseEntity.ok(queryService.getAllForTenant());
    }

    @GetMapping("/{widgetId}")
    @Operation(summary = "Get widget by ID", description = "Returns a single widget")
    public ResponseEntity<WidgetResponseDto> getWidget(
            @Parameter(description = "Widget ID") @PathVariable String widgetId) {
        log.info("Getting widget: {}", widgetId);
        return ResponseEntity.ok(queryService.getById(widgetId));
    }

    @GetMapping("/dashboard/{dashboardId}")
    @Operation(summary = "Get widgets by dashboard", description = "Returns all widgets for a dashboard")
    public ResponseEntity<List<WidgetResponseDto>> getDashboardWidgets(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        log.info("Getting widgets for dashboard: {}", dashboardId);
        return ResponseEntity.ok(queryService.getByDashboardId(dashboardId));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get widgets by type", description = "Returns widgets filtered by type")
    public ResponseEntity<List<WidgetResponseDto>> getByType(
            @Parameter(description = "Widget type") @PathVariable KPIWidget.WidgetType type) {
        log.info("Getting widgets by type: {}", type);
        return ResponseEntity.ok(queryService.getByType(type));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get widgets by category", description = "Returns widgets filtered by category")
    public ResponseEntity<List<WidgetResponseDto>> getByCategory(
            @Parameter(description = "Widget category") @PathVariable KPIWidget.WidgetCategory category) {
        log.info("Getting widgets by category: {}", category);
        return ResponseEntity.ok(queryService.getByCategory(category));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active widgets", description = "Returns all active widgets")
    public ResponseEntity<List<WidgetResponseDto>> getActiveWidgets() {
        log.info("Getting active widgets");
        return ResponseEntity.ok(queryService.getActiveWidgets());
    }

    @GetMapping("/owner/{owner}")
    @Operation(summary = "Get widgets by owner", description = "Returns widgets owned by a user")
    public ResponseEntity<List<WidgetResponseDto>> getByOwner(
            @Parameter(description = "Owner user ID") @PathVariable String owner) {
        log.info("Getting widgets for owner: {}", owner);
        return ResponseEntity.ok(queryService.getByOwner(owner));
    }

    @GetMapping("/search")
    @Operation(summary = "Search widgets", description = "Searches widgets by title")
    public ResponseEntity<List<WidgetResponseDto>> searchWidgets(
            @Parameter(description = "Search term") @RequestParam String searchTerm) {
        log.info("Searching widgets with term: {}", searchTerm);
        return ResponseEntity.ok(queryService.searchByTitle(searchTerm));
    }

    @GetMapping("/needing-refresh")
    @Operation(summary = "Get widgets needing refresh", description = "Returns widgets that need data refresh")
    public ResponseEntity<List<WidgetResponseDto>> getWidgetsNeedingRefresh(
            @Parameter(description = "Minutes threshold") @RequestParam(defaultValue = "5") int minutesThreshold) {
        log.info("Getting widgets needing refresh (threshold: {} minutes)", minutesThreshold);
        return ResponseEntity.ok(queryService.getWidgetsNeedingRefresh(minutesThreshold));
    }

    @PostMapping
    @Operation(summary = "Create widget", description = "Creates a new KPI widget")
    public ResponseEntity<WidgetResponseDto> createWidget(
            @Valid @RequestBody CreateWidgetRequest request) {
        log.info("Creating widget: {} for dashboard: {}", request.title(), request.dashboardId());
        KPIWidget widget = commandService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(queryService.toDto(widget));
    }

    @PostMapping("/batch")
    @Operation(summary = "Create batch widgets", description = "Creates multiple widgets in batch")
    public ResponseEntity<List<WidgetResponseDto>> createBatch(
            @Valid @RequestBody List<CreateWidgetRequest> requests) {
        log.info("Creating batch of {} widgets", requests.size());
        List<KPIWidget> widgets = commandService.createBatch(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                widgets.stream().map(queryService::toDto).toList()
        );
    }

    @PostMapping("/{widgetId}/value")
    @Operation(summary = "Update widget value", description = "Updates the current value of a widget")
    public ResponseEntity<Void> updateValue(
            @Parameter(description = "Widget ID") @PathVariable String widgetId,
            @RequestBody UpdateValueRequest request) {
        log.info("Updating value for widget: {}", widgetId);
        commandService.updateValue(widgetId, request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{widgetId}/value-with-trend")
    @Operation(summary = "Update widget value with trend", description = "Updates widget value with trend info")
    public ResponseEntity<Void> updateValueWithTrend(
            @Parameter(description = "Widget ID") @PathVariable String widgetId,
            @RequestBody UpdateValueWithTrendRequest request) {
        log.info("Updating value with trend for widget: {}", widgetId);
        commandService.updateValueWithTrend(widgetId, request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{widgetId}/value-with-target")
    @Operation(summary = "Update widget value with target", description = "Updates widget value with target info")
    public ResponseEntity<Void> updateValueWithTarget(
            @Parameter(description = "Widget ID") @PathVariable String widgetId,
            @RequestBody UpdateValueWithTargetRequest request) {
        log.info("Updating value with target for widget: {}", widgetId);
        commandService.updateValueWithTarget(widgetId, request);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/{widgetId}")
    @Operation(summary = "Update widget", description = "Updates widget configuration")
    public ResponseEntity<WidgetResponseDto> updateWidget(
            @Parameter(description = "Widget ID") @PathVariable String widgetId,
            @RequestBody UpdateWidgetRequest request) {
        log.info("Updating widget: {}", widgetId);
        KPIWidget widget = commandService.update(widgetId, request);
        return ResponseEntity.ok(queryService.toDto(widget));
    }

    @PutMapping("/{widgetId}/layout")
    @Operation(summary = "Update widget layout", description = "Updates widget layout information")
    public ResponseEntity<Void> updateLayout(
            @Parameter(description = "Widget ID") @PathVariable String widgetId,
            @RequestBody UpdateLayoutRequest request) {
        log.info("Updating layout for widget: {}", widgetId);
        commandService.updateLayout(widgetId, request);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/{widgetId}/thresholds")
    @Operation(summary = "Update thresholds", description = "Updates widget threshold configuration")
    public ResponseEntity<Void> updateThresholds(
            @Parameter(description = "Widget ID") @PathVariable String widgetId,
            @RequestBody UpdateThresholdsRequest request) {
        log.info("Updating thresholds for widget: {}", widgetId);
        commandService.updateThresholds(widgetId, request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{widgetId}/activate")
    @Operation(summary = "Activate widget", description = "Activates a widget")
    public ResponseEntity<Void> activate(
            @Parameter(description = "Widget ID") @PathVariable String widgetId) {
        log.info("Activating widget: {}", widgetId);
        commandService.activate(widgetId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{widgetId}/deactivate")
    @Operation(summary = "Deactivate widget", description = "Deactivates a widget")
    public ResponseEntity<Void> deactivate(
            @Parameter(description = "Widget ID") @PathVariable String widgetId) {
        log.info("Deactivating widget: {}", widgetId);
        commandService.deactivate(widgetId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{widgetId}/tag")
    @Operation(summary = "Add tag", description = "Adds a tag to the widget")
    public ResponseEntity<Void> addTag(
            @Parameter(description = "Widget ID") @PathVariable String widgetId,
            @RequestBody TagRequest request) {
        log.info("Adding tag {} to widget: {}", request.tag(), widgetId);
        commandService.addTag(widgetId, request.tag());
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{widgetId}/tag")
    @Operation(summary = "Remove tag", description = "Removes a tag from the widget")
    public ResponseEntity<Void> removeTag(
            @Parameter(description = "Widget ID") @PathVariable String widgetId,
            @RequestBody TagRequest request) {
        log.info("Removing tag {} from widget: {}", request.tag(), widgetId);
        commandService.removeTag(widgetId, request.tag());
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{widgetId}")
    @Operation(summary = "Delete widget", description = "Deletes a widget")
    public ResponseEntity<Void> deleteWidget(
            @Parameter(description = "Widget ID") @PathVariable String widgetId) {
        log.info("Deleting widget: {}", widgetId);
        commandService.delete(widgetId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{widgetId}/threshold-check")
    @Operation(summary = "Check threshold", description = "Checks if current value is within threshold")
    public ResponseEntity<Map<String, Object>> checkThreshold(
            @Parameter(description = "Widget ID") @PathVariable String widgetId) {
        log.info("Checking threshold for widget: {}", widgetId);
        return ResponseEntity.ok(queryService.checkThreshold(widgetId));
    }

    @GetMapping("/tag/{tag}")
    @Operation(summary = "Get widgets by tag", description = "Returns widgets with a specific tag")
    public ResponseEntity<List<WidgetResponseDto>> getByTag(
            @Parameter(description = "Tag") @PathVariable String tag) {
        log.info("Getting widgets with tag: {}", tag);
        return ResponseEntity.ok(queryService.getByTag(tag));
    }

    // Request DTOs

    public record CreateWidgetRequest(
            String dashboardId,
            String title,
            String description,
            KPIWidget.WidgetType widgetType,
            KPIWidget.WidgetCategory category,
            Map<String, Object> configuration,
            List<DataSourceDto> dataSources,
            Object initialValue,
            String owner,
            List<String> tags,
            Integer refreshFrequencyMinutes
    ) {}

    public record DataSourceDto(
            String sourceId,
            String sourceType,
            String sourceKey,
            String field,
            Map<String, Object> filters,
            String aggregation
    ) {}

    public record UpdateValueRequest(
            Object value,
            String displayValue
    ) {}

    public record UpdateValueWithTrendRequest(
            Object value,
            String displayValue,
            String direction,
            BigDecimal changeValue
    ) {}

    public record UpdateValueWithTargetRequest(
            Object value,
            String displayValue,
            BigDecimal target,
            BigDecimal actual
    ) {}

    public record UpdateWidgetRequest(
            String title,
            String description,
            Map<String, Object> configuration,
            Integer refreshFrequencyMinutes
    ) {}

    public record UpdateLayoutRequest(
            Integer row,
            Integer column,
            Integer rowSpan,
            Integer columnSpan,
            Integer zIndex,
            Boolean isVisible,
            Boolean isCollapsed
    ) {}

    public record UpdateThresholdsRequest(
            List<ThresholdDto> thresholds,
            Boolean enableAlerts
    ) {}

    public record ThresholdDto(
            String label,
            BigDecimal minValue,
            BigDecimal maxValue,
            String color,
            String severity
    ) {}

    public record TagRequest(
            String tag
    ) {}
}
