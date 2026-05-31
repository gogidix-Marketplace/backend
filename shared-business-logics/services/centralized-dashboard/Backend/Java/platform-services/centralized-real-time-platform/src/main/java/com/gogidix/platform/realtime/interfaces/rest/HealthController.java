package com.gogidix.platform.realtime.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Health check controller.
 */
@RestController
@RequestMapping("/api/v1")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "centralized-real-time-platform");
        return ResponseEntity.ok(status);
    }

    @GetMapping("/readiness")
    public ResponseEntity<Map<String, String>> readiness() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "READY");
        return ResponseEntity.ok(status);
    }

    @GetMapping("/liveness")
    public ResponseEntity<Map<String, String>> liveness() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "ALIVE");
        return ResponseEntity.ok(status);
    }
}
