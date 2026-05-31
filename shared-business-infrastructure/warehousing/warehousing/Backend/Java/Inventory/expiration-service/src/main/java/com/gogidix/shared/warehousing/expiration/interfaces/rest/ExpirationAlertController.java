package com.gogidix.shared.warehousing.expiration.interfaces.rest;

import com.gogidix.shared.warehousing.expiration.application.dto.ExpirationAlertDTO;
import com.gogidix.shared.warehousing.expiration.application.service.ExpirationAlertService;
import com.gogidix.shared.warehousing.expiration.domain.entity.ExpirationAlert;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Expiration Alert REST Controller
 */
@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
@Tag(name = "Expiration Alerts", description = "APIs for managing expiration alerts")
public class ExpirationAlertController {

    private final ExpirationAlertService alertService;

    @PostMapping
    @Operation(summary = "Create expiration alert", description = "Create a new expiration alert")
    public ResponseEntity<ExpirationAlertDTO> createAlert(@RequestBody ExpirationAlert alert) {
        ExpirationAlertDTO created = alertService.createAlert(alert);
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get alert by ID", description = "Retrieve an expiration alert by ID")
    public ResponseEntity<ExpirationAlertDTO> getAlert(
            @Parameter(description = "Alert ID") @PathVariable String id) {
        ExpirationAlertDTO alert = alertService.getAlert(id);
        return ResponseEntity.ok(alert);
    }

    @GetMapping
    @Operation(summary = "Get all alerts", description = "Retrieve all alerts for current tenant")
    public ResponseEntity<List<ExpirationAlertDTO>> getAllAlerts() {
        List<ExpirationAlertDTO> alerts = alertService.getAllAlerts();
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/severity/{severity}")
    @Operation(summary = "Get alerts by severity", description = "Retrieve alerts by severity level")
    public ResponseEntity<List<ExpirationAlertDTO>> getAlertsBySeverity(
            @Parameter(description = "Severity level") @PathVariable ExpirationAlert.AlertSeverity severity) {
        List<ExpirationAlertDTO> alerts = alertService.getAlertsBySeverity(severity);
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get alerts by status", description = "Retrieve alerts by status")
    public ResponseEntity<List<ExpirationAlertDTO>> getAlertsByStatus(
            @Parameter(description = "Alert status") @PathVariable ExpirationAlert.AlertStatus status) {
        List<ExpirationAlertDTO> alerts = alertService.getAlertsByStatus(status);
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/expiring")
    @Operation(summary = "Get expiring items", description = "Retrieve items expiring within warning period")
    public ResponseEntity<List<ExpirationAlertDTO>> getExpiringItems(
            @Parameter(description = "Warning days (default 30)") @RequestParam(defaultValue = "30") int warningDays) {
        List<ExpirationAlertDTO> alerts = alertService.getExpiringItems(warningDays);
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/expired")
    @Operation(summary = "Get expired items", description = "Retrieve expired items")
    public ResponseEntity<List<ExpirationAlertDTO>> getExpiredItems() {
        List<ExpirationAlertDTO> alerts = alertService.getExpiredItems();
        return ResponseEntity.ok(alerts);
    }

    @PutMapping("/{id}/disposition")
    @Operation(summary = "Update alert disposition", description = "Update disposition action for an alert")
    public ResponseEntity<ExpirationAlertDTO> updateDisposition(
            @Parameter(description = "Alert ID") @PathVariable String id,
            @Parameter(description = "Disposition action") @RequestParam ExpirationAlert.DispositionAction action,
            @Parameter(description = "Disposition notes") @RequestParam(required = false) String notes,
            @Parameter(description = "Disposition date") @RequestParam(required = false) LocalDate dispositionDate) {
        ExpirationAlertDTO alert = alertService.updateDisposition(id, action, notes, dispositionDate);
        return ResponseEntity.ok(alert);
    }

    @PutMapping("/{id}/notify")
    @Operation(summary = "Mark notification sent", description = "Mark alert as notified")
    public ResponseEntity<ExpirationAlertDTO> markNotificationSent(
            @Parameter(description = "Alert ID") @PathVariable String id,
            @Parameter(description = "Notification recipients") @RequestBody List<String> recipients) {
        ExpirationAlertDTO alert = alertService.markNotificationSent(id, recipients);
        return ResponseEntity.ok(alert);
    }

    @PutMapping("/{id}/acknowledge")
    @Operation(summary = "Acknowledge alert", description = "Mark alert as acknowledged")
    public ResponseEntity<ExpirationAlertDTO> acknowledgeAlert(
            @Parameter(description = "Alert ID") @PathVariable String id) {
        ExpirationAlertDTO alert = alertService.acknowledgeAlert(id);
        return ResponseEntity.ok(alert);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete alert", description = "Delete an alert by ID")
    public ResponseEntity<Void> deleteAlert(
            @Parameter(description = "Alert ID") @PathVariable String id) {
        alertService.deleteAlert(id);
        return ResponseEntity.noContent().build();
    }
}
