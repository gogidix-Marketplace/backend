package com.gogidix.shared.warehousing.access.interfaces.rest;

import com.gogidix.shared.warehousing.access.application.dto.*;
import com.gogidix.shared.warehousing.access.application.service.AccessService;
import com.gogidix.shared.warehousing.access.domain.entity.AccessLog;
import com.gogidix.shared.warehousing.access.domain.entity.AccessRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Access REST Controller
 */
@RestController
@RequestMapping("/access")
@RequiredArgsConstructor
@Tag(name = "Access Control", description = "Storage access control and management APIs")
public class AccessController {

    private final AccessService accessService;

    @PostMapping("/request")
    @Operation(summary = "Request access", description = "Request access to storage area")
    public ResponseEntity<AccessRequestDTO> requestAccess(
            @Valid @RequestBody RequestAccessCommand command) {
        AccessRequestDTO request = accessService.requestAccess(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    @PostMapping("/grant")
    @Operation(summary = "Grant access", description = "Grant/approve access request")
    public ResponseEntity<AccessRequestDTO> grantAccess(
            @Parameter(description = "Request ID") @RequestParam String requestId,
            @Valid @RequestBody GrantAccessCommand command) {
        AccessRequestDTO request = accessService.grantAccess(requestId, command);
        return ResponseEntity.ok(request);
    }

    @PostMapping("/deny")
    @Operation(summary = "Deny access", description = "Deny access request")
    public ResponseEntity<AccessRequestDTO> denyAccess(
            @Parameter(description = "Request ID") @RequestParam String requestId,
            @Parameter(description = "Reason for denial") @RequestParam String reason,
            @Parameter(description = "Denied by") @RequestParam String deniedBy) {
        AccessRequestDTO request = accessService.denyAccess(requestId, reason, deniedBy);
        return ResponseEntity.ok(request);
    }

    @PostMapping("/log")
    @Operation(summary = "Log access", description = "Log access event")
    public ResponseEntity<AccessLogDTO> logAccess(
            @Valid @RequestBody LogAccessCommand command) {
        AccessLogDTO log = accessService.logAccess(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(log);
    }

    @PostMapping("/logs/{logId}/complete")
    @Operation(summary = "Complete access", description = "Complete access log (log exit)")
    public ResponseEntity<AccessLogDTO> completeAccess(
            @Parameter(description = "Log ID") @PathVariable String logId) {
        AccessLogDTO log = accessService.completeAccess(logId);
        return ResponseEntity.ok(log);
    }

    @GetMapping("/requests/{requestId}")
    @Operation(summary = "Get access request", description = "Get access request by ID")
    public ResponseEntity<AccessRequestDTO> getAccessRequest(
            @Parameter(description = "Request ID") @PathVariable String requestId) {
        AccessRequestDTO request = accessService.getAccessRequest(requestId);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/requests/pending")
    @Operation(summary = "Get pending requests", description = "Get pending access requests for warehouse")
    public ResponseEntity<List<AccessRequestDTO>> getPendingRequests(
            @Parameter(description = "Warehouse ID") @RequestParam String warehouseId) {
        List<AccessRequestDTO> requests = accessService.getPendingRequests(warehouseId);
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/requests/{requestId}/cancel")
    @Operation(summary = "Cancel request", description = "Cancel access request")
    public ResponseEntity<AccessRequestDTO> cancelRequest(
            @Parameter(description = "Request ID") @PathVariable String requestId) {
        AccessRequestDTO request = accessService.cancelRequest(requestId);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/logs")
    @Operation(summary = "Get access logs", description = "Get access logs for warehouse")
    public ResponseEntity<List<AccessLogDTO>> getAccessLogs(
            @Parameter(description = "Warehouse ID") @RequestParam String warehouseId,
            @Parameter(description = "From date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @Parameter(description = "To date") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        List<AccessLogDTO> logs = accessService.getAccessLogs(warehouseId, from, to);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/logs/users/{userId}")
    @Operation(summary = "Get user access logs", description = "Get access logs for user")
    public ResponseEntity<List<AccessLogDTO>> getUserAccessLogs(
            @Parameter(description = "User ID") @PathVariable String userId) {
        List<AccessLogDTO> logs = accessService.getUserAccessLogs(userId);
        return ResponseEntity.ok(logs);
    }

    @PostMapping("/permissions")
    @Operation(summary = "Create permission", description = "Create access permission")
    public ResponseEntity<AccessPermissionDTO> createPermission(
            @Valid @RequestBody AccessService.CreatePermissionCommand command) {
        AccessPermissionDTO permission = accessService.createPermission(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(permission);
    }

    @GetMapping("/permissions/users/{userId}")
    @Operation(summary = "Get user permissions", description = "Get permissions for user")
    public ResponseEntity<List<AccessPermissionDTO>> getUserPermissions(
            @Parameter(description = "User ID") @PathVariable String userId) {
        List<AccessPermissionDTO> permissions = accessService.getUserPermissions(userId);
        return ResponseEntity.ok(permissions);
    }
}
