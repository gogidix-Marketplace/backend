package com.gogidix.shared.infrastructure.services.observability.audit.interfaces.rest;

import com.gogidix.shared.infrastructure.services.observability.audit.application.dto.request.CreateAuditLogRequestDto;
import com.gogidix.shared.infrastructure.services.observability.audit.application.dto.response.AuditLogResponseDto;
import com.gogidix.shared.infrastructure.services.observability.audit.application.service.AuditService;
import com.gogidix.shared.infrastructure.services.observability.audit.domain.port.in.AuditPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for audit logs.
 */
@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
@Tag(name = "Audit", description = "Audit log management APIs")
public class AuditController {

    private final AuditService auditService;

    @PostMapping
    @Operation(summary = "Create audit log", description = "Create a new audit log entry")
    public ResponseEntity<AuditLogResponseDto> createAuditLog(
            @Valid @RequestBody CreateAuditLogRequestDto request) {
        AuditLogResponseDto response = auditService.createAuditLog(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{auditLogId}")
    @Operation(summary = "Get audit log", description = "Get audit log by ID")
    public ResponseEntity<AuditLogResponseDto> getAuditLog(@PathVariable String auditLogId) {
        AuditLogResponseDto response = auditService.getAuditLog(auditLogId);
        return response != null ? ResponseEntity.ok(response)
                : ResponseEntity.notFound().build();
    }

    @GetMapping
    @Operation(summary = "List audit logs", description = "List audit logs with filters")
    public ResponseEntity<List<AuditLogResponseDto>> listAuditLogs(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false, defaultValue = "100") Integer limit) {
        List<AuditLogResponseDto> logs = auditService.listAuditLogs(userId, startDate, endDate, limit);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    @Operation(summary = "List entity audit logs", description = "List audit logs for a specific entity")
    public ResponseEntity<List<AuditLogResponseDto>> listEntityAuditLogs(
            @PathVariable String entityType,
            @PathVariable String entityId) {
        List<AuditLogResponseDto> logs = auditService.listEntityAuditLogs(entityType, entityId);
        return ResponseEntity.ok(logs);
    }
}
