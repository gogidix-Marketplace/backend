package com.gogidix.foundation.devtools.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Main REST controller for Developer Portal functionality.
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Tag(name = "Developer Portal", description = "Developer portal and dashboard endpoints")
@CrossOrigin(origins = "${cors.allowed-origins}")
public class DevToolsController {

    @GetMapping("/health")
    @Operation(summary = "Health check endpoint")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "infrastructure-devtools",
                "timestamp", System.currentTimeMillis()
        ));
    }

    @GetMapping("/info")
    @Operation(summary = "Service information")
    public ResponseEntity<Map<String, String>> info() {
        return ResponseEntity.ok(Map.of(
                "service", "Infrastructure DevTools Service",
                "version", "1.0.0",
                "description", "Development tools and utilities for the Gogidix platform"
        ));
    }

    @GetMapping("/tools")
    @Operation(summary = "List available developer tools")
    public ResponseEntity<Map<String, Object>> listTools() {
        Map<String, Object> tools = Map.of(
                "tools", List.of(
                        Map.of(
                                "name", "API Testing",
                                "description", "Test and validate REST APIs",
                                "endpoint", "/api-testing",
                                "enabled", true
                        ),
                        Map.of(
                                "name", "Database Query",
                                "description", "Execute and analyze database queries",
                                "endpoint", "/database",
                                "enabled", true
                        ),
                        Map.of(
                                "name", "Logging & Debugging",
                                "description", "View and analyze application logs",
                                "endpoint", "/logging",
                                "enabled", true
                        ),
                        Map.of(
                                "name", "Deployment",
                                "description", "Manage deployments and rollbacks",
                                "endpoint", "/deployment",
                                "enabled", true
                        ),
                        Map.of(
                                "name", "Documentation Generator",
                                "description", "Generate project documentation",
                                "endpoint", "/documentation",
                                "enabled", true
                        )
                )
        );
        return ResponseEntity.ok(tools);
    }
}
