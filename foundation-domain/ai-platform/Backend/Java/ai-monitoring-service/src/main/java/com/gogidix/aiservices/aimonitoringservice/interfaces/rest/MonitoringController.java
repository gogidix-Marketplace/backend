package com.gogidix.aiservices.aimonitoringservice.interfaces.rest;

import com.gogidix.aiservices.aimonitoringservice.application.dto.AlertRuleResponseDto;
import com.gogidix.aiservices.aimonitoringservice.application.dto.CreateAlertRuleRequestDto;
import com.gogidix.aiservices.aimonitoringservice.application.dto.ServiceHealthResponseDto;
import com.gogidix.aiservices.aimonitoringservice.application.service.AlertRuleApplicationService;
import com.gogidix.aiservices.aimonitoringservice.application.service.ServiceHealthApplicationService;
import com.gogidix.aiservices.aimonitoringservice.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST controller for monitoring operations.
 */
@RestController
@RequestMapping("/api/v1/monitoring")
@Tag(name = "Monitoring", description = "APIs for monitoring and alerting")
public class MonitoringController {

    private final AlertRuleApplicationService alertService;
    private final ServiceHealthApplicationService healthService;

    public MonitoringController(AlertRuleApplicationService alertService,
                                 ServiceHealthApplicationService healthService) {
        this.alertService = alertService;
        this.healthService = healthService;
    }

    @PostMapping("/alerts")
    @Operation(summary = "Create alert rule")
    public ResponseEntity<AlertRuleResponseDto> createAlert(@Valid @RequestBody CreateAlertRuleRequestDto request) {
        String tenantId = RequestContextHolder.getContext().tenantId();
        AlertRuleResponseDto response = alertService.createRule(tenantId, request);
        return ResponseEntity.created(URI.create("/api/v1/monitoring/alerts/" + response.alertId())).body(response);
    }

    @GetMapping("/alerts/{alertId}")
    @Operation(summary = "Get alert rule")
    public ResponseEntity<AlertRuleResponseDto> getAlert(@PathVariable String alertId) {
        String tenantId = RequestContextHolder.getContext().tenantId();
        return ResponseEntity.ok(alertService.getRuleById(alertId, tenantId));
    }

    @GetMapping("/alerts")
    @Operation(summary = "List alert rules")
    public ResponseEntity<List<AlertRuleResponseDto>> listAlerts() {
        String tenantId = RequestContextHolder.getContext().tenantId();
        return ResponseEntity.ok(alertService.getRulesByTenant(tenantId));
    }

    @DeleteMapping("/alerts/{alertId}")
    @Operation(summary = "Delete alert rule")
    public ResponseEntity<Void> deleteAlert(@PathVariable String alertId) {
        String tenantId = RequestContextHolder.getContext().tenantId();
        alertService.deleteRule(alertId, tenantId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/alerts/{alertId}/activate")
    @Operation(summary = "Activate alert rule")
    public ResponseEntity<Void> activateAlert(@PathVariable String alertId) {
        String tenantId = RequestContextHolder.getContext().tenantId();
        alertService.activateRule(alertId, tenantId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/alerts/{alertId}/deactivate")
    @Operation(summary = "Deactivate alert rule")
    public ResponseEntity<Void> deactivateAlert(@PathVariable String alertId) {
        String tenantId = RequestContextHolder.getContext().tenantId();
        alertService.deactivateRule(alertId, tenantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health/{serviceName}")
    @Operation(summary = "Get service health")
    public ResponseEntity<ServiceHealthResponseDto> getServiceHealth(@PathVariable String serviceName) {
        String tenantId = RequestContextHolder.getContext().tenantId();
        return ResponseEntity.ok(healthService.getServiceHealth(serviceName, tenantId));
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(new HealthResponse("UP", "AI Monitoring Service is running"));
    }

    public record HealthResponse(String status, String message) {}
}
