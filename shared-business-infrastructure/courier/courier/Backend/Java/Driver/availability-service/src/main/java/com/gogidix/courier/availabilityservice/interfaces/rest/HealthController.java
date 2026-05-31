package com.gogidix.courier.availabilityservice.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/**
 * Health check controller for availability service.
 */
@RestController
@RequestMapping("/health")
@Tag(name = "Health", description = "Health check endpoints")
public class HealthController {

    @GetMapping
    @Operation(summary = "Health check", description = "Check service health status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service is healthy")
    })
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "availability-service",
                "timestamp", Instant.now().toString()
        ));
    }

    @GetMapping("/ready")
    @Operation(summary = "Readiness check", description = "Check if service is ready to accept traffic")
    public ResponseEntity<Map<String, Object>> ready() {
        return ResponseEntity.ok(Map.of(
                "status", "READY",
                "service", "availability-service"
        ));
    }
}
