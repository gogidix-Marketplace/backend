package com.gogidix.transaction.audit.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating audit log entries.
 */
@Schema(description = "Request to create an audit log entry")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAuditLogRequestDto {

    @Schema(description = "Tenant ID", example = "tenant-001", required = true)
    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    @Schema(description = "Type of entity being audited", example = "Transaction", required = true)
    @NotBlank(message = "Entity type is required")
    private String entityType;

    @Schema(description = "ID of the entity being audited", example = "txn-12345", required = true)
    @NotBlank(message = "Entity ID is required")
    private String entityId;

    @Schema(description = "Action performed", example = "CREATE", required = true)
    @NotBlank(message = "Action is required")
    private String action;

    @Schema(description = "ID of the actor who performed the action", example = "user-001")
    private String actorId;

    @Schema(description = "Type of actor (USER, SYSTEM, SERVICE)", example = "USER")
    private String actorType;

    @Schema(description = "IP address of the actor", example = "192.168.1.1")
    private String ipAddress;

    @Schema(description = "User agent of the actor", example = "Mozilla/5.0...")
    private String userAgent;

    @Schema(description = "Correlation ID for tracking", example = "corr-12345")
    private String correlationId;

    @Schema(description = "Old state before the action (JSON)")
    private String oldState;

    @Schema(description = "New state after the action (JSON)")
    private String newState;

    @Schema(description = "List of changed fields (JSON array)")
    private String changedFields;

    @Schema(description = "Additional metadata (JSON)")
    private String metadata;

    @Schema(description = "Business context information")
    private String businessContext;

    @Schema(description = "Severity level (INFO, WARNING, ERROR, CRITICAL)", example = "INFO")
    private String severity;

    @Schema(description = "Category of the audit event", example = "BUSINESS")
    private String category;

    @Schema(description = "Description of the action")
    private String description;

    @Schema(description = "Status of the action (SUCCESS, FAILURE, PENDING)", example = "SUCCESS")
    private String status;

    @Schema(description = "Error message if the action failed")
    private String errorMessage;

    @Schema(description = "Session ID of the actor")
    private String sessionId;

    @Schema(description = "Request ID for HTTP requests")
    private String requestId;
}
