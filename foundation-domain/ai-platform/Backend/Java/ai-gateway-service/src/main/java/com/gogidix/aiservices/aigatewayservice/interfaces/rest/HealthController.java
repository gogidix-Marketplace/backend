package com.gogidix.aiservices.aigatewayservice.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

/**
 * Health check controller.
 */
@RestController
@RequestMapping("/api/v1/gateway")
public class HealthController {

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the gateway service is running")
    @ApiResponse(responseCode = "200", description = "Service is healthy")
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(new HealthResponse(
                "UP",
                "AI Gateway Service is running",
                Instant.now()
        ));
    }

    public record HealthResponse(
            String status,
            String message,
            Instant timestamp
    ) {}
}
