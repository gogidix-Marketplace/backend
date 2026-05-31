package com.gogidix.aiservices.aiuserprofilingservice.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/**
 * Health check controller for service monitoring.
 */
@RestController
@RequestMapping("/health")
@Tag(name = "Health", description = "Service health check endpoints")
public class HealthController {

    @GetMapping
    @Operation(summary = "Health check", description = "Returns the current health status of the service")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "timestamp", Instant.now().toString(),
                "service", "ai-customer-segmentation-service"
        ));
    }

    @GetMapping("/liveness")
    @Operation(summary = "Liveness probe", description = "Kubernetes liveness probe endpoint")
    public ResponseEntity<Map<String, String>> liveness() {
        return ResponseEntity.ok(Map.of("status", "alive"));
    }

    @GetMapping("/readiness")
    @Operation(summary = "Readiness probe", description = "Kubernetes readiness probe endpoint")
    public ResponseEntity<Map<String, String>> readiness() {
        return ResponseEntity.ok(Map.of("status", "ready"));
    }
}
