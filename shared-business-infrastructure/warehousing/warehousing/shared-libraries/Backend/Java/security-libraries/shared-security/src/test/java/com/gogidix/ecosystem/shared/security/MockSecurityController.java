package com.gogidix.ecosystem.shared.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Mock controller for security testing.
 * 
 * @author Gogidix Development Team
 * @since 1.0.0
 */
@RestController
public class MockSecurityController {

    @GetMapping("/api/auth/login")
    public ResponseEntity<Map<String, String>> login() {
        return ResponseEntity.ok(Map.of("message", "Login endpoint"));
    }

    @GetMapping("/api/health/status")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }

    @GetMapping("/actuator/health")
    public ResponseEntity<Map<String, String>> actuatorHealth() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }

    @GetMapping("/api/protected/resource")
    public ResponseEntity<Map<String, String>> protectedResource() {
        return ResponseEntity.ok(Map.of("message", "Protected resource accessed"));
    }

    @GetMapping("/api/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> adminUsers() {
        return ResponseEntity.ok(Map.of("users", "admin users list"));
    }

    @PostMapping("/api/auth/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken != null && !refreshToken.isEmpty()) {
            return ResponseEntity.ok(Map.of("accessToken", "new.access.token"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid refresh token"));
    }

    @RequestMapping(value = "/api/protected/resource", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        return ResponseEntity.ok().build();
    }
}