package com.gogidix.monitoring.alertmanagementservice.interfaces.rest;

import com.gogidix.monitoring.alertmanagementservice.application.dto.*;
import com.gogidix.monitoring.alertmanagementservice.application.service.AlertManagementApplicationService;
import com.gogidix.monitoring.alertmanagementservice.domain.model.Alert;
import com.gogidix.monitoring.alertmanagementservice.domain.model.AlertRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for alert management operations.
 */
@RestController
@RequestMapping("/alerts")
@Tag(name = "Alert Management", description = "APIs for alert rule and alert management")
public class AlertManagementController {

    private final AlertManagementApplicationService alertManagementService;

    public AlertManagementController(AlertManagementApplicationService alertManagementService) {
        this.alertManagementService = alertManagementService;
    }

    // ==================== Alert Rules ====================

    @PostMapping("/rules")
    @Operation(summary = "Create alert rule", description = "Creates a new alert rule")
    @ResponseStatus(HttpStatus.CREATED)
    public AlertRuleResponseDto createAlertRule(
            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId,

            @Valid @RequestBody CreateAlertRuleRequestDto request,

            @Parameter(description = "User ID from context", hidden = true)
            @RequestParam(defaultValue = "system") String userId
    ) {
        return alertManagementService.createAlertRule(tenantId, request, userId);
    }

    @GetMapping("/rules")
    @Operation(summary = "List alert rules", description = "Returns all alert rules for a tenant")
    public List<AlertRuleResponseDto> getAlertRules(
            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId
    ) {
        return alertManagementService.getAlertRules(tenantId);
    }

    @GetMapping("/rules/{ruleId}")
    @Operation(summary = "Get alert rule", description = "Returns a specific alert rule")
    public AlertRuleResponseDto getAlertRule(
            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId,

            @Parameter(description = "Rule ID", required = true)
            @PathVariable @NotBlank String ruleId
    ) {
        return alertManagementService.getAlertRule(tenantId, ruleId);
    }

    @DeleteMapping("/rules/{ruleId}")
    @Operation(summary = "Delete alert rule", description = "Deletes an alert rule")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteAlertRule(
            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId,

            @Parameter(description = "Rule ID", required = true)
            @PathVariable @NotBlank String ruleId
    ) {
        alertManagementService.deleteAlertRule(tenantId, ruleId);
        return ResponseEntity.noContent().build();
    }

    // ==================== Alerts ====================

    @GetMapping
    @Operation(summary = "List alerts", description = "Returns alerts for a tenant with pagination")
    public Page<AlertResponseDto> getAlerts(
            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId,

            @Parameter(description = "Filter by status")
            @RequestParam(required = false) String status,

            @Parameter(description = "Filter by severity")
            @RequestParam(required = false) String severity,

            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size
    ) {
        Alert.AlertStatus statusEnum = status != null ? Alert.AlertStatus.valueOf(status.toUpperCase()) : null;
        AlertRule.AlertSeverity severityEnum = severity != null ? AlertRule.AlertSeverity.valueOf(severity.toUpperCase()) : null;

        return alertManagementService.getAlerts(tenantId, statusEnum, severityEnum, page, size);
    }

    @GetMapping("/{alertId}")
    @Operation(summary = "Get alert", description = "Returns a specific alert")
    public AlertResponseDto getAlert(
            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId,

            @Parameter(description = "Alert ID", required = true)
            @PathVariable @NotBlank String alertId
    ) {
        return alertManagementService.getAlert(tenantId, alertId);
    }

    @PostMapping("/{alertId}/acknowledge")
    @Operation(summary = "Acknowledge alert", description = "Acknowledges an alert")
    public AlertResponseDto acknowledgeAlert(
            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId,

            @Parameter(description = "Alert ID", required = true)
            @PathVariable @NotBlank String alertId,

            @Valid @RequestBody AcknowledgeAlertRequestDto request
    ) {
        return alertManagementService.acknowledgeAlert(tenantId, alertId, request);
    }

    @PostMapping("/{alertId}/resolve")
    @Operation(summary = "Resolve alert", description = "Resolves an alert")
    public AlertResponseDto resolveAlert(
            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId,

            @Parameter(description = "Alert ID", required = true)
            @PathVariable @NotBlank String alertId,

            @Valid @RequestBody ResolveAlertRequestDto request
    ) {
        return alertManagementService.resolveAlert(tenantId, alertId, request);
    }

    @GetMapping("/{alertId}/history")
    @Operation(summary = "Get alert history", description = "Returns the history of an alert")
    public List<AlertHistoryResponseDto> getAlertHistory(
            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId,

            @Parameter(description = "Alert ID", required = true)
            @PathVariable @NotBlank String alertId
    ) {
        return alertManagementService.getAlertHistory(tenantId, alertId);
    }
}
