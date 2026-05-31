package com.gogidix.shared.courier.ecommerce.interfaces.rest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Health", description = "Health check endpoints")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "ecommerce-integration-service");
        health.put("timestamp", Instant.now());
        return ResponseEntity.ok(health);
    }

    @GetMapping("/health/ready")
    public ResponseEntity<Map<String, Object>> ready() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        return ResponseEntity.ok(health);
    }

    @GetMapping("/health/live")
    public ResponseEntity<Map<String, Object>> live() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        return ResponseEntity.ok(health);
    }
}
