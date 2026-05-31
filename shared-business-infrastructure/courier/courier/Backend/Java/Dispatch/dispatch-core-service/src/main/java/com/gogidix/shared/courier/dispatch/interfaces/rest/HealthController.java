package com.gogidix.shared.courier.dispatch.interfaces.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Health check controller
 */
@RestController
@RequestMapping("/actuator/health")
public class HealthController {

    @GetMapping
    public Map<String, Object> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "dispatch-core-service");
        health.put("database", "MongoDB");
        health.put("geospatial", "enabled");
        return health;
    }
}

