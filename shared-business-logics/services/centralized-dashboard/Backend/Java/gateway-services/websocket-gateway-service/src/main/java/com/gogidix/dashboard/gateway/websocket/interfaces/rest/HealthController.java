package com.gogidix.dashboard.gateway.websocket.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
 * Health check controller.
 */
@Slf4j
@RestController
@RequestMapping("/actuator")
@RequiredArgsConstructor
@Tag(name = "Health", description = "Health check endpoints")
public class HealthController {

    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Health check", description = "Returns the health status of the WebSocket Gateway service")
    public ResponseEntity<Map<String, Object>> health() {
        log.debug("Health check requested");

        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("service", "websocket-gateway-service");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(health);
    }

    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Service information", description = "Returns information about the WebSocket Gateway service")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "WebSocket Gateway Service");
        info.put("description", "Real-time WebSocket gateway with Redis pub/sub");
        info.put("version", "1.0.0");
        info.put("port", 8908);

        return ResponseEntity.ok(info);
    }
}
