package com.gogidix.centralconfiguration.configauditservice.interfaces.rest;

import com.gogidix.centralconfiguration.configauditservice.application.service.ConfigAuditService;
import com.gogidix.centralconfiguration.configauditservice.domain.model.AuditAction;
import com.gogidix.centralconfiguration.configauditservice.domain.model.ConfigAuditLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for Config Audit operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/audit-logs")
@RequiredArgsConstructor
@Tag(name = "Audit Logs", description = "APIs for configuration audit trail")
public class ConfigAuditController {

    private final ConfigAuditService auditService;

    private static final String DEFAULT_TENANT_HEADER = "X-Tenant-ID";

    @GetMapping(produces = "application/json")
    @Operation(summary = "Get all audit logs", description = "Retrieves all audit logs for the tenant")
    public ResponseEntity<List<ConfigAuditLog>> getAuditLogs(
            @Parameter(description = "Tenant ID")
            @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId) {

        List<ConfigAuditLog> response = auditService.getAuditLogs(tenantId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/by-entity-type", produces = "application/json")
    @Operation(summary = "Get audit logs by entity type", description = "Retrieves audit logs for a specific entity type")
    public ResponseEntity<List<ConfigAuditLog>> getAuditLogsByEntityType(
            @Parameter(description = "Entity type", required = true) @RequestParam String entityType,

            @Parameter(description = "Tenant ID")
            @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId) {

        List<ConfigAuditLog> response = auditService.getAuditLogsByEntityType(tenantId, entityType);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/by-entity", produces = "application/json")
    @Operation(summary = "Get audit logs by entity", description = "Retrieves audit logs for a specific entity")
    public ResponseEntity<List<ConfigAuditLog>> getAuditLogsByEntity(
            @Parameter(description = "Entity type", required = true) @RequestParam String entityType,
            @Parameter(description = "Entity ID", required = true) @RequestParam String entityId,

            @Parameter(description = "Tenant ID")
            @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId) {

        List<ConfigAuditLog> response = auditService.getAuditLogsByEntity(tenantId, entityType, entityId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/by-date-range", produces = "application/json")
    @Operation(summary = "Get audit logs by date range", description = "Retrieves audit logs within a date range")
    public ResponseEntity<List<ConfigAuditLog>> getAuditLogsByDateRange(
            @Parameter(description = "Start date", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,

            @Parameter(description = "Tenant ID")
            @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId) {

        List<ConfigAuditLog> response = auditService.getAuditLogsByDateRange(tenantId, startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/by-user", produces = "application/json")
    @Operation(summary = "Get audit logs by user", description = "Retrieves audit logs for a specific user")
    public ResponseEntity<List<ConfigAuditLog>> getAuditLogsByUser(
            @Parameter(description = "User ID", required = true) @RequestParam String userId) {

        List<ConfigAuditLog> response = auditService.getAuditLogsByUser(userId);
        return ResponseEntity.ok(response);
    }
}
