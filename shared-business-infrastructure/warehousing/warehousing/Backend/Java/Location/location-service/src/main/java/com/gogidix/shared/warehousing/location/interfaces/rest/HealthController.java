package com.gogidix.shared.warehousing.location.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Health Check Controller
 */
@RestController
@RequestMapping("/health")
@Tag(name = "Health", description = "APIs for health check")
public class HealthController {

    @GetMapping
    @Operation(summary = "Health check", description = "Check if the location service is running")
    @ApiResponse(responseCode = "200", description = "Service is healthy",
            content = @Content(schema = @Schema(implementation = Map.class)))
    public Map<String, String> health() {
        return Map.of(
            "status", "UP",
            "service", "location-service"
        );
    }
}
