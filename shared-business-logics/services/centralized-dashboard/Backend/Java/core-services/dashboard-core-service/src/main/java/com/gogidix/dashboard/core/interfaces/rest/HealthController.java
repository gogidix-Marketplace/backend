package com.gogidix.dashboard.core.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Health check controller for dashboard core service.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Health", description = "Service health and status endpoints")
public class HealthController {

    private final Optional<BuildProperties> buildProperties;

    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Health check",
        description = "Returns the health status of the dashboard core service"
    )
    @ApiResponse(responseCode = "200", description = "Service is healthy", content = @Content(schema = @Schema(implementation = HashMap.class)))
    public ResponseEntity<Map<String, Object>> health() {
        log.debug("Health check requested");

        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "dashboard-core-service");
        health.put("timestamp", LocalDateTime.now());
        health.put("port", 8904);

        buildProperties.ifPresent(props -> {
            health.put("version", props.getVersion());
            health.put("buildTime", props.getTime());
        });

        return ResponseEntity.ok(health);
    }

    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Service information",
        description = "Returns detailed information about the dashboard core service"
    )
    public ResponseEntity<Map<String, Object>> info() {
        log.debug("Service info requested");

        Map<String, Object> info = new HashMap<>();
        info.put("name", "Dashboard Core Service");
        info.put("description", "Core dashboard logic and KPI management service");
        info.put("version", buildProperties.map(BuildProperties::getVersion).orElse("1.0.0"));
        info.put("architecture", "Hexagonal with DDD");
        info.put("features", java.util.List.of(
            "Multi-tenant SaaS",
            "KPI Management",
            "Real-time Data Processing",
            "Event-driven Architecture",
            "PostgreSQL Persistence",
            "Redis Caching",
            "Kafka Messaging"
        ));

        return ResponseEntity.ok(info);
    }
}
