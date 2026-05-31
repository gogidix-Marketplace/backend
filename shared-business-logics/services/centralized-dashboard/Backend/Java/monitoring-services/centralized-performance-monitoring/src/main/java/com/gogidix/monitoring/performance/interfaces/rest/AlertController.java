package com.gogidix.monitoring.performance.interfaces.rest;

import com.gogidix.monitoring.performance.application.service.MetricQueryService;
import com.gogidix.monitoring.performance.domain.model.PerformanceAlert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for alert management.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final MetricQueryService queryService;

    @GetMapping
    public ResponseEntity<List<PerformanceAlert>> getActiveAlerts(
            @RequestParam String tenantId,
            @RequestParam(required = false) String serviceId) {
        return ResponseEntity.ok(queryService.getActiveAlerts(tenantId, serviceId));
    }

    @GetMapping("/{alertId}")
    public ResponseEntity<PerformanceAlert> getAlert(@PathVariable String alertId) {
        return ResponseEntity.ok(queryService.getAlert(alertId));
    }

    @PostMapping("/{alertId}/resolve")
    public ResponseEntity<PerformanceAlert> resolveAlert(@PathVariable String alertId) {
        return ResponseEntity.ok(queryService.resolveAlert(alertId));
    }
}
