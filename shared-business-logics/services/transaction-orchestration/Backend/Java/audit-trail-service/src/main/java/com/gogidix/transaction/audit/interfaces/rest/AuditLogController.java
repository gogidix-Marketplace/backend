package com.gogidix.transaction.audit.interfaces.rest;

import com.gogidix.transaction.audit.application.dto.request.CreateAuditLogRequestDto;
import com.gogidix.transaction.audit.application.dto.response.AuditLogResponseDto;
import com.gogidix.transaction.audit.application.dto.response.PagedAuditLogsResponseDto;
import com.gogidix.transaction.audit.application.service.AuditLogCommandService;
import com.gogidix.transaction.audit.application.service.AuditLogQueryService;
import com.gogidix.transaction.audit.domain.port.in.GetAuditLogQuery;
import com.gogidix.transaction.audit.domain.port.in.SearchAuditLogsQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for Audit Log operations.
 * Provides API endpoints for managing audit logs.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/audit-logs")
@RequiredArgsConstructor
@Tag(name = "Audit Trail", description = "APIs for managing audit logs and compliance tracking")
public class AuditLogController {

    private final AuditLogCommandService commandService;
    private final AuditLogQueryService queryService;

    /**
     * Create a new audit log entry
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Create an audit log entry",
        description = "Creates a new audit log entry for tracking transaction events"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Audit log created successfully",
            headers = @Header(name = "X-Correlation-ID", description = "Correlation ID for tracking"),
            content = @Content(schema = @Schema(implementation = AuditLogResponseDto.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<AuditLogResponseDto> createAuditLog(
        @Parameter(description = "Audit log creation request", required = true)
        @Valid @RequestBody CreateAuditLogRequestDto request
    ) {
        log.info("POST /api/v1/audit-logs - Creating audit log: entityType={}, entityId={}, action={}",
            request.getEntityType(), request.getEntityId(), request.getAction());

        AuditLogResponseDto response = commandService.createAuditLog(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .location(URI.create("/api/v1/audit-logs/" + response.getId()))
            .body(response);
    }

    /**
     * Get audit log by ID
     */
    @GetMapping(value = "/{auditLogId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get audit log by ID",
        description = "Retrieves detailed information about a specific audit log entry"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Audit log found", content = @Content(schema = @Schema(implementation = AuditLogResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Audit log not found")
    })
    public ResponseEntity<AuditLogResponseDto> getAuditLog(
        @Parameter(description = "Audit log ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable UUID auditLogId
    ) {
        log.info("GET /api/v1/audit-logs/{} - Getting audit log", auditLogId);

        var query = GetAuditLogQuery.GetAuditLogQueryDto.builder()
            .auditLogId(auditLogId.toString())
            .build();

        AuditLogResponseDto response = queryService.getAuditLog(query);
        return ResponseEntity.ok(response);
    }

    /**
     * Search for audit logs
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Search for audit logs",
        description = "Searches for audit logs with optional filters for entity, actor, date ranges, and pagination"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    public ResponseEntity<PagedAuditLogsResponseDto> searchAuditLogs(
        @Parameter(description = "Filter by tenant ID") @RequestParam(required = false) String tenantId,
        @Parameter(description = "Filter by entity type") @RequestParam(required = false) String entityType,
        @Parameter(description = "Filter by entity ID") @RequestParam(required = false) String entityId,
        @Parameter(description = "Filter by action") @RequestParam(required = false) String action,
        @Parameter(description = "Filter by actor ID") @RequestParam(required = false) String actorId,
        @Parameter(description = "Filter by severity") @RequestParam(required = false) String severity,
        @Parameter(description = "Filter by category") @RequestParam(required = false) String category,
        @Parameter(description = "Filter by status") @RequestParam(required = false) String status,
        @Parameter(description = "Filter by correlation ID") @RequestParam(required = false) String correlationId,
        @Parameter(description = "Filter by start date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @Parameter(description = "Filter by end date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
        @Parameter(description = "Page number (0-indexed)", example = "0") @RequestParam(defaultValue = "0") Integer page,
        @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") Integer size,
        @Parameter(description = "Sort field", example = "timestamp") @RequestParam(defaultValue = "timestamp") String sortBy,
        @Parameter(description = "Sort direction (ASC/DESC)", example = "DESC") @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        log.info("GET /api/v1/audit-logs - Searching audit logs: tenant={}, type={}, action={}",
            tenantId, entityType, action);

        var query = SearchAuditLogsQuery.SearchAuditLogsQueryDto.builder()
            .tenantId(tenantId)
            .entityType(entityType)
            .entityId(entityId)
            .action(action)
            .actorId(actorId)
            .severity(severity)
            .category(category)
            .status(status)
            .startDate(startDate)
            .endDate(endDate)
            .page(page)
            .size(size)
            .sortBy(sortBy)
            .sortDirection(sortDirection)
            .build();

        PagedAuditLogsResponseDto response = queryService.searchAuditLogs(query);
        return ResponseEntity.ok(response);
    }

    /**
     * Get audit logs by entity
     */
    @GetMapping(value = "/by-entity/{entityType}/{entityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get audit logs by entity",
        description = "Retrieves all audit logs for a specific entity"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Audit logs retrieved successfully")
    })
    public ResponseEntity<List<AuditLogResponseDto>> getAuditLogsByEntity(
        @Parameter(description = "Entity type", required = true, example = "Transaction")
        @PathVariable String entityType,
        @Parameter(description = "Entity ID", required = true, example = "txn-12345")
        @PathVariable String entityId
    ) {
        log.info("GET /api/v1/audit-logs/by-entity/{}/{} - Getting audit logs", entityType, entityId);

        List<AuditLogResponseDto> response = queryService.getAuditLogsByEntity(entityType, entityId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get audit logs by tenant and entity
     */
    @GetMapping(value = "/by-tenant/{tenantId}/{entityType}/{entityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get audit logs by tenant and entity",
        description = "Retrieves all audit logs for a specific tenant's entity"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Audit logs retrieved successfully")
    })
    public ResponseEntity<List<AuditLogResponseDto>> getAuditLogsByTenantAndEntity(
        @Parameter(description = "Tenant ID", required = true, example = "tenant-001")
        @PathVariable String tenantId,
        @Parameter(description = "Entity type", required = true, example = "Transaction")
        @PathVariable String entityType,
        @Parameter(description = "Entity ID", required = true, example = "txn-12345")
        @PathVariable String entityId
    ) {
        log.info("GET /api/v1/audit-logs/by-tenant/{}/{}/{} - Getting audit logs", tenantId, entityType, entityId);

        List<AuditLogResponseDto> response = queryService.getAuditLogsByTenantAndEntity(tenantId, entityType, entityId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get audit logs by correlation ID
     */
    @GetMapping(value = "/by-correlation/{correlationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get audit logs by correlation ID",
        description = "Retrieves all audit logs for a specific correlation ID (distributed transaction tracking)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Audit logs retrieved successfully")
    })
    public ResponseEntity<List<AuditLogResponseDto>> getAuditLogsByCorrelationId(
        @Parameter(description = "Correlation ID", required = true, example = "corr-12345")
        @PathVariable String correlationId
    ) {
        log.info("GET /api/v1/audit-logs/by-correlation/{} - Getting audit logs", correlationId);

        List<AuditLogResponseDto> response = queryService.getAuditLogsByCorrelationId(correlationId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get audit logs by actor
     */
    @GetMapping(value = "/by-actor/{actorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get audit logs by actor",
        description = "Retrieves all audit logs performed by a specific actor"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Audit logs retrieved successfully")
    })
    public ResponseEntity<List<AuditLogResponseDto>> getAuditLogsByActor(
        @Parameter(description = "Actor ID", required = true, example = "user-001")
        @PathVariable String actorId
    ) {
        log.info("GET /api/v1/audit-logs/by-actor/{} - Getting audit logs", actorId);

        List<AuditLogResponseDto> response = queryService.getAuditLogsByActor(actorId);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete audit log
     */
    @DeleteMapping(value = "/{auditLogId}")
    @Operation(
        summary = "Delete audit log",
        description = "Deletes a specific audit log entry (use with caution)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Audit log deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Audit log not found")
    })
    public ResponseEntity<Void> deleteAuditLog(
        @Parameter(description = "Audit log ID", required = true)
        @PathVariable UUID auditLogId
    ) {
        log.info("DELETE /api/v1/audit-logs/{} - Deleting audit log", auditLogId);

        commandService.deleteAuditLog(auditLogId);
        return ResponseEntity.noContent().build();
    }
}
