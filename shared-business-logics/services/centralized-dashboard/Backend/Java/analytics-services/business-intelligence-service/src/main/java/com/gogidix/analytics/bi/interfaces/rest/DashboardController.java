package com.gogidix.analytics.bi.interfaces.rest;

import com.gogidix.analytics.bi.application.service.DashboardCommandService;
import com.gogidix.analytics.bi.domain.model.Dashboard;
import com.gogidix.analytics.bi.domain.model.DashboardWidget;
import com.gogidix.analytics.bi.domain.port.in.CreateDashboardCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Dashboard API.
 */
@RestController
@RequestMapping("/api/v1/dashboards")
@RequiredArgsConstructor
@Tag(name = "Dashboards", description = "Dashboard management API")
public class DashboardController {

    private final DashboardCommandService commandService;

    @PostMapping
    @Operation(summary = "Create a new dashboard")
    public ResponseEntity<Dashboard> createDashboard(@Valid @RequestBody CreateDashboardCommand command) {
        Dashboard dashboard = commandService.createDashboard(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(dashboard);
    }

    @PutMapping("/{dashboardId}")
    @Operation(summary = "Update a dashboard")
    public ResponseEntity<Dashboard> updateDashboard(
        @PathVariable String dashboardId,
        @Valid @RequestBody CreateDashboardCommand command) {

        return ResponseEntity.ok(commandService.updateDashboard(dashboardId, command));
    }

    @DeleteMapping("/{dashboardId}")
    @Operation(summary = "Delete a dashboard")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDashboard(@PathVariable String dashboardId) {
        commandService.deleteDashboard(dashboardId);
    }

    @PostMapping("/{dashboardId}/widgets")
    @Operation(summary = "Add a widget to a dashboard")
    public ResponseEntity<DashboardWidget> addWidget(
        @PathVariable String dashboardId,
        @Valid @RequestBody CreateDashboardCommand.WidgetCommand command) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(commandService.addWidget(dashboardId, command));
    }

    @PutMapping("/{dashboardId}/widgets/{widgetId}")
    @Operation(summary = "Update a widget")
    public ResponseEntity<DashboardWidget> updateWidget(
        @PathVariable String dashboardId,
        @PathVariable String widgetId,
        @Valid @RequestBody CreateDashboardCommand.WidgetCommand command) {

        return ResponseEntity.ok(commandService.updateWidget(dashboardId, widgetId, command));
    }

    @DeleteMapping("/{dashboardId}/widgets/{widgetId}")
    @Operation(summary = "Delete a widget")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWidget(
        @PathVariable String dashboardId,
        @PathVariable String widgetId) {

        commandService.deleteWidget(dashboardId, widgetId);
    }

    @PutMapping("/{dashboardId}/favorite")
    @Operation(summary = "Toggle favorite status")
    public ResponseEntity<Dashboard> toggleFavorite(@PathVariable String dashboardId) {
        return ResponseEntity.ok(commandService.toggleFavorite(dashboardId));
    }
}
