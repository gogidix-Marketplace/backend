package com.gogidix.aiservices.aianalyticsdashboard.interfaces.rest;

import com.gogidix.aiservices.aianalyticsdashboard.application.dto.request.CreateDashboardRequest;
import com.gogidix.aiservices.aianalyticsdashboard.application.dto.request.UpdateDashboardRequest;
import com.gogidix.aiservices.aianalyticsdashboard.application.dto.request.AddWidgetRequest;
import com.gogidix.aiservices.aianalyticsdashboard.application.dto.response.DashboardResponse;
import com.gogidix.aiservices.aianalyticsdashboard.application.dto.response.WidgetResponse;
import com.gogidix.aiservices.aianalyticsdashboard.application.service.DashboardService;
import com.gogidix.aiservices.aianalyticsdashboard.shared.exception.AnalyticsException;
import com.gogidix.aiservices.aianalyticsdashboard.shared.exception.DashboardNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics/dashboards")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @PostMapping
    public ResponseEntity<DashboardResponse> createDashboard(
            @Valid @RequestBody CreateDashboardRequest request,
            @RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {
        DashboardResponse response = dashboardService.createDashboard(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DashboardResponse> getDashboard(@PathVariable String id) {
        DashboardResponse response = dashboardService.getDashboard(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DashboardResponse>> getUserDashboards(
            @RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {
        List<DashboardResponse> responses = dashboardService.getUserDashboards(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/public")
    public ResponseEntity<List<DashboardResponse>> getPublicDashboards() {
        List<DashboardResponse> responses = dashboardService.getPublicDashboards();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DashboardResponse> updateDashboard(
            @PathVariable String id,
            @Valid @RequestBody UpdateDashboardRequest request) {
        DashboardResponse response = dashboardService.updateDashboard(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/visibility")
    public ResponseEntity<Void> setVisibility(
            @PathVariable String id,
            @RequestParam boolean isPublic) {
        dashboardService.setDashboardVisibility(id, isPublic);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDashboard(@PathVariable String id) {
        dashboardService.deleteDashboard(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/widgets")
    public ResponseEntity<WidgetResponse> addWidget(
            @PathVariable String id,
            @Valid @RequestBody AddWidgetRequest request) {
        WidgetResponse response = dashboardService.addWidget(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}/widgets/{widgetId}")
    public ResponseEntity<Void> removeWidget(
            @PathVariable String id,
            @PathVariable String widgetId) {
        dashboardService.removeWidget(id, widgetId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/export")
    public ResponseEntity<Map<String, Object>> exportDashboard(@PathVariable String id) {
        Map<String, Object> config = dashboardService.exportDashboard(id);
        return ResponseEntity.ok(config);
    }

    @PostMapping("/import")
    public ResponseEntity<DashboardResponse> importDashboard(
            @RequestBody Map<String, Object> config,
            @RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {
        DashboardResponse response = dashboardService.importDashboard(config, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @ExceptionHandler(DashboardNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(DashboardNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(AnalyticsException.class)
    public ResponseEntity<Map<String, Object>> handleAnalyticsException(AnalyticsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("An unexpected error occurred"));
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", message);
        error.put("message", message);
        error.put("timestamp", Instant.now());
        return error;
    }
}
