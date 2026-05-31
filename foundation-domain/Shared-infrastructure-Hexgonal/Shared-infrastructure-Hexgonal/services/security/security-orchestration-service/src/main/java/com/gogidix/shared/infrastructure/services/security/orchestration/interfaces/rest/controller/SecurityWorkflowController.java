package com.gogidix.shared.infrastructure.services.security.orchestration.interfaces.rest.controller;
import com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.request.CreateSecurityWorkflowRequestDto;
import com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.request.UpdateSecurityWorkflowRequestDto;
import com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.response.SecurityWorkflowResponseDto;
import com.gogidix.shared.infrastructure.services.security.orchestration.domain.port.in.SecurityWorkflowPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * REST Controller for SecurityWorkflow management.
 */
@RestController
@RequestMapping("/api/v1/security-workflows")
@CrossOrigin(origins = "*")
public class SecurityWorkflowController {
    private final SecurityWorkflowPort securityWorkflowPort;
    public SecurityWorkflowController(SecurityWorkflowPort securityWorkflowPort) {
        this.securityWorkflowPort = securityWorkflowPort;
    }
    @PostMapping
    public ResponseEntity<SecurityWorkflowResponseDto> createSecurityWorkflow(
            @Valid @RequestBody CreateSecurityWorkflowRequestDto dto) {
        SecurityWorkflowResponseDto created = securityWorkflowPort.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @GetMapping("/{id}")
    public ResponseEntity<SecurityWorkflowResponseDto> getSecurityWorkflow(@PathVariable String id) {
        SecurityWorkflowResponseDto response = securityWorkflowPort.findById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<SecurityWorkflowResponseDto>> getAllSecurityWorkflows() {
        List<SecurityWorkflowResponseDto> workflows = securityWorkflowPort.findAll();
        return ResponseEntity.ok(workflows);
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<List<SecurityWorkflowResponseDto>> getSecurityWorkflowsByStatus(
            @PathVariable String status) {
        List<SecurityWorkflowResponseDto> workflows = securityWorkflowPort.findByStatus(status);
        return ResponseEntity.ok(workflows);
    }
    @PutMapping("/{id}")
    public ResponseEntity<SecurityWorkflowResponseDto> updateSecurityWorkflow(
            @PathVariable String id,
            @Valid @RequestBody UpdateSecurityWorkflowRequestDto dto) {
        SecurityWorkflowResponseDto updated = securityWorkflowPort.update(id, dto);
        return ResponseEntity.ok(updated);
    }
    @PostMapping("/{id}/activate")
    public ResponseEntity<SecurityWorkflowResponseDto> activateSecurityWorkflow(@PathVariable String id) {
        SecurityWorkflowResponseDto updated = securityWorkflowPort.activate(id);
        return ResponseEntity.ok(updated);
    }
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<SecurityWorkflowResponseDto> deactivateSecurityWorkflow(@PathVariable String id) {
        SecurityWorkflowResponseDto updated = securityWorkflowPort.deactivate(id);
        return ResponseEntity.ok(updated);
    }
    @PostMapping("/{id}/execute")
    public ResponseEntity<SecurityWorkflowResponseDto> executeSecurityWorkflow(@PathVariable String id) {
        SecurityWorkflowResponseDto result = securityWorkflowPort.execute(id);
        return ResponseEntity.ok(result);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSecurityWorkflow(@PathVariable String id) {
        securityWorkflowPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
