package com.gogidix.shared.courier.driver.interfaces.rest;

import com.mongodb.client.MongoClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health Check Controller
 * Provides liveness, readiness, and health check endpoints for Kubernetes probes
 */
@Slf4j
@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
@Tag(name = "Health", description = "Health check endpoints for monitoring and Kubernetes probes")
public class HealthController {

    private final MongoClient mongoClient;
    private final BuildProperties buildProperties;

    @GetMapping
    @Operation(
        summary = "General health check",
        description = "Returns the overall health status of the service"
    )
    @ApiResponse(responseCode = "200", description = "Service is healthy")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "driver-pool-service");
        response.put("timestamp", LocalDateTime.now());
        response.put("version", buildProperties != null ? buildProperties.getVersion() : "unknown");
        return response;
    }

    @GetMapping("/liveness")
    @Operation(
        summary = "Liveness probe",
        description = "Kubernetes liveness probe - returns 200 if the service is running"
    )
    @ApiResponse(responseCode = "200", description = "Service is alive")
    public ResponseEntity<Map<String, String>> liveness() {
        log.debug("Liveness probe called");
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "probe", "liveness"
        ));
    }

    @GetMapping("/readiness")
    @Operation(
        summary = "Readiness probe",
        description = "Kubernetes readiness probe - returns 200 if the service is ready to accept traffic"
    )
    @ApiResponse(responseCode = "200", description = "Service is ready")
    @ApiResponse(responseCode = "503", description = "Service is not ready")
    public ResponseEntity<Map<String, Object>> readiness() {
        Map<String, Object> response = new HashMap<>();

        try {
            // Check MongoDB connection
            mongoClient.getClusterDescription();
            response.put("status", "UP");
            response.put("probe", "readiness");
            response.put("database", "connected");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Readiness check failed", e);
            response.put("status", "DOWN");
            response.put("probe", "readiness");
            response.put("database", "disconnected");
            response.put("error", e.getMessage());
            return ResponseEntity.status(503).body(response);
        }
    }
}
