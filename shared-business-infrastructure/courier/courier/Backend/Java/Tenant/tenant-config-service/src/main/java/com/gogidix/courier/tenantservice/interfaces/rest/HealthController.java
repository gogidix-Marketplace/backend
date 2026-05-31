package com.gogidix.courier.tenantservice.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Health check controller.
 */
@RestController
@RequestMapping("/health")
@Tag(name = "Health", description = "Health check endpoints")
public class HealthController {

    @GetMapping
    @Operation(summary = "Health check", description = "Check if the service is running")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", Instant.now());
        health.put("service", "tenant-config-service");
        return ResponseEntity.ok(health);
    }

    @GetMapping("/ready")
    @Operation(summary = "Readiness check", description = "Check if the service is ready")
    public ResponseEntity<Map<String, Object>> ready() {
        Map<String, Object> readiness = new HashMap<>();
        readiness.put("status", "READY");
        readiness.put("timestamp", Instant.now());
        return ResponseEntity.ok(readiness);
    }
}
