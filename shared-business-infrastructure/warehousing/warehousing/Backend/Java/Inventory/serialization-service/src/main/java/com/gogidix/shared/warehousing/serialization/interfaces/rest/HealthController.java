package com.gogidix.shared.warehousing.serialization.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health Check Controller
 *
 * Provides health check endpoints for monitoring
 */
@RestController
@RequestMapping("/health")
@Tag(name = "Health", description = "Health check endpoints")
public class HealthController {

    /**
     * Health check endpoint
     * GET /api/v1/serialization/health
     */
    @GetMapping
    @Operation(summary = "Health check", description = "Check service health status")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "serialization-service");
        health.put("timestamp", LocalDateTime.now());
        health.put("port", 8215);
        return ResponseEntity.ok(health);
    }

    /**
     * Liveness probe endpoint
     * GET /api/v1/serialization/health/live
     */
    @GetMapping("/live")
    @Operation(summary = "Liveness probe", description = "Kubernetes liveness probe")
    public ResponseEntity<Map<String, String>> liveness() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "alive");
        return ResponseEntity.ok(response);
    }

    /**
     * Readiness probe endpoint
     * GET /api/v1/serialization/health/ready
     */
    @GetMapping("/ready")
    @Operation(summary = "Readiness probe", description = "Kubernetes readiness probe")
    public ResponseEntity<Map<String, String>> readiness() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "ready");
        return ResponseEntity.ok(response);
    }
}
