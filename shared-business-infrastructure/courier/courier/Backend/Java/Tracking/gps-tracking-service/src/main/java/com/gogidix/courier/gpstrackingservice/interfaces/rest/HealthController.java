package com.gogidix.courier.gpstrackingservice.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Health check", description = "Check service health status")
    @ApiResponse(responseCode = "200", description = "Service is healthy")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "gps-tracking-service");
        response.put("timestamp", Instant.now());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ready")
    @Operation(summary = "Readiness check", description = "Check if service is ready to accept traffic")
    @ApiResponse(responseCode = "200", description = "Service is ready")
    public ResponseEntity<Map<String, Object>> ready() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "READY");
        response.put("timestamp", Instant.now());
        return ResponseEntity.ok(response);
    }
}
