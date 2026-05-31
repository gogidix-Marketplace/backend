package com.gogidix.transaction.monitoring.controller;

import com.gogidix.transaction.monitoring.dto.*;
import com.gogidix.transaction.monitoring.service.TransactionMonitoringService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/monitoring")
@RequiredArgsConstructor
@Slf4j
public class TransactionMonitoringController {

    private final TransactionMonitoringService monitoringService;

    @PostMapping("/metrics")
    public ResponseEntity<TransactionMetricsResponse> recordMetric(@Valid @RequestBody MetricCreateRequest request) {
        log.info("Recording metric: {}", request.getMetricName());
        TransactionMetricsResponse response = monitoringService.recordMetric(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/alerts")
    public ResponseEntity<AlertResponse> createAlert(@Valid @RequestBody AlertCreateRequest request) {
        log.info("Creating alert: {}", request.getTitle());
        AlertResponse response = monitoringService.createAlert(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/alerts/{id}/acknowledge")
    public ResponseEntity<AlertResponse> acknowledgeAlert(
            @PathVariable UUID id,
            @RequestParam String acknowledgedBy) {
        log.info("Acknowledging alert: {}", id);
        AlertResponse response = monitoringService.acknowledgeAlert(id, acknowledgedBy);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/alerts/{id}/resolve")
    public ResponseEntity<AlertResponse> resolveAlert(
            @PathVariable UUID id,
            @RequestParam String resolvedBy,
            @RequestParam(required = false) String resolutionNotes) {
        log.info("Resolving alert: {}", id);
        AlertResponse response = monitoringService.resolveAlert(id, resolvedBy, resolutionNotes);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/metrics/transaction/{transactionId}")
    public ResponseEntity<List<TransactionMetricsResponse>> getMetricsByTransactionId(@PathVariable UUID transactionId) {
        log.info("Fetching metrics for transaction: {}", transactionId);
        List<TransactionMetricsResponse> response = monitoringService.getMetricsByTransactionId(transactionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/alerts/transaction/{transactionId}")
    public ResponseEntity<List<AlertResponse>> getAlertsByTransactionId(@PathVariable UUID transactionId) {
        log.info("Fetching alerts for transaction: {}", transactionId);
        List<AlertResponse> response = monitoringService.getAlertsByTransactionId(transactionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/alerts/open")
    public ResponseEntity<List<AlertResponse>> getOpenAlerts() {
        log.info("Fetching open alerts");
        List<AlertResponse> response = monitoringService.getOpenAlerts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/alerts/critical")
    public ResponseEntity<List<AlertResponse>> getCriticalAlerts() {
        log.info("Fetching critical alerts");
        List<AlertResponse> response = monitoringService.getCriticalAlerts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<MonitoringDashboard> getDashboardData() {
        log.info("Fetching dashboard data");
        MonitoringDashboard response = monitoringService.getDashboardData();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Transaction Monitoring Service is running");
    }
}
