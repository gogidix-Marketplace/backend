package com.gogidix.shared.infrastructure.services.security.dlp.interfaces.rest.controller;
import com.gogidix.shared.infrastructure.services.security.dlp.application.dto.request.CreateDlpPolicyRequestDto;
import com.gogidix.shared.infrastructure.services.security.dlp.application.dto.request.UpdateDlpPolicyRequestDto;
import com.gogidix.shared.infrastructure.services.security.dlp.application.dto.response.DlpPolicyResponseDto;
import com.gogidix.shared.infrastructure.services.security.dlp.domain.port.in.DlpPolicyPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * REST Controller for DlpPolicy management.
 */
@RestController
@RequestMapping("/api/v1/dlp-policies")
@CrossOrigin(origins = "*")
public class DlpPolicyController {
    private final DlpPolicyPort dlpPolicyPort;
    public DlpPolicyController(DlpPolicyPort dlpPolicyPort) {
        this.dlpPolicyPort = dlpPolicyPort;
    }
    @PostMapping
    public ResponseEntity<DlpPolicyResponseDto> createDlpPolicy(
            @Valid @RequestBody CreateDlpPolicyRequestDto dto) {
        DlpPolicyResponseDto created = dlpPolicyPort.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @GetMapping("/{id}")
    public ResponseEntity<DlpPolicyResponseDto> getDlpPolicy(@PathVariable String id) {
        DlpPolicyResponseDto response = dlpPolicyPort.findById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<DlpPolicyResponseDto>> getAllDlpPolicies() {
        List<DlpPolicyResponseDto> policies = dlpPolicyPort.findAll();
        return ResponseEntity.ok(policies);
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<List<DlpPolicyResponseDto>> getDlpPoliciesByStatus(
            @PathVariable String status) {
        List<DlpPolicyResponseDto> policies = dlpPolicyPort.findByStatus(status);
        return ResponseEntity.ok(policies);
    }
    @PutMapping("/{id}")
    public ResponseEntity<DlpPolicyResponseDto> updateDlpPolicy(
            @PathVariable String id,
            @Valid @RequestBody UpdateDlpPolicyRequestDto dto) {
        DlpPolicyResponseDto updated = dlpPolicyPort.update(id, dto);
        return ResponseEntity.ok(updated);
    }
    @PostMapping("/{id}/activate")
    public ResponseEntity<DlpPolicyResponseDto> activateDlpPolicy(@PathVariable String id) {
        DlpPolicyResponseDto updated = dlpPolicyPort.activate(id);
        return ResponseEntity.ok(updated);
    }
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<DlpPolicyResponseDto> deactivateDlpPolicy(@PathVariable String id) {
        DlpPolicyResponseDto updated = dlpPolicyPort.deactivate(id);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDlpPolicy(@PathVariable String id) {
        dlpPolicyPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
