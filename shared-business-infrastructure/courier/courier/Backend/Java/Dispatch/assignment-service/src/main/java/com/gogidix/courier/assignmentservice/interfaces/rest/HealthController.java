package com.gogidix.courier.assignmentservice.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
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
        health.put("service", "assignment-service");
        health.put("timestamp", Instant.now());
        return ResponseEntity.ok(health);
    }

    @GetMapping("/ready")
    @Operation(summary = "Readiness check", description = "Check if the service is ready to accept traffic")
    public ResponseEntity<Map<String, Object>> ready() {
        Map<String, Object> readiness = new HashMap<>();
        readiness.put("status", "READY");
        readiness.put("timestamp", Instant.now());
        return ResponseEntity.ok(readiness);
    }

    @GetMapping("/live")
    @Operation(summary = "Liveness check", description = "Check if the service is alive")
    public ResponseEntity<Map<String, Object>> live() {
        Map<String, Object> liveness = new HashMap<>();
        liveness.put("status", "ALIVE");
        liveness.put("timestamp", Instant.now());
        return ResponseEntity.ok(liveness);
    }
}
