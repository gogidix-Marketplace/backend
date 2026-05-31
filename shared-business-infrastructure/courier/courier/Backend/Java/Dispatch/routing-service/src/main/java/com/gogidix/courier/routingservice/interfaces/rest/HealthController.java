package com.gogidix.courier.routingservice.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
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
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", Instant.now());
        response.put("service", "routing-service");
        return ResponseEntity.ok(response);
    }
}
