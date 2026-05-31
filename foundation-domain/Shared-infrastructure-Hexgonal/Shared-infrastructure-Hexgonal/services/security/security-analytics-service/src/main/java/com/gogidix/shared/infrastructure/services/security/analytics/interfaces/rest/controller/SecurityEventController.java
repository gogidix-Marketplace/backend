package com.gogidix.shared.infrastructure.services.security.analytics.interfaces.rest.controller;
import com.gogidix.shared.infrastructure.services.security.analytics.application.dto.request.CreateSecurityEventRequestDto;
import com.gogidix.shared.infrastructure.services.security.analytics.application.dto.response.SecurityEventResponseDto;
import com.gogidix.shared.infrastructure.services.security.analytics.domain.port.in.SecurityEventPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * REST Controller for SecurityEvent management.
 */
@RestController
@RequestMapping("/api/v1/security-events")
@CrossOrigin(origins = "*")
public class SecurityEventController {
    private final SecurityEventPort securityEventPort;
    public SecurityEventController(SecurityEventPort securityEventPort) {
        this.securityEventPort = securityEventPort;
    }
    @PostMapping
    public ResponseEntity<SecurityEventResponseDto> createSecurityEvent(
            @Valid @RequestBody CreateSecurityEventRequestDto dto) {
        SecurityEventResponseDto created = securityEventPort.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @GetMapping("/{id}")
    public ResponseEntity<SecurityEventResponseDto> getSecurityEvent(@PathVariable String id) {
        SecurityEventResponseDto response = securityEventPort.findById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<SecurityEventResponseDto>> getAllSecurityEvents() {
        List<SecurityEventResponseDto> events = securityEventPort.findAll();
        return ResponseEntity.ok(events);
    }
    @GetMapping("/severity/{severity}")
    public ResponseEntity<List<SecurityEventResponseDto>> getEventsBySeverity(
            @PathVariable String severity) {
        List<SecurityEventResponseDto> events = securityEventPort.findBySeverity(severity);
        return ResponseEntity.ok(events);
    }
    @GetMapping("/type/{eventType}")
    public ResponseEntity<List<SecurityEventResponseDto>> getEventsByType(
            @PathVariable String eventType) {
        List<SecurityEventResponseDto> events = securityEventPort.findByEventType(eventType);
        return ResponseEntity.ok(events);
    }
    @GetMapping("/recent")
    public ResponseEntity<List<SecurityEventResponseDto>> getRecentEvents(
            @RequestParam(defaultValue = "24") String hours) {
        List<SecurityEventResponseDto> events = securityEventPort.findRecent(hours);
        return ResponseEntity.ok(events);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSecurityEvent(@PathVariable String id) {
        securityEventPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
