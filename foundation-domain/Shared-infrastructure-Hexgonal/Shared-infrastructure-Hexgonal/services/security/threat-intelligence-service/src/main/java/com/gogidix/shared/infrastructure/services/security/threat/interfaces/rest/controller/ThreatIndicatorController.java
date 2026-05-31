package com.gogidix.shared.infrastructure.services.security.threat.interfaces.rest.controller;
import com.gogidix.shared.infrastructure.services.security.threat.application.dto.request.CreateThreatIndicatorRequestDto;
import com.gogidix.shared.infrastructure.services.security.threat.application.dto.request.UpdateThreatIndicatorRequestDto;
import com.gogidix.shared.infrastructure.services.security.threat.application.dto.response.ThreatIndicatorResponseDto;
import com.gogidix.shared.infrastructure.services.security.threat.domain.port.in.ThreatIndicatorPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * REST Controller for ThreatIndicator management.
 */
@RestController
@RequestMapping("/api/v1/threat-indicators")
@CrossOrigin(origins = "*")
public class ThreatIndicatorController {
    private final ThreatIndicatorPort threatIndicatorPort;
    public ThreatIndicatorController(ThreatIndicatorPort threatIndicatorPort) {
        this.threatIndicatorPort = threatIndicatorPort;
    }
    @PostMapping
    public ResponseEntity<ThreatIndicatorResponseDto> createThreatIndicator(
            @Valid @RequestBody CreateThreatIndicatorRequestDto dto) {
        ThreatIndicatorResponseDto created = threatIndicatorPort.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ThreatIndicatorResponseDto> getThreatIndicator(@PathVariable String id) {
        ThreatIndicatorResponseDto response = threatIndicatorPort.findById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<ThreatIndicatorResponseDto>> getAllThreatIndicators() {
        List<ThreatIndicatorResponseDto> indicators = threatIndicatorPort.findAll();
        return ResponseEntity.ok(indicators);
    }
    @GetMapping("/active")
    public ResponseEntity<List<ThreatIndicatorResponseDto>> getActiveIndicators() {
        List<ThreatIndicatorResponseDto> indicators = threatIndicatorPort.findActive();
        return ResponseEntity.ok(indicators);
    }
    @GetMapping("/type/{indicatorType}")
    public ResponseEntity<List<ThreatIndicatorResponseDto>> getIndicatorsByType(
            @PathVariable String indicatorType) {
        List<ThreatIndicatorResponseDto> indicators = threatIndicatorPort.findByType(indicatorType);
        return ResponseEntity.ok(indicators);
    }
    @GetMapping("/severity/{severity}")
    public ResponseEntity<List<ThreatIndicatorResponseDto>> getIndicatorsBySeverity(
            @PathVariable String severity) {
        List<ThreatIndicatorResponseDto> indicators = threatIndicatorPort.findBySeverity(severity);
        return ResponseEntity.ok(indicators);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ThreatIndicatorResponseDto> updateThreatIndicator(
            @PathVariable String id,
            @Valid @RequestBody UpdateThreatIndicatorRequestDto dto) {
        ThreatIndicatorResponseDto updated = threatIndicatorPort.update(id, dto);
        return ResponseEntity.ok(updated);
    }
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<ThreatIndicatorResponseDto> deactivateThreatIndicator(@PathVariable String id) {
        ThreatIndicatorResponseDto updated = threatIndicatorPort.deactivate(id);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteThreatIndicator(@PathVariable String id) {
        threatIndicatorPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
