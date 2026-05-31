package com.gogidix.ecommerce.cart.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
@Tag(name = "Health Check", description = "APIs for health check and service monitoring")
public class HealthController {

    @GetMapping
    @Operation(summary = "Health check", description = "Check the health status of the cart service")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "cart-service");
        response.put("timestamp", Instant.now().toString());
        return response;
    }
}
