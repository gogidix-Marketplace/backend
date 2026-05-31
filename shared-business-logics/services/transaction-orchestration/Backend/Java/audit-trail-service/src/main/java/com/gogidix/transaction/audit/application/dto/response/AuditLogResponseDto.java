package com.gogidix.transaction.audit.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for audit log response.
 */
@Schema(description = "Audit log entry response")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponseDto {

    @Schema(description = "Audit log ID")
    private UUID id;

    @Schema(description = "Tenant ID")
    private String tenantId;

    @Schema(description = "Type of entity")
    private String entityType;

    @Schema(description = "ID of the entity")
    private String entityId;

    @Schema(description = "Action performed")
    private String action;

    @Schema(description = "ID of the actor")
    private String actorId;

    @Schema(description = "Type of actor")
    private String actorType;

    @Schema(description = "IP address")
    private String ipAddress;

    @Schema(description = "User agent")
    private String userAgent;

    @Schema(description = "Correlation ID")
    private String correlationId;

    @Schema(description = "Timestamp of the action")
    private LocalDateTime timestamp;

    @Schema(description = "Old state")
    private String oldState;

    @Schema(description = "New state")
    private String newState;

    @Schema(description = "Changed fields")
    private String changedFields;

    @Schema(description = "Metadata")
    private String metadata;

    @Schema(description = "Business context")
    private String businessContext;

    @Schema(description = "Severity level")
    private String severity;

    @Schema(description = "Category")
    private String category;

    @Schema(description = "Description")
    private String description;

    @Schema(description = "Status")
    private String status;

    @Schema(description = "Error message")
    private String errorMessage;

    @Schema(description = "Session ID")
    private String sessionId;

    @Schema(description = "Request ID")
    private String requestId;

    @Schema(description = "Created at timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Updated at timestamp")
    private LocalDateTime updatedAt;
}
