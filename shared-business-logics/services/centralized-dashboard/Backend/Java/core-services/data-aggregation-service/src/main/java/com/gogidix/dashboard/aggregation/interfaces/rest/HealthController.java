package com.gogidix.dashboard.aggregation.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
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
 * Health check controller for data aggregation service.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Health", description = "Service health and status endpoints")
public class HealthController {

    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Health check", description = "Returns the health status of the data aggregation service")
    public ResponseEntity<Map<String, Object>> health() {
        log.debug("Health check requested");

        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "data-aggregation-service");
        health.put("timestamp", LocalDateTime.now());
        health.put("port", 8905);

        return ResponseEntity.ok(health);
    }

    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Service information", description = "Returns detailed information about the data aggregation service")
    public ResponseEntity<Map<String, Object>> info() {
        log.debug("Service info requested");

        Map<String, Object> info = new HashMap<>();
        info.put("name", "Data Aggregation Service");
        info.put("description", "Cross-service data aggregation service");
        info.put("version", "1.0.0");
        info.put("architecture", "Hexagonal with DDD");
        info.put("features", java.util.List.of(
            "Multi-tenant SaaS",
            "Cross-domain Aggregation",
            "Real-time Data Processing",
            "Feign Client Integration",
            "Redis Caching",
            "Circuit Breaker Pattern"
        ));

        return ResponseEntity.ok(info);
    }
}
