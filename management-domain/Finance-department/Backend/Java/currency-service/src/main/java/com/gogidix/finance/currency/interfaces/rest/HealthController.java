package com.gogidix.finance.currency.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller - Health Check
 * Provides health and status endpoints
 */
@RestController
@RequestMapping("/health")
@Tag(name = "Health", description = "Health check endpoints")
@RequiredArgsConstructor
public class HealthController {

    private final BuildProperties buildProperties;

    @GetMapping
    @Operation(summary = "Health check", description = "Returns service health status")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "currency-service");
        health.put("timestamp", Instant.now());
        if (buildProperties != null) {
            health.put("version", buildProperties.getVersion());
        }
        return ResponseEntity.ok(health);
    }

    @GetMapping("/ready")
    @Operation(summary = "Readiness check", description = "Returns service readiness status")
    public ResponseEntity<Map<String, String>> readiness() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "READY");
        return ResponseEntity.ok(status);
    }

    @GetMapping("/live")
    @Operation(summary = "Liveness check", description = "Returns service liveness status")
    public ResponseEntity<Map<String, String>> liveness() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "ALIVE");
        return ResponseEntity.ok(status);
    }
}
