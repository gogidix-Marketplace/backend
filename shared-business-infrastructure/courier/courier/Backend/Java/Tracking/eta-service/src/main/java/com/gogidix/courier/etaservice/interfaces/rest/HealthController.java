package com.gogidix.courier.etaservice.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/**
 * Health check controller for ETA service.
 */
@RestController
@RequestMapping("/health")
@Tag(name = "Health", description = "Health check endpoints")
public class HealthController {

    @GetMapping
    @Operation(
            summary = "Health check",
            description = "Check if the ETA service is running"
    )
    @ApiResponse(responseCode = "200", description = "Service is healthy")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "eta-service",
                "timestamp", Instant.now()
        ));
    }

    @GetMapping("/ready")
    @Operation(
            summary = "Readiness check",
            description = "Check if the ETA service is ready to accept requests"
    )
    @ApiResponse(responseCode = "200", description = "Service is ready")
    public ResponseEntity<Map<String, Object>> ready() {
        return ResponseEntity.ok(Map.of(
                "status", "READY",
                "service", "eta-service",
                "timestamp", Instant.now()
        ));
    }
}
