package com.gogidix.universal.tracking.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health check controller for Universal Tracking Service.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Health", description = "Health check endpoints")
public class HealthController {

    /**
     * Health check endpoint
     */
    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Health check",
        description = "Check if the service is running"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service is healthy")
    })
    public ResponseEntity<Map<String, Object>> health() {
        log.debug("Health check requested");

        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "universal-tracking-service");
        health.put("timestamp", LocalDateTime.now().toString());
        health.put("version", "1.0.0");

        return ResponseEntity.ok(health);
    }

    /**
     * Readiness probe endpoint
     */
    @GetMapping(value = "/ready", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Readiness probe",
        description = "Check if the service is ready to accept requests"
    )
    public ResponseEntity<Map<String, Object>> ready() {
        log.debug("Readiness check requested");

        Map<String, Object> readiness = new HashMap<>();
        readiness.put("status", "READY");
        readiness.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.ok(readiness);
    }

    /**
     * Liveness probe endpoint
     */
    @GetMapping(value = "/live", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Liveness probe",
        description = "Check if the service is alive"
    )
    public ResponseEntity<Map<String, Object>> live() {
        log.debug("Liveness check requested");

        Map<String, Object> liveness = new HashMap<>();
        liveness.put("status", "ALIVE");
        liveness.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.ok(liveness);
    }
}
